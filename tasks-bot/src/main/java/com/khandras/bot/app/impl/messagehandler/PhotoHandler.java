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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(5)
public class PhotoHandler implements MessageHandler {
    private final UserRepository userRepo;
    private final TelegramMessageSender sender;
    private final TelegramService tgService;

    @Override
    public boolean canHandle(IncomingMessageDto dto) {
        log.info("Check PhotoHandler");
        return dto.getMessage().getPhoto() != null && !dto.getMessage().getPhoto().isEmpty();
    }

    @Override
    @Transactional
    @NotifyMethodCall
    public void handle(IncomingMessageDto dto) throws TelegramApiException, IOException {
        var fileId = getMaxFileId(dto);
        var file = tgService.getFile(fileId);
        var imageFile = getImage(file);
        var user = userRepo.findByTgId(dto.getUserId()).orElseThrow();
        var userSavedInfo = user.getSavedInfoList();
        var userSavedInfoBytes = user.getSavedInfoCountBytes();

        var infoInstance = new SavedInfo()
                .setUserInfo(user)
                .setInfoType(SavedInfoType.PHOTO)
                .setRecordName("just photo")
                .setSavedInfo(SerializationUtils.serialize(imageFile));
        userSavedInfoBytes = userSavedInfoBytes.add(BigInteger.valueOf(imageFile.getUsableSpace()));

        userSavedInfo.add(infoInstance);
        user.setSavedInfoCountBytes(userSavedInfoBytes);

        imageFile.deleteOnExit();
        sender.sendSuccessMessage(dto.getUserId());
    }

    private String getMaxFileId(IncomingMessageDto dto) {
        return dto.getMessage()
                .getPhoto()
                .stream()
                .min((ph1, ph2) -> Math.max(ph2.getFileSize(), ph1.getFileSize()))
                .orElseThrow()
                .getFileId();
    }

    private File getImage(File inputFile) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputFile);
        File outputfile = new File("image.png");
        ImageIO.write(bufferedImage, "png", outputfile);

        return outputfile;
    }
}
