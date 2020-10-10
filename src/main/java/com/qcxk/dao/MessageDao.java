package com.qcxk.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcxk.model.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao extends BaseMapper<Message> {

    void add(Message message);
}