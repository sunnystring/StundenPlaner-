/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schuelerliste;

import core.DataBase;
import core.Schueler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import stundenplan.DayColumn;
import stundenplan.Stundenplan;

import util.Colors;

/**
 *
 * @author Mathias
 */
public class SchuelerRow extends JPanel implements MouseListener {

    private final SchuelerField[] schuelerRow;     // Field-Arrays pro SchuelerRow
    private final VariableField[] emptyRow;  // Field-Arrays für restliche Rows
    private final VariableField[] headerRow;

    private int schuelerIndex;  // Position in der SchülerListe

    // Schalter für GUI-Management
    private static boolean schuelerlisteEnabled;
    private static boolean noRowSelected;
    private static boolean noFieldSelected;
    private boolean schuelerRowEnabled;

    private static Object selectedNameField;  // temporäres Vergleichsobjekt beim Scrollen der NameFields

    private static int columns = DataBase.getNumberOfDays() + 1; // Anzahl Tage plus nameField

    public SchuelerRow() {

        headerRow = new VariableField[columns];
        schuelerRow = new SchuelerField[columns];
        emptyRow = new VariableField[columns];
        setBackground(Colors.BACKGROUND);

        /* Schalter */
        schuelerlisteEnabled = true;  // Schülerliste als ganzes ansprechbar, verhindert Eingaben in Schülerliste im Lectionfield-Dragmodus 
        noRowSelected = true;  // jede Row ist scrollbar
        noFieldSelected = true;  // falls mind. ein Feld selektiert

        schuelerRowEnabled = true;  // selektierte Row = eingeteilter Schueler ist nicht mehr ansprechbar

        setLayout(new GridLayout(1, columns, 1, 0));

    }

    /* HeaderRow mit Daten befüllen und zeichnen  */
    public void createHeaderRow() {

        headerRow[0] = new VariableField("Vorname Name", Colors.NAME_FIELD_SELECTED);
        headerRow[0].setFont(this.getFont().deriveFont(Font.BOLD, 10));
        headerRow[0].setForeground(Color.WHITE);
        add(headerRow[0]);

        for (int i = 1; i < columns; i++) {
            headerRow[i] = new DayField(DataBase.getDayDataList(i - 1).getDayName(), Colors.DAY_FIELD);
            headerRow[i].setFont(this.getFont().deriveFont(Font.BOLD, 10));
            headerRow[i].addMouseListener(new DayFieldListener());
            add(headerRow[i]);
        }
    }

    /* SchülerFields mit Schueler-Daten befüllen und initialisieren */
    public void createSchuelerRow(Schueler s) {

        schuelerRow[0] = new NameField(s.getVorname(), s.getName());
        schuelerRow[0].addMouseListener(new NameFieldListener());
        schuelerRow[0].setSchuelerIndex(schuelerIndex);  // Schueler-ID wird an NameField weitergeleitet
        add(schuelerRow[0]);

        for (int i = 1; i < columns; i++) {   // Zeitfelder adden -> i = 1 -> 1. Tag usw.

            schuelerRow[i] = new SchuelerField();
            schuelerRow[i].addMouseListener(this);  // Referenz auf SchuelerRow
            schuelerRow[i].addMouseListener(Stundenplan.getDayColumn(i - 1)); // Referenz auf den Tag(1. Tag-> i=0) für DayColumn-Aktionen
            schuelerRow[i].setSchuelerIndex(schuelerIndex); // Schueler-ID wird an jedes SchuelerField weitergeleitet
            schuelerRow[i].setVorname(s.getVorname());
            schuelerRow[i].setName(s.getName());
            schuelerRow[i].setDay(s.getSchuelerDay(i - 1));  // SchuelerField bekommt die gekapselten Time-Daten des entspr. Tages(= SchuelerDay)

            //------------Rohfassung: Referenz auf schülerDayList----------
            schuelerRow[i].setSchuelerDayList(s.getSchuelerDayList());

            for (DayColumn d : Stundenplan.getDayColumnList()) {   // jedes SchülerField bekommt eine Referenz auf alle DayColumns
                schuelerRow[i].addValidTimeListener(d);
            }

            schuelerRow[i].setLectionType(s.getLectionType());
            schuelerRow[i].showAvailableTimes();
            add(schuelerRow[i]);
        }
    }

