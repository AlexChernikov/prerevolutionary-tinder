package ru.digitalleague.prerevolutionarytindertgbotclient.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.enums.BotCommandEnum;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.enums.ButtonCommandEnum;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.feign.FeignService;

import java.util.*;

@Slf4j
@Service
public class ButtonService {

    private final FeignService dbService;

    private final MessageService messageService;

    public ButtonService(FeignService dbService, MessageService messageService) {
        this.dbService = dbService;
        this.messageService = messageService;
    }

    public SendMessage getButtonByCommand(BotCommandEnum commandEnum, long chatId) {
        if (Objects.requireNonNull(commandEnum).equals(BotCommandEnum.START)) {
            if (!dbService.isRegistered(chatId)) {
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put(messageService.getMessage("bot.command.person.male.description"),
                        messageService.getMessage("bot.command.person.male.name"));
                paramMap.put(messageService.getMessage("bot.command.person.female.description"),
                        messageService.getMessage("bot.command.person.female.name"));

                SendMessage sendMessage = createKeyboardButtons(paramMap);
                sendMessage.setText(messageService.getMessage("bot.command.person.whoareyou"));
                sendMessage.setChatId(chatId);
                return sendMessage;
            } else {
                return getMenuButtons(chatId);
            }
        }
        return new SendMessage();
    }

    private SendMessage createKeyboardButtons(Map<String, String> paramMap) {
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(entry.getValue());
            button.setCallbackData(entry.getKey());
            rowInLine.add(button);
        }

        rowsInLine.add(rowInLine);
        markup.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(markup);

        return sendMessage;
    }

    public SendMessage getButtonByCommand(ButtonCommandEnum buttonCommandEnum, long chatId, long id) {
        //Не понимаю почему и зачем нужно это перенести в Енум.
        switch (buttonCommandEnum) {
            case ABOUT -> {
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put(messageService.getMessage("bot.command.search.male.description"),
                        messageService.getMessage("bot.command.search.male.name"));
                paramMap.put(messageService.getMessage("bot.command.search.female.description"),
                        messageService.getMessage("bot.command.search.female.name"));
                paramMap.put(messageService.getMessage("bot.command.search.all_search.description"),
                        messageService.getMessage("bot.command.search.all_search.name"));

                SendMessage sendMessage = createKeyboardButtons(paramMap);
                sendMessage.setText(messageService.getMessage("bot.command.search.whoareyou"));
                sendMessage.setChatId(chatId);
                return sendMessage;
            }
            case SEARCH -> {
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put(messageService.getMessage("bot.command.search_menu.like.description") + id,
                        messageService.getMessage("bot.command.search_menu.like.name"));
                paramMap.put(messageService.getMessage("bot.command.search_menu.dislike.description") + id,
                        messageService.getMessage("bot.command.search_menu.dislike.name"));

                SendMessage sendMessage = createKeyboardButtons(paramMap);
                sendMessage.setText(messageService.getMessage("bot.command.search_menu.reaction"));
                sendMessage.setChatId(chatId);
                return sendMessage;
            }
        }
        return new SendMessage();
    }

    //Аналогично, немного не понимаю зачем переносить в енум.
    public SendMessage getMenuButtons(long chatId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(messageService.getMessage("bot.command.menu.search.description"),
                messageService.getMessage("bot.command.menu.search.name"));
        paramMap.put(messageService.getMessage("bot.command.menu.account.description"),
                messageService.getMessage("bot.command.menu.account.name"));
        paramMap.put(messageService.getMessage("bot.command.menu.favorites.description"),
                messageService.getMessage("bot.command.menu.favorites.name"));

        SendMessage sendMessage = createKeyboardButtons(paramMap);
        sendMessage.setText(messageService.getMessage("bot.command.menu.menu"));
        sendMessage.setChatId(chatId);
        return sendMessage;
    }
}
