/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainframe;

import core.DataBase;
import core.StundenplanDay;
import core.Schueler;
import core.SchuelerDay;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import schuelerliste.SchuelerListe;
import stundenplan.Stundenplan;
import util.Colors;
import util.Icons;
import util.Time;

/**
 *
 * @author Mathias
 */
public class MainFrame extends JFrame {

    private final DataBase database;

    private static Stundenplan stundenplan;
    private static SchuelerListe schülerliste;
    private JPanel toolBar;
    private StdPlnButton b0, b1, b2, b3, b4, b5, b6, b7, b8;
    private JSplitPane split;
    private JScrollPane links, rechts;

    public MainFrame() {

        setTitle("StundenPlaner");
        setIconImage(Icons.getImage("table.png"));
        setExtendedState(Frame.MAXIMIZED_BOTH);

        database = new DataBase();

        createWidget();
        addWidget();
        pack();
        setLocation(0, 0);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createWidget() {

        toolBar = new JPanel();
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.LINE_AXIS));
        toolBar.setPreferredSize(new Dimension(0, 30));
        toolBar.setBorder(BorderFactory.createEmptyBorder(2, 12, 0, 15));

        b0 = new StdPlnButton("openFile.png", "Bestehender Stundenplan öffnen");
        b1 = new StdPlnButton("disk.png", "Stundenplan und Schülerdaten speichern");
        b2 = new StdPlnButton("printer.png", "Stundenplan drucken");
        b3 = new StdPlnButton("calendar.png", "Stundenplan erstellen oder ändern");
        b4 = new StdPlnButton("boy.png", "Schülerprofil erstellen");
        b5 = new StdPlnButton("boy&girl.png", "KGU-Profil erstellen");
        b6 = new StdPlnButton("coffee.png", "Automatischer Einteilungsvorschlag machen");
        b7 = new StdPlnButton("color.png", "Verteilung der Zeiten anzeigen: je später, desto dunkler");
        b8 = new StdPlnButton("list.png", "Zeiten chronologisch anordnen");

        stundenplan = new Stundenplan();
        links = new JScrollPane(stundenplan);
        links.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        links.setBackground(Colors.BACKGROUND);

        /*---------------Rohfassung: statische Eingabe Stundenplan-Daten-----------------------*/
        DataBase.addDay(new StundenplanDay("Montag", new Time("13.00"), new Time("21.00")));
        database.addDay(new StundenplanDay("Dienstag", new Time("15.00"), new Time("20.30")));
        database.addDay(new StundenplanDay("Mittwoch", new Time("12.30"), new Time("19.30")));
        /*---------------------------------------------------------------------------*/
        stundenplan.createStundenplan();

        schülerliste = new SchuelerListe();
        rechts = new JScrollPane(schülerliste);
        rechts.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        /*---------------Rohfassung: statische Eingabe Schueler-Daten -----------------------*/
        // Parameter day: startTime1, endTime1, startTime2, endTime2, favorite
        SchuelerDay day1, day2, day3;
               
        day1 = new SchuelerDay("17.15", "18.00", "18.00", "18.05", "");  // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Jarno", "Suda", day1, day2, day3, 30));

        day1 = new SchuelerDay("16.00", "18.00", "", "", "16.00"); // ok 
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Yann", "Pelda", day1, day2, day3, 30));

        day1 = new SchuelerDay("16.15", "17.00", "", "", "16.15"); // ok
        day2 = new SchuelerDay("16.15", "17.00", "", "", "16.15");
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Nicola", "Mussmann", day1, day2, day3, 30));

