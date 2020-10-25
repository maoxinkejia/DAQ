package com.qcxk.service.impl;

import com.qcxk.dao.UserDao;
import com.qcxk.model.user.User;
import com.qcxk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

import static com.qcxk.common.Constants.*;
import static com.qcxk.util.BusinessUtil.getMD5Str;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao dao;

    @Override
    public String login(User user) {
        User exists = dao.findByUsername(user.getUsername());
        if (exists == null) {
            return USER_NOT_EXISTS;
        }

        if (Objects.equals(getMD5Str(user.getPassword()), exists.getPassword())) {
            log.info("user login success, username: {}", user.getUsername());
            return PASSWORD_ERROR;
        }

        return LOGIN_SUCCESS;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        user.setUpdateTime(new Date());

        int num = dao.updateUser(user);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Long id) {
        User user = dao.findById(id);
        user.setDelStatus(DELETED);
        user.setDelTime(new Date());
        user.setUpdateTime(new Date());

        int num = dao.update2Deleted(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        user.setPassword(getMD5Str(user.getPassword()));
        user.setCreateTime(new Date());
        user.setDelStatus(NOT_DELETED);

        int num = dao.addUser(user);
        log.info("add user success, num: {}, user: {}", num, user);
    }
}
