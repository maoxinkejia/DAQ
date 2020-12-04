package com.qcxk.thms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThmsApplicationTests {

    @Test
    public void test1() {
    }

    @Test
    public void contextLoads() {
        StringBuilder builder = new StringBuilder(String.format("%0140d", 0));
        builder.replace(0, 2, String.format("%02x", 10));
        System.out.println(builder.toString());
        System.out.println(builder.length());
    }

    @Test
    public void readFile() {
        String principal = String.format("p%s", "1234");
        System.out.println(principal);
    }

}

