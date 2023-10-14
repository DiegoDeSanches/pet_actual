package com.khandras.bot.app.api.persistance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.khandras.bot.domain.message.MessageLog;

import java.util.List;

public interface MessageLogRepository {
    void save(MessageLog messageLog);
    List<MessageLog> findByTelegramId(long tgId) throws JsonProcessingException;
}
