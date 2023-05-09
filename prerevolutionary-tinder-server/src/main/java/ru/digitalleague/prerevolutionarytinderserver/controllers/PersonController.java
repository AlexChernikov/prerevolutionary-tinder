package ru.digitalleague.prerevolutionarytinderserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.digitalleague.prerevolutionarytinderdatabase.dtos.FavoritePersonDto;
import ru.digitalleague.prerevolutionarytinderdatabase.dtos.PersonDto;
import ru.digitalleague.prerevolutionarytinderserver.servicies.PersonService;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping(value = "/person-controller")
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping(value = "/is-registered-person-by-chat-id/{chatId}")
    public boolean isRegisteredPersonByChatId(@PathVariable("chatId") Long chatId) {
        return personService.isRegisteredPersonByChatId(chatId);
    }

    @GetMapping(value = "/get-dating-profiles-by-chat-id/{chatId}")
    public List<PersonDto> getDatingProfilesByChatId(@PathVariable("chatId") Long chatId) {
        return personService.getDatingProfilesByChatId(chatId);
    }

    @GetMapping(value = "/get-favorites-profiles-by-chat-id/{chatId}")
    public List<FavoritePersonDto> getFavoritesProfilesByChatId(@PathVariable("chatId") Long chatId) {
        return personService.getFavoritesProfilesByChatId(chatId);
    }

    @GetMapping(value = "/get-account-picture/{chatId}")
    public File getAccountPicture(@RequestParam("chatId") Long chatId) {
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
        return personService.addPersonToBlacklist(chatId, bannedPersonId);
    }

    @GetMapping(value = "/add-person-to-favotitelist/{chatId}/{favoritePersonId}")
    public boolean addPersonToFavoritelist(@RequestParam("chatId") Long chatId, @RequestParam("favoritePersonId") Long favoritePersonId) {
        return personService.addPersonToFavoritelist(chatId, favoritePersonId);
    }
}