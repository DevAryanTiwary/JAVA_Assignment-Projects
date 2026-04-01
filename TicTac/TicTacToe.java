package TicTac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe implements ActionListener {
    JFrame frame = new JFrame("Pro Tic-Tac-Toe + Themes");
    JPanel titlePanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel sidePanel = new JPanel();
    
    JLabel statusLabel = new JLabel();
    JLabel scoreLabel = new JLabel();
    JTextArea historyLog = new JTextArea(10, 15);
    JButton[] buttons = new JButton[9];
    JButton resetButton = new JButton("Reset Board");
    JButton themeButton = new JButton("Toggle Dark/Light");

    boolean player1Turn = true;
    boolean isDarkMode = true; // Default theme
    int xScore = 0, oScore = 0, roundCount = 1;

    TicTacToe() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 600);
        frame.setLayout(new BorderLayout(10, 10));

        // --- Header ---
        titlePanel.setLayout(new GridLayout(2, 1));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setOpaque(true);
        
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setOpaque(true);
        
        titlePanel.add(statusLabel);
        titlePanel.add(scoreLabel);

        // --- Grid ---
        buttonPanel.setLayout(new GridLayout(3, 3, 5, 5));
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 80));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
            buttonPanel.add(buttons[i]);
        }

        // --- History & Theme Toggle ---
        sidePanel.setLayout(new BorderLayout());
        themeButton.addActionListener(e -> toggleTheme());
        
        historyLog.setEditable(false);
        historyLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(historyLog);

        sidePanel.add(themeButton, BorderLayout.NORTH);
        sidePanel.add(scrollPane, BorderLayout.CENTER);
        sidePanel.setPreferredSize(new Dimension(220, 0));

        // --- Footer ---
        resetButton.addActionListener(e -> resetBoard());

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(sidePanel, BorderLayout.EAST);
        frame.add(resetButton, BorderLayout.SOUTH);

        applyTheme(); // Set initial colors
        resetBoard();
        frame.setVisible(true);
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme();
    }

    private void applyTheme() {
        Color bg, fg, btnBg, textBg;

        if (isDarkMode) {
            bg = new Color(30, 30, 30);
            fg = Color.WHITE;
            btnBg = new Color(50, 50, 50);
            textBg = new Color(45, 45, 45);
            statusLabel.setForeground(new Color(252, 186, 3));
        } else {
            bg = new Color(240, 240, 240);
            fg = Color.BLACK;
            btnBg = Color.WHITE;
            textBg = Color.WHITE;
            statusLabel.setForeground(new Color(0, 102, 204));
        }

        frame.getContentPane().setBackground(bg);
        titlePanel.setBackground(bg);
        statusLabel.setBackground(bg);
        scoreLabel.setBackground(textBg);
        scoreLabel.setForeground(fg);
        
        buttonPanel.setBackground(bg);
        for (JButton b : buttons) {
            if (b.isEnabled()) b.setBackground(btnBg);
            if (b.getText().equals("X")) b.setForeground(new Color(220, 20, 60));
            else if (b.getText().equals("O")) b.setForeground(new Color(0, 150, 255));
        }

        historyLog.setBackground(textBg);
        historyLog.setForeground(fg);
        sidePanel.setBackground(bg);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 9; i++) {
            if (e.getSource() == buttons[i] && buttons[i].getText().equals("")) {
                if (player1Turn) {
                    buttons[i].setForeground(new Color(220, 20, 60));
                    buttons[i].setText("X");
                    statusLabel.setText("O's Turn");
                } else {
                    buttons[i].setForeground(new Color(0, 150, 255));
                    buttons[i].setText("O");
                    statusLabel.setText("X's Turn");
                }
                player1Turn = !player1Turn;
                checkWinner();
            }
        }
    }

    public void checkWinner() {
        int[][] wins = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
        for (int[] w : wins) {
            if (!buttons[w[0]].getText().isEmpty() &&
                buttons[w[0]].getText().equals(buttons[w[1]].getText()) &&
                buttons[w[1]].getText().equals(buttons[w[2]].getText())) {
                
                String winner = buttons[w[0]].getText();
                historyLog.append(" Round " + roundCount + ": " + winner + " Won\n");
                handleWin(winner, w);
                return;
            }
        }
        boolean full = true;
        for(JButton b : buttons) if(b.getText().isEmpty()) full = false;
        if(full) {
            statusLabel.setText("It's a Draw!");
            historyLog.append(" Round " + roundCount + ": Draw\n");
            roundCount++;
        }
    }

    private void handleWin(String winner, int[] winNodes) {
        if (winner.equals("X")) xScore++; else oScore++;
        scoreLabel.setText("X: " + xScore + " | O: " + oScore);
        statusLabel.setText(winner + " Wins!");
        for (int idx : winNodes) buttons[idx].setBackground(Color.GREEN);
        for (JButton b : buttons) b.setEnabled(false);
        roundCount++;
    }

    public void resetBoard() {
        for (JButton b : buttons) {
            b.setText("");
            b.setEnabled(true);
            b.setBackground(isDarkMode ? new Color(50, 50, 50) : Color.WHITE);
        }
        player1Turn = true;
        statusLabel.setText("X's Turn");
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}