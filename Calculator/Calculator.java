import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator implements ActionListener {
    JFrame frame;
    JTextField textField;
    JTextArea historyArea;
    JPanel buttonPanel;

    String[] labels = {
        "AC", "DEL", "%", "÷",
        "7", "8", "9", "×",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "="
    };
    
    double num1 = 0, num2 = 0, result = 0;
    char operator;

    public Calculator() {
        frame = new JFrame("iCalculator Pro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 550);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new BorderLayout(10, 10));

        // 1. TOP DISPLAY
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        
        textField = new JTextField("0");
        textField.setFont(new Font("SansSerif", Font.PLAIN, 50));
        textField.setBackground(Color.BLACK);
        textField.setForeground(Color.WHITE);
        textField.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        textField.setEditable(false);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        topPanel.add(textField, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.NORTH);

        // 2. CENTER BUTTONS
        buttonPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (String label : labels) {
            RoundButton btn = new RoundButton(label);
            btn.addActionListener(this);
            
            if (label.equals("AC") || label.equals("DEL") || label.equals("%")) {
                btn.setBackground(new Color(165, 165, 165));
                btn.setForeground(Color.BLACK);
            } else if ("÷×-+= ".contains(label)) {
                btn.setBackground(new Color(255, 159, 10));
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(new Color(51, 51, 51));
                btn.setForeground(Color.WHITE);
            }
            buttonPanel.add(btn);
        }
        frame.add(buttonPanel, BorderLayout.CENTER);

        // 3. RIGHT HISTORY
        historyArea = new JTextArea(10, 18);
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(25, 25, 25));
        historyArea.setForeground(new Color(0, 255, 150));
        historyArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        
        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setBorder(BorderFactory.createTitledBorder(null, "HISTORY", 0, 0, null, Color.GRAY));
        frame.add(scroll, BorderLayout.EAST);

        // 4. SETUP KEYBOARD SUPPORT
        setupKeyBindings();

        frame.setVisible(true);
    }

    private void setupKeyBindings() {
        InputMap im = buttonPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = buttonPanel.getActionMap();

        // Array of keys to listen for
        String[] keys = {"0","1","2","3","4","5","6","7","8","9",".","+","-","*","/", "ENTER", "BACK_SPACE", "ESCAPE"};
        
        for (String key : keys) {
            im.put(KeyStroke.getKeyStroke(key.equals("ENTER") ? "ENTER" : 
                                          key.equals("BACK_SPACE") ? "BACK_SPACE" : 
                                          key.equals("ESCAPE") ? "ESCAPE" : key), key);
            
            am.put(key, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleInput(key);
                }
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        handleInput(e.getActionCommand());
    }

    // Unified logic for both Click and Keyboard
    private void handleInput(String input) {
        // Map keyboard symbols to button labels
        if (input.equals("*")) input = "×";
        if (input.equals("/")) input = "÷";
        if (input.equals("ENTER")) input = "=";
        if (input.equals("BACK_SPACE")) input = "DEL";
        if (input.equals("ESCAPE")) input = "AC";

        if (Character.isDigit(input.charAt(0))) {
            if (textField.getText().equals("0")) textField.setText("");
            textField.setText(textField.getText() + input);
        } else if (input.equals(".")) {
            if (!textField.getText().contains(".")) textField.setText(textField.getText() + ".");
        } else if (input.equals("AC")) {
            textField.setText("0");
            historyArea.append("--- Cleared ---\n");
        } else if (input.equals("DEL")) {
            String t = textField.getText();
            if (t.length() > 0) textField.setText(t.substring(0, t.length() - 1));
            if (textField.getText().isEmpty()) textField.setText("0");
        } else if ("÷×-+".contains(input)) {
            num1 = Double.parseDouble(textField.getText());
            operator = input.charAt(0);
            textField.setText("0");
        } else if (input.equals("=")) {
            num2 = Double.parseDouble(textField.getText());
            switch (operator) {
                case '+': result = num1 + num2; break;
                case '-': result = num1 - num2; break;
                case '×': result = num1 * num2; break;
                case '÷': result = (num2 != 0) ? num1 / num2 : 0; break;
            }
            historyArea.append(num1 + " " + operator + " " + num2 + " = " + result + "\n");
            textField.setText(String.valueOf(result));
            num1 = result;
        }
    }

    // Custom Rounded Button Class (Keep this the same)
    class RoundButton extends JButton {
        public RoundButton(String label) {
            super(label);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFont(new Font("SansSerif", Font.BOLD, 18));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isArmed()) g2.setColor(getBackground().darker());
            else g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}