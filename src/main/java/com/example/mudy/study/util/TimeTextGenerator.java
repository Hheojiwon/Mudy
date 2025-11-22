package com.example.mudy.study.util;

import java.time.Duration;

public class TimeTextGenerator {
    private TimeTextGenerator() {}

    public static String generate(Duration time) {
        String timeText = "";
        long days = time.toDays();
        long hours = time.minusDays(days).toHours();
        long minutes = time.minusDays(days).minusHours(hours).toMinutes();

        if (days > 0) { timeText += String.format("%d일 ", days); }
        if (hours > 0) { timeText += String.format("%d시간 ", hours); }
        timeText += String.format("%d분", minutes);

        return timeText;
    }
}
