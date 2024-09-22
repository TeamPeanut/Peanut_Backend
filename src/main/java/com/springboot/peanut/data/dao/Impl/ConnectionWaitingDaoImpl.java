package com.springboot.peanut.data.dao.Impl;

import com.springboot.peanut.data.dao.ConnectionWaitngDao;
import com.springboot.peanut.data.entity.ConnectionWaiting;
import com.springboot.peanut.data.repository.ConnectionWaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConnectionWaitingDaoImpl implements ConnectionWaitngDao {

    private final ConnectionWaitingRepository connectionWaitingRepository;

    @Override
    public void save(ConnectionWaiting connectionWaiting) {
        connectionWaitingRepository.save(connectionWaiting);
    }
}
