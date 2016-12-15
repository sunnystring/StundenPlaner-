/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceListUI;

import attendanceListData.AttendanceListData;
import attendanceListUI.AttendanceTable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
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
public class HeaderField extends JLabel implements TableCellRenderer, MouseMotionListener {

    private AttendanceTable attendanceTable;
    private AttendanceListData attendanceListData;
    private int movedCol;

    public HeaderField(AttendanceTable attendanceTable) {
        this.attendanceTable = attendanceTable;
        attendanceListData = (AttendanceListData) attendanceTable.getModel();
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, BACKGROUND_COLOR));
        setPreferredSize(new Dimension(0, 25));
        setForeground(BACKGROUND_COLOR);
        setFont(this.getFont().deriveFont(Font.BOLD, 10));
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        if (col == 0) {
            setBackground(NAMEFIELD_SELECTED_COLOR);
            setHorizontalAlignment(SwingConstants.LEADING);
        } else {
            setHorizontalAlignment(SwingConstants.CENTER);
            if (col == movedCol) {
                setBackground(LIGHT_GREEN);
            } else {
                if (attendanceListData.isJournalArchiveEnabled()) {
                    setBackground(col == attendanceListData.getCurrentWeekIndex() + 1 ? DAYFIELD_COLOR : DARK_GREEN);
                } else {
                    setBackground(DAYFIELD_COLOR);
                }
            }
        }
        String name = (String) value;
        setText(name);
        return this;
    }

    @Override
    public void mouseMoved(MouseEvent e
    ) {
        Point point = e.getPoint();
        movedCol = attendanceTable.columnAtPoint(point);
        attendanceTable.getTableHeader().repaint();
    }

    public void init() {
        movedCol = attendanceTable.getModel().getColumnCount() - 1;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

}
