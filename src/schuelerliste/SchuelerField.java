/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schuelerliste;

import core.DataBase;
import core.SchuelerDay;
import core.ValidTimeListener;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class SchuelerField extends JLabel {

    private String name, vorname;
    private int schuelerID;  // jedes SchuelerField "kennt" seine Position in der Schülerliste
    private SchuelerDay schuelerDay;
    private int lectionType;

    private SchuelerDay[] schuelerdayList;  // Referenz auf Liste aller Tage eines Schülers (class Schüler)

    private final ValidTimeListener[] validTimeListener;
    private int listCount;

    // Schalter
    private boolean fieldSelected;

    public SchuelerField() {

        this.vorname = vorname;
        this.name = name;

        fieldSelected = false;

        validTimeListener = new ValidTimeListener[DataBase.getNumberOfDays()];
        listCount = 0;

        setHorizontalAlignment(SwingConstants.LEADING);
        setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3));
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setBackground(Colors.SCHUELER_FIELD_BLUE);
        setOpaque(true);

    }

    /*   Schalter */
    public boolean isFieldSelected() {
        return fieldSelected;
    }

    public void setFieldSelected(boolean fieldSelected) {
        this.fieldSelected = fieldSelected;
    }

    /* Getter, Setter */
    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchuelerIndex(int index) {
        schuelerID = index;
    }

    public int getSchuelerID() {
        return schuelerID;
    }

    public void setDay(SchuelerDay day) {
        schuelerDay = day;
    }

    public SchuelerDay getDay() {
        return schuelerDay;
    }

    public int getLectionType() {
        return lectionType;
    }

    public void setLectionType(int lectionType) {
        this.lectionType = lectionType;
    }

    /* ValidTimeListener */
    public void addValidTimeListener(ValidTimeListener l) {
        validTimeListener[listCount] = l;
        listCount++;
    }

    public void setSchuelerDays() {
        for (int i = 0; i < validTimeListener.length; i++) {
            validTimeListener[i].schuelerSelected(schuelerdayList[i]);
        }
    }

    /* -----------------Rohfassung: Referenz auf schülerDayList */
    public SchuelerDay[] getSchuelerDayList() {
        return schuelerdayList;
    }

    public void setSchuelerDayList(SchuelerDay[] list) {
        schuelerdayList = list;
    }

    /* formatierte Textausgabe der Zeiten aus schülerDay */
    public void showAvailableTimes() {
        super.setText("<html>" + schuelerDay + "<font color=blue>" + schuelerDay.getFavoriteAsString() + "</font></html>");
    }

}
