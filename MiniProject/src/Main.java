import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.StudentGUI;

/**
 * Main Application Entry Point for the Student Management System.
 * 
 * Sets the system Look and Feel, configures font anti-aliasing hints,
 * and launches the Swing GUI on the Event Dispatch Thread (EDT).
 */
public class Main {

    public static void main(String[] args) {
        // Enable subpixel text anti-aliasing on modern displays
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Set Cross-platform or System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("[Main] Using default Look & Feel: " + e.getMessage());
        }

        // Launch GUI safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                StudentGUI gui = new StudentGUI();
                gui.setVisible(true);
                System.out.println("==================================================");
                System.out.println("   STUDENT MANAGEMENT SYSTEM - GUI STARTED        ");
                System.out.println("   Developed using 100% Pure Java & Java Swing    ");
                System.out.println("==================================================");
            } catch (Exception e) {
                System.err.println("[Main] Fatal error launching StudentGUI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}

