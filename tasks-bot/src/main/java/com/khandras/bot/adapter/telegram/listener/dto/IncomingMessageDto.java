package com.khandras.bot.adapter.telegram.listener.dto;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public class IncomingMessageDto {
    private final MessageType type;
    private final Message message;

    public IncomingMessageDto(Update update) {
        if (update.getMessage() != null) {
            type = MessageType.MESSAGE;
            message = update.getMessage();
        } else if (update.getCallbackQuery() != null) {
            type = MessageType.CALLBACK;
            message = update.getCallbackQuery().getMessage();
            message.setText(update.getCallbackQuery().getData());
        } else if (update.hasMyChatMember()) {
            type = MessageType.CHAT_MEMBER;
            var mess = new Message();
            var chat = new Chat();
            chat.setId(update.getMyChatMember().getFrom().getId());
            mess.setChat(chat);
            message = mess;
        } else if (update.getEditedMessage() != null) {
            type = MessageType.EDITED_MESSAGE;
            message = update.getEditedMessage();
        } else {
            type = MessageType.UNKNOWN;
            message = new Message();
        }

        if (message.getText() == null) {
            message.setText(" ");
        }
    }

    public String getUserId() {
        return String.valueOf(getMessage().getChatId());
    }

    public enum MessageType {
        CALLBACK, MESSAGE, EDITED_MESSAGE, UNKNOWN, CHAT_MEMBER
    }
}

