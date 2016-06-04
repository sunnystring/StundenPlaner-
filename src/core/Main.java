/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import mainframe.MainFrame;
import utils.Dialogs;

/**
 *
 * @author Mathias
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception e) {
                    Dialogs.showUnspecificErrorMessage();
                    e.printStackTrace();
                }
                JFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }
}
