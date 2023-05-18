package ru.digitalleague.prerevolutionarytinderserver.servicies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.digitalleague.prerevolutionarytinderdatabase.dtos.FavoritePersonDto;
import ru.digitalleague.prerevolutionarytinderdatabase.dtos.PersonDto;
import ru.digitalleague.prerevolutionarytinderdatabase.entities.Person;
import ru.digitalleague.prerevolutionarytinderdatabase.enums.Gender;
import ru.digitalleague.prerevolutionarytinderdatabase.enums.Orientation;
import ru.digitalleague.prerevolutionarytinderdatabase.repositories.PersonRepository;
import ru.digitalleague.prerevolutionarytinderserver.mappers.PersonMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonService {

    PersonRepository personRepository;
    FavoriteListService favoriteListService;
    BlackListService blackListService;
    ImageService imageService;
    PersonMapper personMapper;

    @Autowired
    public PersonService(PersonRepository personRepository, FavoriteListService favoriteListService, BlackListService blackListService, ImageService imageService, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.favoriteListService = favoriteListService;
        this.blackListService = blackListService;
        this.imageService = imageService;
        this.personMapper = personMapper;
    }

    public boolean isRegisteredPersonByChatId(Long chatId) {
        log.info("Check is registered person by chatId {}", chatId);
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        return personOptional.isPresent();
    }

    public PersonDto getDatingProfilesByChatId(Long chatId) {
        log.info("Get dating profiles by chatId {}", chatId);
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person mainPerson = personOptional.orElse(null);

        if (mainPerson == null) return null;

        List<Person> favoriteDatingProfiles = personRepository.getFavoriteDatingProfilesByPersonId(mainPerson.getId());
        List<Person> anotherDatingProfiles = personRepository.getAnotherDatingProfilesByPersonId(mainPerson.getId(), mainPerson.getOrientation().getPartnersGenders(), mainPerson.getGender().getPartnersOrientations());
        favoriteDatingProfiles.addAll(anotherDatingProfiles);

        List<PersonDto> resultDatingProfiles = favoriteDatingProfiles.stream()
                .map(person -> personMapper.mapPersonOnPersonDto(person))
                .collect(Collectors.toList());

        if (resultDatingProfiles.isEmpty()) return new PersonDto();

        return resultDatingProfiles.get(0);
    }

    public List<FavoritePersonDto> getFavoritesProfilesByChatId(Long chatId) {
        log.info("Get favorites profiles by chatId {}", chatId);
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person mainPerson = personOptional.orElse(null);

        if (mainPerson == null) return new ArrayList<>();

        List<Person> favoriteDatingProfiles = personRepository.getDistinctFavoriteDatingProfilesByPersonId(mainPerson.getId());

        List<FavoritePersonDto> favoritePersonDtos = favoriteDatingProfiles.stream()
                .map(person -> personMapper.mapPersonOnFavoritePersonDto(mainPerson.getId(), person))
                .collect(Collectors.toList());

        return favoritePersonDtos;
    }

    public List<Byte> getAccountPicture(Long chatId) {
        log.info("Get account picture chatId {}", chatId);
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person person = personOptional.orElse(null);

        if (person == null) return null;

        return imageService.createImage(person.getId(), person.getHeader(), person.getAge().toString(), person.getDescription());
    }

    public boolean savePersonGender(Long chatId, String gender) {
        log.info("Save person by chatId {} with gender {}", chatId, gender);
        Person person = new Person();
        person.setChatId(chatId);
        person.setGender(Gender.valueOf(gender));
        personRepository.save(person);
        return true;
    }

    public boolean savePersonName(Long chatId, String personName) {
        log.info("Save person by chatId {} with name {}", chatId, personName);
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person person = personOptional.orElse(null);

        if (person == null) return false;

        person.setNickname(personName);
        personRepository.save(person);
        return true;
    }

    public boolean savePersonAboutInfo(Long chatId, String aboutText) {
        log.info("Save person by chatId {} with aboutText {}", chatId, aboutText);
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person person = personOptional.orElse(null);

        if (person == null) return false;

        person.setDescription(aboutText);
        personRepository.save(person);
        return true;
    }

    public boolean havePersonName(Long chatId) {
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person person = personOptional.orElse(null);

        if (person == null) {
            return false;
        }
        if (person.getNickname() == null || person.getNickname().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean savePersonOrientation(Long chatId, String orientation) {
        log.info("Save person by chatId {} with orientation {}", chatId, orientation);
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person person = personOptional.orElse(null);

        if (person == null) return false;

        person.setOrientation(Orientation.valueOf(orientation));
        personRepository.save(person);
        return true;
    }

    public boolean haveAgePersonByChatId(Long chatId) {
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person person = personOptional.orElse(null);

        if (person == null || person.getAge() == null) {
            return false;
        }
        return true;
    }

    public boolean saveAgePersonByChatId(Long chatId, Integer age) {
        log.info("Save person by chatId {} with age {}", chatId, age);
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person person = personOptional.orElse(null);

        if (person == null) return false;

        person.setAge(age);
        personRepository.save(person);
        return true;
    }

    public boolean haveHeaderPersonByChatId(Long chatId) {
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person person = personOptional.orElse(null);

        if (person == null || person.getHeader() == null) {
            return false;
        }
        return true;
    }

    public boolean saveHeaderPersonByChatId(Long chatId, String header) {
        log.info("Save person by chatId {} with header {}", chatId, header);
        Optional<Person> personOptional = personRepository.findByChatId(chatId);
        Person person = personOptional.orElse(null);

        if (person == null) return false;

        person.setHeader(header);
        personRepository.save(person);
        return true;

    }
}
