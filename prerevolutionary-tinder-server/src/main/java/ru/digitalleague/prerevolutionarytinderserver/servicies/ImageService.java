package ru.digitalleague.prerevolutionarytinderserver.servicies;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ImageService {

    @Value("${file.path.name}")
    public String filePathName;
    private static final int STRING_SIZE = 50;
    private static final List<String> wordEndings = List.of("б ",
            "в ",
            "г ",
            "д ",
            "ж ",
            "з ",
            "к ",
            "л ",
            "м ",
            "н ",
            "п ",
            "р ",
            "с ",
            "т ",
            "ф ",
            "х ",
            "ц ",
            "ч ",
            "ш ",
            "щ ");
    private static final List<String> iVowels = List.of("иа",
            "ие",
            "иё",
            "ио",
            "иу",
            "иы",
            "иэ",
            "ию",
            "ия",
            "Иа",
            "Ие",
            "Иё",
            "Ио",
            "Иу",
            "Иы",
            "Иэ",
            "Ию",
            "Ия");
    private static final List<String> W_O_R_D_S = List.of("еда",
            "ем",
            "есть",
            "обед",
            "обедня",
            "сыроежка",
            "сыроега",
            "медведь",
            "снедь",
            "едкий",
            "ехать",
            "езда",
            "уезд",
            "еду",
            "ездить",
            "поезд",
            "бег",
            "беглец",
            "беженец",
            "забегаловка",
            "избегать",
            "избежать",
            "набег",
            "перебежчик",
            "пробег",
            "разбег",
            "убежище",
            "центробежная сила",
            "беда",
            "бедный",
            "победить",
            "убедить",
            "убеждение",
            "белый",
            "бельё",
            "белка",
            "бельмо",
            "белуга",
            "бес",
            "бешеный",
            "обет",
            "обещать",
            "веять",
            "веер",
            "ветер",
            "ветвь",
            "веха",
            "ведать",
            "веди",
            "весть",
            "повесть",
            "ве́дение",
            "вежливый",
            "невежда",
            "вежды",
            "вежа",
            "век",
            "вечный",
            "увечить",
            "веко",
            "венок",
            "венец",
            "вениквенок",
            "венец",
            "веник",
            "вено",
            "вера",
            "вероятно",
            "суеверие",
            "вес",
            "вешать",
            "повеса",
            "равновесие",
            "звезда",
            "зверь",
            "невеста",
            "ответ",
            "совет",
            "привет",
            "завет",
            "вещать",
            "вече",
            "свежий",
            "свежеть",
            "свет",
            "свеча",
            "просвещение",
            "светец",
            "светёлка",
            "Светлана",
            "цвет",
            "цветы",
            "цвести",
            "человек",
            "человеческий",
            "деть",
            "девать",
            "одеть",
            "одевать",
            "одеяло",
            "одеяние",
            "дело",
            "делать",
            "действие",
            "неделя",
            "надеяться",
            "свидетель",
            "дева",
            "девочка",
            "дед",
            "делить",
            "предел",
            "дети",
            "детёныш",
            "детка",
            "детство",
            "зевать",
            "зев",
            "ротозей",
            "зело",
            "зеница",
            "зенки",
            "левый",
            "левша",
            "лезть",
            "лестница",
            "облезлый",
            "лекарь",
            "лечить",
            "лекарство",
            "лень","",
            "ленивец",
            "ленивый",
            "лентяй",
            "лепота",
            "великолепный",
            "лепить",
            "нелепый",
            "слепок",
            "лес",
            "лесник",
            "лесничий",
            "лесопилка",
            "леший",
            "лето",
            "долголетие",
            "Летов",
            "летоисчисление",
            "летописец",
            "летопись",
            "малолетка",
            "однолетка",
            "пятилетка",
            "совершеннолетие",
            "леха́",
            "бледный",
            "железо",
            "железняк",
            "калека",
            "калечить",
            "клеть",
            "клетка",
            "колено",
            "наколенник",
            "поколение",
            "лелеять",
            "млеть",
            "плен",
            "пленённый",
            "пленить",
            "пленник",
            "плесень",
            "плешь",
            "Плеханов",
            "полено",
            "след",
            "последователь",
            "последствие",
            "преследовать",
            "следить",
            "следопыт",
            "слепой",
            "телега",
            "тележный",
            "тлен",
            "тление",
            "тленный",
            "хлеб",
            "хлев",
            "медь",
            "медный",
            "мел",
            "менять",
            "изменник",
            "непременно",
            "мера",
            "намерение",
            "лицемер",
            "месяц",
            "месить",
            "мешать",
            "помеха",
            "место",
            "мещанин",
            "помещик",
            "наместник",
            "метить",
            "замечать",
            "примечание",
            "сметить",
            "смета",
            "мех",
            "мешок",
            "змей",
            "змея",
            "сметь",
            "смелый",
            "смеяться",
            "смех",
            "нега",
            "нежный",
            "нежить",
            "недра",
            "внедрить",
            "немой",
            "немец",
            "нет",
            "отнекаться",
            "гнев",
            "гнедой",
            "гнездо",
            "загнетка",
            "снег",
            "снежный",
            "мнение",
            "сомнение",
            "сомневаться",
            "петь",
            "песня",
            "петух",
            "пегий",
            "пена",
            "пенязь",
            "пестовать",
            "пестун",
            "пехота",
            "пеший",
            "опешить",
            "спеть",
            "спех",
            "спешить",
            "успех",
            "реять",
            "река",
            "речь",
            "наречие",
            "редкий",
            "редька",
            "резать",
            "резвый",
            "репа",
            "репица",
            "ресница",
            "обретать",
            "обрести",
            "сретение",
            "встречать",
            "прореха",
            "решето",
            "решётка",
            "решать",
            "решить",
            "грех",
            "грешный",
            "зреть",
            "созреть",
            "зрелый",
            "зрение",
            "крепкий",
            "крепиться",
            "орех",
            "преть",
            "прелый",
            "прение",
            "пресный",
            "свирепый",
            "свирель",
            "стрела",
            "стрелять",
            "стреха",
            "застреха",
            "хрен",
            "сусек",
            "сеять",
            "семя",
            "север",
            "седло",
            "сесть",
            "беседа",
            "сосед",
            "седой",
            "седеть",
            "секу",
            "сечь",
            "сеча",
            "сечение",
            "просека",
            "насекомое",
            "сень",
            "осенять",
            "сени",
            "сено",
            "серый",
            "сера",
            "посетить",
            "посещать",
            "сетовать",
            "сеть",
            "сетка",
            "стена",
            "застенок",
            "застенчивый",
            "стенгазета",
            "тело",
            "мягкотелость",
            "растелешиться",
            "тельняшка",
            "тень",
            "оттенок",
            "тенёк.",
            "тесто",
            "тесный",
            "стеснять",
            "стесняться",
            "теснить",
            "тесниться",
            "затеять",
            "затея",
            "утеха",
            "потеха",
            "тешить",
            "утешение",
            "хер",
            "похерить",
            "цевка",
            "цевье",
            "цевница",
            "цедить",
            "целый",
            "исцелять",
            "целовать",
            "поцелуй",
            "цель",
            "целиться",
            "цена",
            "цепь",
            "цеплять",
            "цеп");

    public List<Byte> createImage(Long id, String header, String age, String description) {
        BufferedImage bufferedImage = null;
        InputStream resourceAsStream = ImageService.class.getClassLoader().getResourceAsStream(filePathName);
        try {
            bufferedImage = ImageIO.read(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.DARK_GRAY);

        graphics.setFont(new Font("Old Standard TT", Font.BOLD, 50));

        header = header.replace('ф', 'ѳ');
        header = header.replace('Ф', 'ѳ');
        graphics.drawString(header, 20, 100);

        graphics.setFont(new Font("Old Standard TT", Font.PLAIN, 20));
        graphics.drawString(age + " лѣтъ", 30, 150);

        description = translateDescription(description);

        if (description.length() <= STRING_SIZE) {
            graphics.drawString(description, 30, 200);
        } else {
            List<String> descriptions = getDescriptionList(description);
            for (int i = 0; i < descriptions.size(); i++) {
                graphics.drawString(descriptions.get(i), 30, 200 + 30*i);

            }
        }



        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", bas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Byte> byteArray =  Arrays.asList(ArrayUtils.toObject(bas.toByteArray()));

//        InputStream inputStream = new ByteArrayInputStream(byteArray);
//        BufferedImage image = null;
//        try {
//            image = ImageIO.read(inputStream);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        File outputfile = new File("info" + id.toString() + ".png");
//
//        try {
//            ImageIO.write(image, "png", outputfile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        return byteArray;
    }

    private String translateDescription(String description) {
        /**
         *
         * В условии задачи Боря привёл список из конкретных слов, которые надо менять.
         * Поэтому проверяются не все слова, а те, что дал Боря.
         */
        for (String word : W_O_R_D_S) {
            description = description.replaceAll(word, word.replace("е", "ѣ"));
        }

        /**
         * ъ (он же ер) пишется в конце всякого слова, оканчивающегося на согласную: столъ, телефонъ, Санктъ-Петербургъ.
         * Это касается и слов с шипящими согласными в конце: мячъ, ужъ замужъ невтерпежъ.
         * (с) Боря
         *  **/
        for (String wordEnding : wordEndings) {
            description = description.replaceAll(wordEnding, wordEnding.substring(0, 1) + "ъ ");
        }
        for (String iVowel : iVowels) {
            description = description.replaceAll(iVowel, "i" + iVowel.substring(1, 2));
        }

        return description;
    }

    //Разбиваем описание на лист строк, каждая длиной до 50 символов.
    private static List<String> getDescriptionList(String description) {
        List<String> descriptions = new ArrayList<>();
        while (description.length() > STRING_SIZE) {
            //Откусываем от начала дискрипшина строку длинной в 50 символов.
            for (int i = STRING_SIZE; i > 0; i--) {
                char c = description.charAt(i);
                //Ищем первый пробел с конца, чтоб не обрезать слова и выдавать ровную строку.
                if (c == ' ') {
                    //Обрезаем дескрипшин по символу пробела.
                    String newString = description.substring(0, i);
                    //Записываем остаток дискрипшина.
                    description = description.substring(i+1, description.length());
                    //Сохраняем откусанную строку.
                    descriptions.add(newString);
                    break;
                }
            }
        }
        //Последний остаток тоже сохраняем.
        descriptions.add(description);
        return descriptions;
    }
}
