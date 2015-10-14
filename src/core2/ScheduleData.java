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
    private ArrayList<DayColumnModel> dayColumnModelList;  // Jeder Tag hat sein TableModel
    private ScheduleTimeFrame timeFrame;

    public ScheduleData() {

        numberOfDays = 0;
        dayColumnModelList = new ArrayList<>();
        timeFrame = new ScheduleTimeFrame();
    }

    // aufgerufen bei DataEntry -> ScheduleData definieren
    public void defineData(ScheduleTimes scheduleTimes) {

        this.scheduleTimes = scheduleTimes;
        numberOfDays = scheduleTimes.getNumberOfDays();
       
        // DayColumnModels instantiieren und globaler Zeitrahmen aller Tage festlegen
        for (int i = 0; i < numberOfDays; i++) {
            dayColumnModelList.add(new DayColumnModel()); 
            timeFrame.initTimeFrame(scheduleTimes.getScheduleDay(i));
        }

        // Zeitrahmen in Tage einsetzen 
        for (int i = 0; i < numberOfDays; i++) {
            dayColumnModelList.get(i).setTimeFrame(scheduleTimes.getScheduleDay(i), timeFrame);
         //   dayColumnModelList.get(i).fireTableDataChanged();
        }
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public DayColumnModel getDayColumnModel(int i) {
        return dayColumnModelList.get(i);
    }

}
