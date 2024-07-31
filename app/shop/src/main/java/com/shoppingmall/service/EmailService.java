package com.shoppingmall.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender emailSender;
    public static final String emailAuthKey = createEmailAutoCode();

    @Value("${email.title}")
    private String title;

    @Value("${email.from}")
    private String from;

    private static String createEmailAutoCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong(); // 암호학적으로 안전한 무작위성을 제공
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(random.nextInt(10));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("EmailServiceImpl.createEmailAutoCode exception occur, e = {}", e.getMessage());
        }
        return "";
    }

    public void sendAuthCodeToMemberEmail(String email) {
        sendEmail(email, title, emailAuthKey);
    }

    public void sendEmail(String to, String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(from, to, title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.error("e = {}", e.getMessage());
        }
    }

    private SimpleMailMessage createEmailForm(String from, String to, String  title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(text);
        message.setSentDate(new Date());
        return message;
    }
}
