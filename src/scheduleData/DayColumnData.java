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
 * Einheit eines Unterrichtstages in {@link ScheduleData}, bestehend aus einer {@link ScheduleFieldData}-Liste
 * 
 */
public class DayColumnData {

    private ArrayList<ScheduleFieldData> fieldDataList;
    private int totalNumberOfFields;
    private int fieldCountStart;
    private int fieldCountEnd;
    private ScheduleDay scheduleDay;

    public DayColumnData() {
        fieldDataList = new ArrayList<>();
    }

    public void initDayColumn(ScheduleDay scheduleDay, ScheduleTimeFrame timeFrame) {
        this.scheduleDay = scheduleDay;
        totalNumberOfFields = timeFrame.getTotalNumberOfFields();
        Time absoluteStart = timeFrame.getAbsoluteStart();
        fieldCountStart = scheduleDay.getValidStart().diff(absoluteStart);
        fieldCountEnd = scheduleDay.getValidEnd().diff(absoluteStart);
        Time time = absoluteStart.clone();
        for (int i = 0; i < totalNumberOfFields; i++) {
            ScheduleFieldData fieldData = new ScheduleFieldData();
            fieldData.setTime(time.clone());
            fieldData.setTeacherTime(i >= fieldCountStart && i <= fieldCountEnd);
            fieldDataList.add(fieldData);
            time.inc();
        }
    }

    public void setValidTimeMark(StudentDay day, int listIndex) {
        ScheduleFieldData fieldData = fieldDataList.get(listIndex);
        Time listTime = fieldData.getTime();
        if (listTime.greaterEqualsThan(day.getStartTime1()) && listTime.smallerEqualsThan(day.getEndTime1())) {
            fieldData.setValidTimeMark(ScheduleFieldData.TIME_INTERVAL_1);
        }
        if (listTime.greaterEqualsThan(day.getStartTime2()) && listTime.smallerEqualsThan(day.getEndTime2())) {
            fieldData.setValidTimeMark(ScheduleFieldData.TIME_INTERVAL_2);
        }
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

    public ScheduleFieldData getFieldData(int i) {
        return fieldDataList.get(i);
    }

    public String getDayName() {
        return scheduleDay.getDayName();
    }
}
