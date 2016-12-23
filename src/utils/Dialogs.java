/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author mathiaskielholz
 */
public class Dialogs {

    public static final String NO_INPUT_MESSAGE = "Es kann kein leeres Profil erstellt werden!";

    public static void showScheduleTimeSlotError() {
        JOptionPane.showMessageDialog(null, "Ungültige Zeiteingabe:\n"
                + "Beginn und Schluss passen nicht zusammen\n"
                + "oder die Schlusszeit wurde im Format hh.m5 eingeben!", "Stundenplan", JOptionPane.ERROR_MESSAGE);
    }

    public static void showStudentTimeSlotError() {
        JOptionPane.showMessageDialog(null, "Ungültige Zeiteingabe:\n"
                + "Anfangszeit leer oder grösser als Schlusszeit", "Schülerliste", JOptionPane.ERROR_MESSAGE);
    }

    public static int showNameDuplicateMessage(String name) {
        Object[] options = {"Eingabe löschen", "Profil trotzdem speichern"};
        return JOptionPane.showOptionDialog(null, "Der Name \"" + name + "\" existiert bereits!", "Nameneingabe",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
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

    public static int showStudentTimesOutOfBoundOptionMessage(String msg) {
        msg = "Die Schülerzeiten liegen ausserhalb des Stundenplans!\n\n" + msg;
        Object[] options = {"Stundenplan anpassen", "Schülerzeit anpassen"};
        return JOptionPane.showOptionDialog(null, msg, "Zeiteingabe",
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

    public static int showSaveOptionMessage() {
        String msg = " Änderungen speichern?";
        Object[] options = {"Speichern", "Nicht speichern", "Abbrechen"}; // Löschen = 0 = YES_OPTION, Abbrechen = 1 = NO_OPTION;
        return JOptionPane.showOptionDialog(null, msg, "StundenPlaner beenden",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    public static int showAffirmDeleteAttendanceListMessage() {
        String msg = "Soll wirklich die ganze Unterrichtskontrolle gelöscht werden!\n";
        Object[] options = {"Löschen", "Abbrechen"};
        return JOptionPane.showOptionDialog(null, msg, "Unterrichtskontrolle",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }

    public static int showAffirmDeleteJournalArchiveMessage(Component parent, String name) {
        String msg = "Sollen wirklich alle Journale von\n" + name + " gelöscht werden!\n";
        Object[] options = {"Löschen", "Abbrechen"};
        return JOptionPane.showOptionDialog(parent, msg, "Journal-Archiv",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }
}
