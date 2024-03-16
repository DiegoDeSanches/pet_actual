package com.khandras.bot.app.impl.messagehandler;

import com.khandras.bot.adapter.telegram.listener.dto.IncomingMessageDto;
import com.khandras.bot.adapter.telegram.sender.TelegramMessageSender;
import com.khandras.bot.app.api.action.MessageHandler;
import com.khandras.bot.app.api.persistance.UserRepository;
import com.khandras.bot.app.api.telegram.TelegramService;
import com.khandras.bot.domain.info.SavedInfo;
import com.khandras.bot.domain.info.SavedInfoType;
import com.khandras.bot.fw.annotation.NotifyMethodCall;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigInteger;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(5)
public class DocumentHandler implements MessageHandler {
    private final TelegramService tgService;
    private final UserRepository userRepo;
    private final TelegramMessageSender sender;

    @Override
    public boolean canHandle(IncomingMessageDto dto) {
        log.info("Check DocumentHandler");
        return dto.getMessage().getDocument() != null;
    }

    @Override
    @Transactional
    @NotifyMethodCall
    public void handle(IncomingMessageDto dto) throws TelegramApiException {
        var document = dto.getMessage().getDocument();
        var file = tgService.getFile(document.getFileId());
        var user = userRepo.findByTgId(dto.getUserId()).orElseThrow();

        var infoInstance = new SavedInfo()
                .setUserInfo(user)
                .setRecordName(document.getFileName())
                .setInfoType(SavedInfoType.DOCUMENT)
                .setSavedInfo(SerializationUtils.serialize(file));
        var userSavedInfoBytes = user.getSavedInfoCountBytes();
        userSavedInfoBytes = userSavedInfoBytes.add(BigInteger.valueOf(file.getUsableSpace()));

        var userSavedInfo = user.getSavedInfoList();
        userSavedInfo.add(infoInstance);
        user.setSavedInfoCountBytes(userSavedInfoBytes);

        sender.sendSuccessMessage(dto.getUserId());
    }
}
