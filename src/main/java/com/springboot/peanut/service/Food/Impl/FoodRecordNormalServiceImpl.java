package com.springboot.peanut.service.Food.Impl;

import com.springboot.peanut.S3.S3Uploader;
import com.springboot.peanut.data.dao.MealDao;
import com.springboot.peanut.data.dao.NotificationDao;
import com.springboot.peanut.data.dto.food.FoodNutritionDto;
import com.springboot.peanut.data.dto.notification.NotificationRequestDto;
import com.springboot.peanut.data.dto.signDto.ResultDto;
import com.springboot.peanut.data.entity.*;
import com.springboot.peanut.data.repository.BloodSugar.BloodSugarRepository;
import com.springboot.peanut.data.repository.FoodNutrition.FoodNutritionRepository;
import com.springboot.peanut.data.repository.UserRepository;
import com.springboot.peanut.service.Food.FoodRecordNormalService;
import com.springboot.peanut.service.Result.ResultStatusService;
import com.springboot.peanut.jwt.JwtAuthenticationService;
import com.springboot.peanut.service.User.UserService;
import com.springboot.peanut.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodRecordNormalServiceImpl implements FoodRecordNormalService {

    private final FoodNutritionRepository foodNutritionRepository;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final BloodSugarRepository bloodSugarRepository;
    private final ResultStatusService resultStatusService;
    private final MealDao mealDao;
    private final S3Uploader s3Uploader;
    private final NotificationService notificationService;
    private final NotificationDao notificationDao;
    private final UserRepository userRepository;


    @Override
    public List<FoodNutritionDto> getFoodNutritionByName(List<String> name, HttpServletRequest request) {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        log.info("[user] : {} " , user.get().getEmail());
        List<FoodNutrition> foodNutritionList = foodNutritionRepository.findFoodNutritionByFoodNameKor(name);
        log.info("[foodNutritionList] : {} ", foodNutritionList );
        List<FoodNutritionDto> foodDetailInfoDtoList = foodNutritionList.stream().map(foodNutrition ->
                new FoodNutritionDto(
                        foodNutrition.getId(),
                        foodNutrition.getName(),
                        foodNutrition.getCarbohydrate(),
                        foodNutrition.getProtein(),
                        foodNutrition.getFat(),
                        foodNutrition.getCholesterol(),
                        foodNutrition.getGlIndex(),
                        foodNutrition.getGiIndex()
                )).collect(Collectors.toList());

        request.getSession().setAttribute("foodDetailInfoDtoList", foodDetailInfoDtoList);
        return foodDetailInfoDtoList;

    }

    @Override
    public ResultDto saveNormalMealInfoImage(MultipartFile foodImage, HttpServletRequest request) throws IOException {
        Optional<User> user = jwtAuthenticationService.authenticationToken(request);
        String imageUrl = s3Uploader.uploadImage(foodImage,"peanut");
        ResultDto resultDto = new ResultDto();

        if(user == null){
            resultDto.setDetailMessage("존재하지 않는 회원 입니다.");
            resultStatusService.setFail(resultDto);
            return resultDto;
        }else{
            resultDto.setDetailMessage("이미지 등록 완료.");
            resultStatusService.setSuccess(resultDto);
            request.getSession().setAttribute("imageUrl", imageUrl);
            return resultDto;

        }
    }

    @Override
    public ResultDto saveNormalMealInfo(String mealTime, List<Integer> servingCounts, HttpServletRequest request) {
        Optional<User> userOpt = jwtAuthenticationService.authenticationToken(request);
        ResultDto resultDto = new ResultDto();

        if (!userOpt.isPresent()) {
            resultDto.setDetailMessage("사용자 인증에 실패했습니다.");
            resultStatusService.setFail(resultDto);
            return resultDto;
        }

        User user = userOpt.get();
        List<FoodNutritionDto> foodNutritionDtoList = (List<FoodNutritionDto>) request.getSession().getAttribute("foodDetailInfoDtoList");
        String imageUrl = (String) request.getSession().getAttribute("imageUrl");

        if (foodNutritionDtoList == null || foodNutritionDtoList.isEmpty()) {
            resultDto.setDetailMessage("음식 정보가 없습니다.");
            resultStatusService.setFail(resultDto);
            return resultDto;
        }

        if (servingCounts == null || servingCounts.isEmpty() || servingCounts.size() != foodNutritionDtoList.size()) {
            resultDto.setDetailMessage("인분 정보가 올바르지 않습니다.");
            resultStatusService.setFail(resultDto);
            return resultDto;
        }

        List<Long> foodNutritionIds = foodNutritionDtoList.stream()
                .map(FoodNutritionDto::getFoodId)
                .collect(Collectors.toList());

        List<FoodNutrition> foodNutritionList = foodNutritionRepository.findAllById(foodNutritionIds);

        if (foodNutritionList.isEmpty()) {
            resultDto.setDetailMessage("해당하는 음식 정보를 찾을 수 없습니다.");
            resultStatusService.setFail(resultDto);
            return resultDto;
        }

        // 예상 혈당 계산
        double expectedBloodSugar = calculateExpectedBloodSugar(user.getId(), servingCounts, foodNutritionList);

        // 식사 정보 생성 및 저장
        MealInfo mealInfo = MealInfo.createMeal(
                mealTime,
                imageUrl,
                expectedBloodSugar,
                foodNutritionList,
                user
        );
        mealDao.save(mealInfo);

        resultDto.setDetailMessage("식사 기록 저장 완료!");
        resultStatusService.setSuccess(resultDto);

        return resultDto;
    }


    private double calculateExpectedBloodSugar(Long userId, List<Integer> servingCounts, List<FoodNutrition> foodNutritionList) {
        // 사용자 아이디로 최근 혈당 기록 가져오기
        Optional<BloodSugar> currentBloodSugarOpt = bloodSugarRepository.findClosestBloodSugar(userId);

        if (!currentBloodSugarOpt.isPresent()) {
            throw new RuntimeException("최근 등록된 혈당 기록이 없습니다.");
        }

        double currentBloodSugar = Double.parseDouble(currentBloodSugarOpt.get().getBloodSugarLevel());
        double totalBloodSugarIncrease = 0.0;

        // 각 음식의 예상 혈당 상승량 계산
        for (int i = 0; i < foodNutritionList.size(); i++) {
            FoodNutrition food = foodNutritionList.get(i);
            int servingCount = servingCounts.get(i);

            // GL 계산 (GI * 탄수화물)
            double gl = (food.getGiIndex() * food.getCarbohydrate()) / 100.0;

            // 예상 혈당 상승량 계산 (GL * 1.8 * 인분 수)
            double bloodSugarIncrease = gl * 1.8 * servingCount;

            // 총 혈당 상승량에 더하기
            totalBloodSugarIncrease += bloodSugarIncrease;
        }

        // 최종 예상 혈당 계산 (현재 혈당 + 총 혈당 상승량)
        return currentBloodSugar + totalBloodSugarIncrease;
    }


    // 스케줄링된 메서드들: 알림 메시지 템플릿을 전달
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendMorningNotification() {
        String title = "식사 알림";
        String bodyTemplate = "{userName} 님! 아침 식사시간이에요! \n 제 시간에 하는 식사도 혈당 관리에 도움이 돼요. 오늘도 식사 기록을 해볼까요?";
        sendNotificationToAllUsers(title, bodyTemplate);
    }


    @Scheduled(cron = "0 00 12 * * ?")
    public void sendLunchNotification() {
        String title = "식사 알림";
        String bodyTemplate = "{userName} 님! 점심 식사시간이에요! \n 제 시간에 하는 식사도 혈당 관리에 도움이 돼요. 점심 식사 기록을 해볼까요?";
        sendNotificationToAllUsers(title, bodyTemplate);
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public void sendDinnerNotification() {
        String title = "식사 알림";
        String bodyTemplate = "{userName} 님! 저녁 식사시간이에요! 제 시간에 하는 식사도 혈당 관리에 도움이 돼요. 저녁 식사 기록을 해볼까요?";
        sendNotificationToAllUsers(title, bodyTemplate);
    }

    // 공통 메서드: 알림 보내기
    private void sendNotificationToAllUsers(String title, String bodyTemplate) {
        List<User> users = userRepository.findAll();  // 모든 사용자 조회 또는 특정 조건에 맞는 사용자 조회
        log.info("스케줄링된 알림이 실행되었습니다: {}", title);

        for (User user : users) {
            String fcmToken = user.getFcmToken();
            String userName = user.getUserName();
            String body = bodyTemplate.replace("{userName}", userName);

            log.info("[fcmToken] : {}", fcmToken);
            log.info("[userName] : {}", userName);
            log.info("[body] : {}", body);

            if (fcmToken != null && !fcmToken.isEmpty()) {
                try {
                    notificationService.sendAllNotification(fcmToken, title, body);
                    saveNotification(title, body, fcmToken, user);
                } catch (Exception e) {
                    log.error("알림 전송 중 오류 발생: ", e);
                }
            } else {
                log.warn("해당 사용자의 FCM 토큰이 없습니다: " + user.getUsername());
            }
        }
    }
    public void saveNotification(String title, String body,String fcnToken, User user) {
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(
                title,
                body,
                fcnToken
        );
        Notification notification = Notification.saveNotificationInfo(notificationRequestDto,user);

        notificationDao.save(notification);
    }
}
