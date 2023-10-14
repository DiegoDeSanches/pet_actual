package com.khandras.bot.adapter.telegram.listener;

import com.khandras.bot.adapter.telegram.listener.dto.IncomingMessageDto;
import com.khandras.bot.adapter.telegram.sender.TelegramMessageSender;
import com.khandras.bot.app.api.action.ActionService;
import com.khandras.bot.app.api.persistance.MessageLogRepository;
import com.khandras.bot.domain.message.MessageLog;
import com.khandras.bot.fw.props.TelegramProps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class LongPollingBot extends TelegramLongPollingBot {
    private final String username;
    private final String token;
    private final ActionService actionService;
    private final MessageLogRepository repo;
    private final TelegramMessageSender sender;

    public LongPollingBot(DefaultBotOptions options, TelegramProps props, ActionService actionService, MessageLogRepository repo, TelegramMessageSender sender) {
        super(options);
        this.username = props.getUsername();
        this.token = props.getToken();
        this.actionService = actionService;
        this.repo = repo;
        this.sender = sender;
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
        saveMessageLog(update);
        var input = new IncomingMessageDto(update);
        try {
            actionService.handleMessage(input);
        } catch (Exception e) {
            log.error("Exception occured: ", e);
            sender.sendFailMessage(input.getUserId(), e.getLocalizedMessage());
        }
    }

    private void saveMessageLog(Update update) {
        var infoToSave = SerializationUtils.serialize(update.getMessage());
        var messageLog = new MessageLog()
                .setTelegramId(update.getMessage().getChatId())
                .setMessageText(update.getMessage().getText())
                .setMessageFull(update)
                .setSavedInfo(infoToSave);

        repo.save(messageLog);
    }
}
