package com.qcxk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcxk.dao.MessageDao;
import com.qcxk.model.Message;
import com.qcxk.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements MessageService {

}