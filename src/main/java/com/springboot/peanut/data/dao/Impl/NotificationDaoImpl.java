package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.NotificationDao;
import com.springboot.peanut.data.entity.Notification;
import com.springboot.peanut.data.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationDaoImpl implements NotificationDao {
    private final NotificationRepository notificationRepository;

    @Override
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }
}
