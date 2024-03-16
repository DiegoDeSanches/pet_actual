package com.khandras.bot.app.api.history;

import com.khandras.bot.domain.message.MessageLog;
import com.khandras.bot.domain.user.User;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

public interface HistoryService {
    Optional<User> findUserInfo(String userId);

    List<MessageLog> findUserMessages(String userId);

    List<MessageLog> findLastUserMessagesByPage(String userId, int pageNum, int pageSize);

    void saveMessageLog(Update update);
}
