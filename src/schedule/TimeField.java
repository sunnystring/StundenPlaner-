/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import util.Colors;
import util.Time;

/**
 *
 * @author Mathias
 */
public class TimeField extends JLabel implements MouseListener {

    /* TimeField-Status, TimeField-ID */
    private boolean lectionSelected;
    private boolean hourMarkSelected;
//    private int rowIndexTimeField;

    // Farb-Status 
    private boolean validTime, favorite;

    private Time time;

    public TimeField() {

        lectionSelected = false;
        hourMarkSelected = false;

        validTime = false;
        favorite = false;

        time = new Time();

        setBackground(Colors.BACKGROUND);
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setPreferredSize(new Dimension(0, 11));
        setOpaque(true);
    }

    /* Setter, Getter  */
    public void setTime(Time time) {
        this.time = time.clone();
    }

    public Time getTime() {
        return time;
    }

    public void setText(int hour) {
        super.setText(String.valueOf(hour));
    }

//    public void setRowIndex(int rowIndex) {
//        this.rowIndexTimeField = rowIndex;
//    }

//    public int getRowIndex() {
//        return rowIndexTimeField;
//    }

    public void setHourMarkSelected() {
        hourMarkSelected = true;
    }

    public boolean isHourMarkSelected() {
        return hourMarkSelected;
    }

    public void setFieldSelected(boolean lectionSelected) {  // bekommt Status von addLectionPanel
        this.lectionSelected = lectionSelected;
    }

    /* Farb-Status setzen und abfragen*/
    public void setValidTime(boolean valid) {
        validTime = valid;
    }

    public boolean isValidTime() {
        return validTime;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    /*  Listener-Implementation für Mouse-Aktionen in LectionField  */
    @Override
    public void mouseEntered(MouseEvent m) {

        LectionField l = (LectionField) m.getSource();

        // TimeColumn-Scrolling: nur im Drag-Modus, nur wenn keine gesetzte Lektion, bei firstEntry nur der angewählte Tag
        if (l.isTimeColumnEnabled() && DayColumn.isDragEnabled() && !lectionSelected) {

            if (validTime && !l.isOutOfBounds()) { // !l.isOutOfBounds() = Schülerzeit muss innerhalb Lehrerzeit liegen
                setBackground(Colors.LIGHT_GREEN);
            }
            if (favorite && !l.isOutOfBounds()) {
                setBackground(Colors.FAVORITE);
            }
            if (hourMarkSelected && !favorite) {
                setBackground(Colors.TIMEFIELD_HOUR);
            } else {
                setBackground(Colors.LIGHT_GRAY);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent m) {

        LectionField l = (LectionField) m.getSource();

        if (l.isTimeColumnEnabled() && !lectionSelected) {
            setBackground(Colors.BACKGROUND);

            if (validTime && !l.isOutOfBounds()) {
                setBackground(Colors.LIGHT_GREEN);
            }
            if (favorite && !l.isOutOfBounds()) {
                setBackground(Colors.FAVORITE);
            }
            if (!validTime && !favorite && hourMarkSelected) {
                setBackground(Colors.TIMEFIELD_HOUR);
            }
            if (hourMarkSelected && !favorite) {
                setBackground(Colors.TIMEFIELD_HOUR);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

}
