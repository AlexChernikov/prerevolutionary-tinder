package ru.digitalleague.prerevolutionarytindertgbotclient.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.entity.ImageMessageDto;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.service.BotCommandService;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.service.MessageService;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.service.ParseCommandService;
import ru.digitalleague.prerevolutionarytindertgbotclient.config.BotConfiguration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfiguration botConfig;

    private final BotCommandService botCommandService;

    private final ParseCommandService parseCommandService;

    private final MessageService messageService;

    public TelegramBot(BotConfiguration botConfig, BotCommandService botCommandService,
                       ParseCommandService parseCommandService, MessageService messageService) {
        this.botConfig = botConfig;
        this.botCommandService = botCommandService;
        this.parseCommandService = parseCommandService;
        this.messageService = messageService;
    }

    public void initBotCommands() {
        try {
            List<BotCommand> botCommands = botCommandService.getBotCommands();
            execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error at create and setting bot`s command: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.botName();
    }

    @Override
    public String getBotToken() {
        return botConfig.botToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        SendPhoto sendPhoto = null;
        List<ImageMessageDto> imageMessageDtoList = null;

        if (update.getMessage() == null && update.getCallbackQuery() == null) {
            sendMessage.setChatId(update.getMessage().getChatId());
            sendMessage.setText(messageService.getMessage("message.bot.command.emptyCommand"));
            sendMessage(sendMessage, sendPhoto);
        } else {
            imageMessageDtoList = directionMessage(update, sendMessage, sendPhoto);
            for (ImageMessageDto dto : imageMessageDtoList) {
                sendMessage(dto.getSendMessage(), dto.getSendPhoto());
            }
        }
    }

    private void sendMessage(SendMessage sendMessage, SendPhoto sendPhoto) {
        try {
            if (sendPhoto != null) {
                log.info("execute photo");
                execute(sendPhoto);
                log.info("execute photo complete");
            }
            log.info("execute message");
            execute(sendMessage);
            log.info("execute message complete");
        } catch (TelegramApiException e) {
            log.error("Error sending message or photo: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<ImageMessageDto> directionMessage(Update update, SendMessage sendMessage, SendPhoto sendPhoto) {
        ImageMessageDto imageMessageDto = new ImageMessageDto();
        List<ImageMessageDto> imageMessageDtoList = new ArrayList<>();
        if (update.getMessage() != null && update.getMessage().getText().startsWith("/")) {
            sendMessage = parseCommandService.parseCommand(update.getMessage().getText(), update.getMessage().getChatId());
            imageMessageDto.setSendMessage(sendMessage);
            imageMessageDtoList.add(imageMessageDto);
        } else if (update.hasCallbackQuery()) {
            imageMessageDtoList = parseCommandService.parseButtonCommand(update.getCallbackQuery().getData(), update.getCallbackQuery().getMessage().getChatId());
        } else if (update.getMessage() != null && update.getMessage().getText() != null) {
            sendMessage = parseCommandService.parseInputText(update.getMessage().getText(), update.getMessage().getChatId());
            imageMessageDto.setSendMessage(sendMessage);
            imageMessageDtoList.add(imageMessageDto);
        }
        return imageMessageDtoList;
    }
}
