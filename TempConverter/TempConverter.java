package TempConverter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TempConverter extends JFrame {
    private JPanel mainPanel, topPanel, inputPanel, historyPanel, historyHeader;
    private JTextField inputField;
    private JLabel resultLabel, titleLabel, historyLabel;
    private DefaultListModel<String> historyModel;
    private JButton convertBtn, themeBtn, clearBtn;
    private JComboBox<String> unitChooser;
    private JList<String> historyList;
    
    private boolean isDarkMode = false;

    public TempConverter() {
        setTitle("EcoTemp v1.1");
        setSize(450, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // --- TOP SECTION ---
        topPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("Temp Converter");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        
        themeBtn = new JButton("🌙 Dark Mode");
        themeBtn.setFocusPainted(false);
        themeBtn.addActionListener(e -> toggleTheme());
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(themeBtn, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- CENTER SECTION ---
        inputPanel = new JPanel(new GridLayout(4, 1, 12, 12));
        
        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 20));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setBorder(BorderFactory.createTitledBorder("Input Value"));
        
        String[] units = {"Celsius to Fahrenheit", "Fahrenheit to Celsius"};
        unitChooser = new JComboBox<>(units);
        
        convertBtn = new JButton("CONVERT");
        convertBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        convertBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        convertBtn.setBackground(new Color(70, 130, 80));
        convertBtn.setForeground(Color.WHITE);
        
        resultLabel = new JLabel("--", SwingConstants.CENTER);
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

        inputPanel.add(inputField);
        inputPanel.add(unitChooser);
        inputPanel.add(convertBtn);
        inputPanel.add(resultLabel);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // --- BOTTOM SECTION (History + Clear Button) ---
        historyModel = new DefaultListModel<>();
        historyList = new JList<>(historyModel);
        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        
        // Sub-panel for History Label + Clear Button
        historyHeader = new JPanel(new BorderLayout());
        historyLabel = new JLabel("History");
        historyLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        clearBtn.setFocusPainted(false);
        clearBtn.addActionListener(e -> historyModel.clear()); // Simple one-liner to clear list

        historyHeader.add(historyLabel, BorderLayout.WEST);
        historyHeader.add(clearBtn, BorderLayout.EAST);
        
        historyPanel = new JPanel(new BorderLayout(5, 5));
        historyPanel.add(historyHeader, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(historyPanel, BorderLayout.SOUTH);

        // Logic
        convertBtn.addActionListener(e -> performConversion());
        applyTheme(Color.WHITE, new Color(50, 50, 50), new Color(245, 245, 245));
    }

    private void performConversion() {
        try {
            double input = Double.parseDouble(inputField.getText());
            double result;
            String entry;

            if (unitChooser.getSelectedIndex() == 0) {
                result = (input * 9/5) + 32;
                entry = String.format("%.1f°C ➔ %.1f°F", input, result);
            } else {
                result = (input - 32) * 5/9;
                entry = String.format("%.1f°F ➔ %.1f°C", input, result);
            }

            resultLabel.setText(String.format("%.1f", result));
            historyModel.insertElementAt(entry, 0); 
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input a number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            applyTheme(new Color(35, 35, 35), Color.WHITE, new Color(55, 55, 55));
            themeBtn.setText("☀️ Light Mode");
        } else {
            applyTheme(Color.WHITE, new Color(50, 50, 50), new Color(245, 245, 245));
            themeBtn.setText("🌙 Dark Mode");
        }
    }

    private void applyTheme(Color bg, Color fg, Color panelBg) {
        mainPanel.setBackground(bg);
        topPanel.setBackground(bg);
        inputPanel.setBackground(bg);
        historyPanel.setBackground(bg);
        historyHeader.setBackground(bg);
        
        titleLabel.setForeground(fg);
        historyLabel.setForeground(fg);
        resultLabel.setForeground(new Color(100, 180, 255)); 
        
        historyList.setBackground(panelBg);
        historyList.setForeground(fg);
        
        // Apply theme to the Input Border
        inputField.setBackground(panelBg);
        inputField.setForeground(fg);
        inputField.setCaretColor(fg);
        
        SwingUtilities.updateComponentTreeUI(this);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new TempConverter().setVisible(true));
    }
}