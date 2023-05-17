package onBoard.network.utils;

import onBoard.network.exceptions.DateTimeFormatException;

import java.io.Serializable;

public class DateBuilder implements Serializable {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;


    public DateBuilder setYear(int year) {
        this.year = year;
        return this;
    }

    public DateBuilder setMonth(int month) {
        if (month > 12 || month < 1) throw new DateTimeFormatException();
        this.month = month;
        return this;
    }

    public DateBuilder setDay(int day) {
        if (day < 1 || day > 31) throw new DateTimeFormatException();
        this.day = day;
        return this;
    }

    public DateBuilder setHour(int hour) {
        if (hour > 24 || hour < 0) throw new DateTimeFormatException();
        this.hour = hour;
        return this;
    }

    public DateBuilder setMinute(int minute) {
        if (minute < 0 || minute > 60) throw new DateTimeFormatException();
        this.minute = minute;
        return this;
    }

    @Override
    public String toString() {
        if (year == 0 || month == 0 || day == 0) throw new DateTimeFormatException();
        return year+"/"+month+"/"+day+" " + hour+":"+minute+":00";
    }
}