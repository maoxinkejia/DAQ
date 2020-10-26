package com.qcxk.service.impl;

import com.qcxk.controller.model.query.UserDTO;
import com.qcxk.dao.UserDao;
import com.qcxk.model.user.User;
import com.qcxk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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

        if (!Objects.equals(getMD5Str(user.getPassword()), exists.getPassword())) {
            return PASSWORD_ERROR;
        }

        log.info("user login success, username: {}", user.getUsername());
        return LOGIN_SUCCESS;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        user.setUpdateTime(new Date());

        int num = dao.updateUser(user);
        log.info("update user success, num: {}", num);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Long id) {
        User user = dao.findById(id);
        user.setDelStatus(DELETED);
        user.setDelTime(new Date());
        user.setUpdateTime(new Date());

        int num = dao.update2Deleted(user);
        log.info("delete user success, num: {}, username: {}", num, user.getUsername());
    }

    @Override
    public boolean findByPassword(User user) {
        User exists = dao.findByUsername(user.getUsername());
        if (exists == null) {
            log.warn("user not exists, username: {}", user.getUsername());
            return false;
        }

        if (Objects.equals(user.getPassword(), getMD5Str(user.getPassword()))) {
            log.warn("password not true, password: {}", user.getPassword());
            return false;
        }

        return true;
    }

    @Override
    public void resetPassword(User user) {
        user.setPassword(getMD5Str(user.getPassword()));
        int num = dao.resetPassword(user);
        log.info("reset password success, num: {}, username: {}", num, user.getUsername());
    }

    @Override
    public List<User> findList(UserDTO dto) {
        return dao.findList(dto);
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
