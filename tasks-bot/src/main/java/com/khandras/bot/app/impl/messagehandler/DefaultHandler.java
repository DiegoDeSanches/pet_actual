package com.khandras.bot.app.impl.messagehandler;

import com.khandras.bot.adapter.telegram.listener.dto.IncomingMessageDto;
import com.khandras.bot.adapter.telegram.sender.TelegramMessageSender;
import com.khandras.bot.app.api.action.MessageHandler;
import com.khandras.bot.fw.annotation.NotifyMethodCall;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(10)
public class DefaultHandler implements MessageHandler {
    private static final String MESSAGE_TEMPLATE = "No methods to handle your message implemented";

    private final TelegramMessageSender sender;

    @Override
    public boolean canHandle(IncomingMessageDto dto) {
        log.info("Check DefaultHandler");
        return true;
    }

    @Override
    @SneakyThrows
    @NotifyMethodCall
    public void handle(IncomingMessageDto dto) {
        var message = SendMessage.builder()
                .chatId(dto.getUserId())
                .text(MESSAGE_TEMPLATE)
                .build();
        sender.execute(message);
    }
}
