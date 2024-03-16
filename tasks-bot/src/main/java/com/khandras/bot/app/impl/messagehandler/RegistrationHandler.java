package com.khandras.bot.app.impl.messagehandler;

import com.khandras.bot.adapter.telegram.listener.dto.IncomingMessageDto;
import com.khandras.bot.app.api.action.MessageHandler;
import com.khandras.bot.app.api.persistance.UserRepository;
import com.khandras.bot.domain.user.User;
import com.khandras.bot.fw.annotation.NotifyMethodCall;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RegistrationHandler implements MessageHandler {
    private final UserRepository userRepo;

    @Override
    @Transactional
    public boolean canHandle(IncomingMessageDto dto) {
        return userRepo.findByTgId(dto.getUserId()).isEmpty();
    }

    @Override
    @Transactional
    @NotifyMethodCall
    public void handle(IncomingMessageDto dto) {
        var userInfo = new User()
                .setTelegramId(dto.getUserId())
                .setSavedInfoCountBytes(BigInteger.ZERO)
                .setSavedInfoList(Collections.emptyList());

        userRepo.save(userInfo);
    }
}
