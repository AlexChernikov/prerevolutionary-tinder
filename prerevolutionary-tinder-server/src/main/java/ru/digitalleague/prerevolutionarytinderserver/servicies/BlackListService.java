package ru.digitalleague.prerevolutionarytinderserver.servicies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.digitalleague.prerevolutionarytinderdatabase.entities.BlackList;
import ru.digitalleague.prerevolutionarytinderdatabase.entities.Person;
import ru.digitalleague.prerevolutionarytinderdatabase.repositories.BlackListRepository;
import ru.digitalleague.prerevolutionarytinderdatabase.repositories.FavoriteListRepository;
import ru.digitalleague.prerevolutionarytinderdatabase.repositories.PersonRepository;

import java.util.Optional;

@Slf4j
@Service
public class BlackListService {

    PersonRepository personRepository;
    BlackListRepository blackListRepository;
    FavoriteListRepository favoriteListRepository;

    @Autowired
    public BlackListService(PersonRepository personRepository, BlackListRepository blackListRepository, FavoriteListRepository favoriteListRepository) {
        this.personRepository = personRepository;
        this.blackListRepository = blackListRepository;
        this.favoriteListRepository = favoriteListRepository;
    }

    public boolean addPersonToBlacklist(Long chatId, Long bannedPersonId) {
        log.info("At chat {} ban person with id {}", chatId, bannedPersonId);
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person person = personOptional.orElse(null);

//        if (person == null) return false;

        if (!blackListRepository.containsByPersonIdAndBannedPersonId(person.getId(), bannedPersonId)
         && !favoriteListRepository.containsByPersonIdAndFavoritePersonId(person.getId(), bannedPersonId)) {
            BlackList blackList = new BlackList();
            blackList.setPersonId(person.getId());
            blackList.setBannedPersonId(bannedPersonId);
            blackListRepository.save(blackList);
        }

        return true;
    }
}
