/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core2;

import core.ScheduleTimes;
import java.util.ArrayList;
import schedule.ScheduleTimeFrame;

/**
 *
 * @author Mathias
 */
public class ScheduleData {

    private ScheduleTimes scheduleTimes;
    private int numberOfDays;
    private ArrayList<DayColumnData> dayColumnDataList;  // Jeder Tag hat sein TableModel
    private ScheduleTimeFrame timeFrame;

    public ScheduleData() {

        scheduleTimes = new ScheduleTimes();
        dayColumnDataList = new ArrayList<>();
        timeFrame = new ScheduleTimeFrame();
        numberOfDays = 0;
    }

    // aufgerufen bei DataEntry -> ScheduleData definieren
    public void defineScheduleData(ScheduleTimes scheduleTimes) {

        this.scheduleTimes = scheduleTimes;
        scheduleTimes.createList();  // erstellt dynamische Day-List 0 = 1. Tag, 1 = 2. Tag usw.
        numberOfDays = scheduleTimes.getNumberOfDays();

        // DayColumnModels instantiieren und globaler Zeitrahmen aller Tage festlegen
        for (int i = 0; i < numberOfDays; i++) {
            dayColumnDataList.add(new DayColumnData());
            timeFrame.initTimeFrame(scheduleTimes.getScheduleDay(i));
        }

        // Zeitrahmen in Tage einsetzen 
        for (int i = 0; i < numberOfDays; i++) {
            dayColumnDataList.get(i).setTimeFrame(scheduleTimes.getScheduleDay(i), timeFrame);
        }
    }

    public ScheduleTimes getScheduleTimes() {
        return scheduleTimes;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public DayColumnData getDayColumnData(int i) {
        return dayColumnDataList.get(i);
    }

}
