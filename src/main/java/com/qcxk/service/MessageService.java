package com.qcxk.service;

import com.qcxk.model.Message;

import java.util.List;

public interface MessageService {

    void processMsg(Message message);

    Message parse2Msg(String message);

    List<String> responseMessage(Message message);
}