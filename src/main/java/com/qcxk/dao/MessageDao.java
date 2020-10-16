package com.qcxk.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcxk.model.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDao extends BaseMapper<Message> {

    int addBatch(List<Message> messages);
}