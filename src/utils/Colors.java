/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Color;

/**
 *
 * Sammlung aller StundenPlaner-Farb-Konstanten
 */
public class Colors {

    public static final Color LIGHT_GRAY_COLOR = new Color(240, 240, 240),
            DARK_GREEN = new Color(0, 204, 0),
            VERY_DARK_GREEN = new Color(0, 140, 0),
            GROUP_COLOR = new Color(150, 204, 0),
            LIGHT_GREEN = new Color(51, 255, 51),
            VERY_LIGHT_GREEN = new Color(204, 255, 204),
            DAYFIELD_COLOR = new Color(0, 153, 76),
            DAYFIELD_SELECTED_COLOR = new Color(0, 102, 52),
            NAMEFIELD_SINGLE_COLOR = new Color(204, 120, 0),
            NAMEFIELD_GROUP_COLOR = new Color(204, 150, 0),
            KGU_MEMBER_COLOR = Color.ORANGE,
            SDG_MEMBER_COLOR = new Color(255, 230, 50),
            SDG_GROUP_COLOR = new Color(220, 220, 0),
            NAMEFIELD_SELECTED_COLOR = new Color(153, 100, 0),
            FAVORITE_COLOR = Color.BLUE,
            UNVALID_COLOR = Color.ORANGE,
            BACKGROUND_COLOR = Color.WHITE,
            BLUE_0 = new Color(204, 229, 255),
            BLUE_DEFAULT = new Color(153, 204, 255),
            BLUE_2 = new Color(80, 153, 255),
            BLUE_3 = new Color(40, 130, 220),
            BLUE_4 = new Color(0, 76, 153),
            PURPLE_DEFAULT = new Color(214, 190, 255),
            PURPLE_1 = new Color(180, 160, 255),
            PURPLE_2 = new Color(150, 90, 255),
            PURPLE_3 = new Color(110, 40, 220),
            PURPLE_4 = new Color(110, 0, 180),
            RED_DEFAULT = new Color(255, 210, 210),
            RED_1 = new Color(255, 180, 180),
            RED_2 = new Color(240, 140, 140),
            RED_3 = new Color(230, 80, 80),
            RED_4 = new Color(210, 0, 0),
            TIMEFIELD_HOUR_COLOR = new Color(255, 229, 204);
    private static final Color[] BLUE_TYPES = {BLUE_0, BLUE_DEFAULT, BLUE_2, BLUE_3, BLUE_4};
    private static final Color[] RED_TYPES = {RED_DEFAULT, RED_1, RED_2, RED_3, RED_4};
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
