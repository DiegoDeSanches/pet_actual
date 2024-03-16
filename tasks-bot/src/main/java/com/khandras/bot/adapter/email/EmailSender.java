package com.khandras.bot.adapter.email;

import com.khandras.bot.fw.props.MailProps;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender sender;
    private final MailProps mailProps;

    public void sendMessage(String text, String sendTo) throws MessagingException, UnsupportedEncodingException {
        var message = sender.createMimeMessage();
        message.setFrom(new InternetAddress(mailProps.getUsername(), mailProps.getContent().getBotName()));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(sendTo));
        message.setSubject(mailProps.getContent().getSubject());
        message.setText(text);
        sender.send(message);
    }
}
