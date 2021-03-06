/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import core.Database;
import core.Profile;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import static core.ScheduleTimes.DAYS;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import scheduleData.LectionData;
import utils.Time;

/**
 *
 * Bearbeitbare Druckansicht des Stundenplans
 */
public class PrinterTextPane extends JTextPane {

    public final Database database;
    private StyledDocument doc;

    public PrinterTextPane(Database database) {
        this.database = database;
        doc = getStyledDocument();
        initStyle();
        createText();
    }

    private void initStyle() {
        Style defaultStyle = StyleContext.getDefaultStyleContext().
                getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontFamily(defaultStyle, "SansSerif");
        Style regular = doc.addStyle("regular", defaultStyle);
        Style bold = doc.addStyle("bold", regular);
        StyleConstants.setBold(bold, true);
    }

    private void createText() {
        for (int i = 0; i < DAYS; i++) {
            TreeMap<Time, LectionData> lectionMap = database.getLectionMaps().get(i);
            if (!lectionMap.isEmpty()) {
                try {
                    String dayName = database.getScheduleTimes().getSelectedScheduleDay(i).getDayName();
                    doc.insertString(doc.getLength(), dayName + "\n",
                            doc.getStyle("bold"));
                    for (Map.Entry<Time, LectionData> entry : lectionMap.entrySet()) {
                        String lectionStart = entry.getKey().toString();
                        String lectionEnd = entry.getValue().end().plus(5).toString();
                        String lectionTime = lectionStart + "-" + lectionEnd;
                        doc.insertString(doc.getLength(), lectionTime,
                                doc.getStyle("bold"));
                        Profile profile = database.getProfile(entry.getValue().getProfileID());
                        String nameLine = " " + profile.getFirstName() + " " + profile.getName() + " " + profile.getThirdName();
                        doc.insertString(doc.getLength(), nameLine + "\n",
                                doc.getStyle("regular"));
                    }
                    doc.insertString(doc.getLength(), "\n",
                            doc.getStyle(""));
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
