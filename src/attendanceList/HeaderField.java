/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceList;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import static utils.Colors.*;

/**
 *
 * @author mathiaskielholz
 */
public class HeaderField extends JLabel implements TableCellRenderer, MouseListener, MouseMotionListener {

    private AttendanceTable attendanceTable;
    private int movedCol;

    public HeaderField(AttendanceTable attendanceTable) {
        this.attendanceTable = attendanceTable;
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, BACKGROUND_COLOR));
        setPreferredSize(new Dimension(0, 25));
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        if (col == 0) {
            setBackground(NAMEFIELD_SELECTED_COLOR);
            setForeground(BACKGROUND_COLOR);
            setHorizontalAlignment(SwingConstants.LEADING);
            setFont(this.getFont().deriveFont(Font.BOLD, 10));
        } else {
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(col == movedCol ? this.getFont().deriveFont(Font.BOLD, 10) : this.getFont().deriveFont(Font.PLAIN, 10));
            setBackground(col == movedCol ? DAYFIELD_COLOR : BLUE_DEFAULT);
            setForeground(col == movedCol ? BACKGROUND_COLOR : Color.BLACK);
        }
        String name = (String) value;
        setText(name);
        return this;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = e.getPoint();
        movedCol = attendanceTable.columnAtPoint(point);
        attendanceTable.getTableHeader().repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    public void init() {
        movedCol = attendanceTable.getModel().getColumnCount() - 1;
    }

    // unused
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

}
