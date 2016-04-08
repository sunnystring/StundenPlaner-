/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtils;

import core.Database;
import java.util.Map;
import java.util.TreeMap;
import scheduleData.LectionData;
import scheduleUI.Schedule;
import utils.Colors;
import utils.Time;

/**
 *
 * @author mathiaskielholz
 */
public class BreakWatcher {

    private Database database;
    private Schedule schedule;
    public static final int FOUR_HOURS_BOUND = 48, SIX_HOURS_BOUND = 72, FOUR_HOURS_MIN_BREAK = 3, SIX_HOURS_MIN_BREAK = 6;

    public void check(int dayIndex) {
        TreeMap<Time, LectionData> lectionMap = database.getLectionMapAt(dayIndex);
        if (lectionMap.size() > 0) {
            int fieldCount = 0;
            boolean enoughBreakForFourHours = false;
            boolean enoughBreakForSixHours = false;
            Time lectionEnd = lectionMap.firstEntry().getValue().getEnd();
            for (Map.Entry<Time, LectionData> entry : lectionMap.entrySet()) {
                fieldCount += entry.getValue().getLength();
                int diff = 0;
                if (entry.getKey().greaterEqualsThan(lectionEnd)) {
                    diff = entry.getKey().minus(lectionEnd.plus(5)).getNumberOfFields(Time.ROUND_UP);
                    lectionEnd = entry.getValue().getEnd();
                }
                if (diff >= FOUR_HOURS_MIN_BREAK) {
                    enoughBreakForFourHours = true;
                }
                if (diff >= SIX_HOURS_MIN_BREAK) {
                    enoughBreakForSixHours = true;
                }
            }
            if (fieldCount > SIX_HOURS_BOUND && !enoughBreakForSixHours) {
                schedule.showBreakRequired(dayIndex, Colors.RED_4, "30 Minuten Pause einplanen!");
            } else if (fieldCount > FOUR_HOURS_BOUND && !enoughBreakForFourHours) {
                schedule.showBreakRequired(dayIndex, Colors.RED_3, "15 Minuten Pause einplanen!");
            } else {
                schedule.showBreakRequired(dayIndex, Colors.DAY_FIELD, database.getDayNameAt(dayIndex));
            }
        }
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
