package onBoard.network.utils;

import onBoard.network.exceptions.DateTimeFormatException;
import onBoard.network.networkUtils.NetworkGlobals;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;


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

    public boolean isElapsed() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime specifiedDateTime = LocalDateTime.of(year, month, day, hour, minute, 0);
        return now.isAfter(specifiedDateTime);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public Date toSqlDate() {
        if (year == 0 || month == 0 || day == 0) {
            System.out.println(year + " " + month + " " + day);
            throw new DateTimeFormatException();
        }

        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, 0);
        long milliseconds = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return new Date(milliseconds);
    }
}
