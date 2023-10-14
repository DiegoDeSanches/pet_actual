package com.khandras.bot.adapter.persistence.userinfo;

import com.khandras.bot.domain.user.UserInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserCrudRepository extends CrudRepository<UserInfo, String> {
    Optional<UserInfo> findByTelegramId(String telegramId);
}
