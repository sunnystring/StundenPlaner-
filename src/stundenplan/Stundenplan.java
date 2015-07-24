/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stundenplan;

import core.DataBase;
import core.DatabaseListener;
import core.StundenplanDay;
import core.Schueler;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import util.Colors;

/**
 *
 * @author Mathias
 */
public final class Stundenplan extends JComponent implements DatabaseListener {

    private static ArrayList<DayColumn> dayColumnList;

    public Stundenplan() {

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBackground(Colors.BACKGROUND);
        DataBase.addDatabaseListener(this);  // liefert Referenz von Stundenplan an Database

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

    /* Stundenplan zeichnen // ToDo: wenn dynamische Eingabe -> auslösen erst, wenn "ok"- Button gedrückt*/
    public void createStundenplan() {

        for (DayColumn dayColumn : dayColumnList) {

            dayColumn.initTimeFrame();
            dayColumn.createDayColumn();  // leere DayColumn befüllen
//            dayColumn.setDayID(dayColumnList.indexOf(dayColumn));  // ID = Position in DayColumnList
//           System.out.println(dayColumnList.indexOf(dayColumn));
            add(dayColumn);    // Stundenplan zeichnen
        }
    }

    /*  DatabaseListener Implementation  */
    @Override
    public void dayAdded(StundenplanDay d) {

        dayColumnList.add(new DayColumn(d));  // "leere" Daycolumn in Liste speichern, Daycolumns initialisieren
    }

    @Override
    public void dayRemoved(StundenplanDay d) {
    }

    @Override
    public void dayEdited(StundenplanDay d) {
    }

    @Override
    public void schuelerAdded(Schueler s) {
    }

    @Override
    public void schuelerRemoved(Schueler s) {
    }

    @Override
    public void schuelerEdited(Schueler s) {
    }

}
