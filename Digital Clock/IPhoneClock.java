import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IPhoneClock {

    // Main Window Components
    private JFrame frame;
    private JLabel timeLabel;
    private JButton themeToggleButton;
    private SimpleDateFormat timeFormat;

    // State variable: true = Dark Mode, false = Light Mode
    private boolean isDarkMode = true;

    // Define iPhone Color Palettes (hex codes derived from iOS)
    // Dark Mode Palette
    private final Color DARK_BG = new Color(0, 0, 0);          // Pure black
    private final Color DARK_FG = new Color(242, 242, 247);  // Off-white
    private final Color DARK_BTN_BG = new Color(28, 28, 30); // Dark grey button
    private final Color DARK_BTN_FG = new Color(10, 132, 255); // iOS system blue

    // Light Mode Palette
    private final Color LIGHT_BG = new Color(255, 255, 255); // Pure white
    private final Color LIGHT_FG = new Color(0, 0, 0);        // Pure black
    private final Color LIGHT_BTN_BG = new Color(242, 242, 247); // Light grey button
    private final Color LIGHT_BTN_FG = new Color(0, 122, 255); // iOS system blue

    // Constructor: Set up the entire application
    public IPhoneClock() {
        // 1. Initialize the JFrame (Main Window)
        frame = new JFrame("iPhone Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 260);
        
        // Use GridBagLayout for flexible centering of components
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 2. Setup the Time Label (The Clock Face)
        // iOS uses "San Francisco" which might not be on your PC. 
        // We will try SF, then Helvetica Neue, then default SansSerif.
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("San Francisco", Font.BOLD, 70));
        if (!timeLabel.getFont().getName().equals("San Francisco")) {
            timeLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 70));
        }

        // Add 20px padding around the text
        timeLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 3. Setup the Theme Toggle Button
        themeToggleButton = new JButton("Switch Theme");
        themeToggleButton.setFocusable(false); // Remove ugly outline
        themeToggleButton.setFont(new Font("San Francisco", Font.PLAIN, 14));
        if (!themeToggleButton.getFont().getName().equals("San Francisco")) {
            themeToggleButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        }
        
        // Style the button like an iOS utility button
        themeToggleButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // EVENT HANDLING: The Toggle Action
        themeToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Flip the state
                isDarkMode = !isDarkMode;
                // Re-apply the selected theme
                applyTheme();
            }
        });

        // 4. Layout management (Placing components into the grid)
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing around components
        frame.add(timeLabel, gbc);

        gbc.gridy = 1; // Row 1 (below the clock)
        frame.add(themeToggleButton, gbc);

        // 5. Setup the Timer for 1-second updates
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();

        // Initial settings
        updateTime();
        applyTheme(); // Set initial theme (Dark Mode)
        frame.setLocationRelativeTo(null); // Center window on screen
        frame.setVisible(true);
    }

    /**
     * Method called by the Timer every 1 second to refresh the time text.
     */
    private void updateTime() {
        String currentTime = timeFormat.format(new Date());
        timeLabel.setText(currentTime);
    }

    /**
     * Applies the current color scheme to all UI components.
     */
    private void applyTheme() {
        if (isDarkMode) {
            // APPLY DARK MODE
            frame.getContentPane().setBackground(DARK_BG);
            
            timeLabel.setForeground(DARK_FG);
            
            themeToggleButton.setBackground(DARK_BTN_BG);
            themeToggleButton.setForeground(DARK_BTN_FG);
            themeToggleButton.setText("Switch to Light Mode");
            
        } else {
            // APPLY LIGHT MODE
            frame.getContentPane().setBackground(LIGHT_BG);
            
            timeLabel.setForeground(LIGHT_FG);
            
            themeToggleButton.setBackground(LIGHT_BTN_BG);
            themeToggleButton.setForeground(LIGHT_BTN_FG);
            themeToggleButton.setText("Switch to Dark Mode");
        }
    }

    // Main method to run the program
    public static void main(String[] args) {
        // Run UI tasks on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new IPhoneClock());
    }
}