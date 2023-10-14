package com.khandras.bot.adapter.controller.dto;

import com.khandras.bot.domain.message.MessageLog;
import lombok.Data;
import org.springframework.util.SerializationUtils;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
public class MessageInfoDto {
    String messageText;

    Message savedInfo;
    long telegramId;

    public MessageInfoDto(MessageLog logged) {
        this.messageText = logged.getMessageText();
        this.savedInfo = (Message) SerializationUtils.deserialize(logged.getSavedInfo());
        this.telegramId = logged.getTelegramId();
    }
}
