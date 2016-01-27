/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import util.Time;

/**
 *
 * Einheit einer eingeteilten Lektion, bestehend aus n = Lektionsdauer/5 Zellen
 * (= {@link ScheduleFieldData})
 */
public class LectionData {

    private ArrayList<ScheduleFieldData> lection;
    private int fieldCount;

    public LectionData() {
        lection = new ArrayList<>();
        resetFieldCount();
    }

    public void add(ScheduleFieldData field) {
        lection.add(field);
    }

    public boolean hasNextField() {
        return fieldCount < lection.size();
    }

    public ScheduleFieldData getNextField() {
        ScheduleFieldData scheduleFieldData = lection.get(fieldCount);
        fieldCount++;
        return scheduleFieldData;
    }

    public void resetFieldCount() {
        fieldCount = 0;
    }

    public Time getEnd() {
        return lection.get(lection.size() - 1).getFieldTime();
    }

}
