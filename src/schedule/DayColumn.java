/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import mainframe.WidgetInteraction;
import core.StudentDay;
import core.TeacherDay;
import core.ValidTimeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import studentlist.StudentField;

import util.Colors;
import util.GridLayout2;
import util.Time;

/**
 *
 * @author Mathias
 */
public class DayColumn extends JPanel implements MouseListener, ValidTimeListener {

    private TimeField[] timeFieldList;        // enthält alle TimeFields
    private LectionField[] lectionFieldList;  // enthält alle LectionFields
    private ScheduleDayField dayField;
    private TimeColumn timeColumn1, timeColumn2;
    private LectionColumn lectionColumn1, lectionColumn2;
    private JPanel mainPanel;

    private TeacherDay scheduleDay;
    private Time absoluteStart;  // untere globale Zeitgrenze Stundenplan

    /* von Time zu int konvertierte Grössen */
    private int totalNumberOfFields; // globale maximale Anzahl Time- bzw. Lectionfields (= Column-Höhe)
    private int fieldCountStart;  // lokaler Unterrichtsbeginn (Zähler, nicht Zeit)
    private int fieldCountEnd;  // lokales Unterrichtsende (Zähler, nicht Zeit)

    private StudentDay studentDay;  // lokaler Tag für Zugriff auf die tagspezifischen Einteilungszeiten

    /* Variablen/Schalter für GUI-Management */
    private WidgetInteraction wi;
    private boolean spaceAvailable;  // macht, dass Lectionpanel verschwindet, wenn zuwenig Platz zum setzen

