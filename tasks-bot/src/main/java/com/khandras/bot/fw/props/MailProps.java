package com.khandras.bot.fw.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("mail")
public class MailProps {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Content content;

    @Getter
    @Setter
    public static class Content {
        private String botName;
        private String subject;
    }
}
