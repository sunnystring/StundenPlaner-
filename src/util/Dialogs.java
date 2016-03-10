/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import core.ScheduleTimes;
import javax.swing.JOptionPane;

/**
 *
 * @author mathiaskielholz
 */
public class Dialogs {

    public static void showScheduleTimeSlotError() {
        JOptionPane.showMessageDialog(null, "Ungültige Zeiteingabe:\n"
                + "Beginn und Schluss passen nicht zusammen!", "Stundenplan", JOptionPane.ERROR_MESSAGE);
    }

    public static void showStudentTimeSlotError() {
        JOptionPane.showMessageDialog(null, "Ungültige Zeiteingabe:\n"
                + "Anfangszeit leer oder grösser als Schlusszeit", "Schülerzeiten", JOptionPane.ERROR_MESSAGE);
    }

    public static void showNoInputError() {
        JOptionPane.showMessageDialog(null, "Keine Zeiteingabe gemacht!", "Stundenplan", JOptionPane.ERROR_MESSAGE);
    }

    public static void showLectionFormatError() {
        JOptionPane.showMessageDialog(null, "Lektionslänge oder Zeitformat ungültig:\n"
                + "Nur 30 oder 40 Minuten möglich!\n", "Zeiteingabe", JOptionPane.ERROR_MESSAGE);
    }

    public static void showTimeInputFormatError() {
        JOptionPane.showMessageDialog(null, "Ungültiges Zeitformat:\n"
                + "Zeit zwischen 10.00 und 23.55 eingeben\n"
                + "oder Feld löschen!", "Zeiteingabe", JOptionPane.ERROR_MESSAGE);
    }

    public static int showDayEraseOptionMessage(String day) {
        String msg = "Der" + day + "hat ev. noch Zeiteinträge in der Schülerliste.\n"
                + "Soll der" + day + "gelöscht werden?";
        Object[] options = {"Löschen", "Abbrechen"}; // Löschen = 0 = YES_OPTION, Abbrechen = 1 = NO_OPTION;
        return JOptionPane.showOptionDialog(null, msg, "Stundenplan",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }

    public static void showLectionEraseErrorMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Stundenplan", JOptionPane.ERROR_MESSAGE);
    }

    public static int showScheduleOutOfBoundErrorMessage(String day) {
        String msg = "Die eingebene Schülerzeit am" + day + "liegt ausserhalb des Stundenplans.\n"
                + "Der Stundenplan oder die Schülerzeit muss angepasst werden.";
        Object[] options = {"Stundenplan anpassen", "Schülerzeit anpassen"};
        return JOptionPane.showOptionDialog(null, msg, "Zeiteingabe",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }

}
