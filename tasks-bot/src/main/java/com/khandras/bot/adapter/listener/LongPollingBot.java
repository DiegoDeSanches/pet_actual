package com.khandras.bot.adapter.listener;

import com.khandras.bot.adapter.listener.dto.IncomingMessageDto;
import com.khandras.bot.app.api.action.ActionService;
import com.khandras.bot.fw.props.TelegramProps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class LongPollingBot extends TelegramLongPollingBot {
    private final String username;
    private final String token;
    private final ActionService actionService;

    public LongPollingBot(DefaultBotOptions options, TelegramProps props, ActionService actionService) {
        super(options);
        this.username = props.getUsername();
        this.token = props.getToken();
        this.actionService = actionService;
        log.info("LongPollingBot bean created");
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        var input = new IncomingMessageDto(update);
        try {
            actionService.handleMessage(input);
            var message = SendMessage.builder()
                    .chatId(input.getUserId())
                    .text("Your message is - " + input.getMessage().getText());
            execute(message.build());
        } catch (Exception e) {
            log.error("Exception occured: ", e);
            var message = SendMessage.builder()
                .chatId(input.getUserId())
                .text(" ERRORRRR !!!!!!!");
            execute(message.build());
        }
    }
}
