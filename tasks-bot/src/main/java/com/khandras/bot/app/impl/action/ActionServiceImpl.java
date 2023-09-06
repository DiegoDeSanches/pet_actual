package com.khandras.bot.app.impl.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khandras.bot.adapter.listener.dto.IncomingMessageDto;
import com.khandras.bot.app.api.action.ActionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionServiceImpl implements ActionService {
    private final ObjectMapper mapper;

    @Override
    public void handleMessage(IncomingMessageDto dto) throws JsonProcessingException {
        log.info("Income message received - \n{}", mapper.writeValueAsString(dto));
        log.info("Text of received message is - {}", dto.getMessage().getText());
    }
}
