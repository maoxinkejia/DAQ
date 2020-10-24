package com.qcxk.dao;

import com.qcxk.model.user.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    int addUser(User user);

    User findByUsername(String username);
}
