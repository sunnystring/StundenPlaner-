/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import mainframe.MainFrame;

/**
 *
 * Statische Getter für Images und Icons
 */
public class Icons {

    private static BufferedImage bufImg;

    public static BufferedImage getImage(String name) {
        try {
            bufImg = ImageIO.read(MainFrame.class.getClassLoader().getResourceAsStream("images/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufImg;
    }

    public static ImageIcon getIcon(String name) {
        Image img = getImage(name);
        return new ImageIcon(img);
    }
}
