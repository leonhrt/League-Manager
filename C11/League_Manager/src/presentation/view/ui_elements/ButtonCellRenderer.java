package presentation.view.ui_elements;

import business.model.TableListener;
import presentation.controller.AdminOptionController;
import presentation.view.pantalles.InfoLeague;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class that creates the panel that allows the user move across the different options
 */
public class ButtonCellRenderer extends JButton implements TableCellRenderer {
    private TableListener l;


    /**
     * Constructor method
     * @param l ActionListener that represents the controller
     */
    public ButtonCellRenderer(TableListener  l) {
        this.l = l;
        setOpaque(true);
    }

    /**
     * Method that returns the component used for drawing the cell.
     * @param table           the <code>JTable</code> that is asking the
     *                          renderer to draw; can be <code>null</code>
     * @param value           the value of the cell to be rendered.  It is
     *                          up to the specific renderer to interpret
     *                          and draw the value.  For example, if
     *                          <code>value</code>
     *                          is the string "true", it could be rendered as a
     *                          string or it could be rendered as a check
     *                          box that is checked.  <code>null</code> is a
     *                          valid value
     * @param isSelected      true if the cell is to be rendered with the
     *                          selection highlighted; otherwise false
     * @param hasFocus        if true, render cell appropriately.  For
     *                          example, put a special border on the cell, if
     *                          the cell can be edited, render in the color used
     *                          to indicate editing
     * @param row             the row index of the cell being drawn.  When
     *                          drawing the header, the value of
     *                          <code>row</code> is -1
     * @param column          the column index of the cell being drawn
     *
     * @return the component used for drawing the cell (Component)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Set the text of the button to the value in the cell
        setText(value != null ? value.toString() : "");

        if (isSelected) {
            String team_pressed = (String) table.getValueAt(row, column);
            l.teamPressedInTable(team_pressed);
        }

        return this;
    }
}