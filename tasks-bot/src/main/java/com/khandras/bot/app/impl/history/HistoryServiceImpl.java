package com.khandras.bot.app.impl.history;

import com.khandras.bot.app.api.history.HistoryService;
import com.khandras.bot.app.api.persistance.MessageLogRepository;
import com.khandras.bot.app.api.persistance.UserRepository;
import com.khandras.bot.domain.message.MessageLog;
import com.khandras.bot.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final UserRepository userRepo;
    private final MessageLogRepository messageLogRepository;

    @Override
    public Optional<User> findUserInfo(String tgId) {
        return userRepo.findByTgId(tgId);
    }

    @Override
    @Transactional
    public List<MessageLog> findUserMessages(String tgId) {
        var user = userRepo.findByTgId(tgId)
                .orElseThrow(() -> new IllegalArgumentException("No such user"));

        return messageLogRepository.findByUser(user);
    }

    @Override
    @Transactional
    public List<MessageLog> findLastUserMessagesByPage(String tgId, int pageNum, int pageSize) {
        var user = userRepo.findByTgId(tgId)
                .orElseThrow(() -> new IllegalArgumentException("No such user"));

        return messageLogRepository.findByUserLimited(user, pageNum, pageSize);
    }

    @Override
    @Transactional
    public void saveMessageLog(Update update) {
        var infoToSave = SerializationUtils.serialize(update.getMessage());
        var user = userRepo.getUser(update.getMessage().getChatId().toString());

        var messageLog = new MessageLog()
                .setUserInfo(user)
                .setMessageText(update.getMessage().getText())
                .setMessageFull(update)
                .setSavedInfo(infoToSave);

        user.getMessageLogList()
                .add(messageLog);
    }
}
