/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import java.awt.GridLayout;
import javax.swing.JPanel;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class LectionColumn extends JPanel {

    private final LectionField[] lectionList; // Referenz auf lectionFieldList in DayColumn
    private final TimeField[] timeList;

    private int totalNumberOfFields;

    public LectionColumn(DayColumn dayColumn) {

        this.lectionList = dayColumn.getLectionFieldList();
        this.timeList = dayColumn.getTimeFieldList();
        totalNumberOfFields = dayColumn.getTotalNumberOfFields();

        setLayout(new GridLayout(0, 1));
        setBackground(Colors.BACKGROUND);
       
    }

    public void fillLectionColumn(int listCount) {  // LectionColumn bauen

        for (int i = 0; i < totalNumberOfFields / 2; i++) {

            if (listCount >= 0 && listCount < totalNumberOfFields) {

                lectionList[listCount].addMouseListener(timeList[listCount]);  // steuert TimeFields (implementiert in TimeField)
            }
            add(lectionList[listCount]);
            listCount++;
        }
    }
}
