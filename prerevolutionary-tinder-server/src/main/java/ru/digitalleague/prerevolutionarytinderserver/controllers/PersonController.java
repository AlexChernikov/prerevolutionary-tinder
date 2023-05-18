package ru.digitalleague.prerevolutionarytinderserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.digitalleague.prerevolutionarytinderdatabase.dtos.FavoritePersonDto;
import ru.digitalleague.prerevolutionarytinderdatabase.dtos.PersonDto;
import ru.digitalleague.prerevolutionarytinderserver.servicies.BlackListService;
import ru.digitalleague.prerevolutionarytinderserver.servicies.FavoriteListService;
import ru.digitalleague.prerevolutionarytinderserver.servicies.PersonService;

import java.util.List;

@RestController
@RequestMapping(value = "/person-controller")
public class PersonController {

    @Autowired
    PersonService personService;
    @Autowired
    FavoriteListService favoriteListService;
    @Autowired
    BlackListService blackListService;

    @GetMapping(value = "/is-registered-person-by-chat-id/{chatId}")
    public boolean isRegisteredPersonByChatId(@PathVariable("chatId") Long chatId) {
        return personService.isRegisteredPersonByChatId(chatId);
    }

    @GetMapping(value = "/get-dating-profiles-by-chat-id/{chatId}")
    public PersonDto getDatingProfilesByChatId(@PathVariable("chatId") Long chatId) {
        return personService.getDatingProfilesByChatId(chatId);
    }

    @GetMapping(value = "/get-favorites-profiles-by-chat-id/{chatId}")
    public List<FavoritePersonDto> getFavoritesProfilesByChatId(@PathVariable("chatId") Long chatId) {
        return personService.getFavoritesProfilesByChatId(chatId);
    }

    @GetMapping(value = "/get-account-picture/{chatId}")
    public @ResponseBody List<Byte> getAccountPicture(@RequestParam("chatId") Long chatId) {
        return personService.getAccountPicture(chatId);
    }

    @GetMapping(value = "/save-person-gender/{chatId}/{gender}")
    public void savePersonGender(@RequestParam("chatId") Long chatId, @RequestParam("gender") String gender) {
        personService.savePersonGender(chatId, gender);
    }

    @GetMapping(value = "/save-person-name/{chatId}/{personName}")
    public void savePersonName(@RequestParam("chatId") Long chatId, @RequestParam("personName") String personName) {
        personService.savePersonName(chatId, personName);
    }

    @GetMapping(value = "/save-person-about-info/{chatId}/{aboutText}")
    public void savePersonAboutInfo(@RequestParam("chatId") Long chatId , @RequestParam("aboutText") String aboutText) {
        personService.savePersonAboutInfo(chatId, aboutText);
    }

    @GetMapping(value = "/have-person-name/{chatId}/")
    public boolean havePersonName(@RequestParam("chatId") Long chatId) {
        return personService.havePersonName(chatId);
    }

    @GetMapping(value = "/save-person-orientation/{chatId}/{orientation}")
    public void savePersonOrientation(@RequestParam("chatId") Long chatId, @RequestParam("orientation") String orientation) {
        personService.savePersonOrientation(chatId, orientation);
    }

    @GetMapping(value = "/add-person-to-blacklist/{chatId}/{bannedPersonId}")
    public boolean addPersonToBlacklist(@RequestParam("chatId") Long chatId, @RequestParam("bannedPersonId") Long bannedPersonId) {
        return blackListService.addPersonToBlacklist(chatId, bannedPersonId);
    }

    @GetMapping(value = "/add-person-to-favotitelist/{chatId}/{favoritePersonId}")
    public boolean addPersonToFavoritelist(@RequestParam("chatId") Long chatId, @RequestParam("favoritePersonId") Long favoritePersonId) {
        return favoriteListService.addPersonToFavoritelist(chatId, favoritePersonId);
    }

    @GetMapping(value = "/have-person-age/{chatId}")
    boolean haveAge(@RequestParam("chatId") Long chatId) {
        return personService.haveAgePersonByChatId(chatId);
    }

    @GetMapping(value = "/have-person-header/{chatId}")
    boolean haveHeader(@RequestParam("chatId") Long chatId) {
        return personService.haveHeaderPersonByChatId(chatId);

    }

    @GetMapping(value = "/save-person-age/{chatId}/{age}")
    void savePersonAge(@RequestParam("chatId") Long chatId, @RequestParam("age") Integer age) {
        personService.saveAgePersonByChatId(chatId, age);
    }

    @GetMapping(value = "/save-person-header/{chatId}/{header}")
    void savePersonHeader(@RequestParam("chatId") Long chatId, @RequestParam("header") String header) {
        personService.saveHeaderPersonByChatId(chatId, header);
    }
}
