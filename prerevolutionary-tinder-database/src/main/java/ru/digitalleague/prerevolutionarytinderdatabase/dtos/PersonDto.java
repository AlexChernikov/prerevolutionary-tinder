package ru.digitalleague.prerevolutionarytinderdatabase.dtos;

import lombok.Getter;
import lombok.Setter;
import ru.digitalleague.prerevolutionarytinderdatabase.enums.Gender;
import ru.digitalleague.prerevolutionarytinderdatabase.enums.Orientation;

import java.util.List;

@Getter
@Setter
public class PersonDto {
    private Boolean isEmpty = true;
    private Long id;
    private String nickname;
    private Gender gender;
    private Orientation orientation;
    private Integer age;
    private String header;
    private String description;
    private List<Byte> imageFile;
}
