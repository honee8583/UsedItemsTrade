package com.project.usedItemsTrade.member.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailUtilTest {

    @Autowired
    private MailUtil mailUtil;

    @Test
    void testSendMail() {
        // given
        String email = "honee8583@naver.com";
        String subject = "title";
        String text = "<p>content</p>";

        // when
        boolean result = mailUtil.sendMail(email, subject, text);

        // then
        assertTrue(result);
    }
}