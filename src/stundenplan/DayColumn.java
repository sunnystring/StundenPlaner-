/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
  */
package stundenplan;

import core.DataBase;
import core.SchuelerDay;
import core.StundenplanDay;
import core.ValidTimeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import mainframe.MainFrame;
import schuelerliste.SchuelerField;
import schuelerliste.SchuelerRow;

import util.Colors;
import util.GridLayout2;
import util.Time;

/**
 *
 * @author Mathias
 */
public class DayColumn extends JPanel implements MouseListener, ValidTimeListener {

    private TimeField[] timeFieldList;
    private LectionField[] lectionFieldList;
    private DayField dayField;
    private TimeColumn timeColumn1, timeColumn2;
    private LectionColumn lectionColumn1, lectionColumn2;
    private JPanel mainPanel;

    // Unterrichtszeiten
    private StundenplanDay stundenplanDay;
    private static Time absoluteStart = new Time("23.00"); // obere Grenze initialisieren
    private static Time absoluteEnd = new Time(); // untere Grenze initialisieren
    private Time stundenplanStart;  // Beginn Untericht gemäss StundenplanDay
    private Time stundenplanEnd;   // Ende Unterricht gemäss StundenplanDay

    /* von Time zu int konvertierte Grössen */
    private static int totalNumberOfFields; // globale Anzahl Time- und Lectionfields 
    private int fieldCountStart;  // Start Unterrichtsbeginn (Zähler, nicht Zeit)
    private int fieldCountEnd;  // Ende Unterrichtsende (Zähler, nicht Zeit)

    /* Zwischenspeicher Schüler-Daten */
    private static final LectionField TEMP_LECTIONFIELD = new LectionField();
    private static int lectionLength; // globale Lektionsdauer für den Austausch mit dem Zwischenspeicher
    private SchuelerDay schuelerDay;  // lokaler Tag für Zugriff auf die tagspezifischen Einteilungszeiten

    // Schalter für GUI-Management
    private static boolean dragEnabled; // Hauptschalter für dragLectionpanel
    private static boolean firstEntry;  // für Initialisierung aller addListener(schülerListe), falls irgendeine DayColumn angewählt
    private boolean spaceAvailable;  // macht, dass Lectionpanel verschwindet, wenn zuwenig Platz zum setzen