        day1 = new SchuelerDay("17.10", "19.00", "", "", "");  // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Yves", "Henz", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", ""); // ok
        day2 = new SchuelerDay("17.30", "18.00", "", "", "");
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Julian", "Merz", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", "");
        day2 = new SchuelerDay("18.00", "20.00", "", "", "");
        day3 = new SchuelerDay("13.30", "14.00", "", "", "");
        database.addSchueler(new Schueler("Gabriel", "Sturm", day1, day2, day3, 40));

        day1 = new SchuelerDay("", "", "", "", "17.45"); // ok
        day2 = new SchuelerDay("18.00", "20.00", "", "", "18.00"); // nach Julian
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Annalisa", "Strübin", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", ""); // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("12.45", "18.00", "", "", "17.15");
        database.addSchueler(new Schueler("Juan", "Meier", day1, day2, day3, 30));

        day1 = new SchuelerDay("17.00", "18.00", "", "", "17.00"); // ok
        day2 = new SchuelerDay("16.15", "17.00", "", "", "16.15");
        day3 = new SchuelerDay("12.20", "15.00", "", "", "");
        database.addSchueler(new Schueler("Jan", "Egli", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", ""); // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("13.30", "14.30", "", "", "");
        database.addSchueler(new Schueler("David", "Deja", day1, day2, day3, 30));

        day1 = new SchuelerDay("16.00", "17.00", "", "", "");  
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Jan", "Suter", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", "");  // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("", "", "", "", "18.00");
        database.addSchueler(new Schueler("Josua", "Gmür", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", ""); // ok
        day2 = new SchuelerDay("17.40", "20.00", "", "", "17.40");
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Lucien", "Badoux", day1, day2, day3, 30));

        day1 = new SchuelerDay("17.30", "19.00", "", "", ""); // ok
        day2 = new SchuelerDay("18.00", "19.30", "", "", "18.00");
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Liam", "Narbutas", day1, day2, day3, 30));

        day1 = new SchuelerDay("18.00", "21.00", "", "", "");
        day2 = new SchuelerDay("18.00", "21.00", "", "", "");
        day3 = new SchuelerDay("18.00", "21.00", "", "", "");
        database.addSchueler(new Schueler("Jürg", "Stehlin", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", "");
        day2 = new SchuelerDay("19.30", "21.00", "", "", "20.00");
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Shadia", "Handschin", day1, day2, day3, 30));

        day1 = new SchuelerDay("13.45", "14.00", "17.35", "17.45", "14.00"); // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Benny", "Amacher", day1, day2, day3, 30));

        day1 = new SchuelerDay("17.15", "19.30", "", "", ""); // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("", "", "", "","");
        database.addSchueler(new Schueler("Aruthiran", "Sivaligam", day1, day2, day3, 30));

        day1 = new SchuelerDay("16.30", "19.00", "", "", ""); // ok
        day2 = new SchuelerDay("16.30", "19.00", "", "", "");
        day3 = new SchuelerDay("13.45", "19.00", "", "", "");
        database.addSchueler(new Schueler("Florian", "Tognella", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", ""); // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("13.00", "15.00", "", "", "");
        database.addSchueler(new Schueler("Sean", "Kurath", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", "");  // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("13.45", "18.00", "", "", "");
        database.addSchueler(new Schueler("Anis", "Ameti", day1, day2, day3, 30));

        day1 = new SchuelerDay("17.00", "19.00", "", "", ""); // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("13.15", "13.30", "17.00", "18.30", "13.15");
        database.addSchueler(new Schueler("Sante", "Demarco", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", ""); // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("14.30", "17.30", "", "", "14.30");
        database.addSchueler(new Schueler("Timosh", "Rueff", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", ""); // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("14.00", "14.05", "18.00", "18.05", "18.00");
        database.addSchueler(new Schueler("Severin", "Schneider", day1, day2, day3, 30));

        day1 = new SchuelerDay("", "", "", "", ""); // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("16.30", "17.00", "", "", "");
        database.addSchueler(new Schueler("Vancy", "Ravindran", day1, day2, day3, 40));

        day1 = new SchuelerDay("15.25", "18.00", "", "", ""); // ok
        day2 = new SchuelerDay("15.25", "18.00", "", "", "");
        day3 = new SchuelerDay("13.30", "18.00", "", "", "");
        database.addSchueler(new Schueler("Paul", "Weiland", day1, day2, day3, 30));
        
        day1 = new SchuelerDay("16.30", "18.00", "", "", ""); // ok
        day2 = new SchuelerDay("17.30", "18.00", "", "", "");
        day3 = new SchuelerDay("13.45", "17.00", "", "", "");
        database.addSchueler(new Schueler("Arthit", "Gmür", day1, day2, day3, 30));
        
        day1 = new SchuelerDay("", "", "", "", ""); // ok
        day2 = new SchuelerDay("", "", "", "", "");
        day3 = new SchuelerDay("13.45", "17.15", "", "", "");
        database.addSchueler(new Schueler("Niclas", "Nyfeler", day1, day2, day3, 30));
        
        day1 = new SchuelerDay("", "", "", "", ""); // ok
        day2 = new SchuelerDay("19.30", "20.30", "", "", "");
        day3 = new SchuelerDay("", "", "", "", "");
        database.addSchueler(new Schueler("Sophie", "Zurlinden", day1, day2, day3, 30));


        /*---------------------------------------------------------------------------*/
        schülerliste.createSchuelerliste();

        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, links, rechts);
        split.setContinuousLayout(true);
        split.setResizeWeight(0.9);

    }

    private void addWidget() {

        setLayout(new BorderLayout(0, 0));
        add(BorderLayout.CENTER, split);
        add(BorderLayout.PAGE_START, toolBar);

        toolBar.add(b0);
        toolBar.add(b1);
        toolBar.add(b2);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(b3);
        toolBar.add(b4);
        toolBar.add(b5);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(b6);
        toolBar.add(b7);
        toolBar.add(b8);

    }

    public static SchuelerListe getSchülerListe() {
        return schülerliste;
    }

    private class StdPlnButton extends JButton {

        public StdPlnButton(String iconName, String toolTip) {

            setIcon(Icons.setIcon(iconName));
            setToolTipText(toolTip);
            setPreferredSize(new Dimension(60, 0));

        }
    }

}
