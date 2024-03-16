package com.khandras.bot.adapter.persistence.messagelog;

import com.khandras.bot.app.api.persistance.MessageLogRepository;
import com.khandras.bot.domain.message.MessageLog;
import com.khandras.bot.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageLogRepositoryAdapter implements MessageLogRepository {
    private final MessageLogCrudRepository repo;

    @Override
    public List<MessageLog> findByUser(User userInfo) {
        return repo.findAllByUserInfo(userInfo);
    }

    @Override
    public List<MessageLog> findByUserLimited(User userInfo, int pageNum, int pageSize) {
        return repo.findAllByUserInfo(userInfo, PageRequest.of(pageNum, 3));
    }
}
