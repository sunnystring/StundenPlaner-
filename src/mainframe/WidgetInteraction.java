/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainframe;

import schedule.LectionField;

/**
 *
 * @author Mathias
 */
public class WidgetInteraction {

    // Globale Schalter für GUI-Management 
    private boolean studentListEnabled;  // StudentRows und DayColumns 
    private boolean noFieldSelected;  // StudentRows und DayColumns 
    private boolean noRowSelected;  // StudentRows
    private boolean dragEnabled; // DayColumns

    private final LectionField temporaryLectionField; // DayColumn
    private int lectionLength; //  DayColumn

    public WidgetInteraction() {

        studentListEnabled = true;  // Hauptschalter StudentList: diese ist als ganzes ansprechbar, verhindert Eingaben in StudentList im Lectionfield-Dragmodus 
        noFieldSelected = true;  // falls mind. ein StudentField selektiert
        noRowSelected = true;  // jede StudentRow ist scrollbar
        dragEnabled = false;   // Hauptschalter für dragLectionpanel

        temporaryLectionField = new LectionField();
    }

    public void setStudentListEnabled(boolean enabled) {
        studentListEnabled = enabled;
    }

    public boolean studentListEnabled() {
        return studentListEnabled;
    }

    public void setNoRowSelected(boolean enabled) {
        noRowSelected = enabled;
    }

    public boolean noRowSelected() {
        return noRowSelected;
    }

    public boolean noFieldSelected() {
        return noFieldSelected;
    }

    public void setNoFieldSelected(boolean enabled) {
        noFieldSelected = enabled;
    }

    public boolean dragEnabled() {
        return dragEnabled;
    }

    public void setDragEnabled(boolean enabled) {
        dragEnabled = enabled;
    }

    public int getLectionLength() {
        return lectionLength;
    }

    public void setLectionLength(int lectionLength) {
        this.lectionLength = lectionLength;
    }

    public LectionField getTemporaryLectionField() {
        return temporaryLectionField;
    }
    

}
