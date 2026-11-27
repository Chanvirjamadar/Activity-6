package ui;

import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Centralized UI Theme and Styling Constants for the Student Management System.
 * 
 * Palette:
 * - Dark Blue: #0F2744 / #1E3A8A
 * - White: #FFFFFF
 * - Light Blue: #E0F2FE / #F0F7FF
 * - Accent Blue: #2563EB
 */
public final class UITheme {

    private UITheme() {} // Prevent instantiation

    // ==========================================
    // Color Palette
    // ==========================================
    public static final Color COLOR_PRIMARY_DARK  = new Color(15, 39, 68);    // Deep Navy Blue
    public static final Color COLOR_SIDEBAR_BG    = new Color(24, 49, 83);    // Sidebar Dark Blue
    public static final Color COLOR_SIDEBAR_HOVER = new Color(38, 72, 119);   // Sidebar Hover
    public static final Color COLOR_SIDEBAR_ACTIVE= new Color(37, 99, 235);   // Vibrant Royal Blue
    
    public static final Color COLOR_PRIMARY_ACCENT= new Color(37, 99, 235);   // #2563EB
    public static final Color COLOR_PRIMARY_HOVER = new Color(29, 78, 216);   // #1D4ED8
    public static final Color COLOR_LIGHT_BLUE    = new Color(224, 242, 254); // #E0F2FE
    public static final Color COLOR_SELECTION_BG  = new Color(219, 234, 254); // #DBEAFE
    
    public static final Color COLOR_BACKGROUND    = new Color(248, 250, 252); // #F8FAFC Soft Slate
    public static final Color COLOR_CARD_BG       = Color.WHITE;
    public static final Color COLOR_BORDER        = new Color(226, 232, 240); // #E2E8F0
    public static final Color COLOR_BORDER_FOCUS  = new Color(59, 130, 246);  // #3B82F6
    
    public static final Color COLOR_TEXT_PRIMARY  = new Color(15, 23, 42);    // #0F172A
    public static final Color COLOR_TEXT_SECONDARY= new Color(100, 116, 139); // #64748B
    public static final Color COLOR_TEXT_MUTED    = new Color(148, 163, 184); // #94A3B8
    public static final Color COLOR_TEXT_WHITE    = Color.WHITE;

    public static final Color COLOR_SUCCESS       = new Color(16, 185, 129);  // #10B981 Emerald
    public static final Color COLOR_DANGER        = new Color(239, 68, 68);   // #EF4444 Rose Red
    public static final Color COLOR_DANGER_HOVER  = new Color(220, 38, 38);   // #DC2626
    public static final Color COLOR_WARNING       = new Color(245, 158, 11);  // #F59E0B Amber
    public static final Color COLOR_INFO          = new Color(14, 165, 233);  // #0EA5E9 Sky

    public static final Color COLOR_TABLE_ROW_ALT = new Color(248, 250, 252);

    // ==========================================
    // Typography
    // ==========================================
    public static final Font FONT_APP_TITLE       = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_PAGE_HEADER     = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_CARD_TITLE      = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_CARD_VALUE      = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_LABEL_BOLD      = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_LABEL_REGULAR   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_INPUT           = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON          = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE_HEADER    = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE_CELL      = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_STATUS_BAR      = new Font("Segoe UI", Font.PLAIN, 11);

    // ==========================================
    // Common Borders
    // ==========================================
    public static Border createFieldBorder() {
        return new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(7, 10, 7, 10)
        );
    }

    public static Border createCardBorder() {
        return new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(16, 18, 16, 18)
        );
    }
}

