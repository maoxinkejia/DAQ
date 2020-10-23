package com.qcxk.service;

import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.model.message.Message;
import com.qcxk.model.message.OriginalData;

import java.util.List;

public interface MessageService {

    void processMsg(List<Message> messages);

    List<Message> parse2Msg(String message, List<Message> messages);

    List<String> responseMessage(List<Message> messages);

    void addOriginalData(String bodyStr, String deviceNum);

    List<OriginalData> findOriginalDataList(TerminalDeviceDTO dto);
}