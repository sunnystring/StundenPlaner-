/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendanceListUI;

import attendanceListData.AbsenceTypes;
import attendanceListData.AttendanceFieldData;
import static java.awt.Color.*;
import java.awt.Component;
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
 * Renderer-Component für {@link AttendanceTable}
 */
public class AttendanceField extends JLabel implements MouseMotionListener, TableCellRenderer {

    private AttendanceTable attendanceTable;
    private int movedRow, movedCol, tempRow, tempCol;

    public AttendanceField(AttendanceTable attendanceTable) {
        this.attendanceTable = attendanceTable;
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        AttendanceFieldData field = (AttendanceFieldData) value;
        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        // Namensfeld
        if (col == 0) {
            setText(field.getNameString());
            if (row == movedRow) {
                setFont(this.getFont().deriveFont(Font.BOLD, 10));
                setBackground(NAMEFIELD_SELECTED_COLOR);
                setForeground(BACKGROUND_COLOR);
            } else {
                setBackground(NAMEFIELD_SINGLE_COLOR);
                setForeground(BLACK);
            }
        }
        // Absenzenfelder
        if (col > 0) {
            int absenceType = field.getAbsenceType();
            setText(field.getAbsenceTypeString());
            setHorizontalAlignment(SwingConstants.CENTER);
            if (field.isLectionReplaced()) {
                setFont(this.getFont().deriveFont(Font.BOLD, 10));
            } else {
                setFont(this.getFont().deriveFont(Font.BOLD, 20));
            }
            setBackground(field.isDayMarked() ? VERY_LIGHT_GREEN : LIGHT_GRAY_COLOR);
            setForeground(AbsenceTypes.getColorOf(absenceType));
            if (row == movedRow && col == movedCol) {
                setBackground(BACKGROUND_COLOR);
                if (absenceType == AbsenceTypes.OFFICIAL) {
                    setForeground(field.isDayMarked() ? VERY_LIGHT_GREEN : LIGHT_GRAY_COLOR);
                }
            }
        }
        return this;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = e.getPoint();
        movedRow = attendanceTable.rowAtPoint(point);
        movedCol = attendanceTable.columnAtPoint(point);
        if (movedRow >= 0) {
            if (movedRow != tempRow || movedCol != tempCol) {
                paintNameFields(movedRow < tempRow);
                tempRow = movedRow;
                tempCol = movedCol;
            }
        } else { // ausserhalb Table (unten)
            attendanceTable.repaint();
        }

    }

    private void paintNameFields(boolean moveUp) {
        if (moveUp) {
            for (int i = 0; i < attendanceTable.getRowCount(); i++) {
                for (int j = 0; j < attendanceTable.getColumnCount(); j++) {
                    attendanceTable.repaint(attendanceTable.getCellRect(movedRow + i, j, false));
                }
            }
        } else {
            for (int i = 0; i < attendanceTable.getRowCount(); i++) {
                for (int j = 0; j < attendanceTable.getColumnCount(); j++) {
                    attendanceTable.repaint(attendanceTable.getCellRect(movedRow - i, j, false));
                }
            }
        }
    }

    public void init() {
        movedRow = 0;
        movedCol = attendanceTable.getColumnCount() - 1;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
