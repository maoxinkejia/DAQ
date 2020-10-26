package com.qcxk.service;

import com.qcxk.controller.model.query.UserDTO;
import com.qcxk.model.user.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    String login(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    boolean findByPassword(User user);

    void resetPassword(User user);

    List<User> findList(UserDTO dto);
}
