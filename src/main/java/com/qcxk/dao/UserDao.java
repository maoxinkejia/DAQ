package com.qcxk.dao;

import com.qcxk.controller.model.query.UserDTO;
import com.qcxk.model.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    int addUser(User user);

    User findByUsername(String username);

    int updateUser(User user);

    User findById(Long id);

    int update2Deleted(User user);

    int resetPassword(User user);

    List<User> findList(UserDTO dto);
}
