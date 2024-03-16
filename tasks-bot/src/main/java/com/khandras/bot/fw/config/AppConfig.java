package com.khandras.bot.fw.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.khandras.bot.fw.props.MailProps;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final MailProps mailProps;

    @Bean
    public ObjectMapper objectMapper() {
        var json = new ObjectMapper();
        json.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        json.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        json.registerModule(new JavaTimeModule());
        json.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return json;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProps.getHost());
        mailSender.setPort(mailProps.getPort());
        mailSender.setUsername(mailProps.getUsername());
        mailSender.setPassword(mailProps.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
