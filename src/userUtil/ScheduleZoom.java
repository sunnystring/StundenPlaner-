/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userUtil;

import scheduleUI.TimeTable;

/**
 * Jeder Klick auf Zoom-Button verkleinert oder vergrössert Stundenplan-View (4 Stufen), 
 * enthält Default-Schriftgrössen und -RowHeight von {@link TimeTable}
 * 
 */
public class ScheduleZoom {

    public static final int DEFAULT_HEIGHT = 14;
    private static final int HEIGHT_DIFF = 4;
    private static final int UPPER_HEIGHT = DEFAULT_HEIGHT + HEIGHT_DIFF;
    private static final int LOWER_HEIGHT = DEFAULT_HEIGHT - HEIGHT_DIFF;
    public static final float DEFAULT_SIZE_1 = (float) 10, DEFAULT_SIZE_2 = (float) 8;
    private static final float INC1 = (float) 0.3, INC2 = (float) 0.2;
    private static final float UPPER_SIZE_1 = DEFAULT_SIZE_1 + HEIGHT_DIFF * INC1;
    private static final float UPPER_SIZE_2 = DEFAULT_SIZE_2 + HEIGHT_DIFF * INC2;
    private int rowHeight = DEFAULT_HEIGHT;
    private float fontSize1, fontSize2;

    public void setNextSize() {
        if (rowHeight > LOWER_HEIGHT) {
            rowHeight--;
            fontSize1 -= INC1;
            fontSize2 -= INC2;
        } else {
            rowHeight = UPPER_HEIGHT;
            fontSize1 = UPPER_SIZE_1;
            fontSize2 = UPPER_SIZE_2;
        }
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public float getFontSize1() {
        return fontSize1;
    }

    public float getFontSize2() {
        return fontSize2;
    }
}
