/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stundenplan;

import core.DataBase;
import core.SchuelerDay;
import core.ValidTimeListener;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import schuelerliste.SchuelerField;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class LectionField extends JLabel {

    // relevante Schülerdaten für Lectionpanels 
    private String name, vorname;
    private int schuelerID;
    private int lectionType;

    private SchuelerDay[] schuelerdayList;  // Referenz auf Liste aller Tage eines Schülers (class Schüler)

    private final ValidTimeListener[] validTimeListener;
    private int listCount;

    // bei firstEntry darf nur der aktuelle Tag aktiv sein für validTimes
    private boolean timeColumnEnabled;
    // falls Schülerzeit ausserhalb Lehrerzeit ( = stundenplanStart, stundenplanEnd) kein TimeColumn-Scrolling
    private boolean outOfBounds;

    /* Schalter, Panel-Konstruktions-Variablen */
    private boolean lectionSelected; // Lection definitiv  gesetzt oder wieder verschiebbar
    private boolean lectionTemporarySelected; // Switch: als provisorisch markieren
    private int indexLectionField; // Position in LectionField-Liste
    private int lectionID;  // = rowIndexLectionField des 1. Fields
    private int fieldPosition;  // Position des LectionFields innerhalb des Lectionpanels

    public LectionField() {

        lectionSelected = false;
        lectionTemporarySelected = false;
        lectionID = 0;
        fieldPosition = 0;
        schuelerID = 0;

        validTimeListener = new ValidTimeListener[DataBase.getNumberOfDays()];
        listCount = 0;

        timeColumnEnabled = false;  // enabled: DayColumn#markValidTime(), disabled: DayColumn#cleanValidTimeMark() 
        outOfBounds = false;

        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.BOLD, 10));
        setText(" ");
        setBackground(Colors.BACKGROUND);
        setPreferredSize(new Dimension(0, 11));
        setOpaque(true);

    }

    /*   Schalter   */
    public void setFieldSelected(boolean setSelected) {
        this.lectionSelected = setSelected;
    }

    public boolean isSelected() {
        return lectionSelected;
    }

    public void setTemporarySelected(boolean setSelected) {
        lectionTemporarySelected = setSelected;
    }

    public boolean isTemporarySelected() {
        return lectionTemporarySelected;
    }

    /* überträgt relevante Schülerdaten von Schülerfield zu LectionField*/
    public void transferSchuelerDataFrom(SchuelerField schuelerField) {
        this.schuelerID = schuelerField.getSchuelerID();
        this.name = schuelerField.getName();
        this.vorname = schuelerField.getVorname();
        this.lectionType = schuelerField.getLectionType();
        this.schuelerdayList = schuelerField.getSchuelerDayList(); // für Zugriff auf alle SchülerDays
    }

    /* Pseudo-clone(): überträgt relevante Schülerdaten von LectionField zu LectionField */
    public void transferSchuelerDataFrom(LectionField lectionField) {
        this.schuelerID = lectionField.getSchuelerID();
        this.name = lectionField.getName();
        this.vorname = lectionField.getVorname();
        this.lectionType = lectionField.getLectionType();
        this.schuelerdayList = lectionField.getSchuelerDayList(); // für Zugriff auf alle SchülerDays
    }

    /* Getter, Setter für Lection-Panel-Konstruktion */
    public void setRowIndex(int rowIndex) {
        this.indexLectionField = rowIndex;
    }

    public int getRowIndex() {
        return indexLectionField;
    }

    // macht LectionFields als Gruppe (= Lectionpanel) ansprechbar
    public void setLectionID(int lectionID) {
        this.lectionID = lectionID;
    }

    public int getLectionID() {
        return lectionID;
    }

    public void setFieldPosition(int fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public int getFieldPosition() {
        return fieldPosition;
    }

    /* Getter, Setter  für Schueler-Daten */
    public int getSchuelerID() {
        return schuelerID;
    }

    public void setSchuelerID(int schuelerID) {
        this.schuelerID = schuelerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public int getLectionType() {
        return lectionType;
    }

    public void setLectionType(int lectionType) {
        this.lectionType = lectionType;
    }

    public SchuelerDay[] getSchuelerDayList() {  // nötig für die Datentransfer LectionField - LectionField
        return schuelerdayList;
    }

    public void setTimeColumnEnabled(boolean timeColumnEnabled) {
        this.timeColumnEnabled = timeColumnEnabled;
    }

    public boolean isTimeColumnEnabled() {
        return timeColumnEnabled;
    }

    public void setOutOfBounds(boolean outOfBounds) {
        this.outOfBounds = outOfBounds;
    }

    public boolean isOutOfBounds() {
        return outOfBounds;
    }

    /* ValidTimeListener adden*/
    public void addValidTimeListener(ValidTimeListener l) {
        validTimeListener[listCount] = l;
        listCount++;
    }

    /* malt TimeColumns in allen DayColumn-Instanzen */
    public void markTimeColumns() {

        long t1, t2;   // Timestamps
        t1 = System.nanoTime();

        for (int i = 0; i < validTimeListener.length; i++) {
            validTimeListener[i].validTimeSelected(this, schuelerdayList[i]);
        }

        t2 = System.nanoTime();
        //    System.out.println("markTimeColumns(): " + (t2 - t1) / 1000 + " microsec");
    }

    /* löscht Markierungen in allen TimeColumns */
    public void cleanTimeColumns() {

        long t1, t2;  // Timestamps
        t1 = System.nanoTime();

        for (int i = 0; i < validTimeListener.length; i++) {
            validTimeListener[i].validTimeDeselected();
        }

        t2 = System.nanoTime();
        //    System.out.println("cleanTimeColumns(): " + (t2 - t1) / 1000 + " microsec");
    }

    /*  setzt validTimes in allen DayColumns */
    public void setSchuelerDays() {
        for (int i = 0; i < validTimeListener.length; i++) {
            validTimeListener[i].schuelerSelected(schuelerdayList[i]);
        }
    }
}
