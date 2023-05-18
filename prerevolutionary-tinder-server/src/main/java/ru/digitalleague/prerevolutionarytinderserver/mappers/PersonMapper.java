package ru.digitalleague.prerevolutionarytinderserver.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.digitalleague.prerevolutionarytinderdatabase.dtos.FavoritePersonDto;
import ru.digitalleague.prerevolutionarytinderdatabase.dtos.PersonDto;
import ru.digitalleague.prerevolutionarytinderdatabase.entities.Person;
import ru.digitalleague.prerevolutionarytinderdatabase.enums.Gender;
import ru.digitalleague.prerevolutionarytinderdatabase.enums.Orientation;
import ru.digitalleague.prerevolutionarytinderdatabase.repositories.PersonRepository;
import ru.digitalleague.prerevolutionarytinderserver.servicies.BlackListService;
import ru.digitalleague.prerevolutionarytinderserver.servicies.FavoriteListService;
import ru.digitalleague.prerevolutionarytinderserver.servicies.ImageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonMapper {

    FavoriteListService favoriteListService;
    ImageService imageService;

    @Autowired
    public PersonMapper(FavoriteListService favoriteListService, ImageService imageService) {
        this.favoriteListService = favoriteListService;
        this.imageService = imageService;
    }

    public PersonDto mapPersonOnPersonDto(Person person) {
        log.debug("Mapping person on personDto personId {}", person.getId());
        PersonDto personDto = new PersonDto();
        personDto.setIsEmpty(false);
        personDto.setId(person.getId());
        personDto.setNickname(person.getNickname());
        personDto.setGender(person.getGender());
        personDto.setOrientation(person.getOrientation());
        personDto.setAge(person.getAge());
        personDto.setHeader(person.getHeader());
        personDto.setDescription(person.getDescription());
        List<Byte> imageFile = imageService.createImage(person.getId(), person.getHeader(), person.getAge().toString(), person.getDescription());
        personDto.setImageFile(imageFile);
        return personDto;
    }

    public FavoritePersonDto mapPersonOnFavoritePersonDto(Long mainPersonId, Person person) {
        log.debug("Mapping person on favoritePersonDto personId {}", person.getId());
        FavoritePersonDto favoritePersonDto = new FavoritePersonDto();
        favoritePersonDto.setId(person.getId());
        favoritePersonDto.setNickname(person.getNickname());
        favoritePersonDto.setGender(person.getGender());
        favoritePersonDto.setOrientation(person.getOrientation());
        favoritePersonDto.setAge(person.getAge());
        favoritePersonDto.setHeader(person.getHeader());
        favoritePersonDto.setDescription(person.getDescription());
        List<Byte>  imageFile = imageService.createImage(person.getId(), person.getHeader(), person.getAge().toString(), person.getDescription());
        favoritePersonDto.setImageFile(imageFile);
        favoritePersonDto.setRomanceStatus(favoriteListService.getRomanceStatus(mainPersonId, person.getId()));
        return favoritePersonDto;
    }
}
