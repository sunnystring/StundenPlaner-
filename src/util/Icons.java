/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

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
    //  private static ImageIcon icon;
    // private static Image img;

    public static BufferedImage getImage(String name) {
        try {
            bufImg = ImageIO.read(MainFrame.class.getClassLoader().getResourceAsStream("images/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufImg;
    }

    public static ImageIcon setIcon(String name) {
        Image img = getImage(name);
        img = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        
        if (name.equals("disk.png")) {
            img = img.getScaledInstance(25, 23, Image.SCALE_SMOOTH);
        }
        if (name.equals("color.png")) {
            img = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        }
        if (name.equals("list.png")) {
            img = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        }
        if (name.equals("boy.png")) {
            img = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        }
        return new ImageIcon(img);
    }
}
