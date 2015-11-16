/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import java.util.ArrayList;
import studentListData.StudentDay;

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
                fieldData.setIsTeacherTime(i >= fieldCountStart && i <= fieldCountEnd);
                fieldDataList.add(fieldData);
                time.inc();
            }
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    public void setValidTimeMarks(StudentDay day) {

        ScheduleFieldData fieldData;
        for (int i = 0; i < totalNumberOfFields; i++) {
            fieldData = fieldDataList.get(i);
            if (fieldData.getTime().greaterEqualsThan(day.getStartTime1()) && fieldData.getTime().smallerEqualsThan(day.getEndTime1())) {
                fieldData.setValidTime(ScheduleFieldData.TIME_INTERVAL_1);
            }
            if (fieldData.getTime().greaterEqualsThan(day.getStartTime2()) && fieldData.getTime().smallerEqualsThan(day.getEndTime2())) {
                fieldData.setValidTime(ScheduleFieldData.TIME_INTERVAL_2);
            }
            if (fieldData.getTime().equals(day.getFavorite())) {
                fieldData.setValidTime(ScheduleFieldData.FAVORITE);
            }
        }
    }

    public void resetValidTimeMarks() {
        for (int i = 0; i < totalNumberOfFields; i++) {
            fieldDataList.get(i).setValidTime(0);
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
