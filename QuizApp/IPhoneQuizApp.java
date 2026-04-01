package QuizApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class IPhoneQuizApp extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    // Data Storage
    private String username = "";
    private List<Question> questions;
    private List<Integer> userAnswers = new ArrayList<>();
    private List<String> leaderboard = new ArrayList<>();
    private int currentIdx = 0, score = 0;

    // UI Components
    private JLabel questionLabel, userDisplayLabel, scoreFinalLabel;
    private JRadioButton[] options = new JRadioButton[4];
    private ButtonGroup group;
    private JProgressBar progressBar;

    // Theme Colors
    private final Color IOS_BLUE = new Color(0, 122, 255);
    private final Color IOS_BG = new Color(242, 242, 247);
    private final Color IOS_CARD = Color.WHITE;

    public IPhoneQuizApp() {
        setTitle("Java Mastery iOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 750);
        setLocationRelativeTo(null);

        initQuestions();
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createQuizPanel(), "QUIZ");
        cardPanel.add(createResultPanel(), "RESULT");
        cardPanel.add(createLeaderboardPanel(), "LEADERBOARD");

        add(cardPanel);
    }

    private void initQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("Which is a reserved word in Java?", new String[]{"method", "native", "subclass", "array"}, 1));
        questions.add(new Question("Size of char in Java?", new String[]{"8-bit", "16-bit", "32-bit", "Depends on OS"}, 1));
        questions.add(new Question("Which allows duplicate elements?", new String[]{"Set", "List", "Map", "All"}, 1));
        questions.add(new Question("Keyword for constant in Java?", new String[]{"const", "fixed", "final", "static"}, 2));
        questions.add(new Question("Default value of Local variables?", new String[]{"0", "null", "No default", "false"}, 2));
        questions.add(new Question("Which is not a loop in Java?", new String[]{"for", "do-while", "foreach", "repeat-until"}, 3));
        questions.add(new Question("Which class is the superclass of Error?", new String[]{"Exception", "Throwable", "RuntimeException", "System"}, 1));
        questions.add(new Question("Is String a primitive type?", new String[]{"Yes", "No", "Only in Java 8", "Depends"}, 1));
        questions.add(new Question("Garbage collection is handled by?", new String[]{"JVM", "OS", "Programmer", "Compiler"}, 0));
        questions.add(new Question("Which operator is for string concatenation?", new String[]{"&", ".", "+", "concat"}, 2));
    }

    private JPanel createLoginPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(IOS_BG);
        JTextField nameIn = new JTextField(12);
        JButton btn = new RoundedButton("Start Quiz", IOS_BLUE, Color.WHITE);
        
        btn.addActionListener(e -> {
            username = nameIn.getText().trim();
            if(!username.isEmpty()){
                startNewQuiz();
            }
        });

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10,10,10,10);
        g.gridy=0; p.add(new JLabel("Enter Name:"), g);
        g.gridy=1; p.add(nameIn, g);
        g.gridy=2; p.add(btn, g);
        return p;
    }
