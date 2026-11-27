package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 * Custom modern JButton with anti-aliased rounded corners and dynamic hover effects.
 */
public class ModernButton extends JButton {

    public enum ButtonType {
        PRIMARY,
        SECONDARY,
        DANGER,
        SUCCESS,
        SIDEBAR,
        OUTLINE
    }

    private final ButtonType type;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private int cornerRadius = 8;

    public ModernButton(String text, ButtonType type) {
        super(text);
        this.type = type;
        init();
    }

    public ModernButton(String text, ButtonType type, int cornerRadius) {
        super(text);
        this.type = type;
        this.cornerRadius = cornerRadius;
        init();
    }

    private void init() {
        setFont(UITheme.FONT_BUTTON);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(8, 16, 8, 16));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        Color bgColor;
        Color textColor;
        Color borderColor = null;

        if (!isEnabled()) {
            bgColor = new Color(226, 232, 240);
            textColor = new Color(148, 163, 184);
        } else {
            switch (type) {
                case PRIMARY:
                    bgColor = isPressed ? UITheme.COLOR_PRIMARY_HOVER.darker()
                            : (isHovered ? UITheme.COLOR_PRIMARY_HOVER : UITheme.COLOR_PRIMARY_ACCENT);
                    textColor = UITheme.COLOR_TEXT_WHITE;
                    break;
                case SECONDARY:
                    bgColor = isPressed ? new Color(203, 213, 225)
                            : (isHovered ? new Color(226, 232, 240) : new Color(241, 245, 249));
                    textColor = UITheme.COLOR_TEXT_PRIMARY;
                    borderColor = UITheme.COLOR_BORDER;
                    break;
                case DANGER:
                    bgColor = isPressed ? UITheme.COLOR_DANGER_HOVER.darker()
                            : (isHovered ? UITheme.COLOR_DANGER_HOVER : UITheme.COLOR_DANGER);
                    textColor = UITheme.COLOR_TEXT_WHITE;
                    break;
                case SUCCESS:
                    bgColor = isPressed ? UITheme.COLOR_SUCCESS.darker()
                            : (isHovered ? new Color(5, 150, 105) : UITheme.COLOR_SUCCESS);
                    textColor = UITheme.COLOR_TEXT_WHITE;
                    break;
                case SIDEBAR:
                    bgColor = isPressed ? UITheme.COLOR_SIDEBAR_ACTIVE.darker()
                            : (isHovered ? UITheme.COLOR_SIDEBAR_HOVER : Color.BLACK);
                    // Use transparent background for normal state, handled below
                    textColor = isHovered ? UITheme.COLOR_TEXT_WHITE : new Color(226, 232, 240);
                    break;
                case OUTLINE:
                    bgColor = isHovered ? UITheme.COLOR_LIGHT_BLUE : Color.WHITE;
                    textColor = UITheme.COLOR_PRIMARY_ACCENT;
                    borderColor = UITheme.COLOR_PRIMARY_ACCENT;
                    break;
                default:
                    bgColor = UITheme.COLOR_PRIMARY_ACCENT;
                    textColor = Color.WHITE;
            }
        }

        // Draw Background
        if (type == ButtonType.SIDEBAR) {
            if (isHovered || isPressed) {
                g2.setColor(bgColor);
                g2.fillRoundRect(2, 2, width - 4, height - 4, cornerRadius, cornerRadius);
            }
        } else {
            g2.setColor(bgColor);
            g2.fillRoundRect(1, 1, width - 2, height - 2, cornerRadius, cornerRadius);

            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.drawRoundRect(1, 1, width - 3, height - 3, cornerRadius, cornerRadius);
            }
        }

        // Text rendering
        g2.setFont(getFont());
        g2.setColor(textColor);
        FontMetrics fm = g2.getFontMetrics();
        int textX = (width - fm.stringWidth(getText())) / 2;
        int textY = (height + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}

