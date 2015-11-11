/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData_new;

import util.Time;

/**
 *
 * @author Mathias
 */
public class ScheduleTimeFrame_new {

    private Time absoluteStart;  // untere globale Zeitgrenze Stundenplan
    private Time absoluteEnd;   // obere globale Zeitgrenze Stundenplan

    /* von Time zu int konvertierte Grössen */
    private int totalNumberOfFields; // globale maximale Anzahl Time- bzw. Lectionfields (= Column-Höhe)

    public ScheduleTimeFrame_new() {
        absoluteStart = new Time("23.00"); // obere Grenze initialisieren
        absoluteEnd = new Time(); // untere Grenze initialisieren
        totalNumberOfFields = 0;
    }

    public int getTotalNumberOfFields() {
        return totalNumberOfFields;
    }

    public Time getAbsoluteStart() {
        return absoluteStart;
    }

    /* hier wird der Zeitrahmen jeder DayColumn initialisiert und zu int konvertiert*/
    public void createTimeFrame(ScheduleDay_new scheduleDay) {

        Time scheduleStart;
        Time scheduleEnd;

        scheduleStart = scheduleDay.getValidStart();
        scheduleEnd = scheduleDay.getValidEnd();

        /* hier wird der abolute Zeitrahmen aller Tage bestimmt */
        if (scheduleStart.smallerThan(absoluteStart)) {
            absoluteStart = scheduleStart;
        }
        if (scheduleEnd.greaterThan(absoluteEnd)) {
            absoluteEnd = scheduleEnd;
        }
        totalNumberOfFields = absoluteEnd.diff(absoluteStart);
    }

}
