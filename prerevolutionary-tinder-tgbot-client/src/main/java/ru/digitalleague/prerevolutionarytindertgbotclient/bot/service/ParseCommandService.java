package ru.digitalleague.prerevolutionarytindertgbotclient.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.entity.ImageMessageDto;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.enums.BotCommandEnum;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.enums.ButtonCommandEnum;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.feign.FeignService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ParseCommandService {

    private final ButtonService buttonService;

    private final FeignService dbService;

    private final MessageService messageService;

    private final PictureService pictureService;

    private final AccountService searchService;

    private final FavoritesService favoritesService;

    public ParseCommandService(ButtonService buttonService, FeignService dbService,
                               MessageService messageService, PictureService pictureService,
                               AccountService searchService, FavoritesService favoritesService) {
        this.buttonService = buttonService;
        this.dbService = dbService;
        this.messageService = messageService;
        this.pictureService = pictureService;
        this.searchService = searchService;
        this.favoritesService = favoritesService;
    }

    private BotCommandEnum parseBotCommand(String text) {
        String command = text.replaceAll("/", "");
        return BotCommandEnum.valueOf(command.toUpperCase());
    }

    public SendMessage parseCommand(String textCommand, long chatId) {
        log.info("Parse command");
        SendMessage sendMessage = new SendMessage();
        BotCommandEnum botAndButtonCommandEnum = null;
        try{
            botAndButtonCommandEnum = parseBotCommand(textCommand);
        } catch (NullPointerException | IllegalArgumentException nullPointerException) {
            log.error(nullPointerException.getMessage());
            sendMessage.setText(messageService.getMessage("message.bot.command.unknown"));
            sendMessage.setChatId(chatId);
            return sendMessage;
        }

        switch (botAndButtonCommandEnum) {
            case START: {
                sendMessage = buttonService.getButtonByCommand(botAndButtonCommandEnum, chatId);
            }
            case HELP: {
                sendMessage.setText(messageService.getMessage("bot.command.help"));
                sendMessage.setChatId(chatId);
            }
        }
        return sendMessage;
    }

    public List<ImageMessageDto> parseButtonCommand(String data, long chatId) {
        log.info("Parse button command");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        long likedOrDislikedId = 0;
        ButtonCommandEnum buttonCommandEnum = createButtonCommandEnum(data);
        List<ImageMessageDto> imageMessageDtoList = new ArrayList<>();
        ImageMessageDto imageMessageDto = new ImageMessageDto();

        if (buttonCommandEnum.equals(ButtonCommandEnum.MALE) || buttonCommandEnum.equals(ButtonCommandEnum.FEMALE)) {
            dbService.savePersonGender(chatId, buttonCommandEnum);
            sendMessage.setText(messageService.getMessage("bot.command.person.whatyourname"));
        } else if (buttonCommandEnum.equals(ButtonCommandEnum.MALE_SEARCH)
                || buttonCommandEnum.equals(ButtonCommandEnum.FEMALE_SEARCH)
                || buttonCommandEnum.equals(ButtonCommandEnum.ALL_SEARCH)) {

            dbService.savePersonOrientation(chatId, buttonCommandEnum);
            imageMessageDto.setSendPhoto(pictureService.getPicture(chatId));
            sendMessage = buttonService.getMenuButtons(chatId);
        } else if (buttonCommandEnum.equals(ButtonCommandEnum.ACCOUNT)) {
            imageMessageDto.setSendPhoto(pictureService.getPicture(chatId));
            sendMessage = buttonService.getMenuButtons(chatId);
        } else if (buttonCommandEnum.equals(ButtonCommandEnum.SEARCH)) {
            imageMessageDtoList.add(searchService.searchAccount(chatId));
            return imageMessageDtoList;
        } else if (buttonCommandEnum.equals(ButtonCommandEnum.LIKE) || buttonCommandEnum.equals(ButtonCommandEnum.DISLIKE)) {
            likedOrDislikedId = parseLikedOrDislikedId(data, buttonCommandEnum.name());
            dbService.setReactionToAccount(buttonCommandEnum, chatId, likedOrDislikedId);
            imageMessageDtoList.add(searchService.searchAccount(chatId));
            return imageMessageDtoList;
        } else if (buttonCommandEnum.equals(ButtonCommandEnum.FAVORITES)) {
            imageMessageDtoList = favoritesService.getFavorites(chatId);
            sendMessage = buttonService.getMenuButtons(chatId);
        }
        imageMessageDto.setSendMessage(sendMessage);
        imageMessageDtoList.add(imageMessageDto);

        return imageMessageDtoList;
    }

    private ButtonCommandEnum createButtonCommandEnum(String data) {
        ButtonCommandEnum buttonCommandEnum;

        if (isDislikeCommand(data)) {
            buttonCommandEnum = ButtonCommandEnum.DISLIKE;
        } else if (isLikeCommand(data)) {
            buttonCommandEnum = ButtonCommandEnum.LIKE;
        } else {
            buttonCommandEnum = ButtonCommandEnum.valueOf(data.toUpperCase().substring(1));
        }
        return buttonCommandEnum;
    }

    private long parseLikedOrDislikedId(String data, String replacer) {
        return Long.parseLong(data.toUpperCase().substring(1).replace(replacer, ""));
    }

    private boolean isLikeCommand(String command) {
        return command.contains("like");
    }

    private boolean isDislikeCommand(String command) {
        return command.contains("dislike");
    }

    public SendMessage parseInputText(String textCommand, long chatId) {

        if (!dbService.haveName(chatId)) {
            return parsePersonName(textCommand, chatId);
        } else if (!dbService.haveAge(chatId)) {
            return parseAge(textCommand, chatId);
        } else if (!dbService.haveHeader(chatId)) {
            return parseHeader(textCommand, chatId);
        } else {
            return parseAboutPerson(textCommand, chatId);
        }
    }

    private SendMessage parseHeader(String textCommand, long chatId) {
        log.info("Parse person header command");
        dbService.savePersonHeader(textCommand, chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageService.getMessage("bot.command.person.persondescription"));
        return sendMessage;
    }

    private SendMessage parseAge(String textCommand, long chatId) {
        log.info("Parse person age command");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (!textCommand.isEmpty() && textCommand.length() <= 3) {
            dbService.savePersonAge(Integer.parseInt(textCommand), chatId);
            sendMessage.setText(messageService.getMessage("bot.command.person.header"));
        } else {
            sendMessage.setText(messageService.getMessage(("message.bot.command.uncorrectage")));
        }
        return sendMessage;
    }

    private SendMessage parsePersonName(String personName, long chatId) {
        log.info("Parse person name command");
        dbService.savePersonName(personName, chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageService.getMessage("bot.command.person.age"));
        return sendMessage;
    }

    private SendMessage parseAboutPerson(String textCommand, long chatId) {
        log.info("Parse about person command");
        dbService.saveAboutPersonInformation(textCommand, chatId);
        return buttonService.getButtonByCommand(ButtonCommandEnum.ABOUT, chatId, 0);
    }
}
