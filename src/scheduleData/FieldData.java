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
    private int validStart1, validStart2, validEnd1, validEnd2, favorite;

    
    
    /*  Getter, Setter */
    
    public void setTime(Time time) {
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public void setValidStart1(int validStart1) {
        this.validStart1 = validStart1;
    }

    public void setValidEnd1(int validEnd1) {
        this.validEnd1 = validEnd1;
    }

    public void setValidEnd2(int validEnd2) {
        this.validEnd2 = validEnd2;
    }

    public void setValidStart2(int validStart2) {
        this.validStart2 = validStart2;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
    
    public int getValidEnd2() {
        return validEnd2;
    }

    public int getValidStart2() {
        return validStart2;
    }

    public int getValidEnd1() {
        return validEnd1;
    }

    public int getValidStart1() {
        return validStart1;
    }

    public int getFavorite() {
        return favorite;
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

}
