package com.rin.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail-info.from}")
    String from;

    public void sendOrderEmail(com.rin.notificationservice.event.OrderPlacedEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            System.out.println("From: " + from);
            helper.setFrom(from);

            helper.setTo("abc0905440632@gmail.com");
            helper.setSubject("🛒 Đơn hàng mới #" + event.getOrderId());

            String body = "<p>Xin chào userId <b>" + event.getUserId() + "</b>,</p>" +
                    "<p>Đơn hàng #" + event.getOrderId() + " vừa được tạo thành công!</p>" +
                    "<p>Tổng tiền: <b>$" + event.getTotal() + "</b></p>";

            helper.setText(body, true);

            mailSender.send(message);
            System.out.println("📧 Gửi email thành công!");

        } catch (MessagingException e) {
            System.err.println("❌ Gửi email thất bại: " + e.getMessage());
        }
    }
}
