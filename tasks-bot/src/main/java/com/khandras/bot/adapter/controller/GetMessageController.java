package com.khandras.bot.adapter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.khandras.bot.adapter.controller.dto.MessageInfoDto;
import com.khandras.bot.app.api.persistance.MessageLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GetMessageController {
    private final MessageLogRepository logRepo;

    @GetMapping("/messages")
    public List<MessageInfoDto> getUserMessages(@RequestParam long telegramId) throws JsonProcessingException {
        var messages = logRepo.findByTelegramId(telegramId).stream().map(MessageInfoDto::new).toList();
//        var fileId = messages.get(1).getSavedInfo().getPhoto().stream().findAny().orElseThrow().getFileId();
//
//        var sendedToTg = messages.get(1);
        return messages;
    }
}
