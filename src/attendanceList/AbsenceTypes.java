/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceList;

import java.awt.Color;
import utils.Colors;
import static utils.Colors.*;

/**
 *
 * @author mathiaskielholz
 */
public class AbsenceTypes {

    public static final int VALID_LESSON = 0,
            STUDENT_REPORTED = 1,
            STUDENT_UNREPORTED = 2,
            TEACHER = 3,
            OFFICIAL = 4,
            EMPTY_LESSON = 5;

    public static Color getColorOf(int absenceType) {
        switch (absenceType) {
            case 0:
                return DARK_GREEN;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.RED;
            case 3:
                return Color.BLACK;
            case 4:
                return Color.MAGENTA;
            default:
                return Colors.BACKGROUND_COLOR;
        }
    }

    public static String getCharacterOf(int absenceType) {
        switch (absenceType) {
            case 0:
                return "A";
            case 1:
                return "E";
            case 2:
                return "U";
            case 3:
                return "L";
            case 4:
                return "F";
            default:
                return "";
        }
    }
}