    public void createEmptyRow() {

        for (int i = 0; i < columns; i++) {   // leere Felder adden
            emptyRow[i] = new VariableField(" ", Colors.BACKGROUND);
            emptyRow[i].setPreferredSize(new Dimension(20, 20));
            add(emptyRow[i]);
        }
    }

    public void cleanSchülerRow(boolean cleanMode) {

        schuelerlisteEnabled = true;

        if (cleanMode) {   // SchuelerRow wird markiert und gesperrt 
            schuelerRow[0].setBackground(Colors.LIGHT_GRAY);
            schuelerRow[0].setForeground(Color.GRAY);
            schuelerRow[0].setFont(this.getFont().deriveFont(Font.PLAIN, 10));

            for (int i = 1; i < columns; i++) {
                schuelerRow[i].setBackground(Colors.LIGHT_GRAY);
                schuelerRow[i].setForeground(Color.GRAY);
                schuelerRow[i].setFieldSelected(false);
            }
            schuelerRowEnabled = !cleanMode;

        } else { // SchuelerRow wird entsperrt 
            schuelerRow[0].setBackground(Colors.NAME_FIELD);
            schuelerRow[0].setForeground(Color.BLACK);
            schuelerRow[0].setFont(this.getFont().deriveFont(Font.PLAIN, 10));

            for (int i = 1; i < columns; i++) {
                schuelerRow[i].setBackground(Colors.SCHUELER_FIELD_BLUE);
                schuelerRow[i].setForeground(Color.BLACK);
                schuelerRow[i].setFieldSelected(false);
            }
            schuelerRowEnabled = !cleanMode;
        }
        noRowSelected = true;
        noFieldSelected = true;
    }

    /* Schalter */
    public static void setSchuelerlisteEnabled(boolean enabled) {
        schuelerlisteEnabled = enabled;
    }

    public static boolean isSchuelerlisteEnabled() {
        return schuelerlisteEnabled;
    }

    /* Getter, Setter usw.  */
    public void setSchuelerIndex(int schuelerRowIndex) {
        this.schuelerIndex = schuelerRowIndex;
    }

    public static boolean noFieldSelected() {
        return noFieldSelected;
    }

    public SchuelerField getSchuelerFieldAt(int index) {
        return schuelerRow[index];
    }

