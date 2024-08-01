package com.springboot.peanut.service.Impl;

import com.springboot.peanut.S3.S3Uploader;
import com.springboot.peanut.dao.FoodPredictDao;
import com.springboot.peanut.dto.FoodPredictDto;
import com.springboot.peanut.dto.FoodPredictResponseDto;
import com.springboot.peanut.entity.FoodPredict;
import com.springboot.peanut.service.FoodAIService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FoodAIServiceImpl implements FoodAIService {

    private final FoodPredictDao foodPredictDao;
    private final S3Uploader s3Uploader;
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(FoodAIService.class);

    public FoodAIServiceImpl(FoodPredictDao foodPredictDao, S3Uploader s3Uploader, WebClient.Builder webClientBuilder) throws IOException {
        this.foodPredictDao = foodPredictDao;
        this.s3Uploader = s3Uploader;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }

    @Override
    public FoodPredictResponseDto foodPredict(MultipartFile foodImage) {
        FoodPredictResponseDto foodPredictResponseDto = null;
        try {
            String imageUrl = s3Uploader.uploadImage(foodImage, "peanut/before");
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("image_url", imageUrl);

            Mono<FoodPredictResponseDto> response = webClient.post()
                    .uri("/predict")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(FoodPredictResponseDto.class);

            foodPredictResponseDto = response.block();
            logger.info("Received Response: {}", foodPredictResponseDto);

            if (foodPredictResponseDto != null) {
                FoodPredict foodPredict = new FoodPredict();
                foodPredict.setFoodName(foodPredictResponseDto.getPredictions().stream().map(FoodPredictDto::getFoodName).collect(Collectors.toList()).toString());
                foodPredict.setAccuracy(foodPredictResponseDto.getPredictions().stream().map(p -> String.valueOf(p.getAccuracy())).collect(Collectors.toList()).toString());
                foodPredict.setImageUrl(foodPredictResponseDto.getImage_url());
                foodPredictDao.saveFoodPredictResults(foodPredict);

                logger.info("Saved FoodPredict Result: {}", foodPredict);
            }

        } catch (WebClientException | IOException e) {
            logger.error("Error occurred while fetching results: ", e);
            e.printStackTrace();
        }

        return foodPredictResponseDto;
    }
}
