/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Color;

/**
 *
 * @author Mathias
 */
public class Colors {

    public static final Color LIGHT_GRAY = new Color(240, 240, 240),
            DARK_GREEN = new Color(0, 204, 0),
            LIGHT_GREEN = new Color(51, 255, 51),
            DAY_FIELD = new Color(0, 153, 76),
            DAY_FIELD_SELECTED = new Color(0, 102, 52),
            NAME_FIELD = new Color(204, 150, 0),
            NAME_FIELD_SELECTED = new Color(153, 100, 0),
            BLUE_4 = new Color(0, 102, 204),
            BLUE_3 = new Color(0, 128, 255),
            BLUE_2 = new Color(102, 178, 255),
            BLUE_DEFAULT = new Color(153, 204, 255),
            BLUE_0 = new Color(204, 229, 255),
            FAVORITE = Color.BLUE,
            UNVALID = Color.ORANGE,
            BACKGROUND = Color.WHITE,
            PURPLE_1 = new Color(153, 153, 255),
            PURPLE_DEFAULT = new Color(204, 204, 255),
            PURPLE_2 = new Color(102, 102, 255),
            PURPLE_3 = new Color(0, 0, 255),
            PURPLE_4 = new Color(0, 0, 204),
            RED_1 = new Color(255, 180, 180),
            RED_DEFAULT = new Color(255, 210, 210),
            RED_2 = new Color(240, 150, 140),
            RED_3 = new Color(230, 120, 110),
            RED_4 = new Color(210, 0, 0),
            TIMEFIELD_HOUR = new Color(255, 229, 204);
    private static final Color[] BLUE_TYPES = {BLUE_0, BLUE_DEFAULT, BLUE_2, BLUE_3, BLUE_4};
    private static final Color[] RED_TYPES = {RED_1, RED_DEFAULT, RED_2, RED_3, RED_4};
    private static final Color[] PURPLE_TYPES = {PURPLE_DEFAULT, PURPLE_1, PURPLE_2, PURPLE_3, PURPLE_4};

    public static Color getBlue(int i) {
        return BLUE_TYPES[i];
    }

    public static Color getRed(int i) {
        return RED_TYPES[i];
    }

    public static Color getPurple(int i) {
        return PURPLE_TYPES[i];
    }
}
