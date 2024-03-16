package com.khandras.bot.app.api.persistance;

import com.khandras.bot.domain.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByTgId(String tgId);

    void save(User userInfo);

    User getUser(String tgId);
}
