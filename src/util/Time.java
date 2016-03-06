/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import core.ScheduleDay;
import core.StudentDay;
import java.text.DecimalFormat;
import scheduleData.DayColumnData;
import scheduleData.ScheduleFieldData;
import scheduleData.ScheduleTimeFrame;

/**
 *
 * Hilfsklasse für die Erzeugung und Bearbeitung von Zeiten, benutzt in {@link ScheduleDay},
 * {@link StudentDay}, {@link DayColumnData}, {@link ScheduleFieldData}, {@link ScheduleTimeFrame}
 */
public class Time implements Cloneable, Comparable {

    private int hour, minute;
    private String timeString;
    private static final DecimalFormat OUTPUT_FORMAT = new DecimalFormat("##.00");

    public Time(String timeString) {
        checkAndExtractTimeComponents(timeString);
    }

    public Time() {
        reset();
    }

    public void reset() {
        this.hour = 0;
        this.minute = 0;
        this.timeString = "";
    }

    private void checkAndExtractTimeComponents(String timeString) {
        double time = 0.0;
        try {
            time = Double.parseDouble(timeString);
        } catch (NumberFormatException e) {
            this.hour = 0;
            this.minute = 0;
            this.timeString = "";
        }
        this.timeString = timeString;
        hour = (int) time;
        time = time - hour;
        time = Math.floor(time * 100 + 0.5) / 100; // Rundungsfehler eliminieren, 2 Kommastellen
        time = time * 100;
        minute = (int) time;
    }

    public void setTime(String timeString) {
        checkAndExtractTimeComponents(timeString);
    }

    public void setTime(Time time) {
        this.hour = time.getHour();
        this.minute = time.getMinute();
        this.timeString = time.getTimeString();
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public String getTimeString() {
        return timeString;
    }

    /* Differenz in 5-Min-Auflösung */
    public int diff(Time t) {
        Time temp;
        int i = 0;
        if (this.equals(t)) {
            return 0;
        } else if (this.lessThan(t)) {
            temp = this.clone();
            while (temp.lessThan(t)) {
                temp.inc();
                i++;
            }
        } else if (this.greaterThan(t)) {
            temp = t.clone();
            while (temp.lessThan(this)) {
                temp.inc();
                i++;
            }
        }
        return i;
    }

    /* Inkrementierung in 5-Min-Auflösung */
    public void inc() {
        this.minute = this.minute + 5;
        if (this.minute > 55) {
            this.hour = this.hour + 1;
            if (this.hour > 24 || this.hour == 24 && this.minute > 0) {
                throw new IllegalArgumentException(" Ergebnis übersteigt Grenze 24.00");
            }
            this.minute = 0;
        }
    }

    public boolean isEmpty() {
        return timeString.isEmpty() || (hour == 0 && minute == 0);
    }

    public boolean greaterThan(Time t) {
        if (this.equals(t)) {
            return false;
        }
        if (this.hour < t.hour) {
            return false;
        } else if (this.hour == t.hour && this.minute < t.minute) {
            return false;
        }
        return true;
    }

    public boolean lessThan(Time t) {
        if (this.equals(t)) {
            return false;
        }
        if (this.hour > t.hour) {
            return false;
        } else if (this.hour == t.hour && this.minute > t.minute) {
            return false;
        }
        return true;
    }

    public boolean lessEqualsThan(Time t) {
        if (this.hour > t.hour) {
            return false;
        } else if (this.hour == t.hour && this.minute > t.minute) {
            return false;
        }
        return true;
    }

    public boolean greaterEqualsThan(Time t) {
        if (this.hour < t.hour) {
            return false;
        } else if (this.hour == t.hour && this.minute < t.minute) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        Time time;
        if (!(obj instanceof Time)) {
            return false;
        }
        time = (Time) obj;
        if (this.minute != time.minute) {
            return false;
        }
        if (this.hour != time.hour) {
            return false;
        }
        return true;
    }

    @Override
    public Time clone() {
        Time time = null;
        try {
            time = (Time) super.clone();
        } catch (CloneNotSupportedException ex) {
        }
        return time;
    }

    @Override
    public int hashCode() {
        return hour + minute + timeString.hashCode();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return " ";
        } else {
            return OUTPUT_FORMAT.format(Math.floor((hour * 100 + minute) + 0.5) / 100);
        }
    }

