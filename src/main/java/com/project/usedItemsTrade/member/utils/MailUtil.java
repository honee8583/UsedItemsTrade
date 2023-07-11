package com.project.usedItemsTrade.member.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailUtil {
    private final JavaMailSender javaMailSender;

    public void sendMailTest() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("honee8583@naver.com");
        message.setSubject("Hello");
        message.setText("This is Content");

        javaMailSender.send(message);
    }

    public boolean sendMail(String mail, String subject, String text) {
        boolean result = false;
        MimeMessagePreparator message = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(mail);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(text, true);

                // Mail에 img 삽입
//                ClassPathResource resource = new ClassPathResource("img 주소/img 이름.png");
//                message.addInline("img", resource.getFile());
            }
        };

        try {
            javaMailSender.send(message);
            result = true;
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return result;
    }
}
