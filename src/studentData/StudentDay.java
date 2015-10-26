/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentData;

import util.Time;

/**
 *
 * @author Mathias
 */
/* Einteilungszeiten eines Tages eines Schülers */
public class StudentDay {

    private Time[] timeSlots;

    public StudentDay(int slots) {  // slots = COLUMNS - 1 = 5

        timeSlots = new Time[slots];
        for (int i = 0; i < slots; i++) {
            timeSlots[i] = new Time();
        }
    }

    /* Getter, Setter */
    public void setStudentTime(String timeString, int slot) throws IllegalArgumentException {
        if (slot == 1 || slot == 3) {   // endTime1 und endTime2 checken
            if (!timeString.trim().isEmpty() & timeSlots[slot - 1].toString().trim().isEmpty()) {
                throw new IllegalArgumentException(" Kein Anfangswert eingegeben!");
            }
        }
        timeSlots[slot].setTime(timeString);
    }

    public Time getStartTime1() {
        return timeSlots[0];
    }

    public Time getEndTime1() {
        return timeSlots[1];
    }

    public Time getStartTime2() {
        return timeSlots[2];
    }

    public Time getEndTime2() {
        return timeSlots[3];
    }

    public Time getFavorite() {
        return timeSlots[4];
    }

    /* Favorit muss separater String sein für Formatierung in StudentField */
    public String getFavoriteAsString() {
        return getFavorite().toString();
    }

    /* gibt StudentDay-Objekt im richtigen Format (für StudentField) zurück */
    @Override
    public String toString() {

        String endString1, endString2;
        /* setzt nach Bedarf Bindestrich */
        endString1 = getEndTime1().toString().trim().isEmpty() ? getEndTime1().toString() : "-" + getEndTime1().toString();
        endString2 = getEndTime2().toString().trim().isEmpty() ? getEndTime2().toString() : "-" + getEndTime2().toString();

        return " " + getStartTime1() + endString1 + " " + getStartTime2() + endString2 + " ";
    }

    public void setSingleLections() {
        if (!getStartTime1().toString().trim().isEmpty() & getEndTime1().toString().trim().isEmpty()) {
            timeSlots[1] = getStartTime1();
        }
        if (!getStartTime2().toString().trim().isEmpty() & getEndTime2().toString().trim().isEmpty()) {
            timeSlots[3] = getStartTime2();
        }
    }

    public boolean isValidDay() { // -> unnötig in neuer Version

        for (Time t : timeSlots) {
            if (!t.toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
