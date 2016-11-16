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
                + "oder die Schlusszeit wurde im Format hh.m5 eingeben!", "Stundenplan", JOptionPane.ERROR_MESSAGE);
    }

    public static void showStudentTimeSlotError() {
        JOptionPane.showMessageDialog(null, "Ungültige Zeiteingabe:\n"
                + "Anfangszeit leer oder grösser als Schlusszeit", "Schülerliste", JOptionPane.ERROR_MESSAGE);
    }

    public static void showNoInputError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Ungültige Zeiten", JOptionPane.ERROR_MESSAGE);
    }

    public static void showTimeInputFormatError() {
        JOptionPane.showMessageDialog(null, "Ungültiges Zeitformat:\n"
                + "Zeit zwischen 7 und 23.50 (im Format hh.m0 eingeben)\n"
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

    public static int showStudentTimesOutOfBoundOptionMessage(String errorLog) {
        String msg = "Die Zeiteingabe liegt ausserhalb des Stundenplans!\n\nEinteilbare Zeit am\n" + errorLog;
        Object[] options = {"Stundenplan anpassen", "Schülerzeit anpassen"};
        return JOptionPane.showOptionDialog(null, msg, "Schülerdaten",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }

    public static void showScheduleOutOfBoundErrorMessage(String errorLog) {
        String msg = "Der eingebene Zeitrahmen ist in\n"
                + "Konflikt mit den Schülerzeiten!\n\n" + errorLog;
        JOptionPane.showMessageDialog(null, msg, "Stundenplan", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSaveFileErrorMessage() {
        String msg = "Fehler beim Speichern der Datei!\nBitte versuche es nochmals.";
        JOptionPane.showMessageDialog(null, msg, "Datei speichern", JOptionPane.ERROR_MESSAGE);
    }

    public static void showLoadFileErrorMessage() {
        String msg = "Fehler beim Laden der Datei!\nBitte versuche es nochmals.";
        JOptionPane.showMessageDialog(null, msg, "Datei laden", JOptionPane.ERROR_MESSAGE);
    }

    public static void showUnspecificErrorMessage(String msg) {
        String text = "Es ist ein unbekannter Fehler aufgetreten!\n"
                + msg + "\n" + "Vorgang bitte wiederholen, Programm neu laden oder neu starten.";
        JOptionPane.showMessageDialog(null, text, "???", JOptionPane.ERROR_MESSAGE);
    }

    public static void showIllegalKGUEntryErrorMessage() {
        String msg = "Es muss eine Gruppe ausgewählt oder eine Auswahl\nvon 2 oder 3 Mitgliedern gemacht werden!";
        JOptionPane.showMessageDialog(null, msg, "Unzulässige Auswahl", JOptionPane.ERROR_MESSAGE);
    }

    public static int showSaveOptionMessage() {
        String msg = "Änderungen der Schülerdaten und des Stundenplans speichern?";
        Object[] options = {"Speichern", "Nicht speichern"}; // Löschen = 0 = YES_OPTION, Abbrechen = 1 = NO_OPTION;
        return JOptionPane.showOptionDialog(null, msg, "StundenPlaner beenden",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }
}
