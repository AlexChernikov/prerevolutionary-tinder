package ru.digitalleague.prerevolutionarytinderserver.servicies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.digitalleague.prerevolutionarytinderdatabase.entities.FavoriteList;
import ru.digitalleague.prerevolutionarytinderdatabase.entities.Person;
import ru.digitalleague.prerevolutionarytinderdatabase.enums.RomanceStatus;
import ru.digitalleague.prerevolutionarytinderdatabase.repositories.BlackListRepository;
import ru.digitalleague.prerevolutionarytinderdatabase.repositories.FavoriteListRepository;
import ru.digitalleague.prerevolutionarytinderdatabase.repositories.PersonRepository;

import java.util.Optional;

@Slf4j
@Service
public class FavoriteListService {

    PersonRepository personRepository;
    BlackListRepository blackListRepository;
    FavoriteListRepository favoriteListRepository;

    @Autowired
    public FavoriteListService(PersonRepository personRepository, BlackListRepository blackListRepository, FavoriteListRepository favoriteListRepository) {
        this.personRepository = personRepository;
        this.blackListRepository = blackListRepository;
        this.favoriteListRepository = favoriteListRepository;
    }

    public RomanceStatus getRomanceStatus(Long mainPersonId, Long personId) {
        log.debug("Get romance status by mainPersonId {} and personId {}", mainPersonId, personId);
        Optional<FavoriteList> mainPersonIdAndPersonIdFavoriteList = favoriteListRepository.findByPersonIdAndFavoritePersonId(mainPersonId, personId);
        FavoriteList favoriteList = mainPersonIdAndPersonIdFavoriteList.orElse(null);

        if (favoriteList != null) {
            return favoriteList.getRomanceStatus();
        } else {
            Optional<FavoriteList> personIdAndMainPersonIdFavoriteList = favoriteListRepository.findByPersonIdAndFavoritePersonId(personId, mainPersonId);
            favoriteList = personIdAndMainPersonIdFavoriteList.orElse(null);

            if (favoriteList != null) {
                return RomanceStatus.YOU_LIKED;
            } else {
                throw new RuntimeException("Cant find at FavoriteList romance status!");
            }
        }
    }

    public boolean addPersonToFavoritelist(Long chatId, Long favoritePersonId) {
        log.info("At chat {} add to favorite person with id {}", chatId, favoritePersonId);
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person person = personOptional.orElse(null);

//        if (person == null) return false;

        if (!favoriteListRepository.containsByPersonIdAndFavoritePersonId(person.getId(), favoritePersonId)
         && !blackListRepository.containsByPersonIdAndBannedPersonId(person.getId(), favoritePersonId)) {
            FavoriteList favoriteList = new FavoriteList();
            favoriteList.setPersonId(person.getId());
            favoriteList.setFavoritePersonId(favoritePersonId);
            favoriteList.setRomanceStatus(getRomanceStatusAndUpdate(person.getId(), favoritePersonId));
            favoriteListRepository.save(favoriteList);
        }

        return true;


    }

    private RomanceStatus getRomanceStatusAndUpdate(Long personId, Long favoritePersonId) {
        log.debug("Get romance status by mainPersonId {} and favoritePersonId {}", personId, favoritePersonId);
        Optional<FavoriteList> favoriteListOptional = favoriteListRepository.findByPersonIdAndFavoritePersonId(favoritePersonId, personId);
        FavoriteList favoriteList = favoriteListOptional.orElse(null);
        if (favoriteList == null) {
            return RomanceStatus.YOU_LIKE;
        } else {
            favoriteList.setRomanceStatus(RomanceStatus.MUTUALLY_LIKED);
            favoriteListRepository.save(favoriteList);
            return RomanceStatus.MUTUALLY_LIKED;
        }
    }
}
