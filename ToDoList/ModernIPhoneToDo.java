package ToDoList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ModernIPhoneToDo {
    public static void main(String[] args) {
        // --- 1. THEME & COLORS ---
        Color iosBlue = new Color(0, 122, 255);
        Color bgWhite = new Color(255, 255, 255);
        Color lightGray = new Color(242, 242, 247); // iOS background gray
        Font mainFont = new Font("SansSerif", Font.PLAIN, 15);
        Font boldFont = new Font("SansSerif", Font.BOLD, 15);

        JFrame frame = new JFrame("Reminders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380, 550);
        frame.getContentPane().setBackground(lightGray);
        frame.setLayout(new BorderLayout(0, 0));

        // --- 2. THE DATA MODEL ---
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> taskList = new JList<>(listModel);
        
        // --- 3. NUMBERING & WORD WRAP RENDERER ---
        taskList.setBackground(lightGray);
        taskList.setCellRenderer(new NumberedWrapRenderer(iosBlue));
        taskList.setSelectionBackground(new Color(220, 235, 255));
        
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(lightGray);
        frame.add(scrollPane, BorderLayout.CENTER);

        // --- 4. TOP INPUT "CARD" ---
        JPanel inputContainer = new JPanel(new BorderLayout(10, 0));
        inputContainer.setBackground(bgWhite);
        inputContainer.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(210, 210, 210)), // Bottom divider
            new EmptyBorder(15, 20, 15, 20)
        ));

        JTextField taskField = new JTextField();
        taskField.setBorder(null); // Flat look
        taskField.setFont(mainFont);
        taskField.setToolTipText("New Reminder...");

        JButton addButton = new JButton("Add");
        addButton.setFont(boldFont);
        addButton.setForeground(iosBlue);
        addButton.setContentAreaFilled(false);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        inputContainer.add(taskField, BorderLayout.CENTER);
        inputContainer.add(addButton, BorderLayout.EAST);
        frame.add(inputContainer, BorderLayout.NORTH);

        // --- 5. DELETE FOOTER ---
        JButton deleteButton = new JButton("Clear Selected");
        deleteButton.setFont(mainFont);
        deleteButton.setForeground(Color.RED);
        deleteButton.setBackground(bgWhite);
        deleteButton.setBorder(new MatteBorder(1, 0, 0, 0, new Color(210, 210, 210)));
        deleteButton.setPreferredSize(new Dimension(0, 50));
        deleteButton.setFocusPainted(false);
        frame.add(deleteButton, BorderLayout.SOUTH);

        // --- LOGIC ---
        frame.getRootPane().setDefaultButton(addButton);

        addButton.addActionListener(e -> {
            String text = taskField.getText().trim();
            if (!text.isEmpty()) {
                listModel.addElement(text);
                taskField.setText("");
            }
        });

        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) listModel.remove(index);
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // --- CUSTOM NUMBERED WORD-WRAP RENDERER ---
    static class NumberedWrapRenderer extends JPanel implements ListCellRenderer<String> {
        private JTextArea textArea = new JTextArea();
        private JLabel numberLabel = new JLabel();

        public NumberedWrapRenderer(Color accentColor) {
            setLayout(new BorderLayout(10, 0));
            setBackground(Color.WHITE);
            // Adds spacing between the items so they look like "cells"
            setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(230, 230, 235)), 
                new EmptyBorder(12, 15, 12, 15)
            ));

            numberLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            numberLabel.setForeground(accentColor);
            numberLabel.setVerticalAlignment(SwingConstants.TOP);

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setOpaque(false);
            textArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
            textArea.setEditable(false);

            add(numberLabel, BorderLayout.WEST);
            add(textArea, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, 
                                                       int index, boolean isSelected, boolean cellHasFocus) {
            textArea.setText(value);
            numberLabel.setText((index + 1) + "."); // The Task Numbering
            
            // Adjust height based on text wrap
            textArea.setSize(list.getWidth() - 60, Short.MAX_VALUE); 
            
            if (isSelected) {
                setBackground(list.getSelectionBackground());
            } else {
                setBackground(Color.WHITE);
            }
            return this;
        }
    }
}