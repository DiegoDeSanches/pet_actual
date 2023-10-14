package com.khandras.bot.adapter.persistence.messagelog;

import com.khandras.bot.app.api.persistance.MessageLogRepository;
import com.khandras.bot.domain.message.MessageLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageLogRepositoryAdapter implements MessageLogRepository {
    private final MessageLogCrudRepository repo;

    @Override
    public void save(MessageLog logInstance) {
        repo.save(logInstance);
    }

    @Override
    public List<MessageLog> findByTelegramId(long tgId) {
        return repo.findAllByTelegramId(tgId);
    }
}
