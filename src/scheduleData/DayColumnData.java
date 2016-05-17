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
import utils.Time;
import static scheduleData.ScheduleFieldData.*;
import studentListData.StudentListData;
import userUtils.LectionGapFiller;

/**
 *
 * Einheit eines Unterrichtstages in {@link ScheduleData} mit der zugehörigen
 * {@link ScheduleFieldData}-Liste
 *
 */
public class DayColumnData {

    private final Database database;
    private ArrayList<ScheduleFieldData> fieldList;
    private TreeMap<Time, LectionData> lectionMap;
    private TreeMap<Time, Integer> timeToFieldIndexMap;
    private Time absoluteStart, absoluteEnd;
    private int totalNumberOfFields;
    private int fieldCountStart;
    private int fieldCountEnd;
    private ScheduleDay scheduleDay;
    private LectionGapFiller lectionGapFiller;

    public DayColumnData(Database database, StudentListData studentListData) {
        this.database = database;
        fieldList = new ArrayList<>();
        timeToFieldIndexMap = new TreeMap<>();
        lectionGapFiller = new LectionGapFiller(database, this, studentListData);
    }

    public void create(ScheduleDay scheduleDay, ScheduleTimeFrame timeFrame) {
        this.scheduleDay = scheduleDay;
        lectionMap = database.getLectionMapAt(scheduleDay);
        setTimeFrame(timeFrame);
        createFieldList();
    }

    public void update(ScheduleDay scheduleDay, ScheduleTimeFrame timeFrame) {
        this.scheduleDay = scheduleDay;
        lectionMap = database.getLectionMapAt(scheduleDay);
        setTimeFrame(timeFrame);
        if (hasAllocatedLections()) {
            rebuildFieldList();
        } else {
            createFieldList();
        }
    }

    private void setTimeFrame(ScheduleTimeFrame timeFrame) {
        totalNumberOfFields = timeFrame.getTotalNumberOfFields();
        absoluteStart = timeFrame.getAbsoluteStart();
        absoluteEnd = timeFrame.getAbsoluteEnd();
        fieldCountStart = scheduleDay.getValidStart().diff(absoluteStart);
        fieldCountEnd = scheduleDay.getValidEnd().diff(absoluteStart);
    }

    private void createFieldList() {
        timeToFieldIndexMap.clear();
        Time fieldTime = absoluteStart.clone();
        for (int i = 0; i < totalNumberOfFields; i++) {
            ScheduleFieldData scheduleField = new ScheduleFieldData(database);
            scheduleField.setFieldTime(fieldTime.clone());
            scheduleField.setTeacherTimeEnabled(i >= fieldCountStart && i <= fieldCountEnd);
            fieldList.add(scheduleField);
            timeToFieldIndexMap.put(fieldTime.clone(), i);
            fieldTime.inc();
        }
    }

    private void rebuildFieldList() {
        timeToFieldIndexMap.clear();
        int i = 0;
        Time fieldTime = absoluteStart.clone();
        while (i < totalNumberOfFields) {
            ScheduleFieldData scheduleField = new ScheduleFieldData(database);
            LectionData lection = lectionMap.get(fieldTime);
            if (lection != null) {
                lection.updateTeacherTime(i >= fieldCountStart && i <= fieldCountEnd);
                fieldList.addAll(lection.getFieldList());
                for (int j = 0; j < lection.getLength(); j++) {
                    timeToFieldIndexMap.put(fieldTime.clone(), i);
                    fieldTime.inc();
                    i++;
                }
            } else {
                scheduleField.setFieldTime(fieldTime.clone());
                scheduleField.setTeacherTimeEnabled(i >= fieldCountStart && i <= fieldCountEnd);
                fieldList.add(scheduleField);
                timeToFieldIndexMap.put(fieldTime.clone(), i);
                fieldTime.inc();
                i++;
            }
        }
    }

    public boolean hasAllocatedLections() {
        return !lectionMap.isEmpty();
    }

    public boolean hasLectionsOutOfBounds(Time absoluteStart, Time absoluteEnd) {
        Time endOfLastLection = lectionMap.lastEntry().getValue().end();
        return (hasAllocatedLections() && (absoluteStart.greaterThan(lectionMap.firstKey()) || absoluteEnd.lessEqualsThan(endOfLastLection)));
    }

    public void setValidTimeMark(StudentDay day, int listIndex) {
        ScheduleFieldData field = fieldList.get(listIndex);
        Time listTime = field.getFieldTime();
        if (listTime.greaterEqualsThan(day.start1()) && listTime.lessEqualsThan(day.end1())) {
            field.setValidTimeMark(TIME_INTERVAL_1);
        }
        if (listTime.greaterEqualsThan(day.start2()) && listTime.lessEqualsThan(day.end2())) {
            field.setValidTimeMark(TIME_INTERVAL_2);
        }
        if (listTime.equals(day.favorite())) {
            field.setValidTimeMark(FAVORITE);
        }
    }

    public void setValidTimeMarks(StudentDay day) {
        for (int i = 0; i < totalNumberOfFields; i++) {
            setValidTimeMark(day, i);
        }
    }

    public void resetValidTimeMarks() {
        for (int i = 0; i < totalNumberOfFields; i++) {
            fieldList.get(i).setValidTimeMark(NO_VALUE);
        }
    }

    public void addLection(Time time, LectionData lection) {
        lectionMap.put(time, lection);
        database.getLectionIDMap().put(lection.getStudentID(), lection);
    }

    public void removeLection(Time time) {
        database.getLectionIDMap().remove(lectionMap.get(time).getStudentID());
        lectionMap.remove(time);
    }

    public void updateStudentID(int deletedStudentID) {
        Set entrySet = lectionMap.entrySet();
        Iterator iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            ((LectionData) mapEntry.getValue()).updateStudentID(deletedStudentID);
        }
    }

    public ArrayList<ScheduleFieldData> getFieldList() {
        return fieldList;
    }

    public ScheduleFieldData getFieldDataAt(int i) {
        return fieldList.get(i);
    }

    public String getDayName() {
        return scheduleDay.getDayName();
    }

    public int getFieldIndexAt(Time fieldTime) {
        return timeToFieldIndexMap.get(fieldTime);
    }

    public LectionGapFiller getLectionGapFiller() {
        return lectionGapFiller;
    }

    public int getTotalNumberOfFields() {
        return totalNumberOfFields;
    }

    public int getFieldCountStart() {
        return fieldCountStart;
    }

    public int getFieldCountEnd() {
        return fieldCountEnd;
    }

    public Time getAbsoluteStart() {
        return absoluteStart;
    }

    public Time getAbsoluteEnd() {
        return absoluteEnd;
    }
}
