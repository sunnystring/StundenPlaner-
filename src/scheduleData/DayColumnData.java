/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import core.ScheduleDay;
import java.util.ArrayList;
import core.StudentDay;
import java.util.TreeMap;

import util.Time;

/**
 *
 * Einheit eines Unterrichtstages in {@link ScheduleData} mit der zugehörigen
 * {@link ScheduleFieldData}-Liste
 *
 */
public class DayColumnData {

    private ArrayList<ScheduleFieldData> fieldList;
    private TreeMap<Time, LectionData> lectionMap;
    private int totalNumberOfFields;
    private int fieldCountStart;
    private int fieldCountEnd;
    private ScheduleDay scheduleDay;

    public DayColumnData() {
        fieldList = new ArrayList<>();
        lectionMap = new TreeMap<>();
    }

    public void createDayColumn(ScheduleDay scheduleDay, ScheduleTimeFrame timeFrame) {
        this.scheduleDay = scheduleDay;
        totalNumberOfFields = timeFrame.getTotalNumberOfFields();
        Time absoluteStart = timeFrame.getAbsoluteStart();
        fieldCountStart = scheduleDay.getValidStart().diff(absoluteStart);
        fieldCountEnd = scheduleDay.getValidEnd().diff(absoluteStart);
        Time time = absoluteStart.clone();
        for (int i = 0; i < totalNumberOfFields; i++) {
            ScheduleFieldData fieldData = new ScheduleFieldData();
            fieldData.setFieldTime(time.clone());
            fieldData.setTeacherTime(i >= fieldCountStart && i <= fieldCountEnd);
            fieldList.add(fieldData);
            time.inc();
        }
    }

    public boolean checkAllocatedLectionBounds(Time absoluteStart, Time absoluteEnd) {
        return (absoluteStart.greaterThan(lectionMap.firstKey()) || absoluteEnd.lessThan(absoluteEnd));
    }

    public void setValidTimeMark(StudentDay day, int listIndex) {
        ScheduleFieldData field = fieldList.get(listIndex);
        Time listTime = field.getFieldTime();
        if (listTime.greaterEqualsThan(day.getStartTime1()) && listTime.lessEqualsThan(day.getEndTime1())) {
            field.setValidTimeMark(ScheduleFieldData.TIME_INTERVAL_1);
        }
        if (listTime.greaterEqualsThan(day.getStartTime2()) && listTime.lessEqualsThan(day.getEndTime2())) {
            field.setValidTimeMark(ScheduleFieldData.TIME_INTERVAL_2);
        }
        if (listTime.equals(day.getFavorite())) {
            field.setValidTimeMark(ScheduleFieldData.FAVORITE);
        }
    }

    public void setValidTimeMarks(StudentDay day) {
        for (int i = 0; i < totalNumberOfFields; i++) {
            setValidTimeMark(day, i);
        }
    }

    public void resetValidTimeMarks() {
        for (int i = 0; i < totalNumberOfFields; i++) {
            fieldList.get(i).setValidTimeMark(ScheduleFieldData.NO_VALUE);
        }
    }

    public void addLection(Time time, LectionData lection) {
        lectionMap.put(time, lection);
        System.out.println("after add: " + lectionMap.size());

    }

    public void removeLection(Time time) {
        lectionMap.remove(time);
        System.out.println("after remove: " + lectionMap.size());
    }

    public ScheduleFieldData getFieldData(int i) {
        return fieldList.get(i);
    }

    public String getDayName() {
        return scheduleDay.getDayName();
    }
}