    /* Listener-Implementationen */
    @Override
    public void mouseClicked(MouseEvent m) {

        if (schuelerlisteEnabled) {
            if (noRowSelected && schuelerRowEnabled) { // Row-"Hauptschalter" on = jede Row kann selektiert werden
                if (noFieldSelected) {  // SchuelerField selektierbar, falls kein nameField selektiert 
                    drawRow(m.getSource());
                    noFieldSelected = false;
                } else // Row-"Hauptschalter" off = nur die aktuelle Row ist selektierbar
                if (fieldsStillSelected()) { //  weitere SchülerFields selektieren, es kann keine andere Row selektiert werden, solange Felder in dieser Row selektiert sind 
                    drawRow(m.getSource());
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent m) {

        if (schuelerlisteEnabled) {
            if (noRowSelected && noFieldSelected && schuelerRowEnabled) {   // Feld nicht selektiert
                for (int i = 0; i < columns; i++) {
                    if (i == 0) {
                        schuelerRow[i].setBackground(Colors.NAME_FIELD_SELECTED);
                        schuelerRow[i].setForeground(Color.WHITE);
                        schuelerRow[i].setFont(this.getFont().deriveFont(Font.BOLD, 10));
                    } else {
                        schuelerRow[i].setBackground(Colors.LIGHT_GREEN);
                    }
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent m) {

        if (schuelerlisteEnabled) {
            if (noRowSelected && noFieldSelected && schuelerRowEnabled) {
                for (int i = 0; i < columns; i++) {
                    if (i == 0) {
                        schuelerRow[i].setBackground(Colors.NAME_FIELD);
                        schuelerRow[i].setForeground(Color.BLACK);
                        schuelerRow[i].setFont(this.getFont().deriveFont(Font.PLAIN, 10));
                    } else {
                        schuelerRow[i].setBackground(Colors.SCHUELER_FIELD_BLUE);
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent m) {
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /* Hilfsmethode für MouseListener Implementation */
    private void drawRow(Object source) {

        for (int i = 1; i < columns; i++) {
            if (source.equals(schuelerRow[i])) {
                if (schuelerRow[i].isFieldSelected()) {
                    schuelerRow[i].setBackground(Colors.LIGHT_GREEN);
                    schuelerRow[i].setFieldSelected(false);
                } else {
                    schuelerRow[i].setBackground(Colors.DARK_GREEN);
                    schuelerRow[i].setFieldSelected(true);
                }
            }
        }
        if (!fieldsStillSelected()) {
            noFieldSelected = true;
        }
    }

    /* Hilfsmethode für Listener-Implementation Schalter: wenn mind. ein SchuelerField selektiert ist */
    private boolean fieldsStillSelected() {

        for (int i = 0; i < columns; i++) {
            if (schuelerRow[i].isFieldSelected()) {
                return true;
            }
        }
        return false;
    }
    
    /* innere Listenerklassen  */
    private class NameFieldListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent m) {
            if (schuelerlisteEnabled) {
                if (m.getClickCount() == 2) { //Doppelklick
                    if (noFieldSelected && schuelerRowEnabled) {  // Namefield nur selektierbar, wenn kein Schülerfeld selektiert
                        if (noRowSelected) {     // falls noch kein nameField selekt. -> selektieren
                            selectedNameField = m.getSource();
                            schuelerRow[0].setBackground(Colors.NAME_FIELD_SELECTED);
                            schuelerRow[0].setForeground(Color.WHITE);
                            schuelerRow[0].setFont(schuelerRow[0].getFont().deriveFont(Font.BOLD, 10));
                            noRowSelected = false;

                        } else if (selectedNameField.equals(schuelerRow[0])) { // falls NameField selekt. -> wieder deselektieren
                            schuelerRow[0].setBackground(Colors.NAME_FIELD);
                            schuelerRow[0].setForeground(Color.BLACK);
                            schuelerRow[0].setFont(schuelerRow[0].getFont().deriveFont(Font.PLAIN, 10));
                            noRowSelected = true;
                        }
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent m) {

            if (schuelerlisteEnabled) {
                if (noFieldSelected && noRowSelected && schuelerRowEnabled) {
                    schuelerRow[0].setBackground(Colors.NAME_FIELD_SELECTED);
                    schuelerRow[0].setFont(schuelerRow[0].getFont().deriveFont(Font.BOLD, 10));
                    schuelerRow[0].setForeground(Color.WHITE);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent m) {

            if (schuelerlisteEnabled) {
                if (noFieldSelected && noRowSelected && schuelerRowEnabled) {
                    schuelerRow[0].setBackground(Colors.NAME_FIELD);
                    schuelerRow[0].setFont(schuelerRow[0].getFont().deriveFont(Font.PLAIN, 10));
                    schuelerRow[0].setForeground(Color.BLACK);
                }
            }
        }
    }

    private class DayFieldListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent m) {

            for (int i = 1; i < columns; i++) {
                if (m.getSource().equals(headerRow[i])) {
                    if (!headerRow[i].isSelected()) {
                        headerRow[i].setBackground(Colors.DAY_FIELD_SELECTED);
                        headerRow[i].switchSelectionState();
                    } else {
                        headerRow[i].setBackground(Colors.DAY_FIELD);
                        headerRow[i].switchSelectionState();
                    }
                }
            }
        }
    }
}