private JPanel createQuizPanel() {
    // Main container with 20px vertical gaps
    JPanel p = new JPanel(new BorderLayout(0, 20));
    p.setBackground(IOS_BG);
    p.setBorder(new EmptyBorder(30, 30, 30, 30));

    // 1. TOP SECTION: User Name and Progress Bar
    JPanel topSection = new JPanel(new GridLayout(2, 1, 5, 5));
    topSection.setOpaque(false);
    userDisplayLabel = new JLabel();
    userDisplayLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    
    progressBar = new JProgressBar(0, 10);
    progressBar.setForeground(IOS_BLUE);
    progressBar.setPreferredSize(new Dimension(300, 12));
    
    topSection.add(userDisplayLabel);
    topSection.add(progressBar);

    // 2. CENTER SECTION: Question (Top) and Options (Below)
    JPanel centerContent = new JPanel(new BorderLayout(0, 30)); // 30px gap between Q and A
    centerContent.setOpaque(false);

    questionLabel = new JLabel("", SwingConstants.CENTER);
    questionLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    
    // Options Panel
    JPanel optsP = new JPanel(new GridLayout(4, 1, 0, 15));
    optsP.setOpaque(false);
    group = new ButtonGroup();
    for(int i = 0; i < 4; i++) {
        options[i] = new JRadioButton();
        options[i].setFont(new Font("SansSerif", Font.PLAIN, 15));
        options[i].setBackground(IOS_CARD);
        options[i].setFocusPainted(false);
        // Add a margin inside the radio buttons for a cleaner look
        options[i].setBorder(new EmptyBorder(5, 10, 5, 10)); 
        group.add(options[i]);
        optsP.add(options[i]);
    }

    centerContent.add(questionLabel, BorderLayout.NORTH); // Question goes ABOVE
    centerContent.add(optsP, BorderLayout.CENTER);        // Options go BELOW

    // 3. BOTTOM SECTION: Submit Button
    JButton sub = new RoundedButton("Submit Answer", IOS_BLUE, Color.WHITE);
    sub.addActionListener(e -> handleAnswer());

    // Assemble everything into the main panel
    p.add(topSection, BorderLayout.NORTH);
    p.add(centerContent, BorderLayout.CENTER);
    p.add(sub, BorderLayout.SOUTH);

    return p;
}

    private JPanel createResultPanel() {
        JPanel p = new JPanel(new GridLayout(4,1,10,10));
        p.setBackground(IOS_BG);
        p.setBorder(new EmptyBorder(50,50,50,50));
        scoreFinalLabel = new JLabel("", SwingConstants.CENTER);
        
        JButton revBtn = new RoundedButton("Review Answers", Color.GRAY, Color.WHITE);
        revBtn.addActionListener(e -> showReview());

        JButton leaderBtn = new RoundedButton("Leaderboard", IOS_BLUE, Color.WHITE);
        leaderBtn.addActionListener(e -> cardLayout.show(cardPanel, "LEADERBOARD"));

        JButton againBtn = new RoundedButton("Try Again", Color.BLACK, Color.WHITE);
        againBtn.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));

        p.add(scoreFinalLabel); p.add(revBtn); p.add(leaderBtn); p.add(againBtn);
        return p;
    }

    private JPanel createLeaderboardPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(IOS_BG);
        JTextArea area = new JTextArea();
        area.setEditable(false);
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        
        JButton back = new JButton("Back to Results");
        back.addActionListener(e -> cardLayout.show(cardPanel, "RESULT"));
        p.add(back, BorderLayout.SOUTH);

        // Update logic
        p.addHierarchyListener(e -> {
            StringBuilder sb = new StringBuilder("--- LEADERBOARD ---\n\n");
            for(String s : leaderboard) sb.append(s).append("\n");
            area.setText(sb.toString());
        });
        return p;
    }

    private void startNewQuiz() {
        currentIdx = 0; score = 0;
        userAnswers.clear();
        userDisplayLabel.setText("User: " + username);
        cardLayout.show(cardPanel, "QUIZ");
        displayQuestion();
    }

    private void displayQuestion() {
        Question q = questions.get(currentIdx);
        questionLabel.setText("<html><div style='text-align:center'>"+q.prompt+"</div></html>");
        for(int i=0; i<4; i++) options[i].setText(q.options[i]);
        group.clearSelection();
        progressBar.setValue(currentIdx);
    }

    private void handleAnswer() {
        int sel = -1;
        for(int i=0; i<4; i++) if(options[i].isSelected()) sel = i;
        if(sel == -1) return;

        userAnswers.add(sel);
        if(sel == questions.get(currentIdx).correctIdx) score++;

        currentIdx++;
        if(currentIdx < questions.size()) displayQuestion();
        else finishQuiz();
    }

    private void finishQuiz() {
        leaderboard.add(username + " - Score: " + score);
        scoreFinalLabel.setText("Result: " + score + "/10");
        cardLayout.show(cardPanel, "RESULT");
    }

    private void showReview() {
        StringBuilder sb = new StringBuilder("Review Your Answers:\n\n");
        for(int i=0; i<questions.size(); i++) {
            Question q = questions.get(i);
            int ans = userAnswers.get(i);
            sb.append("Q").append(i+1).append(": ").append(q.prompt).append("\n");
            sb.append("Your Answer: ").append(q.options[ans]);
            sb.append(ans == q.correctIdx ? " (Correct)\n\n" : " (Wrong! Correct: "+q.options[q.correctIdx]+")\n\n");
        }
        JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(sb.toString(), 15, 30)));
    }

    // Reuse RoundedButton and RoundedBorder from previous example...
    class RoundedButton extends JButton {
        private Color bg, fg;
        public RoundedButton(String t, Color bg, Color fg) {
            super(t); this.bg = bg; this.fg = fg;
            setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
            setForeground(fg); setFont(new Font("SansSerif", Font.BOLD, 14));
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    static class Question {
        String prompt; String[] options; int correctIdx;
        Question(String p, String[] o, int c) { this.prompt = p; this.options = o; this.correctIdx = c; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IPhoneQuizApp().setVisible(true));
    }
}