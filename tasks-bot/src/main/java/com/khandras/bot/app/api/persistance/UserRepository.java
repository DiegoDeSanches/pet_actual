package com.khandras.bot.app.api.persistance;

import com.khandras.bot.domain.user.UserInfo;

import java.math.BigInteger;
import java.util.Optional;

public interface UserRepository {
    Optional<UserInfo> findByTgId(String tgId);
    void updateUserBytes(String tgId, BigInteger bytes);
    void save(UserInfo userInfo);
}