    @Override
    public int compareTo(Object o) {
        Time time = (Time) o;
        if (this.greaterThan(time)) {
            return 1;
        }
        if (this.lessThan(time)) {
            return -1;
        }
        return 0;
    }

    public Time plus(int minute) {
        if (minute < 0 || minute >= 60) {
            throw new IllegalArgumentException(" Nur Zahlen zwischen 0 und 60 möglich");
        }
        Time time = new Time();
        time.setHour(0);
        time.setMinute(0);

        time.minute = this.minute + minute;
        if (time.minute > 55) {
            time.hour = this.hour + 1;
            time.minute = time.minute - 60;
        } else {
            time.hour = this.hour;
        }
        return time;
    }

    public Time plus(Time t) {
        Time time = new Time();
        time.hour = this.hour + t.hour;
        time.minute = this.minute + t.minute;
        if (time.minute > 55) {
            time.hour = time.hour + 1;
            time.minute = time.minute - 60;
        }
        if (time.hour > 24 || time.hour == 24 && time.minute > 0) {
            throw new IllegalArgumentException(" Ergebnis übersteigt Grenze 24.00");
        }
        return time;
    }

    public Time plus(String inputString) {
        Time time = new Time();
        time.checkAndExtractTimeComponents(inputString);
        time.hour = this.hour + time.hour;
        time.minute = this.minute + time.minute;
        if (time.minute > 55) {
            time.hour = time.hour + 1;
            time.minute = time.minute - 60;
        }
        if (time.hour > 24 || time.hour == 24 && time.minute > 0) {
            throw new IllegalArgumentException(" Ergebnis übersteigt Grenze 24.00");
        }
        return time;
    }

    public Time minus(int minute) {
        if (minute < 0 || minute >= 60) {
            throw new IllegalArgumentException(" Nur Zahlen zwischen 0 und 60 möglich");
        }
        Time time = new Time();
        time.setHour(0);
        time.setMinute(0);
        time.minute = this.minute - minute;
        if (time.minute < 0) {
            time.hour = this.hour - 1;
            time.minute = 60 + time.minute;
        } else {
            time.hour = this.hour;
        }
        return time;
    }

    public Time minus(Time t) {
        Time time = new Time();
        time.hour = this.hour - t.hour;
        if (time.hour < 0) {
            throw new IllegalArgumentException(" Ergebnis liegt unter 00.00");
        }
        time.minute = this.minute - t.minute;
        if (time.minute < 0) {
            time.hour -= 1;
            if (time.hour < 0) {
                throw new IllegalArgumentException(" Ergebnis liegt unter 00.00");
            }
            time.minute = 60 + time.minute;
        }
        return time;
    }

    public Time minus(String inputString) {
        Time time = new Time();
        time.checkAndExtractTimeComponents(inputString);
        time.hour = this.hour - time.hour;
        if (time.hour < 0) {
            throw new IllegalArgumentException(" Ergebnis liegt unter 00.00");
        }
        time.minute = this.minute - time.minute;
        if (time.minute < 0) {
            time.hour -= 1;
            if (time.hour < 0) {
                throw new IllegalArgumentException(" Ergebnis liegt unter 00.00");
            }
            time.minute = 60 + time.minute;
        }
        return time;
    }

    /* Ganzzahldivision in 5-Min-Auflösung */
    public Time divBy(int t) {
        Time time = new Time();
        time.setHour(0);
        time.setMinute(0);
        int minutes, fields, inc;
        minutes = this.hour * 60 + this.minute;
        fields = minutes / 5;
        inc = fields / t;
        for (int i = 0; i < inc; i++) {
            time.inc();
        }
        return time;
    }

    /* Modulodivision in 5-Min-Auflösung */
    public int modBy(int t) {
        int minutes, fields;
        minutes = this.hour * 60 + this.minute;
        fields = minutes / 5;
        return fields % t;
    }

}
