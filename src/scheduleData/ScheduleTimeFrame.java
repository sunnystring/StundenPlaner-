/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import core.ScheduleDay;
import utils.Time;

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
        reset();
    }

    public void setBounds(ScheduleDay scheduleDay) {
        Time scheduleStart = scheduleDay.getValidStart();
        Time scheduleEnd = scheduleDay.getValidEnd();
        if (scheduleStart.lessThan(absoluteStart)) {
            absoluteStart = scheduleStart;
        }
        if (scheduleEnd.greaterThan(absoluteEnd)) {
            absoluteEnd = scheduleEnd;
        }
        totalNumberOfFields = absoluteEnd.diff(absoluteStart); // Differenz = Anzahl 5-Minuten-Blöcke
    }

    public void reset() {
        absoluteStart = new Time("23.55");
        absoluteEnd = new Time();
        totalNumberOfFields = 0;
    }

    public int getTotalNumberOfFields() {
        return totalNumberOfFields;
    }

    public Time getAbsoluteStart() {
        return absoluteStart;
    }

    public Time getAbsoluteEnd() {
        return absoluteEnd;
    }

}
