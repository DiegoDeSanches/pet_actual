package com.khandras.bot.adapter.persistence.userinfo;

import com.khandras.bot.domain.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserCrudRepository extends CrudRepository<User, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional(propagation = Propagation.MANDATORY)
    Optional<User> findByTelegramId(String telegramId);
}
