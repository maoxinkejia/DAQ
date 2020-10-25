package com.qcxk.service;

import com.qcxk.model.user.User;

public interface UserService {
    void addUser(User user);

    String login(User user);

    void updateUser(User user);

    void deleteUser(Long id);
}
