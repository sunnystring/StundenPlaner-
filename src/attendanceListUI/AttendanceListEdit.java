/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceListUI;

import attendanceListData.AttendanceListData;
import com.toedter.calendar.JCalendar;
import core.Database;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import mainframe.MainFrame;
import utils.DateUtil;

/**
 *
 * Dialogfenster für das Erstellen/Löschen der Unterrichtswochen in {@link AttendanceListUI}
 */
public class AttendanceListEdit extends JDialog {

    private MainFrame mainFrame;
    private Database database;
    private AttendanceListData attendanceListData;
    private AttendanceListUI attendanceListUI;
    private JPanel center, bottom;
    private JButton cancelButton, deleteButton, createButton;
    private JCalendar dateChooser;
    private String selectedDateString;
    private int selectedDateIndex;

    public AttendanceListEdit(MainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        database = mainFrame.getDatabase();
        attendanceListData = mainFrame.getAttendanceListData();
        attendanceListUI = mainFrame.getAttendanceListUI();
        selectedDateString = DateUtil.getTodayString();
        setTitle("Datum Unterrichtswoche auswählen");
        setModal(true);
        setResizable(false);
        setMinimumSize(new Dimension(450, 400));
        createWidgets();
        addWidgets();
        addButtonListeners();
        pack();
    }

    private void createWidgets() {
        dateChooser = new JCalendar();
        initDateChooser();
        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));
        bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        cancelButton = new JButton("Abbrechen");
        deleteButton = new JButton("Woche löschen");
        createButton = new JButton("Woche erstellen");
        setButtonsEnabledState();
    }

    private void initDateChooser() {
        dateChooser.setFont(this.getFont().deriveFont(Font.BOLD, 10));
        dateChooser.setLocale(Locale.GERMAN);
        dateChooser.setWeekOfYearVisible(false);
        dateChooser.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals("calendar")) {
                    selectedDateString = DateUtil.getDateStringOf(dateChooser.getDate());
                    setButtonsEnabledState();
                }
            }
        });
    }

    private void setButtonsEnabledState() {
        boolean isDuplicate = false;
        boolean isOutOfChronology = false;
        for (String weekNames : database.getWeekNames()) {
            isDuplicate = selectedDateString.equals(weekNames);
            if (isDuplicate) {
                selectedDateIndex = attendanceListData.getWeekIndex(selectedDateString);
                break;
            }
            isOutOfChronology = DateUtil.isBefore(selectedDateString, weekNames);
            if (isOutOfChronology) {
                break;
            }
        }
        deleteButton.setEnabled(isDuplicate && !isOutOfChronology);
        createButton.setEnabled(!isDuplicate && !isOutOfChronology);
    }

    private void addWidgets() {
        center.add(dateChooser);
        bottom.add(cancelButton);
        bottom.add(Box.createHorizontalGlue());
        bottom.add(deleteButton);
        bottom.add(createButton);
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, center);
        add(BorderLayout.PAGE_END, bottom);
    }

    private void addButtonListeners() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.removeWeek(selectedDateIndex);
                updateAttendanceListUI();
                mainFrame.saveCurrentEntriesToFile();
                dispose();
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.addWeek(selectedDateString);
                updateAttendanceListUI();
                mainFrame.saveCurrentEntriesToFile();
                dispose();
            }
        });
    }

    private void updateAttendanceListUI() {
        attendanceListData.update();
        attendanceListUI.update();
        attendanceListData.fireTableDataChanged();
    }

    public void setLocation() {
        super.setLocation(getXCoordinate(), 200);
    }

    private int getXCoordinate() {
        return (int) (mainFrame.getSize().getWidth() / 2) - (int) (this.getSize().getWidth() / 2);
    }
}
