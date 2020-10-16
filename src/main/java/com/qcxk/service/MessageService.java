package com.qcxk.service;

import com.qcxk.model.Message;

import java.util.List;

public interface MessageService {

    void processMsg(List<Message> messages);

    List<Message> parse2Msg(String message, List<Message> messages);

    List<String> responseMessage(List<Message> messages);
}