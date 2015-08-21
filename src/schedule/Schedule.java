/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import core.DataBase;
import core.DatabaseListener;
import core.ScheduleDay;
import core.Student;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import util.Colors;

/**
 *
 * @author Mathias
 */
public final class Schedule extends JComponent implements DatabaseListener {

    private static ArrayList<DayColumn> dayColumnList;

    public Schedule() {

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBackground(Colors.BACKGROUND);
        DataBase.addDatabaseListener(this);  // liefert Referenz von Schedule an Database

        dayColumnList = new ArrayList<>();
    }

    /*  liefert Referenz auf eine DayColumn */
    // ToDo -> schöneres Design ??
    public static DayColumn getDayColumn(int index) {
        return dayColumnList.get(index);
    }

    public static ArrayList<DayColumn> getDayColumnList() {
        return dayColumnList;
    }

    /* Schedule zeichnen // ToDo: wenn dynamische Eingabe -> auslösen erst, wenn "ok"- Button gedrückt*/
    public void createSchedule() {

        for (DayColumn dayColumn : dayColumnList) {

            dayColumn.initTimeFrame();
            dayColumn.createDayColumn();  // leere DayColumn befüllen
//            dayColumn.setDayID(dayColumnList.indexOf(dayColumn));  // ID = Position in DayColumnList
//           System.out.println(dayColumnList.indexOf(dayColumn));
            add(dayColumn);    // Schedule zeichnen
        }
    }

    /*  DatabaseListener Implementation  */
    @Override
    public void dayAdded(ScheduleDay d) {

        dayColumnList.add(new DayColumn(d));  // "leere" Daycolumn in Liste speichern, Daycolumns initialisieren
    }

    @Override
    public void dayRemoved(ScheduleDay d) {
    }

    @Override
    public void dayEdited(ScheduleDay d) {
    }

    @Override
    public void studentAdded(Student s) {
    }

    @Override
    public void studentRemoved(Student s) {
    }

    @Override
    public void studentEdited(Student s) {
    }

}
