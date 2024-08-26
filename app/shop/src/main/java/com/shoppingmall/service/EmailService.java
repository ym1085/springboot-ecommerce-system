package com.shoppingmall.service;

import com.shoppingmall.utils.RedisEmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisEmailUtils redisEmailUtils;

    @Value("${email.title}")
    private String title;

    @Value("${email.from}")
    private String from;

    /**
     * Random AuthCode 생성
     * @return A randomly generated 6-digit code as a String
     */
    private static String createAuthCode() {
        int length = 6;
        StringBuilder sb = new StringBuilder(length);
        try {
            Random random = SecureRandom.getInstanceStrong(); // 암호학적으로 안전한 무작위성을 제공
            for (int i = 0; i < length; i++) {
                sb.append(random.nextInt(10)); // 0 ~ bound(n - 1) => 0 - 9 random int
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to generate secure random code: {}", e.getMessage());
            return ""; // Fallback to an empty string in case of an exception.
        }
        return sb.toString();
    }

    /**
     * 이메일 FORM 양식 생성
     * @return MimeMessage obj
     */
    private MimeMessage createEmailForm(String email) throws MessagingException {
        String authCode = createAuthCode();

        MimeMessage messages = javaMailSender.createMimeMessage();
        messages.addRecipients(MimeMessage.RecipientType.TO, email);
        messages.setSubject(title);
        messages.setFrom(from);
        messages.setText(authCode); // TODO: Later change to an email form
        messages.setSentDate(new Date());

        redisEmailUtils.setValues(email, authCode, 60 * 3L); // 3 min (key: email, value: authCode, 3분만 기억)
        return messages;
    }

    /**
     * 이메일 전송 코드 발송
     * @param email Recipient email
     */
    public void sendEmail(String email) {
        // 이메일이 redis의 key로 존재하는지 체크, 존재하면 삭제
        if (redisEmailUtils.hasKey(email)) {
            redisEmailUtils.deleteValues(email);
        }

        try {
            MimeMessage emailForm = createEmailForm(email);
            javaMailSender.send(emailForm);
            log.info("Auth Email sent successfully email = {}", email);
        } catch (MessagingException e) {
            log.error("Failed to create email for = {}, {}", email, e.getMessage(), e);
            throw new RuntimeException("Failed to create email form", e);
        } catch (RuntimeException e) {
            log.error("Failed to send email to = {}, {}", email, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Verifies Email Auto Code
     * @param email Recipient email
     * @param code Auth code
     */
    public Boolean verifyEmail(String email, String code) {
        String authCodeByEmail = redisEmailUtils.getValues(email);
        log.info("Email Auth Code, authCodeByEmail = {}", authCodeByEmail);
        if (!StringUtils.hasText(authCodeByEmail)) {
            return false;
        }
        return authCodeByEmail.equalsIgnoreCase(code);
    }
}
