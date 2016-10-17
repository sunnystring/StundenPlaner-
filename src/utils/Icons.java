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
 * @author Mathias
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
        //  img = img.getScaledInstance(25, 25, Image.SCALE_DEFAULT);
//        if (name.equals("disk.png")) {
//            img = img.getScaledInstance(22, 22, Image.SCALE_SMOOTH);
//        }
        return new ImageIcon(img);
    }
}
