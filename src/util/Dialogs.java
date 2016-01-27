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
                + "Anfangszeit nicht angeben oder grösser als Schlusszeit", "Schülerzeiten", JOptionPane.ERROR_MESSAGE);
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

    public static int showDayEraseOptionMessage(String erasedDay) {
        String msg = "Der" + erasedDay + "hat ev. noch Zeiteinträge in der Schülerliste.\n"
                + "Soll der" + erasedDay + "gelöscht werden?";
        Object[] options = {"Löschen", "Abbrechen"};
        return JOptionPane.showOptionDialog(null, msg, "Stundenplan", // Abbrechen = 1 = NO_OPTION, Löschen = 0 = YES_OPTION;
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }

    public static void showLectionEraseErrorMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Stundenplan", JOptionPane.ERROR_MESSAGE);
    }

}
