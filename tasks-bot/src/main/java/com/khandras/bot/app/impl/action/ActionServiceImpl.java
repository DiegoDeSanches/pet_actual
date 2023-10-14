package com.khandras.bot.app.impl.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khandras.bot.adapter.telegram.listener.dto.IncomingMessageDto;
import com.khandras.bot.app.api.action.ActionService;
import com.khandras.bot.app.api.action.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionServiceImpl implements ActionService {
    private final ObjectMapper mapper;
    private final List<MessageHandler> handlerList;

    @Override
    public void handleMessage(IncomingMessageDto dto) throws IOException, TelegramApiException {
        log.info("Income message received - \n{}", mapper.writeValueAsString(dto));
        handlerList.stream().filter(it -> it.canHandle(dto))
                .findFirst().orElseThrow().handle(dto);
        log.info("Text of received message is - {}", dto.getMessage().getText());
    }
}
