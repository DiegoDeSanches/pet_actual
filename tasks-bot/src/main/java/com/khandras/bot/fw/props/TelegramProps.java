package com.khandras.bot.fw.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("bot")
public class TelegramProps {
    private String token;
    private String username;
}