    //   private int dayID; //1. Tag = 0, 2. Tag = 1 usw.  
    public DayColumn(StundenplanDay stundenplanDay) {

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
        setBackground(Colors.BACKGROUND);

        this.stundenplanDay = stundenplanDay;
        stundenplanStart = stundenplanDay.getValidStart();
        stundenplanEnd = stundenplanDay.getValidEnd();

        /* hier wird der abolute Zeitrahmen aller Tage bestimmt */
        if (stundenplanStart.smallerThan(absoluteStart)) {
            absoluteStart = stundenplanStart;
        }
        if (stundenplanEnd.greaterThan(absoluteEnd)) {
            absoluteEnd = stundenplanEnd;
        }

        firstEntry = true;
        dragEnabled = false;
        spaceAvailable = true;

        dayField = new DayField();

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout2(1, 4, 0, 0));
        mainPanel.setBackground(Colors.BACKGROUND);
    }

    /* hier wird der individuelle Zeitrahmen jeder DayColumn initialisiert und zu int konvertiert*/
    public void initTimeFrame() {

        fieldCountStart = stundenplanStart.diff(absoluteStart); // validStart - absoluteStart = Anzahl 5-Min.-Felder
        fieldCountEnd = stundenplanEnd.diff(absoluteStart); // validEnd - absoluteStart = Anzahl 5-Min.-Felder
        totalNumberOfFields = absoluteEnd.diff(absoluteStart);
    }

    /* DayColumn zeichnen  */
    public void createDayColumn() {

        /* Liste mit allen TimeFields befüllen */
        timeFieldList = new TimeField[totalNumberOfFields];
        for (int i = 0; i < totalNumberOfFields; i++) {
            timeFieldList[i] = new TimeField();
            //           timeFieldList[i].setRowIndex(i);  // jedes TimeField "weiss" seine Position in der Liste 
        }

        /* Liste mit allen LectionFields befüllen */
        lectionFieldList = new LectionField[totalNumberOfFields];
        for (int i = 0; i < totalNumberOfFields; i++) {
            lectionFieldList[i] = new LectionField();
            lectionFieldList[i].addMouseListener(this); // Listener für Mouse-Aktionen (impl. hier und in TimeField)

            for (DayColumn d : Stundenplan.getDayColumnList()) {   // jedes LectionField bekommt eine Referenz auf alle DayColumns
                lectionFieldList[i].addValidTimeListener(d);
            }
            lectionFieldList[i].setRowIndex(i);  // jedes lectionField "weiss" seine Position in der Liste 
        }

        timeColumn1 = new TimeColumn(this);

        lectionColumn1 = new LectionColumn(this);
        lectionColumn1.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 3));

        timeColumn2 = new TimeColumn(this);

        lectionColumn2 = new LectionColumn(this);
        lectionColumn2.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        dayField.setBackground(Colors.DAY_FIELD);
        dayField.setPreferredSize(new Dimension(0, 25));
        dayField.setText(stundenplanDay.getDayName());    // ToDo : dynamisch
        add(BorderLayout.PAGE_START, dayField);

        createTimeColumn(timeColumn1, 0, getStartTime());
        createLectionColumn(lectionColumn1, 0);

        createTimeColumn(timeColumn2, totalNumberOfFields / 2, getBreakPoint());
        createLectionColumn(lectionColumn2, totalNumberOfFields / 2);

        mainPanel.add(timeColumn1);
        mainPanel.add(lectionColumn1);
        mainPanel.add(timeColumn2);
        mainPanel.add(lectionColumn2);
        mainPanel.setMinimumSize(getPreferredSize());
        add(BorderLayout.CENTER, mainPanel);

        JLabel emptyField = new JLabel(); // Filler
        emptyField.setPreferredSize(new Dimension(0, 30));
        add(BorderLayout.PAGE_END, emptyField);
    }

    // Hilfsmethoden
    private void createTimeColumn(TimeColumn timeCol, int listCount, Time startTime) {
        timeCol.fillTimeColumn(listCount, startTime);
        timeCol.add(Box.createRigidArea(new Dimension(14, 0)));  // Todo -> Wert dynamisch anpassbar machen ?
        add(timeCol);
    }

    private void createLectionColumn(LectionColumn lectCol, int listCount) {
        lectCol.fillLectionColumn(listCount);
        lectCol.add(Box.createRigidArea(new Dimension(90, 0)));   // Todo Wert dynamisch anpassbar
        add(lectCol);
    }

    /* LectionPanel setzen und wieder verschiebbar machen, setSelected setzt LectionField SelectionState, 
     dataTransferEnable =  SchülerID kommt in Zwischenspeicher, lectionTyp kommt immer vom gerade angeklickten LectionField
     */
    private void addLectionPanel(int start, Color background, Color foreground, boolean setSelected, boolean dataTransferEnabled, int lectionType) {

        int position = 0;  // interner LectionPanel-Counter
        for (int i = start; i < start + lectionType; i++) {

            if (!spaceAvailable) {   // falls Space < lectionLength, abfangen
                break;
            }
            lectionFieldList[i].setBackground(selectValidTimeColor(background, Color.ORANGE, background, start, schuelerDay));
            lectionFieldList[i].setForeground(selectValidTimeColor(foreground, foreground, Colors.FAVORITE, start, schuelerDay));
            lectionFieldList[i].setLectionID(start);  // gibt Koordinate von erstem Lectionfield als ID des Lectionpanels            
            lectionFieldList[i].setFieldSelected(setSelected); // jedes Lectionfield als gesetzt markieren
            timeFieldList[i].setFieldSelected(setSelected);  // kein TimeField-Scroll

            /* Datenübergabe */
            if (dataTransferEnabled) {
                if (setSelected) {
                    lectionFieldList[i].transferSchuelerDataFrom(TEMP_LECTIONFIELD); // falls gesetzt
                } else {
                    TEMP_LECTIONFIELD.transferSchuelerDataFrom(lectionFieldList[i]); // falls im Wechsel zum Drag-Modus
                    lectionLength = TEMP_LECTIONFIELD.getLectionType();
                }
            }
            if (position == 0) {
                lectionFieldList[i].setText(timeFieldList[i].getTime().toString());
                lectionFieldList[i].setFont(this.getFont().deriveFont(Font.PLAIN, 8));
                lectionFieldList[i].setForeground(Color.GRAY);
                lectionFieldList[i].setBorder(BorderFactory.createEmptyBorder(5, 6, 0, 0));
            }
            if (position == 1) {
                lectionFieldList[i].setText(lectionFieldList[i].getVorname());
            }
            if (position == 2) {
                lectionFieldList[i].setText(lectionFieldList[i].getName());
            }
            if (position == lectionType - 1) {
                lectionFieldList[i].setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, getBackground()));  // Gap zwischen Lections setzen
            }
            if (lectionFieldList[i].isSelected()) {
                /* falls selektiert: jedes LectionField "weiss" seine Position innerhalb des Lectionpanels, damit es sich zeichnen kann beim Dragen    */
                lectionFieldList[i].setFieldPosition(position);
            } else {
                lectionFieldList[i].setFieldPosition(0);  // wenn nicht sel. zurücksetzen
            }
            position++;
        }
    }

    private void dragLectionPanel(int start) {

        int position = 0;
        int diff = 4;  // Falls auch 50-Lektionen, sonst hätte diff = 2 gereicht (lectionLength40 - lectionLength30)

        for (int i = start; i < start + lectionLength; i++) {

            /* verhindert Setzen ausserhalb der oberen Zeitgrenze*/
            if (start > totalNumberOfFields - lectionLength) {
                spaceAvailable = false;
                break;
            }
            // verhindert Setzen einer Lection, wenn oberhalb einer gesetzten Lection nicht genug Platz ist
            if (lectionFieldList[i + lectionLength - 1 - position].isSelected()) {
                spaceAvailable = false;
                break;
            }
            // Kunstgriff damit ein längeres-DragPanel kein kürzeres LectionPanel wegwischt
            if (!lectionFieldList[i + lectionLength - 1 - position].isSelected() && lectionFieldList[i + lectionLength - 1 - position - diff].isSelected()) {
                spaceAvailable = false;
                break;
            }
            spaceAvailable = true;

            lectionFieldList[i].transferSchuelerDataFrom(TEMP_LECTIONFIELD); // Schülerdaten vom Zwischenspeicher zu LectionField
            lectionFieldList[i].setBackground(selectValidTimeColor(Colors.LIGHT_GREEN, Color.ORANGE, Colors.LIGHT_GREEN, start, schuelerDay));
            lectionFieldList[i].setForeground(selectValidTimeColor(Color.WHITE, Color.WHITE, Colors.FAVORITE, start, schuelerDay));
            lectionFieldList[i].setFont(this.getFont().deriveFont(Font.BOLD, 10));
            lectionFieldList[i].setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0)); // Gap wieder löschen
            lectionFieldList[i].setFieldSelected(false);

            if (position == 1) {
                lectionFieldList[i].setText(lectionFieldList[i].getVorname());
            }
            if (position == 2) {
                lectionFieldList[i].setText(lectionFieldList[i].getName());
            }
            if (position == lectionLength - 1) {
                lectionFieldList[i].setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, getBackground()));  // Gap zwischen Lections setzen
            }
            position++;
        }
    }

    private void cleanLectionPanel(int start) {

        timeFieldList[start].setBackground(Colors.BACKGROUND);

        for (int i = start; i < start + lectionFieldList[start].getLectionType(); i++) {  // lokale lectionLength des aktuellen Schülers benutzen,
            lectionFieldList[i].setBackground(Colors.BACKGROUND);
            lectionFieldList[i].setForeground(Colors.BACKGROUND);
            lectionFieldList[i].setText(" ");
            lectionFieldList[i].setFieldSelected(false);
            timeFieldList[i].setFieldSelected(false);
        }
    }

    /* gültige Zeitintervalle in TimeColumn markieren */
    private void markValidTime(SchuelerDay schuelerday) {

        for (int i = 0; i < totalNumberOfFields; i++) {
            timeFieldList[i].setBackground(Colors.BACKGROUND);
            if (i >= fieldCountStart && i <= fieldCountEnd) {
                timeFieldList[i].setBackground(selectValidTimeColor(Colors.LIGHT_GREEN, Colors.BACKGROUND, Colors.FAVORITE, i, schuelerday));
            }
            if (timeFieldList[i].isHourMarkSelected() && !timeFieldList[i].isFavorite()) {
                timeFieldList[i].setBackground(Colors.TIMEFIELD_HOUR);
            }
        }
    }

    private void cleanValidTimeMark() {

        for (int i = 0; i < totalNumberOfFields; i++) {
            timeFieldList[i].setBackground(Colors.BACKGROUND);
            timeFieldList[i].setValidTime(false);  // Farb-Status zurücksetzen
            timeFieldList[i].setFavorite(false); // Farb-Status zurücksetzen
            if (timeFieldList[i].isHourMarkSelected()) {
                timeFieldList[i].setBackground(Colors.TIMEFIELD_HOUR);
            }
        }
    }

    private void initTimeColumn(boolean columnEnabled) {

        for (int i = 0; i < totalNumberOfFields; i++) {
            lectionFieldList[i].setTimeColumnEnabled(columnEnabled); // bei firstEntry darf nur der aktuelle Tag aktiv sein für validTimes
            lectionFieldList[i].setOutOfBounds(i < fieldCountStart || i > fieldCountEnd); // Flag setzen, falls Schülerzeit ausserhalb Lehrerzeit}
        }
    }

    /* ordnet den Zeiten die entsprechenden Farben zu */
    private Color selectValidTimeColor(Color validColor, Color notValidColor, Color favorite, int index, SchuelerDay schuelerday) {

        Time listTime = timeFieldList[index].getTime();  // aktuelle Stundenplan-Zeit

        if (listTime.equals(schuelerday.getFavorite())) {  // Favorit zuerst, damit nicht überschrieben werden kann
            timeFieldList[index].setFavorite(true);  // TimeField bekommt seinen Farb-Status
            return favorite;
        }
        if (listTime.greaterEqualsThan(schuelerday.getStartTime1()) && listTime.smallerEqualsThan(schuelerday.getEndTime1())) {
            timeFieldList[index].setValidTime(true); // TimeField bekommt seinen Farb-Status
            return validColor;
        }
        if (listTime.greaterEqualsThan(schuelerday.getStartTime2()) && listTime.smallerEqualsThan(schuelerday.getEndTime2())) {
            timeFieldList[index].setValidTime(true); // TimeField bekommt seinen Farb-Status
            return validColor;
        }
        return notValidColor;
    }

    /*  Getter, Setter  */
    public int getTotalNumberOfFields() {
        return totalNumberOfFields;
    }

    public int getStundenplanStart() {
        return fieldCountStart;
    }

    public int getStundenplanEnd() {
        return fieldCountEnd;
    }

    public TimeField[] getTimeFieldList() {
        return timeFieldList;
    }

    public LectionField[] getLectionFieldList() {
        return lectionFieldList;
    }

    public Time getStartTime() {
        Time absStart = new Time();
        absStart = absoluteStart.clone();
        return absStart;
    }

    public Time getBreakPoint() {
        Time breakPoint = new Time();
        breakPoint = absoluteStart.clone();
        for (int i = 0; i < totalNumberOfFields / 2; i++) {
            breakPoint.inc();
        }
        return breakPoint;
    }

