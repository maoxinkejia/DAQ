package com.qcxk.thms;

import com.qcxk.model.message.Message;
import com.qcxk.service.MessageService;
import com.qcxk.service.TerminalDeviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThmsApplicationTests {

    @Autowired
    private MessageService messageService;
    @Autowired
    private TerminalDeviceService terminalDeviceService;

    @Test
    public void test1() {
        String s = "68000315138509140b0a020c161b8a00ed16";
        List<Message> messages = messageService.parse2Msg(s, new ArrayList<>());
        messageService.processMsg(messages);
    }

    @Test
    public void contextLoads() {
    }

}

