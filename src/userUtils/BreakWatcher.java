/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtils;

import core.Database;
import java.util.Map;
import scheduleData.DayColumnData;
import scheduleData.LectionData;
import scheduleData.ScheduleData;
import scheduleData.ScheduleFieldData;
import scheduleUI.DayField;
import scheduleUI.Schedule;
import utils.Colors;
import utils.Time;

/**
 *
 * Zeigt im {@link DayField} des Stundenplans an, sobald eine 15- oder
 * 30-Minuten-Pause gemacht werden muss
 *
 */
public class BreakWatcher {

    private Database database;
    private ScheduleData scheduleData;
    private Schedule schedule;
    public static final int FOUR_HOURS_BOUND = 48, SIX_HOURS_BOUND = 72, FOUR_HOURS_MIN_BREAK = 3, SIX_HOURS_MIN_BREAK = 6;

    public BreakWatcher(Database database, ScheduleData scheduleData) {
        this.database = database;
        this.scheduleData = scheduleData;
    }

    public void check(int dayIndex) {
        if (!database.getLectionMapAt(dayIndex).isEmpty()) {
            DayColumnData dayColumn = scheduleData.getDayColumn(dayIndex);
            Map.Entry<Time, LectionData> entry = database.getLectionMapAt(dayIndex).lastEntry();
            Time endLastLection = entry.getValue().end();
            int lectionFieldsTotal = 0;
            int breakFields = 0;
            int breakFieldMax = 0;
            for (ScheduleFieldData field : dayColumn.getFieldList()) {
                if (field.isLectionAllocated()) {
                    lectionFieldsTotal++;
                    breakFields = 0;
                } else if (lectionFieldsTotal > 0 && field.getFieldTime().lessEqualsThan(endLastLection)) {
                    breakFields++;
                    if (breakFieldMax <= breakFields) {
                        breakFieldMax = breakFields;
                    }
                }
            }
            if (lectionFieldsTotal > SIX_HOURS_BOUND && breakFieldMax < SIX_HOURS_MIN_BREAK) {
                schedule.showBreakRequired(dayIndex, Colors.RED_4, "30 Minuten Pause einplanen!");
            } else if (lectionFieldsTotal > FOUR_HOURS_BOUND && breakFieldMax < FOUR_HOURS_MIN_BREAK) {
                schedule.showBreakRequired(dayIndex, Colors.RED_2, "15 Minuten Pause einplanen!");
            } else {
                schedule.showBreakRequired(dayIndex, Colors.DAY_FIELD_COLOR, dayColumn.getDayName());
            }
        }
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
