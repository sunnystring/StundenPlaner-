/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class TimeTable_Only extends JPanel {

    private JPanel headerRow;
    private JLabel header;
    private JTable timeTable;

    public TimeTable_Only() {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 0));
        headerRow = new JPanel(new GridLayout(1, 3, 2, 2));

        timeTable = new JTable(50, 12);
        timeTable.getColumnModel().setColumnSelectionAllowed(true); //  in alle Zellen kann geschrieben werden
        timeTable.setFillsViewportHeight(true);
        timeTable.setShowGrid(true);
        timeTable.setSelectionBackground(Colors.LIGHT_GREEN);
     //   timeTable.setShowHorizontalLines(false);

        for (int i = 0; i < 3; i++) {
            header = new JLabel("    ...Tag....");
            header.setPreferredSize(new Dimension(0, 25));
            header.setBackground(Colors.DAY_FIELD);
            header.setOpaque(true);
            headerRow.add(header);
        }

        for (int i = 0; i < 12; i++) {

            if (i % 2 == 0) {
                timeTable.getColumnModel().getColumn(i).setPreferredWidth(10);
            } else {
                timeTable.getColumnModel().getColumn(i).setPreferredWidth(190);
            }
        }
        add(BorderLayout.PAGE_START, headerRow);
        add(BorderLayout.CENTER, timeTable);

    }

}
