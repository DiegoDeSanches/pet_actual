package com.khandras.adapter.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khandras.adapter.api.dto.UserDto;
import com.khandras.adapter.api.mapper.UserInfoDestinationMapper;
import com.khandras.adapter.bot.grpc.BotAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import resources.proto.GetMessagesResponse;
import resources.proto.Message;

import java.util.Collection;
import java.util.List;

/**
 * Для инициации запроса по gRPC.
 * Сначала из постмана идет запрос сюда,
 * дальше он переводится в gRPC и направляется к сервису бота,
 * потом ответ обратно по ресту возвращается.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/grpc")
@RequiredArgsConstructor
public class MeshAdapter {
    private final BotAdapter botAdapter;
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping
    public List<UserDto> getAllSavedInfo(@RequestParam String userId) {
        log.info("received request for {}", userId);
        var response = botAdapter.getUserInfoById(userId);
        return UserInfoDestinationMapper.INSTANCE.toRestDto(response);
    }

    @GetMapping("/{userId}/messages")
    public String getUserMessages(@PathVariable String userId) throws JsonProcessingException {
        log.info("received request user messages for {}", userId);
        var response = botAdapter.startBidirectionalStreaming(userId);
        return mapper.writeValueAsString(response.stream()
                .map(GetMessagesResponse::getInfoList)
                .flatMap(Collection::stream)
                .map(Message::getMessageId)
                .toList());
    }
}
