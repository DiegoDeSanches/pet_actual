package com.khandras.bot.app.impl.messagehandler;

import com.khandras.bot.adapter.telegram.listener.dto.IncomingMessageDto;
import com.khandras.bot.app.api.action.MessageHandler;
import com.khandras.bot.app.api.persistance.UserRepository;
import com.khandras.bot.domain.user.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RegistrationHandler implements MessageHandler {
    private final UserRepository userRepo;

    @Override
    public boolean canHandle(IncomingMessageDto dto) {
        log.info("Check RegistrationHandler");
        return userRepo.findByTgId(dto.getUserId()).isEmpty();
    }

    @Override
    public void handle(IncomingMessageDto dto) {
        var userInfo = new UserInfo()
                .setTelegramId(dto.getUserId())
                .setSavedInfoCountBytes(BigInteger.ZERO);

        userRepo.save(userInfo);
    }
}
