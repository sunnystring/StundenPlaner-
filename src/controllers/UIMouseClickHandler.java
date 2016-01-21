/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import scheduleData.ScheduleData;
import studentListData.StudentListData;

/**
 *
 * @author mathiaskielholz
 */
public class UIMouseClickHandler extends MouseAdapter {

    private ScheduleData scheduleData;
    private StudentListData studentListData;

    public UIMouseClickHandler(ScheduleData scheduleData, StudentListData studentListData) {
        this.scheduleData = scheduleData;
        this.studentListData = studentListData;
        // Refrenzen von Lection-/TimeField von TimeTable = e.getSource()
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

}
