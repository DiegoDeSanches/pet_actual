package com.khandras.bot.adapter.persistence.messagelog;

import com.khandras.bot.domain.message.MessageLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageLogCrudRepository extends CrudRepository<MessageLog, UUID> {
    List<MessageLog> findAllByTelegramId(Long telegramId);
    Optional<MessageLog> findById(UUID messageLogId);
}
