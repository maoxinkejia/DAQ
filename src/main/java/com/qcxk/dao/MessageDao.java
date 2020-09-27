package com.qcxk.dao;


import java.util.List;
import com.qcxk.model.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
@Repository
public interface MessageDao extends BaseMapper<Message> {
    
}