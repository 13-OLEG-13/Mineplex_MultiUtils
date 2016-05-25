package ru.luvas.multiutils.achievements;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author RinesThaix
 */
public enum AchievementSection {
    
    FEATS_OF_STRENGTH,
    GENERAL,
    SKYWARS,
    HUNGER_GAMES,
    BEDWARS;
    
    @Getter
    private final List<Achievement> achievements = new ArrayList<>();
}
