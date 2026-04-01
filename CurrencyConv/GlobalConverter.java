package CurrencyConv;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.TreeMap;
import java.util.HashMap;

public class GlobalConverter extends JFrame {
    private JComboBox<String> fromCombo, toCombo;
    private JTextField inputAmount;
    private JLabel resultLabel;
    private JTextArea historyArea;
    private JPanel mainPanel;
    private JButton convertBtn, themeBtn;
    private boolean isDarkMode = false;

    // Rates based on March 2026 Market Data (Base 1 USD)
    private HashMap<String, Double> exchangeRates = new HashMap<>();

    public GlobalConverter() {
        initRates();
        setupUI();
    }

    private void initRates() {
        // Current 2026 estimated rates
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.86);
        exchangeRates.put("GBP", 0.75);
        exchangeRates.put("INR", 92.40);
        exchangeRates.put("JPY", 159.20);
        exchangeRates.put("AUD", 1.41);
        exchangeRates.put("CAD", 1.37);
        exchangeRates.put("CNY", 6.90);
        exchangeRates.put("CHF", 0.77);
        exchangeRates.put("AED", 3.67);
        exchangeRates.put("SGD", 1.27);
        exchangeRates.put("MXN", 17.92);
        exchangeRates.put("BRL", 5.12);
        exchangeRates.put("SAR", 3.75);
    }

    private void setupUI() {
        setTitle("World Currency Pro 2026");
        setSize(480, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(Color.WHITE);

        // Heading
        JLabel title = new JLabel("Currency Converter");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input Styling
        inputAmount = new JTextField("1.00");
        inputAmount.setFont(new Font("SansSerif", Font.PLAIN, 18));
        inputAmount.setMaximumSize(new Dimension(400, 45));
        
        String[] codes = new TreeMap<>(exchangeRates).keySet().toArray(new String[0]);
        fromCombo = new JComboBox<>(codes);
        toCombo = new JComboBox<>(codes);
        toCombo.setSelectedItem("EUR");

        // --- CONVERT BUTTON (High Visibility) ---
        convertBtn = new JButton("CONVERT NOW");
        convertBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        convertBtn.setMaximumSize(new Dimension(400, 50));
        convertBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Force colors to be visible regardless of OS theme
        convertBtn.setBackground(new Color(0, 102, 204)); // Deep Royal Blue
        convertBtn.setForeground(Color.WHITE);           // Pure White Text
        convertBtn.setFocusPainted(false);               // Remove ugly inner border
        convertBtn.setOpaque(true);                      // Required for Mac/Linux visibility
        convertBtn.setBorder(new LineBorder(new Color(0, 80, 150), 2));

        // Theme Button
        themeBtn = new JButton("🌙 Switch to Dark Mode");
        themeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        themeBtn.setFocusPainted(false);

        // Result Label
        resultLabel = new JLabel("---", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        resultLabel.setForeground(new Color(0, 153, 76)); // Green for money
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // History Area
        historyArea = new JTextArea(10, 20);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Conversion History"));

        // Assemble
        addLabel(mainPanel, "Enter Amount:");
        mainPanel.add(inputAmount);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        addLabel(mainPanel, "From Currency:");
        mainPanel.add(fromCombo);
        addLabel(mainPanel, "To Currency:");
        mainPanel.add(toCombo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(convertBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(resultLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(themeBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(scroll);

        add(mainPanel);

        // Listeners
        convertBtn.addActionListener(e -> calculate());
        themeBtn.addActionListener(e -> toggleTheme());
    }

    // Helper to add styled labels
    private void addLabel(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setFont(new Font("SansSerif", Font.ITALIC, 12));
        p.add(l);
    }

    private void calculate() {
        try {
            double amount = Double.parseDouble(inputAmount.getText());
            String from = (String) fromCombo.getSelectedItem();
            String to = (String) toCombo.getSelectedItem();

            // Logic: Value relative to USD
            double amountInUSD = amount / exchangeRates.get(from);
            double finalAmount = amountInUSD * exchangeRates.get(to);

            String resultStr = String.format("%.2f %s = %.2f %s", amount, from, finalAmount, to);
            resultLabel.setText(String.format("%.2f %s", finalAmount, to));
            historyArea.append("• " + resultStr + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        Color bg = isDarkMode ? new Color(33, 33, 33) : Color.WHITE;
        Color fg = isDarkMode ? Color.WHITE : Color.BLACK;

        mainPanel.setBackground(bg);
        historyArea.setBackground(isDarkMode ? new Color(50, 50, 50) : Color.WHITE);
        historyArea.setForeground(fg);
        
        // Switch component colors
        for (Component c : mainPanel.getComponents()) {
            if (c instanceof JLabel) c.setForeground(fg);
        }
        
        resultLabel.setForeground(isDarkMode ? Color.CYAN : new Color(0, 153, 76));
        themeBtn.setText(isDarkMode ? "☀️ Light Mode" : "🌙 Dark Mode");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GlobalConverter().setVisible(true));
    }
}