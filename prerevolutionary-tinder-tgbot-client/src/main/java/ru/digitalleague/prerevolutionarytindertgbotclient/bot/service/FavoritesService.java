package ru.digitalleague.prerevolutionarytindertgbotclient.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.digitalleague.prerevolutionarytinderdatabase.dtos.FavoritePersonDto;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.entity.ImageMessageDto;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.feign.FeignService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FavoritesService {

    private final FeignService dbService;

    private final PictureService pictureService;

    private final MessageService messageService;

    public FavoritesService(FeignService dbService, PictureService pictureService, MessageService messageService) {
        this.dbService = dbService;
        this.pictureService = pictureService;
        this.messageService = messageService;
    }

    public List<ImageMessageDto> getFavorites(long chatId){
        List<FavoritePersonDto> favoritesList = dbService.getFavoritesByChatId(chatId);
        List<ImageMessageDto> imageMessageDtoList = new ArrayList<>();
        ImageMessageDto imageMessageDto = new ImageMessageDto();
        SendMessage sendMessage = new SendMessage();

        if (favoritesList.isEmpty()){
            sendMessage.setChatId(chatId);
            sendMessage.setText(messageService.getMessage("bot.command.menu.favorites.empty"));
            imageMessageDto.setSendMessage(sendMessage);
            imageMessageDtoList.add(imageMessageDto);
            return imageMessageDtoList;
        }

        for (FavoritePersonDto favoritePersonDto : favoritesList) {
            sendMessage.setChatId(chatId);
            sendMessage.setText(favoritePersonDto.getRomanceStatus().getRussianName());
            File picture = pictureService.createPicture(favoritePersonDto.getId(), favoritePersonDto.getImageFile());
            imageMessageDto.setSendPhoto(pictureService.getSendPhoto(chatId, picture));
            imageMessageDto.setSendMessage(sendMessage);
            imageMessageDtoList.add(imageMessageDto);
        }
        return imageMessageDtoList;
    }
}
