package ru.digitalleague.prerevolutionarytindertgbotclient.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.digitalleague.prerevolutionarytindertgbotclient.bot.enums.ButtonCommandEnum;

@Slf4j
@Service
public class DbService {

    public boolean isRegistered(long chatId){
        //TODO
        //идем в БД, проверяем зарегистрировал ли пользователь по чат айди
        return false;
    }

    public void saveMale(long chatId, ButtonCommandEnum buttonCommandEnum) {
        //TODO
        //сохраняем в БД пол по енуму и чатАйдишке
    }

    public void savePersonName(String personName, long chatId) {
        //TODO
        //Сохраняем в БД имя по чатйдихе
    }

    public void saveAboutPersonInformation(String text, long chatId) {
        //TODO
        //Сохраняем в БД инфу о персоне по чатйдихе
    }

    public void saveSearchParam(long chatId, ButtonCommandEnum buttonCommandEnum) {
        //TODO
        //Сохраняем в БД параметр поиска (Енум параметр)
    }
}