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

    private Time[] timeSlots;

    public StudentDay(int slots) {  // slots = COLUMNS - 1 = 5

        timeSlots = new Time[slots];
        for (int i = 0; i < slots; i++) {
            timeSlots[i] = new Time();
        }
    }

    /* Getter, Setter */
    public void setStudentTime(String time, int i) throws IllegalArgumentException { // i = 1,2,..5

        if (invalidSlot(time, i - 1)) {
            throw new IllegalArgumentException(" Unkorrekte Eingabe!");
        }
        timeSlots[i - 1].setTime(time);
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

    public boolean isValidDay() { // unnötig in neuer Version

        for (Time t : timeSlots) {
            if (!t.toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /* falls EndTime ohne StartTime einegeben */
    private boolean invalidSlot(String time, int i) {

        switch (i) {
            case 1:
                return (timeSlots[0].toString().trim().isEmpty() && !time.trim().isEmpty()) || (timeSlots[0].greaterEqualsThan(new Time(time)));
            case 3:
                return timeSlots[2].toString().trim().isEmpty() && !time.trim().isEmpty() || (timeSlots[2].greaterEqualsThan(new Time(time)));
            default:
                return false;
        }
    }
}
