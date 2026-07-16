package dev.andrii.bosstracker.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final ZoneId gameZone;

    public TimeUtil(ZoneId gameZone) {
        this.gameZone = gameZone;
    }

    public long parseTime(String input) {
        LocalTime time = LocalTime.parse(input, TIME_FORMAT);
        LocalDateTime now = LocalDateTime.now(gameZone);
        LocalDateTime killTime = LocalDateTime.of(now.toLocalDate(), time);

        if (killTime.isAfter(now)) {
            killTime = killTime.minusDays(1);
        }

        return killTime.atZone(gameZone)
            .toInstant()
            .toEpochMilli();
    }

    public String formatTime(long millis) {
        return Instant.ofEpochMilli(millis)
            .atZone(gameZone)
            .toLocalTime()
            .format(TIME_FORMAT);
    }

    public String timeAgo(long millis) {
        long diff = System.currentTimeMillis() - millis;
        long minutes = diff / (60 * 1000);
        long hours = minutes / 60;
        minutes = minutes % 60;

        return hours > 0
            ? String.format("%dч %dм назад", hours, minutes)
            : String.format("%dм назад", minutes);
    }

    public String formatDuration(long millis) {
        long minutes = millis / (60 * 1000);
        long hours = minutes / 60;
        minutes = minutes % 60;

        return hours > 0
            ? String.format("%dч %dм", hours, minutes)
            : String.format("%dм", minutes);
    }
}