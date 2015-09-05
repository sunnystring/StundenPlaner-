/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import util.Time;

/**
 *
 * @author Mathias
 */
/* Einteilungszeiten eines Tages eines Schülers */
public class StudentDay {

    private Time startTime1;
    private Time endTime1;
    private Time startTime2;
    private Time endTime2;
    private Time favorite;

    private Time[] timeSlots;

    private Time lectionLength; //Lektionsdauer in Minuten -> weshalb hier??

    private String endString1, endString2;

    public StudentDay() {

        timeSlots = new Time[5];
      
        for (int i = 0; i < 5; i++) {
            timeSlots[i] = new Time("12.00");
                   
        }
    }

    /* statische Demoversion */
    public StudentDay(String startTime1, String endTime1, String startTime2, String endTime2, String favorite) {

        try {

            this.startTime1 = createStartTime(startTime1);
            this.startTime2 = createStartTime(startTime2);
            this.endTime1 = createEndTime(startTime1, endTime1);
            this.endTime2 = createEndTime(startTime2, endTime2);
            this.favorite = createStartTime(favorite);

            if (endTime1.isEmpty()) {
                endString1 = this.endTime1.toString();
            } else {
                endString1 = "-" + this.endTime1.toString();
            }
            if (endTime2.isEmpty()) {
                endString2 = this.endTime2.toString();
            } else {
                endString2 = "-" + this.endTime2.toString();
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /* falls keine Zeiteingabe gemacht, muss Platz in StudentField leer bleiben ( = new Time()) */
    private Time createStartTime(String startTime) {
        return startTime.trim().isEmpty() ? new Time() : new Time(startTime);
    }

    /* zusätzlich checken, dass keine endTime ohne startTime eingegeben wurde */
    private Time createEndTime(String startTime, String endTime) throws IllegalArgumentException {
        if (!endTime.trim().isEmpty()) {
            if (startTime.trim().isEmpty()) {
                throw new IllegalArgumentException(" Es muss zuerst eine Anfangszeit eingegeben werden!");
            } else {
                return new Time(endTime);
            }
        } else {
            return new Time();
        }
    }

    /* Getter, Setter */
    public void setTime(String time, int i) {
        timeSlots[i] = new Time(time);
    }

    public Time getTime(int i) {
        return timeSlots[i];
    }

    // --------------------------------
    public Time getStartTime1() {
        return startTime1;
    }

    public Time getEndTime1() {
        return endTime1;
    }

    public Time getStartTime2() {
        return startTime2;
    }

    public Time getEndTime2() {
        return endTime2;
    }

    public Time getFavorite() {
        return favorite;
    }

    public void setLectionLength(int minutes) {
        lectionLength = new Time();
        lectionLength.setMinute(minutes);
    }

    public Time getLectionLength() {
        return lectionLength;
    }

    /* Favorit muss separater String sein für Formatierung in StudentField */
    public String getFavoriteAsString() {
        return favorite.toString();
    }

    /* gibt StudentDay-Objekt im richtigen Format (für StudentField) zurück */
    @Override
    public String toString() {
        return " " + startTime1 + endString1 + " " + startTime2 + endString2 + " ";
    }

}
