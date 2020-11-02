package com.qcxk.thms;

import com.qcxk.model.message.Message;
import com.qcxk.service.MessageService;
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

    @Test
    public void test1() {
        String s = "6804000000a23200000000000000010000000005000000020100000000000000936270000000000000000000000000000000000000000000005216";
        List<Message> messages = messageService.parse2Msg(s, new ArrayList<>());
        messageService.processMsg(messages);
    }

    @Test
    public void contextLoads() {
    }

}

