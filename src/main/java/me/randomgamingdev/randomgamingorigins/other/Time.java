package me.randomgamingdev.randomgamingorigins.other;

public class Time {
    public static long sec = 1000;
    public static long min = sec * 60;
    public static long hour = min * 60;
    public static long day = hour * 24;
    public static long week = day * 7;
    public static long month = day * 30;
    public static long year = day * 365;
    public static Object[] units = new Object[]{
            new Pair(year, "year(s)"),
            new Pair(month, "month(s)"),
            new Pair(week, "week(s)"),
            new Pair(day, "day(s)"),
            new Pair(hour, "hour(s)"),
            new Pair(min, "minute(s)"),
            new Pair(sec, "second(s)"),
    };

    public static String FormatLong(long time) {
        StringBuilder date = new StringBuilder();
        for (Object unitObj : units) {
            Pair<Long, String> unit = (Pair<Long, String>)unitObj;

            long numUnit = time / unit.first;

            if (numUnit == 0)
                continue;

            time -= numUnit * unit.first;

            date.append(numUnit);
            date.append(' ');
            date.append(unit.second);
            date.append(' ');
        }

        return date.toString();
    }
}
