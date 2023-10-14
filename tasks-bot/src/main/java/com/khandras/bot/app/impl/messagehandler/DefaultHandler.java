package com.khandras.bot.app.impl.messagehandler;

import com.khandras.bot.adapter.telegram.listener.dto.IncomingMessageDto;
import com.khandras.bot.adapter.telegram.sender.TelegramMessageSender;
import com.khandras.bot.app.api.action.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultHandler implements MessageHandler {
    private static final String MESSAGE_TEMPLATE = "No methods to handle your message provided";

    private final TelegramMessageSender sender;

    @Override
    public boolean canHandle(IncomingMessageDto dto) {
        log.info("Check DefaultHandler");
        return true;
    }

    @Override
    public void handle(IncomingMessageDto dto) throws TelegramApiException {
        var message = new SendMessage();
        message.setChatId(dto.getUserId());
        message.setText(MESSAGE_TEMPLATE);
        sender.execute(message);
    }
}
