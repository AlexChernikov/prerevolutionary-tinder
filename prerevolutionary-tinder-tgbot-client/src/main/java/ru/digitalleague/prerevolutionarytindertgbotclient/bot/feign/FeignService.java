package ru.digitalleague.prerevolutionarytindertgbotclient.bot.feign;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.digitalleague.prerevolutionarytinderdatabase.dtos.FavoritePersonDto;
import ru.digitalleague.prerevolutionarytinderdatabase.dtos.PersonDto;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.enums.ButtonCommandEnum;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.service.MessageService;

import java.util.List;

@Slf4j
@Service
public class FeignService {

    private final FeignClientInterface feignClientInterface;
    private final MessageService messageService;

    public FeignService(FeignClientInterface feignClientInterface,MessageService messageService) {
        this.feignClientInterface = feignClientInterface;
        this.messageService = messageService;
    }

    public boolean isRegistered(long chatId) {
        return feignClientInterface.isRegistered(chatId);
    }

    public void savePersonGender(long chatId, ButtonCommandEnum buttonCommandEnum) {
        feignClientInterface.savePersonGender(chatId, buttonCommandEnum.name());
    }

    public void savePersonName(String personName, long chatId) {
        feignClientInterface.savePersonName(chatId, personName);
    }

    public void saveAboutPersonInformation(String aboutText, long chatId) {
        feignClientInterface.savePersonAboutInfo(chatId, aboutText);
    }

    public void savePersonOrientation(long chatId, ButtonCommandEnum buttonCommandEnum) {
        feignClientInterface.savePersonOrientation(chatId, buttonCommandEnum.name());
    }

    public boolean haveName(long chatId) {
        return feignClientInterface.havePersonName(chatId);
    }

    public List<Byte> getAccountPicture(long chatId) {
        return feignClientInterface.getAccountPicture(chatId);
    }

    public boolean haveAge(long chatId) {
        return feignClientInterface.haveAge(chatId);
    }

    public boolean haveHeader(long chatId) {
        return feignClientInterface.haveHeader(chatId);
    }

    public void savePersonAge(int textCommand, long chatId) {
        feignClientInterface.savePersonAge(chatId, textCommand);
    }

    public void savePersonHeader(String textCommand, long chatId) {
        feignClientInterface.savePersonHeader(chatId, textCommand);
    }

    public PersonDto searchAccount(long chatId){
        return feignClientInterface.searchAccount(chatId);
    }

    public void setReactionToAccount(ButtonCommandEnum reaction, long chatId, long personId){
        if (reaction.equals(ButtonCommandEnum.LIKE)){
            feignClientInterface.addPersonToFavoritelist(chatId, personId);
        } else {
            feignClientInterface.addPersonToBlacklist(chatId, personId);
        }
    }

    public List<FavoritePersonDto> getFavoritesByChatId(long chatId) throws NotFoundException{
        List<FavoritePersonDto> favoritesByChatId = feignClientInterface.getFavoritesProfilesByChatId(chatId);
        if (favoritesByChatId.isEmpty()){
            throw new NotFoundException(messageService.getMessage("bot.command.menu.favorites.empty"));
        } else {
            return favoritesByChatId;
        }
    }
}
