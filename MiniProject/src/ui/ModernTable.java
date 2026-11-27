package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Modern JTable with alternating row colors, customized dark header,
 * comfortable cell padding, and high-readability typography.
 */
public class ModernTable extends JTable {

    public ModernTable() {
        super();
        initStyle();
    }

    public ModernTable(TableModel model) {
        super(model);
        initStyle();
    }

    private void initStyle() {
        setRowHeight(34);
        setFont(UITheme.FONT_TABLE_CELL);
        setSelectionBackground(UITheme.COLOR_SELECTION_BG);
        setSelectionForeground(UITheme.COLOR_PRIMARY_DARK);
        setGridColor(UITheme.COLOR_BORDER);
        setShowVerticalLines(false);
        setShowHorizontalLines(true);
        setFillsViewportHeight(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Header Styling
        JTableHeader header = getTableHeader();
        header.setFont(UITheme.FONT_TABLE_HEADER);
        header.setBackground(UITheme.COLOR_PRIMARY_DARK);
        header.setForeground(UITheme.COLOR_TEXT_WHITE);
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);

        header.setDefaultRenderer(new TableCellRenderer() {
            private final DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) defaultRenderer.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setBackground(UITheme.COLOR_PRIMARY_DARK);
                label.setForeground(UITheme.COLOR_TEXT_WHITE);
                label.setFont(UITheme.FONT_TABLE_HEADER);
                label.setBorder(new EmptyBorder(0, 10, 0, 10));
                
                // Center-align ID, Roll, Gender, Year/Sem columns
                if (column == 0 || column == 2 || column == 4 || column == 7) {
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                }
                return label;
            }
        });

        // Alternating row renderer with custom padding
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(UITheme.COLOR_SELECTION_BG);
                    c.setForeground(UITheme.COLOR_PRIMARY_DARK);
                    setFont(UITheme.FONT_TABLE_CELL.deriveFont(Font.BOLD));
                } else {
                    c.setBackground((row % 2 == 0) ? Color.WHITE : UITheme.COLOR_TABLE_ROW_ALT);
                    c.setForeground(UITheme.COLOR_TEXT_PRIMARY);
                    setFont(UITheme.FONT_TABLE_CELL);
                }

                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setBorder(new EmptyBorder(0, 10, 0, 10));
                    // Center align specific columns
                    if (column == 0 || column == 2 || column == 4 || column == 7) {
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                    } else {
                        label.setHorizontalAlignment(SwingConstants.LEFT);
                    }
                }
                return c;
            }
        });
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Prevent accidental direct editing in the view table
    }
}

