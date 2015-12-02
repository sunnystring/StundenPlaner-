/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import core.ScheduleDay;
import java.util.ArrayList;
import core.StudentDay;

import util.Time;

/**
 *
 * @author mathiaskielholz
 */
public class DayColumnData {

    private ArrayList<ScheduleFieldData> fieldDataList;

    /* von Time zu int konvertierte Grössen */
    private int totalNumberOfFields; // globale Anzahl Time- bzw. Lectionfields (= Column-Höhe)
    private int fieldCountStart;  // Index lokaler Unterrichtsbeginn 
    private int fieldCountEnd;  // Index lokales Unterrichtsende 
    private ScheduleDay scheduleDay;

    public DayColumnData() {

        fieldDataList = new ArrayList<>();
    }

    public void initDayColumn(ScheduleDay scheduleDay, ScheduleTimeFrame timeFrame) {

        this.scheduleDay = scheduleDay;
        totalNumberOfFields = timeFrame.getTotalNumberOfFields();
        Time absoluteStart = timeFrame.getAbsoluteStart(); // untere globale Zeitgrenze Stundenplan

        /* von Time zu int konvertierte Grössen */
        fieldCountStart = scheduleDay.getValidStart().diff(absoluteStart); // Anzahl 5-Min.-Felder
        fieldCountEnd = scheduleDay.getValidEnd().diff(absoluteStart);

        try {
            Time time = absoluteStart.clone();
            for (int i = 0; i < totalNumberOfFields; i++) {
                ScheduleFieldData fieldData = new ScheduleFieldData();
                fieldData.setTime(time.clone());
                fieldData.setTeacherTime(i >= fieldCountStart && i <= fieldCountEnd);
                fieldDataList.add(fieldData);
                time.inc();
            }
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    public void setValidTimeMark(StudentDay day, int listIndex) {

        ScheduleFieldData fieldData = fieldDataList.get(listIndex);
        Time listTime = fieldData.getTime();
        // ValidTime 1 
        if (listTime.greaterEqualsThan(day.getStartTime1()) && listTime.smallerEqualsThan(day.getEndTime1())) {
            fieldData.setValidTimeMark(ScheduleFieldData.TIME_INTERVAL_1);
        }
        // ValidTime 2
        if (listTime.greaterEqualsThan(day.getStartTime2()) && listTime.smallerEqualsThan(day.getEndTime2())) {
            fieldData.setValidTimeMark(ScheduleFieldData.TIME_INTERVAL_2);
        }
        // Favorite
        if (listTime.equals(day.getFavorite())) {
            fieldData.setValidTimeMark(ScheduleFieldData.FAVORITE);
        }
    }

    public void setValidTimeMarks(StudentDay day) {
        for (int i = 0; i < totalNumberOfFields; i++) {
            setValidTimeMark(day, i);
        }
    }

    public void resetValidTimeMarks() {
        for (int i = 0; i < totalNumberOfFields; i++) {
            fieldDataList.get(i).setValidTimeMark(ScheduleFieldData.NO_VALUE);
        }
    }

    /* Getter, Setter */
    public ScheduleFieldData getFieldData(int i) {
        return fieldDataList.get(i);
    }

    public String getDayName() {
        return scheduleDay.getDayName();
    }
}
