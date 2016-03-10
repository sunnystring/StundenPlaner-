/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI;

import core.Database;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import core.ScheduleTimes;
import exceptions.DayEraseException;
import exceptions.IllegalLectionEraseException;
import exceptions.IllegalTimeSlotException;
import exceptions.NoEntryException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import mainframe.MainFrame;
import util.Dialogs;

/**
 *
 * UI, das von {@link ScheduleEntry} und {@link ScheduleEdit} benutzt wird
 */
public class ScheduleInputMask extends JPanel {

    private final MainFrame mainFrame;
    private final ScheduleTimes scheduleTimes;
    private JScrollPane center;
    private JPanel bottom;
    private SelectionTable selectionTable;
    private JButton cancelButton, saveButton;
    private ActionListener cancelButtonListener, saveButtonListener, editButtonListener;

    public ScheduleInputMask(Database database, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        scheduleTimes = database.getScheduleTimes();
        setLayout(new BorderLayout());
        createWidgets();
        addWidgets();
    }

    private void createWidgets() {
        selectionTable = new SelectionTable(scheduleTimes);
        selectionTable.setParameters();
        center = new JScrollPane(selectionTable, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cancelButton = new JButton("Abbrechen");
        saveButton = new JButton("Speichern");
    }

    private void addWidgets() {
        bottom.add(Box.createHorizontalGlue());
        bottom.add(cancelButton);
        bottom.add(saveButton);
        add(BorderLayout.CENTER, center);
        add(BorderLayout.PAGE_END, bottom);
    }

    public void addCancelButtonListener(DataEntryAndEdit dataEntryAndEdit) {  // ev zusammenfassen mit DataEntryAndEdit
        cancelButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataEntryAndEdit.dispose();
                removeButtonListeners();
            }
        };
        cancelButton.addActionListener(cancelButtonListener);
    }

    public void addSaveButtonListener(ScheduleEntry scheduleEntry) {
        saveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    scheduleTimes.checkTimeSlots();
                } catch (NoEntryException ex) {
                    Dialogs.showNoInputError();
                    return;
                } catch (IllegalTimeSlotException ex) {
                    Dialogs.showScheduleTimeSlotError();
                    return;
                }
                mainFrame.setupAndShowUI();
                scheduleEntry.dispose();
                removeButtonListeners();
            }
        };
        saveButton.addActionListener(saveButtonListener);
    }

    public void addEditSaveButtonListener(ScheduleEdit scheduleEdit) {
        editButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    scheduleTimes.checkTimeSlots();
                    mainFrame.validateNewEntry();
                } catch (NoEntryException ex) {
                    Dialogs.showNoInputError();
                    return;
                } catch (IllegalTimeSlotException ex) {
                    Dialogs.showScheduleTimeSlotError();
                    return;
                } catch (IllegalLectionEraseException ex) {
                    Dialogs.showLectionEraseErrorMessage(ex.getMessage());
                    scheduleTimes.returnToExistingSelection();
                    scheduleEdit.dispose();
                    return;
                } catch (DayEraseException ex) {
                    int choice = Dialogs.showDayEraseOptionMessage(ex.getMessage());
                    if (choice == JOptionPane.NO_OPTION) { // = Abbrechen
                        scheduleTimes.returnToExistingSelection();
                        return;
                    }
                }
                mainFrame.updateAndShowUI();
                scheduleEdit.dispose();
                removeButtonListeners();
            }
        };
        saveButton.addActionListener(editButtonListener);
    }

    private void removeButtonListeners() {
        cancelButton.removeActionListener(cancelButtonListener);
        saveButton.removeActionListener(saveButtonListener);
        saveButton.removeActionListener(editButtonListener);
    }
}
