package com.khandras.bot.adapter.sender;

import com.khandras.bot.fw.props.TelegramProps;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Service
public class TelegramMessageSender extends DefaultAbsSender {
    private final TelegramProps props;

    public TelegramMessageSender(DefaultBotOptions options, TelegramProps props) {
        super(options);
        this.props = props;
    }

    @Override
    public String getBotToken() {
        return props.getToken();
    }
}