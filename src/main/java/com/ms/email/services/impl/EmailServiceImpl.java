package com.ms.email.services.impl;

import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import com.ms.email.repositories.EmailRepository;
import com.ms.email.services.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSenderImpl mailSender;

    @Override
    public EmailModel sendEmail(EmailModel emailModel) {
        emailModel.setSendDateEmail(LocalDateTime.now());

        try {
            // Prepara a mensagem (mas não envia aqui)
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());

            emailModel.setStatusEmail(StatusEmail.PENDING);

        } catch (Exception e) {
            emailModel.setError(e.getMessage());
            emailModel.setStatusEmail(StatusEmail.ERROR);
        }

        return emailRepository.save(emailModel);
    }

    @Scheduled(fixedRate = 10000) // Recomendado: ajustar para algo como 60000 (1 minuto)
    @Transactional
    public void processPendingEmail() {
        var pendingEmails = emailRepository.findByStatusEmail(StatusEmail.PENDING);

        for (EmailModel emailModel : pendingEmails) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(emailModel.getEmailFrom());
                message.setTo(emailModel.getEmailTo());
                message.setSubject(emailModel.getSubject());
                message.setText(emailModel.getText());

                mailSender.send(message);

                emailModel.setError(null);
                emailModel.setStatusEmail(StatusEmail.SENT);

            } catch (Exception e) {
                emailModel.setStatusEmail(StatusEmail.ERROR);
                emailModel.setError(e.getMessage());
            }

            emailRepository.save(emailModel);
        }
    }
}
