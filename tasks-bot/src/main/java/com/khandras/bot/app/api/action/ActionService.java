package com.khandras.bot.app.api.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.khandras.bot.adapter.listener.dto.IncomingMessageDto;

public interface ActionService {
    void handleMessage(IncomingMessageDto dto) throws JsonProcessingException;
}
