/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author Mathias
 */
/* Schülerdaten */
public class Schueler {

    private String vorname;
    private String name;

    private SchuelerDay[] schuelerdayList;  //1. Tag = 0, 2. Tag = 1 usw.  

    private int lectionType;  // Anzahl Lection- bzw. TimeFields (= 5 Min.)

    private int schuelerListIndex;  // Position in der Schülerdaten-Liste = Schueler-ID

    public Schueler(String vorname, String name, SchuelerDay day0, SchuelerDay day1, SchuelerDay day2, int lectionLength) {

        this.vorname = vorname;
        this.name = name;

        lectionType = lectionLength / 5;

        /* -----------Rohfassung -> später dynamisch ----------------------------*/
        schuelerdayList = new SchuelerDay[DataBase.getNumberOfDays()];

        schuelerdayList[0] = day0;  // 1. Unterrichtstag
        schuelerdayList[0].setLectionLength(lectionLength);  // Lektionsdauer wird später gebraucht
        schuelerdayList[1] = day1;  // usw.
        schuelerdayList[1].setLectionLength(lectionLength);
        schuelerdayList[2] = day2;
        schuelerdayList[2].setLectionLength(lectionLength);

        /*--------------------------------------------------------------*/
    }

    public void setSchuelerIndex(int index) {
        schuelerListIndex = index;
    }

    public int getSchuelerIndex() {
        return schuelerListIndex;
    }

    public String getVorname() {
        return vorname;
    }

    public String getName() {
        return name;
    }

    public SchuelerDay getSchuelerDay(int index) {
        return schuelerdayList[index];
    }

    public int getLectionType() {
        return lectionType;
    }

    public void setLectionType(int lectionType) {
        this.lectionType = lectionType;
    }

    /* -----------------Rohfassung: Referenz auf schülerDayList */
    public SchuelerDay[] getSchuelerDayList() {
        return schuelerdayList;
           }
}
