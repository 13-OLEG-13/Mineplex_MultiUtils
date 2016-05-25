package ru.luvas.multiutils.achievements;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import static ru.luvas.multiutils.achievements.AchievementSection.*;

/**
 *
 * @author RinesThaix
 */
public enum Achievement {
    WELCOME("Добро пожаловать!", GENERAL, 0, "Зайди на наш проект впервые"),
    FIRST_MESSAGE("Оно говорит!!", GENERAL, 0, "Напишите свое первое сообщение в чат"),
    PRO("ПРОфессионал", GENERAL, 0, "Приобретите группу VIP"),
    PRO_PLUS("Лучший из лучших", GENERAL, 0, "Приобретите группу VIP+"),
    TITAN("I am rich, baby", GENERAL, 0, "Приобретите группу RICH"),
    YOUTUBE("Камера, мотор!", GENERAL, 0, "Получите группу ютубера"),
    BUILDER("Прирожденный архитектор", GENERAL, 0, "Получите группу строителя"),
    FIRST_FRIEND("Уже не одинок", GENERAL, 0, "Найдите своего первого друга :)"),
    SW_FIRST_KILL("Первая небесная кровь", SKYWARS, 0, "Убейте своего первого врага на SkyWars"),
    SW_KILLER_I("Небесный воитель I", SKYWARS, 0, "Убейте 300 игроков на SkyWars"),
    SW_KILLER_II("Небесный воитель II", SKYWARS, 0, "Убейте 800 игроков на SkyWars"),
    SW_KILLER_III("Небесный воитель III", SKYWARS, 0, "Убейте 1500 игроков на SkyWars"),
    SW_KILLER_IV("Небесный воитель IV", SKYWARS, 0, "Убейте 4000 игроков на SkyWars"),
    SW_KILLER_V("Небесный воитель V", SKYWARS, 0, "Убейте 10000 игроков на SkyWars"),
    SW_WINNER_I("Небесный покровитель I", SKYWARS, 0, "Победите в 20 играх на SkyWars"),
    SW_WINNER_II("Небесный покровитель II", SKYWARS, 0, "Победите в 70 играх на SkyWars"),
    SW_WINNER_III("Небесный покровитель III", SKYWARS, 0, "Победите в 185 играх на SkyWars"),
    SW_WINNER_IV("Небесный покровитель IV", SKYWARS, 0, "Победите в 530 играх на SkyWars"),
    SW_WINNER_V("Небесный покровитель V", SKYWARS, 0, "Победите в 1000 играх на SkyWars"),
    SW_MEGAKILLER("Небесный мясник", SKYWARS, 0, "Убейте 8 игроков за одну игру на SkyWars"),
    FOS_KEEPER_I("Бывалый", FEATS_OF_STRENGTH, 0, "Добейтесь 10 достижений"),
    FOS_KEEPER_II("Герой", FEATS_OF_STRENGTH, 0, "Добейтесь 25 достижений"),
    FOS_KEEPER_III("Мейстер", FEATS_OF_STRENGTH, 0, "Добейтесь 50 достижений"),
    FOS_KEEPER_IV("Великий мейстер", FEATS_OF_STRENGTH, 0, "Добейтесь 75 достижений"),
    FOS_KEEPER_V("Величайший мейстер", FEATS_OF_STRENGTH, 0, "Добейтесь 100 достижений"),
    FOS_SW("Небесный полководец", FEATS_OF_STRENGTH, 0,
            "Получите достижения небесного",
            "воителя и покровителя 5-го уровня"),
    FIRST_ANCIENT_CHEST("Коллекционер", GENERAL, 0, "Откройте свой первый Древний Сундук"),
    TRANSFER_ANCIENT_CHEST("Настоящий друг", GENERAL, 0,
            "Передайте один из своих",
            "Древних Сундуков другому игроку"),
    HG_FIRST_KILL("Голодный убийца", HUNGER_GAMES, 0, "Убейте своего первого врага на голодных играх"),
    HG_KILLER_I("Доминатор дистрикта I", HUNGER_GAMES, 0, "Убейте 100 игроков на голодных играх"),
    HG_KILLER_II("Доминатор дистрикта II", HUNGER_GAMES, 0, "Убейте 400 игроков на голодных играх"),
    HG_KILLER_III("Доминатор дистрикта III", HUNGER_GAMES, 0, "Убейте 900 игроков на голодных играх"),
    HG_KILLER_IV("Доминатор дистрикта IV", HUNGER_GAMES, 0, "Убейте 3000 игроков на голодных играх"),
    HG_KILLER_V("Доминатор дистрикта V", HUNGER_GAMES, 0, "Убейте 8000 игроков на голодных играх"),
    HG_WINNER_I("Бессменный победитель I", HUNGER_GAMES, 0, "Победите в 10 голодных играх"),
    HG_WINNER_II("Бессменный победитель II", HUNGER_GAMES, 0, "Победите в 50 голодных играх"),
    HG_WINNER_III("Бессменный победитель III", HUNGER_GAMES, 0, "Победите в 100 голодных играх"),
    HG_WINNER_IV("Бессменный победитель IV", HUNGER_GAMES, 0, "Победите в 300 голодных играх"),
    HG_WINNER_V("Бессменный победитель V", HUNGER_GAMES, 0, "Победите в 700 голодных играх"),
    FOS_HG("Организатор голодных игр", FEATS_OF_STRENGTH, 0,
            "Получите достижения доминатора",
            "дистрикта и бессменного",
            "победителя 5-го уровня");
    
    @Getter
    private final int id;
    
    @Getter
    private final AchievementSection section;
    
    @Getter
    private final String name;
    
    @Getter
    private final List<String> description;
    
    @Getter
    private int prize;
    
    Achievement(String name, AchievementSection section, int prize, String... description) {
        this.id = ordinal();
        this.section = section;
        this.name = name;
        this.prize = prize;
        this.description = Arrays.asList(description);
    }
    
    public static Achievement getById(int id) {
        return values()[id];
    }
}
