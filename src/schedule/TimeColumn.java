/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import util.Colors;
import util.Time;

/**
 *
 * @author Mathias
 */
public class TimeColumn extends JPanel {

    private final TimeField[] timeList; // Referenz auf timeFieldList in DayColumn
    private int validTimeStart;
    private int validTimeEnd;
    private int totalNumberOfFields;

    public TimeColumn(DayColumn dayColumn) {

        this.timeList = dayColumn.getTimeFieldList();
        validTimeStart = dayColumn.getScheduleStart();
        validTimeEnd = dayColumn.getScheduleEnd();
        totalNumberOfFields = dayColumn.getTotalNumberOfFields();

        setBackground(Colors.BACKGROUND);
        setLayout(new GridLayout(0, 1));
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 1));

    }

    public void fillTimeColumn(int listCount, Time startTime) {  // TimeColumn bauen 

        /* listCount = alle Zeitfelder eines Tages, 2. Time- bzw. LectionColumn beginnt bei listCount = Breakpoint */
        for (int i = 0; i < totalNumberOfFields / 2; i++) {

            timeList[listCount].setForeground(Color.LIGHT_GRAY);

            if (startTime.getMinute() != 0) {
                timeList[listCount].setText(startTime.getMinute());
            } else {
                timeList[listCount].setText(startTime.getHour());
                timeList[listCount].setBackground(Colors.TIMEFIELD_HOUR);
                timeList[listCount].setHourMarkSelected();
            }
            // gültiges Intervall = einteilbare Zeit
            if (listCount >= validTimeStart && listCount <= validTimeEnd) {
                timeList[listCount].setForeground(Color.BLACK);
            }
            timeList[listCount].setTime(startTime);  // jedes TimeField muss seine Zeit wissen
            add(timeList[listCount]);
            startTime.inc();
            listCount++;
        }
    }
}
