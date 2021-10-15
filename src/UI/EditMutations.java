package UI;

import Virus.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMutations extends JDialog {

    private static EditMutations editMutations=null;
    private String[] columnNames;
    private final int tableSize=VirusManager.getData().length;



    private EditMutations(JFrame MainWindow) {
        super(MainWindow, "Edit Mutations Window", true);
        JPanel table = new JPanel();
        table.setLayout(new BoxLayout(table, BoxLayout.PAGE_AXIS));

        columnNames=new String[tableSize];
        for(int i=0;i<tableSize;i++)
                columnNames[i]= VirusManager.Variants.values()[i].toString();
        DefaultTableModel tableModel=new DefaultTableModel(VirusManager.getData(), columnNames){
            @Override
            public Class getColumnClass(int columnIndex) {
                return Boolean.class;
            }
        };

        JTable variantTable = new JTable(tableModel);
        variantTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        variantTable.setPreferredScrollableViewportSize(new Dimension(700, 100));
        variantTable.setFillsViewportHeight(true);
        table.add(new RowedTableScroll(variantTable,columnNames));

        //ok button saving changes
        JButton ok = new JButton("OK", new ImageIcon("src/UI/Icons/check-mark.png"));
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainWindow,
                        "At the click of OK, the changes will be saved",
                        "WARNING",
                        JOptionPane.WARNING_MESSAGE);
                for (int i = 0; i <tableSize ; i++) {
                    for (int j = 0; j < tableSize; j++) {
                        if (VirusManager.getData()[i][j] != variantTable.getValueAt(i, j)) {
                            VirusManager.Change(i, j);
                        }
                    }
                }
                /*boolean tf;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        //Save a true or false variable if a variant is marked
                        tf = (Boolean) variantTable.getValueAt(i, j);
                        if (tf) {//If truth has been marked, variant develops into this type.
                            switch (i) {//Add to the appropriate variant mutation array
                                case 0 -> ChineseVariant.setMutations(variatns[j]);
                                case 1 -> BritishVariant.setMutations(variatns[j]);
                                case 2 -> SouthAfricanVariant.setMutations(variatns[j]);
                            }
                        } else {//If falsely marked removal of the appropriate Moriante mutation
                            switch (i) {//Remove to the appropriate variant mutation array
                                case 0 -> ChineseVariant.removeMutations(variatns[j]);
                                case 1 -> BritishVariant.removeMutations(variatns[j]);
                                case 2 -> SouthAfricanVariant.removeMutations(variatns[j]);

                            }
                        }
                    }*/
                }

        });

        //cancel button to exit without saving changes
        JButton cancel = new JButton("CANCEL", new ImageIcon("src/UI/Icons/error.png"));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        //help button to explain user what to do
        JButton help = new JButton("HELP", new ImageIcon("src/UI/Icons/helomut.png"));
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainWindow,
                        "In this window you can make changes to the evolution of any variant\n" +
                                "Note that a variant that doesn't evolving for itself no longer infects",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });


        JLabel mutationPic = new JLabel();
        mutationPic.setIcon(new ImageIcon("src/UI/Icons/mutazia.jpg"));
        JLabel mutationPic1 = new JLabel();
        mutationPic1.setIcon(new ImageIcon("src/UI/Icons/mutazia2.jpg"));
        JLabel mut = new JLabel("Edit Mutations");
        mut.setFont(new Font("Arial", Font.ITALIC, 20));
        mut.setForeground(Color.RED);


        //panel for pic & title
        JPanel up = new JPanel();
        up.setLayout(new BoxLayout(up, BoxLayout.LINE_AXIS));
        up.add(mut, BorderLayout.NORTH);
        up.add(mutationPic1);
        up.add(mutationPic);


        //panel for buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
        buttons.add(new JLabel("                                                                              "));
        buttons.add(cancel);
        buttons.add(ok);
        buttons.add(help);


        this.add(up, BorderLayout.NORTH);
        this.add(table, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);


        this.pack();
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        //setVisible(true);

    }

    public static EditMutations getInstance(JFrame MainWindow){
        if(editMutations==null)
            editMutations=new EditMutations(MainWindow);
        return editMutations;
    }

}
