import javax.swing.*;
import java.awt.*;

public class VibrantLogin extends JFrame {

    public VibrantLogin() {
        // 1. Setup Frame
        setTitle("Secure Portal");
        setSize(400, 320); // Slightly taller to fit buttons comfortably
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // 2. Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(30, 39, 46)); // Deeper Dark Blue

        // 3. Labels and Fields
        JLabel titleLabel = new JLabel("USER LOGIN");
        titleLabel.setForeground(new Color(0, 210, 211)); // Teal/Cyan
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 22));
        titleLabel.setBounds(120, 20, 200, 30);
        panel.add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(50, 80, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField();
        userText.setBounds(140, 80, 180, 25);
        panel.add(userText);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(50, 120, 80, 25);
        panel.add(passLabel);

        JPasswordField passText = new JPasswordField();
        passText.setBounds(140, 120, 180, 25);
        panel.add(passText);

        // 4. LOGIN BUTTON
        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(140, 170, 85, 30);
        loginButton.setBackground(new Color(76, 209, 55)); // Vibrant Green
        loginButton.setForeground(Color.BLACK);
        panel.add(loginButton);

        // 5. RESET BUTTON
        JButton resetButton = new JButton("RESET");
        resetButton.setBounds(235, 170, 85, 30);
        resetButton.setBackground(new Color(255, 159, 67)); // Vibrant Orange
        resetButton.setForeground(Color.BLACK);
        panel.add(resetButton);

        // --- EVENTS ---

        // Login Logic
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passText.getPassword());

            if (username.equals("admin") && password.equals("1234")) {
                JOptionPane.showMessageDialog(this, "Welcome, " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Login Failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Reset Logic
        resetButton.addActionListener(e -> {
            userText.setText("");
            passText.setText("");
            userText.requestFocus(); // Puts the cursor back in the username field
        });

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new VibrantLogin();
    }
}