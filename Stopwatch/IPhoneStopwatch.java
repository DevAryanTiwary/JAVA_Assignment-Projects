package Stopwatch;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class IPhoneStopwatch extends JFrame {

    // Time Tracking
    private Timer timer;
    private int elapsedTime = 0; // in milliseconds
    private int lapCount = 0;

    // Components
    private JLabel timeDisplay;
    private RoundedButton startStopBtn, lapResetBtn; // Updated to custom class
    private JTextArea lapArea;
    private boolean isRunning = false;

    // Standard iOS Stopwatch Colors
    Color startGreen = new Color(51, 102, 51);
    Color startTextGreen = new Color(102, 255, 102);
    Color stopRed = new Color(102, 0, 0);
    Color stopTextRed = new Color(255, 102, 102);
    Color lapGrey = new Color(51, 51, 51);

    public IPhoneStopwatch() {
        // --- 1. Window Setup (Unchanged) ---
        setTitle("Stopwatch");
        setSize(360, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(10, 10));

        // --- 2. Time Display (Unchanged) ---
        timeDisplay = new JLabel("00:00.00", SwingConstants.CENTER);
        // Using light font weight for iOS look
        timeDisplay.setFont(new Font("SansSerif", Font.BOLD, 70));
        timeDisplay.setForeground(Color.WHITE);
        timeDisplay.setBorder(new EmptyBorder(60, 0, 40, 0));
        add(timeDisplay, BorderLayout.NORTH);

        // --- 3. Buttons (Layout unchanged, custom button class used) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        buttonPanel.setBackground(Color.BLACK);

        // We use our custom RoundedButton class here
        lapResetBtn = new RoundedButton("Lap", 80); 
        lapResetBtn.setBackground(lapGrey);
        lapResetBtn.setForeground(Color.WHITE);

        startStopBtn = new RoundedButton("Start", 80);
        startStopBtn.setBackground(startGreen);
        startStopBtn.setForeground(startTextGreen);

        buttonPanel.add(lapResetBtn);
        buttonPanel.add(startStopBtn);
        add(buttonPanel, BorderLayout.CENTER);

        // --- 4. Lap List (Unchanged) ---
        lapArea = new JTextArea(10, 20);
        lapArea.setEditable(false);
        lapArea.setBackground(Color.BLACK);
        lapArea.setForeground(new Color(200, 200, 200));
        lapArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lapArea.setBorder(new EmptyBorder(10, 20, 10, 10)); // Indent the text
        
        JScrollPane scrollPane = new JScrollPane(lapArea);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
        scrollPane.getVerticalScrollBar().setBackground(Color.BLACK);
        add(scrollPane, BorderLayout.SOUTH);

        // --- 5. Logic & Events (Unchanged) ---
        timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                elapsedTime += 10;
                updateTimeStrings();
            }
        });

        startStopBtn.addActionListener(e -> toggleStart());
        lapResetBtn.addActionListener(e -> handleLapOrReset());

        setLocationRelativeTo(null);
    }

    private void toggleStart() {
        if (!isRunning) {
            timer.start();
            isRunning = true;
            startStopBtn.setText("Stop");
            startStopBtn.setBackground(stopRed); 
            startStopBtn.setForeground(stopTextRed); 
            lapResetBtn.setText("Lap");
        } else {
            timer.stop();
            isRunning = false;
            startStopBtn.setText("Start");
            startStopBtn.setBackground(startGreen);
            startStopBtn.setForeground(startTextGreen);
            lapResetBtn.setText("Reset");
        }
    }

    private void handleLapOrReset() {
        if (isRunning) {
            // Lap Logic
            lapCount++;
            String lapTime = timeDisplay.getText();
            String lapPadded = String.format("%-10s", " Lap " + lapCount);
            lapArea.append(lapPadded + "       " + lapTime + "\n");
        } else {
            // Reset Logic
            elapsedTime = 0;
            updateTimeStrings();
            lapArea.setText("");
            lapCount = 0;
            lapResetBtn.setText("Lap");
        }
    }

    private void updateTimeStrings() {
        int mins = (elapsedTime / 60000) % 60;
        int secs = (elapsedTime / 1000) % 60;
        int ms = (elapsedTime % 1000) / 10; // Get only 2 digits for MS
        timeDisplay.setText(String.format("%02d:%02d.%02d", mins, secs, ms));
    }

    // =========================================================
    // CUSTOM ROUNDED BUTTON CLASS
    // =========================================================
    class RoundedButton extends JButton {
        private int diameter;

        public RoundedButton(String text, int diameter) {
            super(text);
            this.diameter = diameter;
            setFont(new Font("SansSerif", Font.PLAIN, 17));
            setFocusPainted(false);
            setBorderPainted(false); // We paint our own border/shape
            setContentAreaFilled(false); // We paint our own background
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Paint background (the circle)
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, diameter, diameter, diameter, diameter));

            // Paint border (subtle black line between circle and panel)
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.draw(new RoundRectangle2D.Double(1, 1, diameter-2, diameter-2, diameter, diameter));

            g2.dispose();
            super.paintComponent(g); // Paint the label text over our shape
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(diameter, diameter);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IPhoneStopwatch().setVisible(true));
    }
}