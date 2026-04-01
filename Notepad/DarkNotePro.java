package Notepad;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class DarkNotePro extends JFrame implements ActionListener {
    // UI Components
    private JTextArea textArea;
    private JLabel statusLabel;
    
    // Settings
    private int fontSize = 16;
    private String fontName = "Roboto"; // Ensure Roboto is installed on your OS

    public DarkNotePro() {
        // 1. Basic Frame Setup
        setTitle("DarkNote Pro - New Document");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window on screen

        // 2. Initialize Text Area & Scroll Pane
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        updateFont(); // Apply initial font settings

        // 3. Styling (Dark Mode Colors)
        Color darkBg = new Color(30, 30, 30);      // Main background
        Color lightFg = new Color(220, 220, 220);   // Text color
        Color statusBg = new Color(45, 45, 45);     // Status bar background

        textArea.setBackground(darkBg);
        textArea.setForeground(lightFg);
        textArea.setCaretColor(Color.CYAN);
        textArea.setMargin(new Insets(15, 15, 15, 15));

        // 4. Status Bar Setup
        statusLabel = new JLabel("  Words: 0  |  Size: " + fontSize + "px");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(statusBg);
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setPreferredSize(new Dimension(getWidth(), 30));

        // 5. Add Word Count Listener
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateCount(); }
            public void removeUpdate(DocumentEvent e) { updateCount(); }
            public void changedUpdate(DocumentEvent e) { updateCount(); }
        });

        // 6. Menu Bar Setup
        setupMenu();

        // 7. Assembly
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null); // Modern flat look
        
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        addMenuItem(fileMenu, "New", KeyEvent.VK_N);
        addMenuItem(fileMenu, "Open", KeyEvent.VK_O);
        addMenuItem(fileMenu, "Save", KeyEvent.VK_S);
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Exit", 0);

        // Format Menu
        JMenu formatMenu = new JMenu("Format");
        addMenuItem(formatMenu, "Zoom In (+)", KeyEvent.VK_EQUALS);
        addMenuItem(formatMenu, "Zoom Out (-)", KeyEvent.VK_MINUS);
        addMenuItem(formatMenu, "Reset Size", KeyEvent.VK_0);

        menuBar.add(fileMenu);
        menuBar.add(formatMenu);
        setJMenuBar(menuBar);
    }

    private void addMenuItem(JMenu menu, String title, int keyCode) {
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(this);
        
        // Add Ctrl keyboard shortcuts
        if (keyCode != 0) {
            item.setAccelerator(KeyStroke.getKeyStroke(keyCode, InputEvent.CTRL_DOWN_MASK));
        }
        
        menu.add(item);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "New": textArea.setText(""); setTitle("DarkNote Pro - New Document"); break;
            case "Open": openFile(); break;
            case "Save": saveFile(); break;
            case "Exit": System.exit(0); break;
            case "Zoom In (+)": fontSize += 2; updateFont(); break;
            case "Zoom Out (-)": if (fontSize > 8) fontSize -= 2; updateFont(); break;
            case "Reset Size": fontSize = 16; updateFont(); break;
        }
    }

    private void updateFont() {
        // Fallback to Dialog (SansSerif) if Roboto isn't found
        textArea.setFont(new Font(fontName, Font.PLAIN, fontSize));
        if (statusLabel != null) updateCount();
    }

    private void updateCount() {
        String text = textArea.getText().trim();
        int words = text.isEmpty() ? 0 : text.split("\\s+").length;
        statusLabel.setText("  Words: " + words + "  |  Size: " + fontSize + "px");
    }

    private void openFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()))) {
                textArea.read(br, null);
                updateCount();
                setTitle("DarkNote Pro - " + fc.getSelectedFile().getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Could not open file.");
            }
        }
    }

    private void saveFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fc.getSelectedFile()))) {
                textArea.write(bw);
                setTitle("DarkNote Pro - " + fc.getSelectedFile().getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Could not save file.");
            }
        }
    }

    public static void main(String[] args) {
        // Set UI to match system look for menus
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }
        
        new DarkNotePro();
    }
}