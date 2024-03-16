package com.khandras.bot.app.api.persistance;

import com.khandras.bot.domain.message.MessageLog;
import com.khandras.bot.domain.user.User;

import java.util.List;

public interface MessageLogRepository {
    List<MessageLog> findByUser(User userInfo);

    List<MessageLog> findByUserLimited(User userInfo, int pageNum, int pageSize);
}
