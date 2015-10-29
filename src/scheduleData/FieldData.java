/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import util.Time;

/**
 *
 * @author Mathias
 */
public class FieldData {

    private Time time;
    private int validTime = 0;
    public static final int TIME_INTERVAL_1 = 1, TIME_INTERVAL_2 = 2, FAVORITE = 3;

    /*  Getter, Setter */
    public void setValidTime(int validTime) {
        this.validTime = validTime;
    }

    public int getValidTime() {
        return validTime;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public Boolean isMinute(int index) {
        return time.getMinute() != 0;
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
