package com.khandras.bot.app.api.telegram;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

public interface TelegramService {
    File getFile(String fileId) throws TelegramApiException;
}
