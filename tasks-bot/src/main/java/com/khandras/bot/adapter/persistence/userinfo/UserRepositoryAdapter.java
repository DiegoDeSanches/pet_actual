package com.khandras.bot.adapter.persistence.userinfo;

import com.khandras.bot.app.api.persistance.UserRepository;
import com.khandras.bot.domain.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserCrudRepository repo;

    @Override
    public Optional<UserInfo> findByTgId(String tgId) {
        return repo.findByTelegramId(tgId);
    }

    @Override
    public void updateUserBytes(String tgId, BigInteger bytes) {//todo
    }

    @Override
    public void save(UserInfo userInfo) {
        repo.save(userInfo);
    }
}