    public DayColumn(WidgetInteraction wi) {

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
        setBackground(Colors.BACKGROUND);

        this.wi = wi;

        spaceAvailable = true;

        dayField = new ScheduleDayField();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout2(1, 4, 0, 0));
        mainPanel.setBackground(Colors.BACKGROUND);
    }

    public void setTimeFrame(TeacherDay scheduleDay, ScheduleTimeFrame timeFrame) {

        this.scheduleDay = scheduleDay;

        totalNumberOfFields = timeFrame.getTotalNumberOfFields();
        absoluteStart = timeFrame.getAbsoluteStart();

        /* von Time zu int konvertierte Grössen */
        fieldCountStart = scheduleDay.getValidStart().diff(absoluteStart); // validStart - absoluteStart = Anzahl 5-Min.-Felder
        fieldCountEnd = scheduleDay.getValidEnd().diff(absoluteStart); // validEnd - absoluteStart = Anzahl 5-Min.-Felder
    }

    /* DayColumn zeichnen  */
    public void drawDayColumn() {

        /* Liste mit allen TimeFields befüllen */
        timeFieldList = new TimeField[totalNumberOfFields];
        for (int i = 0; i < totalNumberOfFields; i++) {
            timeFieldList[i] = new TimeField(wi);
        }

        /* Liste mit allen LectionFields befüllen */
        lectionFieldList = new LectionField[totalNumberOfFields];
        for (int i = 0; i < lectionFieldList.length; i++) {
            lectionFieldList[i] = new LectionField();
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
        dayField.setText(scheduleDay.getDayName());
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
            lectionFieldList[i].setBackground(selectValidTimeColor(background, Color.ORANGE, background, start, studentDay));
            lectionFieldList[i].setForeground(selectValidTimeColor(foreground, foreground, Colors.FAVORITE, start, studentDay));
            lectionFieldList[i].setLectionID(start);  // gibt Koordinate von erstem Lectionfield als ID des Lectionpanels            
            lectionFieldList[i].setFieldSelected(setSelected); // jedes Lectionfield als gesetzt markieren
            timeFieldList[i].setFieldSelected(setSelected);  // kein TimeField-Scroll

            /* Datenübergabe */
            if (dataTransferEnabled) {
                if (setSelected) {
                    lectionFieldList[i].transferStudentDataFrom(wi.getTemporaryLectionField()); // falls gesetzt
                } else {
                    wi.getTemporaryLectionField().transferStudentDataFrom(lectionFieldList[i]); // falls im Wechsel zum Drag-Modus
                    wi.setLectionLength(wi.getTemporaryLectionField().getLectionType());
                }
            }
            if (position == 0) {
                lectionFieldList[i].setText(timeFieldList[i].getTime().toString());
                lectionFieldList[i].setFont(this.getFont().deriveFont(Font.PLAIN, 8));
                lectionFieldList[i].setForeground(Color.GRAY);
                lectionFieldList[i].setBorder(BorderFactory.createEmptyBorder(5, 6, 0, 0));
            }
            if (position == 1) {
                lectionFieldList[i].setText(lectionFieldList[i].getFirstName());
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

        int lectionLength = wi.getLectionLength();

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

            lectionFieldList[i].transferStudentDataFrom(wi.getTemporaryLectionField()); // Schülerdaten vom Zwischenspeicher zu LectionField
            lectionFieldList[i].setBackground(selectValidTimeColor(Colors.LIGHT_GREEN, Color.ORANGE, Colors.LIGHT_GREEN, start, studentDay));
            lectionFieldList[i].setForeground(selectValidTimeColor(Color.WHITE, Color.WHITE, Colors.FAVORITE, start, studentDay));
            lectionFieldList[i].setFont(this.getFont().deriveFont(Font.BOLD, 10));
            lectionFieldList[i].setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0)); // Gap wieder löschen
            lectionFieldList[i].setFieldSelected(false);

            if (position == 1) {
                lectionFieldList[i].setText(lectionFieldList[i].getFirstName());
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
    private void markValidTime(StudentDay studentDay) {

        for (int i = 0; i < totalNumberOfFields; i++) {
            timeFieldList[i].setBackground(Colors.BACKGROUND);
            if (i >= fieldCountStart && i <= fieldCountEnd) {
                timeFieldList[i].setBackground(selectValidTimeColor(Colors.LIGHT_GREEN, Colors.BACKGROUND, Colors.FAVORITE, i, studentDay));
            }
            if (timeFieldList[i].isHourMarkSelected() && !timeFieldList[i].isFavorite() && !timeFieldList[i].isValidTime()) {
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
    private Color selectValidTimeColor(Color validColor, Color notValidColor, Color favorite, int index, StudentDay studentDay) {

        Time listTime = timeFieldList[index].getTime();  // aktuelle Schedule-Zeit

        /* Favorit */
        if (listTime.equals(studentDay.getFavorite())) {  // Favorit zuerst, damit nicht überschrieben werden kann
            timeFieldList[index].setFavorite(true);  // TimeField bekommt seinen Farb-Status
            return favorite;
        }
        /* StartTime1 */
        if (listTime.equals(studentDay.getStartTime1())) {
            timeFieldList[index].setValidTime(true);
            return validColor;
        }

        /* gültiges Intervall Time1 */
        if (listTime.greaterEqualsThan(studentDay.getStartTime1()) && listTime.smallerEqualsThan(studentDay.getEndTime1())) {
            timeFieldList[index].setValidTime(true);
            return validColor;
        }
        /* StartTime2 */
        if (listTime.equals(studentDay.getStartTime2())) {
            timeFieldList[index].setValidTime(true);
            return validColor;
        }

        /* gültiges Intervall Time2 */
        if (listTime.greaterEqualsThan(studentDay.getStartTime2()) && listTime.smallerEqualsThan(studentDay.getEndTime2())) {
            timeFieldList[index].setValidTime(true);
            return validColor;
        }
        return notValidColor;
    }

    /*  Getter, Setter  */
    public int getTotalNumberOfFields() {
        return totalNumberOfFields;
    }

    public int getScheduleStart() {
        return fieldCountStart;
    }

    public int getScheduleEnd() {
        return fieldCountEnd;
    }

    public TimeField[] getTimeFieldList() {
        return timeFieldList;
    }

    public LectionField[] getLectionFieldList() {
        return lectionFieldList;
    }

    public Time getStartTime() {
        try {
            return absoluteStart.clone();//absStart;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DayColumn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Time getBreakPoint() {
        try {
            Time breakPoint = absoluteStart.clone();
            for (int i = 0; i < totalNumberOfFields / 2; i++) {
                breakPoint.inc();
            }
            return breakPoint;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DayColumn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /* ValidTimeListener-Implementation */
    @Override
    public void studentSelected(StudentDay studentDay) {
        this.studentDay = studentDay;
    }

    @Override
    public void validTimeSelected(StudentDay day) {
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

        StudentField s;
        LectionField l;

        int start;

        if (m.getSource() instanceof StudentField) {

            s = (StudentField) m.getSource();

            if (wi.studentListEnabled()) { // StudentList gesperrt, damit Lectionfield-Daten nicht überschrieben werden kann

                if (s.isFieldSelected()) {
                    /* Datatransfer Schülerdaten von StudentField an Schedule bzw. LectionField */
                    wi.getTemporaryLectionField().transferStudentDataFrom(s);
                    wi.setLectionLength(wi.getTemporaryLectionField().getLectionType());
                    wi.setDragEnabled(!wi.noFieldSelected()); // dragEnabled = !noFieldSelected: sobald mind. ein StudentField selected, kann LectionField gedragt werden
                    /* setzt in allen DayColumns die StudentDays des angeklickten StudentFields, 
                     damit beim Lectionpanel die ValidTimes farbig angezeigt werden */
                    s.setStudentDays();
                    /*  markiert die verfügbare Zeit in der TimeColumn des in StudentField angeklickten Tages */
                    markValidTime(s.getStudentDay());
                    initTimeColumn(true);
                } else {
                    wi.setDragEnabled(!wi.noFieldSelected()); // dragEnabled = !noFieldSelected: falls kein StudentField selected, dragEnabled = false = keine Panel-Aktionen
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
                if (!l.isSelected() && wi.dragEnabled()) {

                    addLectionPanel(start, Colors.DARK_GREEN, Color.BLACK, true, true, l.getLectionType()); // true = lectionSelected, true = dataTransferEnabled
                    wi.setDragEnabled(!spaceAvailable); // falls Lection gesetzt, dragen deaktivieren. Falls kein Space, muss Schedule aktiv bleiben für Dragen
                    if (spaceAvailable) {  // beim Klick auf leeren Space (da zuwenig Platz zum Setzen) darf TimeColumn nicht gecleaned werden
                        l.cleanTimeColumns();
                    }
                    /* falls gesetzt, LectionPanel wieder freimachen (nur erstes Feld von Lectionpanel ist ansprechbar), nur ansprechbar wenn nicht im Drag-Modus */
                } else if (l.isSelected() && l.getFieldPosition() == 0 && !wi.dragEnabled()) {

                    l.setStudentDays();
                    l.markTimeColumns();
                    spaceAvailable = true;
                    addLectionPanel(start, Colors.LIGHT_GREEN, Color.WHITE, false, true, l.getLectionType()); // false = lection deselected = wieder verschiebbar
                    wi.setDragEnabled(true);

                    /* falls gesetzt, Panel neu zeichnen, nur letztes Feld ansprechbar und wenn nicht im Drag-Modus */
                } else if (m.getClickCount() == 2 && l.getFieldPosition() == l.getLectionType() - 1 && !wi.dragEnabled()) {

                    l.setStudentDays();
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
                } else if (l.isSelected() && (l.getFieldPosition() == 3 || l.getFieldPosition() == 4) && !wi.dragEnabled()) {
                    if (m.getClickCount() == 2) {
                        spaceAvailable = true;  // schalter reset
                        cleanLectionPanel(l.getLectionID());
                        initTimeColumn(false);
                        wi.setDragEnabled(false); // Dragen sperren, ausser es werden bereits gesetzte Panels angeklickt
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

            if (!l.isSelected() && start >= 0 && start < totalNumberOfFields && wi.dragEnabled()) {
                dragLectionPanel(start);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent m) {

        LectionField l;
        int start;
        int lectionLength = wi.getLectionLength();

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
