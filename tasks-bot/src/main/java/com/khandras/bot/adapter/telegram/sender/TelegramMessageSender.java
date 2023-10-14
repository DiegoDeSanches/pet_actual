package com.khandras.bot.adapter.telegram.sender;

import com.khandras.bot.fw.props.TelegramProps;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramMessageSender extends DefaultAbsSender {
    private static final String MESSAGE = "Your info saved successfully";

    private final TelegramProps props;

    public TelegramMessageSender(DefaultBotOptions options, TelegramProps props) {
        super(options);
        this.props = props;
    }

    public void sendSuccessMessage(String chatId) throws TelegramApiException {
        var sendMessage = new SendMessage();
        sendMessage.setText(MESSAGE);
        sendMessage.setChatId(chatId);

        this.execute(sendMessage);
    }

    public void sendFailMessage(String chatId, String reason) throws TelegramApiException {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(String.format("Unable perform action for reason:\n%s", reason));

        this.execute(message);
    }

    @Override
    public String getBotToken() {
        return props.getToken();
    }
}