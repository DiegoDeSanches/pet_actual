package com.khandras.bot.adapter.telegram.rest;

import com.khandras.bot.adapter.telegram.rest.dto.PhotoDto;
import com.khandras.bot.adapter.telegram.sender.TelegramMessageSender;
import com.khandras.bot.app.api.telegram.TelegramService;
import com.khandras.bot.fw.props.TelegramProps;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Service
@RequiredArgsConstructor
public class TelegramCustomAdapter implements TelegramService {
    private static final String TG_URL = "https://api.telegram.org/bot%s/getFile?file_id=%s";
    private static final String FAIL_MESSAGE = "Received null result from telegram";

    private final TelegramMessageSender sender;
    private final TelegramProps tgProps;
    private final WebClient webClient;

    @Override
    public File getFile(String fileId) throws TelegramApiException {
        var response = webClient.get().uri(String.format(TG_URL, tgProps.getToken(), fileId))
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(rs -> rs.bodyToMono(PhotoDto.class))
                .block();

        if (response == null || response.getResult() == null) throw new RuntimeException(FAIL_MESSAGE);
        var filePath = response.getResult().getFilePath();

        return sender.downloadFile(filePath);
    }
}
