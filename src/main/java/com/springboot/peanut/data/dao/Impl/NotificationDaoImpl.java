package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.NotificationDao;
import com.springboot.peanut.data.dto.notification.NotificationListResponseDto;
import com.springboot.peanut.data.entity.Notification;
import com.springboot.peanut.data.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationDaoImpl implements NotificationDao {
    private final NotificationRepository notificationRepository;

    @Override
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationListResponseDto> getNotificationListByUserId(Long userId) {
        List<Notification> notificationList = notificationRepository.findNotificationByUserId(userId);
        List<NotificationListResponseDto> responseDtoList = new ArrayList<>();
        for(Notification notification : notificationList){
            NotificationListResponseDto notificationListResponseDto = new NotificationListResponseDto(
                    notification.getId(),
                    notification.getTitle(),
                    notification.getBody(),
                    notification.getCreate_At()
            );
            responseDtoList.add(notificationListResponseDto);
        }

        return responseDtoList;
    }
}
