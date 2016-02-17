/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import core.Database;
import core.ScheduleDay;
import java.util.ArrayList;
import core.StudentDay;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import util.Time;

/**
 *
 * Einheit eines Unterrichtstages in {@link ScheduleData} mit der zugehörigen
 * {@link ScheduleFieldData}-Liste
 *
 */
public class DayColumnData {

    private Database database;
    private ArrayList<ScheduleFieldData> fieldList;
    private TreeMap<Time, LectionData> lectionMap;
    private Time absoluteStart;
    private int totalNumberOfFields;
    private int fieldCountStart;
    private int fieldCountEnd;
    private ScheduleDay scheduleDay;

    public DayColumnData(Database database) {
        this.database = database;
        fieldList = new ArrayList<>();
        lectionMap = new TreeMap<>();
      
    }

    public void createDayData(ScheduleDay scheduleDay, ScheduleTimeFrame timeFrame) {
        this.scheduleDay = scheduleDay;
        setTimeFrame(timeFrame);
        createFieldList();
    }

    public void updateDayData(ScheduleDay scheduleDay, ScheduleTimeFrame timeFrame) {
        this.scheduleDay = scheduleDay;
        setTimeFrame(timeFrame);
        if (hasAllocatedLections()) {
            resetLectionFieldCounts();
            rebuildFieldList();
        } else {
            createFieldList();
        }
    }

    private void resetLectionFieldCounts() {
        Set entrySet = lectionMap.entrySet();
        Iterator iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            ((LectionData) mapEntry.getValue()).resetFieldCount();
        }
    }

    private void setTimeFrame(ScheduleTimeFrame timeFrame) {
        totalNumberOfFields = timeFrame.getTotalNumberOfFields();
        absoluteStart = timeFrame.getAbsoluteStart();
        fieldCountStart = scheduleDay.getValidStart().diff(absoluteStart);
        fieldCountEnd = scheduleDay.getValidEnd().diff(absoluteStart);
    }

    private void createFieldList() {
        Time time = absoluteStart.clone();
        for (int i = 0; i < totalNumberOfFields; i++) {
            ScheduleFieldData scheduleField = new ScheduleFieldData(database);
            scheduleField.setFieldTime(time.clone());
            scheduleField.setTeacherTime(i >= fieldCountStart && i <= fieldCountEnd);
            fieldList.add(scheduleField);
            time.inc();
        }
    }

    private void rebuildFieldList() {
        Set entrySet = lectionMap.entrySet();
        Iterator lectionsIterator = entrySet.iterator();
        Map.Entry mapEntry = (Map.Entry) lectionsIterator.next();
        Time lectionStart = (Time) mapEntry.getKey();
        LectionData lection = (LectionData) mapEntry.getValue();
        Time fieldTime = absoluteStart.clone();
        for (int i = 0; i < totalNumberOfFields; i++) {
            ScheduleFieldData scheduleField = new ScheduleFieldData(database);
            if (fieldTime.greaterEqualsThan(lectionStart)) {
                if (lection.hasNextField()) {
                    scheduleField = lection.getNextField();
                } else if (lectionsIterator.hasNext()) {
                    mapEntry = (Map.Entry) lectionsIterator.next();
                    lectionStart = (Time) mapEntry.getKey();
                    lection = (LectionData) mapEntry.getValue();
                }
            }
            scheduleField.setFieldTime(fieldTime.clone());
            scheduleField.setTeacherTime(i >= fieldCountStart && i <= fieldCountEnd);
            fieldList.add(scheduleField);
            fieldTime.inc();
        }
    }

    public boolean hasAllocatedLections() {
        return !lectionMap.isEmpty();
    }

    public boolean hasLectionsOutOfBounds(Time absoluteStart, Time absoluteEnd) {
        Time endOfLastLection = lectionMap.lastEntry().getValue().getEnd();
        return (absoluteStart.greaterThan(lectionMap.firstKey()) || absoluteEnd.lessEqualsThan(endOfLastLection));
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
    }

    public void removeLection(Time time) {
        lectionMap.remove(time);
    }
    
    public void updateStudentIDs(int deletedStudentID){
     Set entrySet = lectionMap.entrySet();
        Iterator iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            ((LectionData) mapEntry.getValue()).updateStudentID(deletedStudentID);
        }
    }

    public ScheduleFieldData getFieldData(int i) {
        return fieldList.get(i);
    }

    public String getDayName() {
        return scheduleDay.getDayName();
    }

    public void setLectionMap(TreeMap<Time, LectionData> lectionMap) {
        this.lectionMap = lectionMap;
    }

    public TreeMap<Time, LectionData> getLectionMap() {
        return lectionMap;
    }

}
