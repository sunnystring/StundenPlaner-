/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;



import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import mainframe.MainFrame;




/**
 *
 * @author Mathias
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                // laf version 1:
                
//                try {
//                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //  "javax.swing.plaf.nimbus.NimbusLookAndFeel"
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                
                // laf version 2:
                
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                new MainFrame().setVisible(true);
            }

        });
    }
}