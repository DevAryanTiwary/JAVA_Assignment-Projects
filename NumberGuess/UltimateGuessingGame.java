package NumberGuess;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class UltimateGuessingGame extends JFrame {
    // Game Logic Variables
    private int targetNumber;
    private int attempts;
    private final ArrayList<Integer> scores = new ArrayList<>();
    private boolean isDarkMode = true;

    // UI Components
    private JPanel mainContainer, gamePanel, historyPanel, controls;
    private JTextField inputField;
    private JLabel feedbackLabel, attemptLabel, titleLabel, historyTitle;
    private JTextArea historyArea;
    private JButton guessButton, restartButton, themeButton;

    // Psychology-based Color Palettes
    private final Color DARK_BG = new Color(18, 18, 28);
    private final Color DARK_CARD = new Color(30, 30, 46);
    private final Color LIGHT_BG = new Color(240, 242, 245);
    private final Color LIGHT_CARD = Color.WHITE;
    private final Color ACCENT_PURPLE = new Color(110, 89, 222); // Calm/Logical
    private final Color SUCCESS_GREEN = new Color(46, 204, 113); // Reward signal
    private final Color ERROR_RED = new Color(231, 76, 60);     // Alert signal

    public UltimateGuessingGame() {
        setTitle("Number Quest: Master Edition");
        setSize(650, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        setupComponents();
        applyTheme();
        initGame();

        setVisible(true);
    }

    private void setupComponents() {
        // Main Layout: BorderLayout gives us structured zones
        mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- Center Game Panel ---
        gamePanel = new JPanel(new GridLayout(5, 1, 15, 15));
        gamePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel("I'm thinking of a number (1-100)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        inputField = new JTextField();
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setFont(new Font("Consolas", Font.BOLD, 36));
        inputField.setBorder(new LineBorder(ACCENT_PURPLE, 2));

        feedbackLabel = new JLabel("Take your first guess!", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        
        attemptLabel = new JLabel("Attempts: 0", SwingConstants.CENTER);

        controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        controls.setOpaque(false);
        
        guessButton = createStyledButton("GUESS", SUCCESS_GREEN);
        restartButton = createStyledButton("RESTART", new Color(52, 152, 219));
        themeButton = createStyledButton("🌓 THEME", Color.GRAY);

        controls.add(guessButton);
        controls.add(restartButton);
        controls.add(themeButton);

        gamePanel.add(titleLabel);
        gamePanel.add(inputField);
        gamePanel.add(feedbackLabel);
        gamePanel.add(attemptLabel);
        gamePanel.add(controls);

        // --- Right History Panel ---
        historyPanel = new JPanel(new BorderLayout(5, 5));
        historyPanel.setPreferredSize(new Dimension(200, 0));
        historyPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        historyTitle = new JLabel("🏆 BEST SCORES", SwingConstants.CENTER);
        historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(null);

        historyPanel.add(historyTitle, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        mainContainer.add(gamePanel, BorderLayout.CENTER);
        mainContainer.add(historyPanel, BorderLayout.EAST);
        add(mainContainer);

        // --- Event Listeners ---
        guessButton.addActionListener(e -> checkGuess());
        inputField.addActionListener(e -> checkGuess()); // Allows 'Enter' key
        restartButton.addActionListener(e -> initGame());
        themeButton.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            applyTheme();
        });
    }

    private void applyTheme() {
        Color bg = isDarkMode ? DARK_BG : LIGHT_BG;
        Color card = isDarkMode ? DARK_CARD : LIGHT_CARD;
        Color text = isDarkMode ? Color.WHITE : new Color(40, 40, 40);

        mainContainer.setBackground(bg);
        gamePanel.setBackground(card);
        historyPanel.setBackground(card);
        historyArea.setBackground(card);
        historyArea.setForeground(text);
        titleLabel.setForeground(text);
        attemptLabel.setForeground(text);
        historyTitle.setForeground(text);
        
        // Refresh UI
        SwingUtilities.updateComponentTreeUI(this);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void initGame() {
        targetNumber = (int) (Math.random() * 100) + 1;
        attempts = 0;
        feedbackLabel.setText("New game! Enter 1-100.");
        feedbackLabel.setForeground(ACCENT_PURPLE);
        attemptLabel.setText("Attempts: 0");
        inputField.setEnabled(true);
        inputField.setText("");
        inputField.requestFocus();
        guessButton.setEnabled(true);
    }

    private void checkGuess() {
        try {
            String text = inputField.getText().trim();
            if (text.isEmpty()) return;

            int guess = Integer.parseInt(text);
            attempts++;
            attemptLabel.setText("Attempts: " + attempts);

            if (guess == targetNumber) {
                feedbackLabel.setText("CORRECT! 🎉");
                feedbackLabel.setForeground(SUCCESS_GREEN);
                scores.add(attempts);
                updateHistory();
                inputField.setEnabled(false);
                guessButton.setEnabled(false);
            } else {
                feedbackLabel.setText(guess < targetNumber ? "Too Low! ↑" : "Too High! ↓");
                feedbackLabel.setForeground(ERROR_RED);
                
                // AUTOMATIC CLEAR FEATURE
                inputField.setText("");
                inputField.requestFocus();
            }
        } catch (NumberFormatException ex) {
            feedbackLabel.setText("Enter a valid number!");
            inputField.setText("");
            inputField.requestFocus();
        }
    }

    private void updateHistory() {
        Collections.sort(scores);
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < Math.min(scores.size(), 10); i++) {
            sb.append(String.format("  Rank %d: %d tries\n", i + 1, scores.get(i)));
        }
        historyArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        // Run in the Event Dispatch Thread for thread-safety
        SwingUtilities.invokeLater(UltimateGuessingGame::new);
    }
}