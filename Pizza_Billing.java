import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PizzaBilling implements ActionListener {

    JFrame frame;
    JCheckBox pizza, burger, pasta;
    JTextField qtyPizza, qtyBurger, qtyPasta;
    JButton calculateButton;
    JTextArea billArea;

    PizzaBilling() {
        frame = new JFrame("Pizza Billing System");
        frame.setSize(450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel title = new JLabel("Food Order");
        title.setBounds(170, 10, 150, 30);
        frame.add(title);

        pizza = new JCheckBox("Pizza (₹200)");
        pizza.setBounds(30, 50, 150, 25);
        frame.add(pizza);

        qtyPizza = new JTextField("0");
        qtyPizza.setBounds(200, 50, 50, 25);
        frame.add(qtyPizza);

        burger = new JCheckBox("Burger (₹100)");
        burger.setBounds(30, 90, 150, 25);
        frame.add(burger);

        qtyBurger = new JTextField("0");
        qtyBurger.setBounds(200, 90, 50, 25);
        frame.add(qtyBurger);

        pasta = new JCheckBox("Pasta (₹150)");
        pasta.setBounds(30, 130, 150, 25);
        frame.add(pasta);

        qtyPasta = new JTextField("0");
        qtyPasta.setBounds(200, 130, 50, 25);
        frame.add(qtyPasta);

        calculateButton = new JButton("Generate Bill");
        calculateButton.setBounds(120, 170, 150, 30);
        calculateButton.addActionListener(this);
        frame.add(calculateButton);

        billArea = new JTextArea();
        billArea.setBounds(30, 220, 350, 120);
        frame.add(billArea);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new PizzaBilling();
    }

    public void actionPerformed(ActionEvent e) {

        int total = 0;
        billArea.setText("---- Bill ----\n");

        try {
            if (pizza.isSelected()) {
                int qty = Integer.parseInt(qtyPizza.getText());
                int cost = qty * 200;
                total += cost;
                billArea.append("Pizza x " + qty + " = ₹" + cost + "\n");
            }

            if (burger.isSelected()) {
                int qty = Integer.parseInt(qtyBurger.getText());
                int cost = qty * 100;
                total += cost;
                billArea.append("Burger x " + qty + " = ₹" + cost + "\n");
            }

            if (pasta.isSelected()) {
                int qty = Integer.parseInt(qtyPasta.getText());
                int cost = qty * 150;
                total += cost;
                billArea.append("Pasta x " + qty + " = ₹" + cost + "\n");
            }

            billArea.append("\nTotal = ₹" + total);

        } catch (Exception ex) {
            billArea.setText("Invalid Input!");
        }
    }
}
