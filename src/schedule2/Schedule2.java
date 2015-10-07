/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import schedule.DayColumn;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class Schedule2 extends JPanel implements ListSelectionListener {

    private JPanel dayColumn;
    private JLabel header;
    private TimeTable timeTable;

    private ArrayList<JPanel> dayColumnList;

    public Schedule2() {

        dayColumnList = new ArrayList<>();
        setLayout(new GridLayout(1, 3, 2, 2));

        createDayColumns();
        addDayColumns();

    }

    private void createDayColumns() {

        for (int i = 0; i < 3; i++) {

            dayColumn = new JPanel(new BorderLayout(1, 1));
            dayColumn.setPreferredSize(new Dimension(200, 0));

            header = new JLabel(".....Tag.....");
            header.setBackground(Colors.DAY_FIELD);
            header.setOpaque(true);
            header.setPreferredSize(new Dimension(0, 25));

            timeTable = new TimeTable();
       
            dayColumn.add(header);
            dayColumn.add(BorderLayout.NORTH, header);
            dayColumn.add(BorderLayout.CENTER, timeTable);

            dayColumnList.add(dayColumn);

        }

    }

    private void addDayColumns() {

        for (JPanel p : dayColumnList) {
            add(p);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();  // ToDo
    }

}
