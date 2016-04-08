/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.swing.JOptionPane;

/**
 *
 * @author mathiaskielholz
 */
public class Dialogs {

    public static void showScheduleTimeSlotError() {
        JOptionPane.showMessageDialog(null, "Ungültige Zeiteingabe:\n"
                + "Beginn und Schluss passen nicht zusammen\n"
                + "oder die Zeit wurde nicht im Format hh.m0 eingeben!", "Stundenplan", JOptionPane.ERROR_MESSAGE);
    }

    public static void showStudentTimeSlotError() {
        JOptionPane.showMessageDialog(null, "Ungültige Zeiteingabe:\n"
                + "Anfangszeit leer oder grösser als Schlusszeit", "Schülerliste", JOptionPane.ERROR_MESSAGE);
    }

    public static void showNoInputError() {
        JOptionPane.showMessageDialog(null, "Keine Zeiteingabe gemacht!", "Stundenplan", JOptionPane.ERROR_MESSAGE);
    }

    public static void showLectionFormatError() {
        JOptionPane.showMessageDialog(null, "Lektionslänge oder Zeitformat ungültig:\n"
                + "Nur 30 oder 40 Minuten möglich!\n", "Schülerliste", JOptionPane.ERROR_MESSAGE);
    }

    public static void showTimeInputFormatError() {
        JOptionPane.showMessageDialog(null, "Ungültiges Zeitformat:\n"
                + "Zeit zwischen 10.00 und 23.55 eingeben\n"
                + "oder Feld löschen!", "Zeiteingabe", JOptionPane.ERROR_MESSAGE);
    }

    public static int showDayEraseOptionMessage(String days) {
        String msg = "Der" + days + "hat ev. noch Zeiteinträge in der Schülerliste.\n"
                + "Soll der" + days + "gelöscht werden?";
        Object[] options = {"Löschen", "Abbrechen"}; // Löschen = 0 = YES_OPTION, Abbrechen = 1 = NO_OPTION;
        return JOptionPane.showOptionDialog(null, msg, "Stundenplan",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }

    public static void showLectionEraseErrorMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Stundenplan", JOptionPane.ERROR_MESSAGE);
    }

    public static int showStudentTimesOutOfBoundOptionMessage(String days) {
        String msg = "Die Zeiteingabe am" + days + "\nliegt ausserhalb des Stundenplans!\n";
        Object[] options = {"Stundenplan anpassen", "Schülerzeit anpassen"};
        return JOptionPane.showOptionDialog(null, msg, "Schülerliste",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }

    public static void showScheduleOutOfBoundErrorMessage(String errorLog) {
        String msg = "Der eingebene Zeitrahmen ist in\n"
                + "Konflikt mit den Schülerzeiten!\n\n" + errorLog;
        JOptionPane.showMessageDialog(null, msg, "Stundenplan", JOptionPane.ERROR_MESSAGE);
    }
}
