/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schuelerliste;

import core.DataBase;
import core.DatabaseListener;
import core.StundenplanDay;
import core.Schueler;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import stundenplan.DayColumn;
import stundenplan.LectionField;
import util.Colors;

/**
 *
 * @author Mathias
 */
public final class SchuelerListe extends JPanel implements MouseListener, DatabaseListener {

    private static SchuelerRow[] schuelerList;  // Liste mit allen SchuelerRow-Instanzen
    private static SchuelerRow header;
    private static SchuelerRow emptyRow;

    private static int rows = DataBase.getSchuelerzahl(); // max. Schülerzahl ohne HeaderRow
    private static int temporaryListSize;

    /* Default-Minimalhöhe  */
    private static final int MAX_ROWS = 40;

    public SchuelerListe() {

        schuelerList = new SchuelerRow[rows];

      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        DataBase.addDatabaseListener(this);
        setBackground(Colors.BACKGROUND);

        /*   leere HeaderRow instanzieren   */
        header = new SchuelerRow();
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));

        /* schülerList initialisieren und mit leeren SchülerRows instanzieren*/
        for (int i = 0; i < rows; i++) {
            schuelerList[i] = new SchuelerRow();
            schuelerList[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
        }
   }

    /* ganze Schülerliste zeichnen // ToDo: wenn dynamische Eingabe -> auslösen erst, wenn "ok"- Button gedrückt*/
    public void createSchuelerliste() {
        
        temporaryListSize = DataBase.getSchuelerIndex();  // bekommt die momentane List-Länge ( = Anzahl Schueler-Einträge)

        header.createHeaderRow();
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
        add(header);

        for (int i = 0; i < temporaryListSize; i++) {

            schuelerList[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
            add(schuelerList[i]);
        }
        
        /* nach dem letzten Schülereintrag mit leeren Reihen auffüllen -> konstante Höhe*/
        for (int i = temporaryListSize; i < MAX_ROWS; i++) {
            emptyRow = new SchuelerRow();
            emptyRow.createEmptyRow();
            add(emptyRow);
        }
    }

    /*  Implementation für Zugriff auf die einzelnen Rows via die Schülerliste,
     es muss somit nur ein Listener an jedes LectionField gehängt werden
     */
    @Override
    public void mouseClicked(MouseEvent m) {

        LectionField l;

        if (m.getSource() instanceof LectionField) {

            l = (LectionField) m.getSource();

            /* SchuelerRow als eingeteilt markieren, l.isSelected ->klick auf leeren Stundenplan darf keine Wirkung haben*/
            if (l.isSelected() && l.getFieldPosition() == 0 && !DayColumn.dragEnabled()) {
                schuelerList[l.getSchuelerID()].cleanSchülerRow(true);  // SchülerID = Position in SchuelerListe
            } else {
                SchuelerRow.setSchuelerlisteEnabled(false);  // während Lection-Drag ist Schülerliste gesperrt
            }
            /* Doppelklick auf Lectionpanel = SchuelerRow entsperren  */
            if (m.getClickCount() == 2 && (l.getFieldPosition() == 3 || l.getFieldPosition() == 4) && !DayColumn.dragEnabled()) {
                schuelerList[l.getSchuelerID()].cleanSchülerRow(false);
            }
            /* SchuelerListe nur während Dragen gesperrt, wenn Lection gesetzt, muss Schülerliste wieder aktiv sein */
            if (!DayColumn.dragEnabled()) {
                SchuelerRow.setSchuelerlisteEnabled(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }
    @Override
    public void mouseReleased(MouseEvent me) {
    }
    @Override
    public void mouseEntered(MouseEvent me) {
    }
    @Override
    public void mouseExited(MouseEvent me) {
    }

    /*  DatabaseListener Implementation  */
    @Override
    public void dayAdded(StundenplanDay d) {
     }
    @Override
    public void dayRemoved(StundenplanDay d) {
    }
    @Override
    public void dayEdited(StundenplanDay d) {
    }
    
    /*   SchuelerRow instanzieren und befüllen  */
    @Override
    public void schuelerAdded(Schueler s) {

        int i = s.getSchuelerIndex(); // der List-Index (= Schueler-ID) aus der DataList wird übernommen
        schuelerList[i].setSchuelerIndex(i);  // jede SchuelerRow-Instanz bekommt ihre Position in der schülerList (= Schueler-ID)
        schuelerList[i].createSchuelerRow(s);   // SchuelerRow befüllen
    }
    
    @Override
    public void schuelerRemoved(Schueler s) {
    }
    @Override
    public void schuelerEdited(Schueler s) {
    }
}
