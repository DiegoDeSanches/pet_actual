package com.khandras.bot.adapter.persistence.userinfo;

import com.khandras.bot.app.api.persistance.UserRepository;
import com.khandras.bot.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserCrudRepository repo;

    @Override
    public Optional<User> findByTgId(String tgId) {
        return repo.findByTelegramId(tgId);
    }

    @Override
    public void save(User userInfo) {
        repo.save(userInfo);
    }

    @Override
    @Transactional
    public User getUser(String tgId) {
        return findByTgId(tgId)
                .orElseGet(() -> createUser(tgId));
    }
    
    private User createUser(String tgId) {
        var user = new User()
                .setTelegramId(tgId)
                .setSavedInfoCountBytes(BigInteger.ZERO)
                .setSavedInfoList(Collections.emptyList())
                .setMessageLogList(Collections.emptyList());

        return repo.save(user);
    }
}
