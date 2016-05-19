/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtilsUI;

import core.Database;
import core.ScheduleTimes;
import core.StudentDay;
import java.awt.Color;
import scheduleData.ScheduleData;
import studentListData.StudentListData;
import userUtils.IncompatibleStudentTimes;
import userUtils.StudentTimesDistribution;
import utils.Colors;
import utils.Time;

/**
 * Manager von {@link StudentTimesDistribution} und
 * {@link IncompatibleStudentTimes}
 *
 */
public class ColoredStudentDays {

    public static final boolean COLORED = true, DEFAULT_COLORS = false;
    private Database database;
    private ScheduleTimes scheduleTimes;
    private StudentTimesDistribution timeDistribution;
    private IncompatibleStudentTimes incompatibleStudentTimes;
    private boolean isColored;

    public ColoredStudentDays(Database database, StudentListData studentListData) {
        this.database = database;
        scheduleTimes = database.getScheduleTimes();
        timeDistribution = new StudentTimesDistribution(scheduleTimes);
        incompatibleStudentTimes = new IncompatibleStudentTimes(database, studentListData);
        isColored = DEFAULT_COLORS;
    }

    public void init(ScheduleData scheduleData) {
        timeDistribution.init();
        incompatibleStudentTimes.setScheduleData(scheduleData);
    }

    public void update() {
        timeDistribution.update();
        incompatibleStudentTimes.resetAllStudentFields();
    }

    public void findIncompatibleStudentTimes() {
        incompatibleStudentTimes.resetAllStudentFields();
        incompatibleStudentTimes.findBlockingScheduleTimes();
        incompatibleStudentTimes.findAll();
    }

    public void updateIncompatibleStudentDays() {
        findIncompatibleStudentTimes();
    }

    public Color getFieldColorAt(int rowIndex, int dayIndex, boolean isIncompatible, boolean isSingleDay) { // rowIndex = studentID, dayIndex = col-1
        StudentDay studentDay = database.getStudent(rowIndex).getStudentTimes().getValidStudentDay(dayIndex);
        Time time = studentDay.earliestStart();
        if (isColored) {
            if (time.isEmpty()) {
                return Colors.LIGHT_GRAY;
            } else if (isIncompatible) {
                return timeDistribution.getRed(time, dayIndex);
            } else if (isSingleDay) {
                return timeDistribution.getPurple(time, dayIndex);
            } else {
                return timeDistribution.getBlue(time, dayIndex);
            }
        } else {
            if (isIncompatible) {
                return Colors.RED_DEFAULT;
            } else if (isSingleDay) {
                return Colors.PURPLE_DEFAULT;
            } else {
                return Colors.BLUE_DEFAULT;
            }
        }
    }

    public void setMode(boolean isColored) {
        this.isColored = isColored;
    }
}
