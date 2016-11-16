/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import utils.GUIConstants;
import  core.ProfileTypes;
import core.Database;
import core.Profile;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import mainframe.MainFrame;

/**
 *
 * @author mathiaskielholz
 */
public abstract class KGUInputMask extends JDialog {

    private Database database;
    private ArrayList<Profile> KGUStudentDataList;
    private JPanel selectionField, buttonField;
    private JLabel textlabel;
    protected JRadioButton studentSelection;
    protected JButton cancelButton, approveButton;

    public KGUInputMask(MainFrame mainFrame, Profile group) {
        this.database = mainFrame.getDatabase();
        KGUStudentDataList = new ArrayList<>();
        getKGUStudents();
        setModal(true);
        setLocation((int) (mainFrame.getSize().getWidth() / 2), 200);
        setResizable(false);
        setMinimumSize(GUIConstants.KGU_DIMENSION);
        setLayout(new BorderLayout());
        createWidgets();
        addWidgets();
        addCancelButtonListener();
        addApproveButtonListener();
        pack();

    }

    private void getKGUStudents() {
        ArrayList<Profile> studentDataList = database.getStudentDataList();
        for (Profile profile : studentDataList) {
            if (profile.getProfileType()== ProfileTypes.KGU_MEMBER) {
                KGUStudentDataList.add(profile);
            }
        }
    }

    private void createWidgets() {
        textlabel = new JLabel("SchülerInnen auswählen:");
        textlabel.setHorizontalAlignment(SwingConstants.LEADING);
        textlabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        selectionField = new JPanel();
        selectionField.setLayout(new BoxLayout(selectionField, BoxLayout.PAGE_AXIS));
        selectionField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonField = new JPanel();
        buttonField.setLayout(new BoxLayout(buttonField, BoxLayout.LINE_AXIS));
        buttonField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cancelButton = new JButton("Abbrechen");
        approveButton = new JButton("Profil speichern");
    }

    private void addWidgets() {
        selectionField.add(textlabel);
        for (Profile profile : KGUStudentDataList) {
            selectionField.add(new JRadioButton(profile.getFirstName() +" "+ profile.getName()));
        }
//        for (int i = 0; i < 6; i++) {
//            selectionField.add(new JRadioButton("schüler"));
//        }
        buttonField.add(Box.createHorizontalGlue());
        buttonField.add(cancelButton);
        buttonField.add(approveButton);
        //  add(BorderLayout.PAGE_START, textlabel);
        add(BorderLayout.CENTER, selectionField);
        //   add(Box.createVerticalGlue());
        add(BorderLayout.PAGE_END, buttonField);
    }

    private void addCancelButtonListener() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void addApproveButtonListener() {
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //...
            }
        });
    }
}
