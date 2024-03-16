package com.khandras.bot.adapter.persistence.messagelog;

import com.khandras.bot.domain.message.MessageLog;
import com.khandras.bot.domain.user.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageLogCrudRepository extends CrudRepository<MessageLog, UUID> {
    List<MessageLog> findAllByUserInfo(User userInfo);
    Optional<MessageLog> findById(@NotNull UUID messageLogId);

    List<MessageLog> findAllByUserInfo(User userInfo, PageRequest pageRequest);
}
