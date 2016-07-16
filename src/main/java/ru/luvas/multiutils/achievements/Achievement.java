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
            "победителя 5-го уровня"),
    BW_FIRST_KILL("Когда ну очень хочешь спать", BEDWARS, 0, "Убейте своего первого врага на BedWars"),
    BW_KILLER_I("Диванный воитель I", BEDWARS, 0, "Убейте 500 игроков на BedWars"),
    BW_KILLER_II("Диванный воитель II", BEDWARS, 0, "Убейте 1200 игроков на BedWars"),
    BW_KILLER_III("Диванный воитель III", BEDWARS, 0, "Убейте 2300 игроков на BedWars"),
    BW_KILLER_IV("Диванный воитель IV", BEDWARS, 0, "Убейте 5000 игроков на BedWars"),
    BW_KILLER_V("Диванный воитель V", BEDWARS, 0, "Убейте 10000 игроков на BedWars"),
    BW_WINNER_I("Мистер Bed I", BEDWARS, 0, "Победите в 10 играх на BedWars"),
    BW_WINNER_II("Мистер Bed II", BEDWARS, 0, "Победите в 40 играх на BedWars"),
    BW_WINNER_III("Мистер Bed III", BEDWARS, 0, "Победите в 90 играх на BedWars"),
    BW_WINNER_IV("Мистер Bed IV", BEDWARS, 0, "Победите в 235 играх на BedWars"),
    BW_WINNER_V("Мистер Bed V", BEDWARS, 0, "Победите в 550 играх на BedWars"),
    FOS_BW("Повелитель снов", FEATS_OF_STRENGTH, 0,
            "Получите достижения диванного",
            "воителя и мистера",
            "Bed'a 5-го уровня"),
    FOS_TRIPLE("Троеборье", FEATS_OF_STRENGTH, 0,
            "Получите следующие достижения:",
            "- Небесный полководец (SkyWars)",
            "- Организатор голодных игр (Голодные Игры)",
            "- Повелитель снов (BedWars)"),
    ANNI_KILLER_I("Боец I", ANNIHILATION, 0, "Убейте 500 игроков на Annihilation"),
    ANNI_KILLER_II("Боец II", ANNIHILATION, 0, "Убейте 1250 игроков на Annihilation"),
    ANNI_KILLER_III("Боец III", ANNIHILATION, 0, "Убейте 3500 игроков на Annihilation"),
    ANNI_KILLER_IV("Боец IV", ANNIHILATION, 0, "Убейте 8000 игроков на Annihilation"),
    ANNI_KILLER_V("Боец V", ANNIHILATION, 0, "Убейте 15000 игроков на Annihilation"),
    ANNI_WOODCUTTER_I("Лесоруб I", ANNIHILATION, 0, "Срубите 500 деревьев на Annihilation"),
    ANNI_WOODCUTTER_II("Лесоруб II", ANNIHILATION, 0, "Срубите 1750 деревьев на Annihilation"),
    ANNI_WOODCUTTER_III("Лесоруб III", ANNIHILATION, 0, "Срубите 4500 деревьев на Annihilation"),
    ANNI_WOODCUTTER_IV("Лесоруб IV", ANNIHILATION, 0, "Срубите 10000 деревьев на Annihilation"),
    ANNI_WOODCUTTER_V("Лесоруб V", ANNIHILATION, 0, "Срубите 16000 деревьев на Annihilation"),
    ANNI_MINER_I("Шахтер I", ANNIHILATION, 0, "Добудьте 500 руды на Annihilation"),
    ANNI_MINER_II("Шахтер II", ANNIHILATION, 0, "Добудьте 1750 руды на Annihilation"),
    ANNI_MINER_III("Шахтер III", ANNIHILATION, 0, "Добудьте 4500 руды на Annihilation"),
    ANNI_MINER_IV("Шахтер IV", ANNIHILATION, 0, "Добудьте 10000 руды на Annihilation"),
    ANNI_MINER_V("Шахтер V", ANNIHILATION, 0, "Добудьте 16000 руды на Annihilation"),
    ANNI_NEXUSDAMAGER_I("Разрушитель Нексусов I", ANNIHILATION, 0, "Нанесите 100 урона нексусам на Annihilation"),
    ANNI_NEXUSDAMAGER_II("Разрушитель Нексусов II", ANNIHILATION, 0, "Нанесите 350 урона нексусам на Annihilation"),
    ANNI_NEXUSDAMAGER_III("Разрушитель Нексусов III", ANNIHILATION, 0, "Нанесите 750 урона нексусам на Annihilation"),
    ANNI_NEXUSDAMAGER_IV("Разрушитель Нексусов IV", ANNIHILATION, 0, "Нанесите 1500 урона нексусам на Annihilation"),
    ANNI_NEXUSDAMAGER_V("Разрушитель Нексусов V", ANNIHILATION, 0, "Нанесите 4000 урона нексусам на Annihilation"),
    ANNI_BOWKILLER_I("Лучник I", ANNIHILATION, 0, "Убейте из лука 30 игроков на Annihilation"),
    ANNI_BOWKILLER_II("Лучник II", ANNIHILATION, 0, "Убейте из лука 100 игроков на Annihilation"),
    ANNI_BOWKILLER_III("Лучник III", ANNIHILATION, 0, "Убейте из лука 400 игроков на Annihilation"),
    ANNI_BOWKILLER_IV("Лучник IV", ANNIHILATION, 0, "Убейте из лука 950 игроков на Annihilation"),
    ANNI_BOWKILLER_V("Лучник V", ANNIHILATION, 0, "Убейте из лука 1800 игроков на Annihilation"),
    FOS_ANNI_KILLER("Отряд Альфа", FEATS_OF_STRENGTH, 0,
            "Получите следующие достижения:",
            "- Боец V (Annihilation)",
            "- Лучник V (Annihilation)"),
    FOS_ANNI_SUPPORT("Надежда команды", FEATS_OF_STRENGTH, 0,
            "Получите следующие достижения:",
            "- Лесоруб V (Annihilation)",
            "- Шахтер V (Annihilation)");
    
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
