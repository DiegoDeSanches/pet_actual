package com.khandras.bot.app.api.action;

import com.khandras.bot.adapter.telegram.listener.dto.IncomingMessageDto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface ActionService {
    void handleMessage(IncomingMessageDto dto) throws IOException, TelegramApiException;
}
