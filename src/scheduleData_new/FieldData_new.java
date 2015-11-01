/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData_new;

import util.Time;

/**
 *
 * @author Mathias
 */
public class FieldData_new {

    private Time time;
    private int validTime = 0;
    public static final int TIME_INTERVAL_1 = 1, TIME_INTERVAL_2 = 2, FAVORITE = 3;
    private int columnID;
    private boolean isTeacherTime;
  //  private boolean isRowMoved;

    /*  Getter, Setter */
    public void setValidTime(int validTime) {
        this.validTime = validTime;
    }

    public int getValidTime() {
        return validTime;
    }

    public void setColumnID(int columnID) {
        this.columnID = columnID;
    }

    public int getColumnID() {
        return columnID;
    }

//    public boolean isRowMoved() {
//        return isRowMoved;
//    }
//
//    public void setIsRowMoved(boolean isRowMoved) {
//        this.isRowMoved = isRowMoved;
//    }
    
    public void setTime(Time time) {
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public Boolean isMinute(int index) {
        return time.getMinute() != 0;
    }

    public boolean isTeacherTime() {
        return isTeacherTime;
    }

    public void setIsTeacherTime(boolean teacherTime) {
        this.isTeacherTime = teacherTime;
    }

    public String getMinute(int index) {
        return String.valueOf(time.getMinute());
    }

    public String getHour(int index) {
        return String.valueOf(time.getHour());
    }

    @Override
    public String toString() {
        return time.toString();
    }

}