//    public void setDayID(int dayID) {
//        this.dayID = dayID;
//    }
//    public int getDayID() {
//        return dayID;
//    }

    /*  Schalter */
    public static boolean dragEnabled() {
        return dragEnabled;
    }

    /* ValidTimeListener-Implementation */
    @Override
    public void schuelerSelected(SchuelerDay schuelerDay) {
        this.schuelerDay = schuelerDay;
    }

    @Override
    public void validTimeSelected(LectionField l, SchuelerDay day) {
        markValidTime(day);
        initTimeColumn(true);
    }

    @Override
    public void validTimeDeselected() {
        cleanValidTimeMark();
        initTimeColumn(false);
    }

    /* MouseListener-Implementation*/
    @Override
    public void mouseClicked(MouseEvent m) {

        SchuelerField s;
        LectionField l;

        int start;

        if (m.getSource() instanceof SchuelerField) {
            s = (SchuelerField) m.getSource();
            /* dieser MouseListener(Schülerliste) kann erst im nachhinein geaddet werden,
             da Schülerliste in MainFrame noch nicht initialisiert ist: MainFrame.getSchülerListe() = null! */
            if (firstEntry) {
                for (int i = 0; i < DataBase.getNumberOfDays(); i++) {
                    for (int j = 0; j < totalNumberOfFields; j++) {
                        Stundenplan.getDayColumn(i).lectionFieldList[j].addMouseListener(MainFrame.getSchülerListe());
                    }
                }
                firstEntry = false;
            }

            if (SchuelerRow.isSchuelerlisteEnabled()) { // Schülerliste gesperrt, damit Lectionfield-Daten nicht überschrieben werden

                if (s.isFieldSelected()) {
                    /* Datatransfer Schülerdaten von SchülerField an Stundenplan bzw. LectionField */
                    TEMP_LECTIONFIELD.transferSchuelerDataFrom(s);
                    lectionLength = TEMP_LECTIONFIELD.getLectionType();
                    dragEnabled = !SchuelerRow.noFieldSelected(); // sobald mind. ein SchuelerField selected, kann LectionField gedragt werden (= dragEnabled)
                    s.setSchuelerDays();  // setzt validTimes in allen DayColumns
                /*  markiert die verfügbare Zeit des in SchuelerField angeklickten Tages */
                    markValidTime(s.getDay());
                    initTimeColumn(true);
                } else {
                    cleanValidTimeMark();
                    initTimeColumn(false);
                }
            }
        }

        /* LectionPanels zeichnen */
        if (m.getSource() instanceof LectionField) {

            l = (LectionField) m.getSource();
            start = l.getRowIndex();

            if (start >= 0 && start <= totalNumberOfFields) {

                /* falls nicht schon gesetzt, LectionPanel zeichnen (kann nur im Drag-Modus gesetzt werden) */
                if (!l.isSelected() && dragEnabled) {

                    addLectionPanel(start, Colors.DARK_GREEN, Color.BLACK, true, true, l.getLectionType()); // true = lectionSelected, true = dataTransferEnabled
                    dragEnabled = !spaceAvailable; // falls Lection gesetzt, dragen deaktivieren. Falls kein Space, muss Stundenplan aktiv bleiben für Dragen

                    if (spaceAvailable) {  // beim Klick auf leeren Space (da zuwenig Platz zum Setzen) darf TimeColumn nicht gecleaned werden
                        l.cleanTimeColumns();
                    }


                    /* falls gesetzt, LectionPanel wieder freimachen (nur erstes Feld von Lectionpanel ist ansprechbar), nur ansprechbar wenn nicht im Drag-Modus */
                } else if (l.isSelected() && l.getFieldPosition() == 0 && !dragEnabled) {

                    l.setSchuelerDays();
                    l.markTimeColumns();
                    spaceAvailable = true;
                    addLectionPanel(start, Colors.LIGHT_GREEN, Color.WHITE, false, true, l.getLectionType()); // false = lection deselected = wieder verschiebbar
                    dragEnabled = true;

                    /* falls gesetzt, provisorische Einteilung, Panel neu zeichnen, nur letztes Feld ansprechbar und wenn nicht im Drag-Modus  */
                } else if (l.getFieldPosition() == l.getLectionType() - 1 && !dragEnabled) {

                    l.setSchuelerDays();
                    spaceAvailable = true;
                    start = start - l.getLectionType() + 1;

                    if (!lectionFieldList[start].isTemporarySelected()) {
                        addLectionPanel(start, Colors.LIGHT_GREEN, Colors.DARK_GREEN, true, false, l.getLectionType());
                        lectionFieldList[start].setTemporarySelected(true);

                    } else {
                        addLectionPanel(start, Colors.DARK_GREEN, Color.BLACK, true, false, l.getLectionType());
                        lectionFieldList[start].setTemporarySelected(false);
                    }

                    /* Doppelklick auf Field 3 und 4 = Panel löschen */
                } else if (l.isSelected() && (l.getFieldPosition() == 3 || l.getFieldPosition() == 4) && !dragEnabled) {
                    if (m.getClickCount() == 2) {
                        spaceAvailable = true;  // schalter reset
                        cleanLectionPanel(l.getLectionID());
                        initTimeColumn(false);
                        dragEnabled = false; // Dragen sperren, ausser es werden bereits gesetzte Panels angeklickt
                    }
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent m) {

        LectionField l;
        int start;

        if (m.getSource() instanceof LectionField) {

            l = (LectionField) m.getSource();
            start = l.getRowIndex();

            if (!l.isSelected() && start >= 0 && start < totalNumberOfFields && dragEnabled) {
                dragLectionPanel(start);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent m) {

        LectionField l;
        int start;

        if (m.getSource() instanceof LectionField) {
            l = (LectionField) m.getSource();
            start = l.getRowIndex();

            /* falls ausserhalb der Stundenplanzeit liegt*/
            if (start < 0) {
                start = 0;
            }
            if (start > totalNumberOfFields - lectionLength) {
                start = totalNumberOfFields - lectionLength;
            }
            for (int i = start; i < start + lectionLength; i++) {
                if (!lectionFieldList[i].isSelected()) {
                    lectionFieldList[i].setBackground(Colors.BACKGROUND);
                    lectionFieldList[i].setText(" ");
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent m) {
    }

    @Override
    public void mouseReleased(MouseEvent m) {
    }

}
