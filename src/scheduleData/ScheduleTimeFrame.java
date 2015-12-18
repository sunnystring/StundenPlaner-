/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import core.ScheduleDay;
import util.Time;

/**
 *
 * Hilfsklasse zur Bestimmung und Bearbeitung der Anfangs- und Schlusszeiten
 * aller Unterrichtstage
 */
public class ScheduleTimeFrame {

    private Time absoluteStart;
    private Time absoluteEnd;
    private int totalNumberOfFields;

    public ScheduleTimeFrame() {
        absoluteStart = new Time("23.00");
        absoluteEnd = new Time();
        totalNumberOfFields = 0;
    }

    public void createTimeFrame(ScheduleDay scheduleDay) {
        Time scheduleStart = scheduleDay.getValidStart();
        Time scheduleEnd = scheduleDay.getValidEnd();
        if (scheduleStart.smallerThan(absoluteStart)) {
            absoluteStart = scheduleStart;
        }
        if (scheduleEnd.greaterThan(absoluteEnd)) {
            absoluteEnd = scheduleEnd;
        }
        totalNumberOfFields = absoluteEnd.diff(absoluteStart); // Differenz = Anzahl 5-Minuten-Blöcke
    }

    public int getTotalNumberOfFields() {
        return totalNumberOfFields;
    }

    public Time getAbsoluteStart() {
        return absoluteStart;
    }

}
