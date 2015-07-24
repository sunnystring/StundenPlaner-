/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;

/**
 *
 * @author Mathias
 */
public final class DataBase {

    /* ---------------globale Daten------------------------ */
    private static final ArrayList<DatabaseListener> databaseListener = new ArrayList<>();

    private static int dayIndex;  // counter für Tage = Position in dayDataList  = Day-ID
    private static int numberOfDays; // vorgegebene Maximalzahl,  ToDo: dynamisch,
    private static int schuelerIndex;  // counter für Schueler = Position in schülerDataList = Schueler-ID
    private static int schülerzahl;  // vorgegebene Maximalzahl,  ToDo: dynamisch, 

    public DataBase() {

        dayIndex = 0;
        numberOfDays = 3;
        schuelerIndex = 0;
        schülerzahl = 36;

    }

    /* ---------------Stundenplan------------------------*/
    private static final ArrayList<StundenplanDay> dayDataList = new ArrayList<>();  // enthält die StundenplanDay-Rohdaten

    /* ----------------Schülerliste------------------------*/
    private static final ArrayList<Schueler> schuelerDataList = new ArrayList<>();  // enthält die Schueler-Rohdaten

    /* globale Getter, Setter  */
    public static int getSchuelerzahl() {
        return schülerzahl;
    }

    public static int getNumberOfDays() {
        return numberOfDays;
    }

    public static int getSchuelerIndex() {
        return schuelerIndex;
    }

    public static StundenplanDay getDayDataList(int index) {
        return dayDataList.get(index);
    }

    /*  Listener */
    public static void addDatabaseListener(DatabaseListener l) {
        databaseListener.add(l);
    }

    public static void removeDatabaseListener(DatabaseListener l) {
        databaseListener.remove(l);
    }

    /*-------------------für Dateneingabe Rohfassung-------------------------*/
    public static void addDay(StundenplanDay day) {

        day.setDayIndex(dayIndex);
        dayDataList.add(day);   // StundenplanDay-Daten in dayDataList speichern
        dayIndex++;  // zählt die Anzahl eingebener Tage (= vom Tag unabhängiger Tag-Index)

        for (DatabaseListener l : databaseListener) {  // l = Referenz auf Stundenplan,
            l.dayAdded(day);                  // füllt Daten in DayColumns und zeichnet diese      
        }
    }

    public static void addSchueler(Schueler schueler) {
    
        schueler.setSchuelerIndex(schuelerIndex);
        schuelerDataList.add(schueler);// Schueler-Daten in schülerDataList speichern
        schuelerIndex++;

        for (DatabaseListener l : databaseListener) { // l = Referenz auf SchülerListe
            l.schuelerAdded(schueler);        // füllt Daten in SchülerRows und zeichnet diese      
            
        }
    }
}
