package com.example.msordenes.application.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreoHtml(String destinatario, String asunto, String htmlContenido) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom("franpvp.98@gmail.com");
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(htmlContenido, true);

            mailSender.send(mensaje);
            log.info("Correo HTML enviado correctamente a {}", destinatario);

        } catch (Exception e) {
            log.error("Error enviando correo HTML: {}", e.getMessage(), e);
        }
    }
}