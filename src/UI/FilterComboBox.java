package UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FilterComboBox extends JPanel implements ActionListener {

    private static final String[] colNames = {"Settlement Type", "Settlement Name", "Population Size", "Ramzor Color",
            "Sick Percentage", "Vaccinated", "Number of Deceased"};
    private static int ColNumber;
    private final JComboBox<String> filterBox;


    FilterComboBox() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        filterBox = new JComboBox<>(colNames);
        this.add(filterBox);
        filterBox.addActionListener(this);
        this.setVisible(true);
    }

    public static int getColNumber() {
        return ColNumber;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == filterBox) {
            switch (filterBox.getItemAt(filterBox.getSelectedIndex())) {
                case "Settlement Type" -> ColNumber = 0;
                case "Settlement Name" -> ColNumber = 1;
                case "Population Size" -> ColNumber = 2;
                case "Ramzor Color" -> ColNumber = 3;
                case "Sick Percentage" -> ColNumber = 4;
                case "Vaccinated" -> ColNumber = 5;
                case "Number of Deceased" -> ColNumber = 6;

            }

        }


    }

    public void addActionListener(ActionListener actionListener) {
    }
}
