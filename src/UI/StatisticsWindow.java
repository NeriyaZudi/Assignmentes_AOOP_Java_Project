package UI;


import Country.Map;
import Country.Settlement;
import IO.StatisticsFile;
import Simulation.Clock;
import Simulation.Main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

public class StatisticsWindow extends JDialog {

    private static StatsSettlement model;
    private static long vaccinatedDoses;
    private Map map;
    private final String space1 = "                                                                                     ";
    private final String space2 = "                              ";
    private final JPanel upPanel;
    private final JPanel statsTable;
    private final JPanel downPanel;
    private final JTextField filterText;
    private final FilterComboBox filterBox;
    private final JTable table;

    private final JButton save;
    private final JButton addSick;
    private final JButton vaccinate;

    private final TableRowSorter<StatsSettlement> sorter;

    public StatisticsWindow(MainWindow mainWindow,Map map) throws Exception {

        super(mainWindow, "Statistics Window", false);

        this.map=map;
        this.upPanel = new JPanel();
        this.downPanel = new JPanel();
        this.statsTable = new JPanel();

        BorderLayout myBorderLayout = new BorderLayout();
        myBorderLayout.setHgap(10);
        myBorderLayout.setVgap(10);
        this.setLayout(myBorderLayout);


        //define table with model
        model = new StatsSettlement(this.map);
        table = new JTable(model);
        table.setRowSorter(sorter = new TableRowSorter<>(model));

        statsTable.setLayout(new BoxLayout(statsTable, BoxLayout.PAGE_AXIS));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 200));
        table.setBackground(Color.cyan);
        table.setFillsViewportHeight(true);
        statsTable.add(new JScrollPane(table));

        upPanel.add(new JLabel("Filter: "));

        this.filterBox = new FilterComboBox();
        filterBox.addActionListener(e -> newFilter());
        upPanel.add(filterBox);

        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.LINE_AXIS));
        filterText = new JTextField();
        filterText.setToolTipText("Filter By Column");
        filterText.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                newFilter();
            }

            public void removeUpdate(DocumentEvent e) {
                newFilter();
            }

            public void changedUpdate(DocumentEvent e) {
                newFilter();
            }
        });
        upPanel.add(filterText);


        ImageIcon saveIc = createImageIcon("Icons/floppy-disk.png", "saveIc");
        ImageIcon addIc = createImageIcon("Icons/add.png", "addIc");
        ImageIcon vacIc = createImageIcon("Icons/syringe.png", "vacIc");


        this.save = new JButton("Save", saveIc);
        this.addSick = new JButton("Add Sick", addIc);
        this.vaccinate = new JButton("Vaccinate", vacIc);

        //save button to export table to file
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainWindow,
                        "The data will be saved in the file in the selected location",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                File statsFile = saveFileFunc("statistics" + Clock.now() + ".csv", mainWindow);
                try {
                    StatisticsFile export = StatisticsFile.getInstance(statsFile, table);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
        //add button 0.1 precent from the population
        addSick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selction = model.getCurrentRow(table);
                    map.getSettlement().get(selction).InitContagion();
                    //model.fireTableDataChanged();
                    // GUI Update
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            mainWindow.updateAll();
                        }
                    });
                } catch (Exception ep) {
                    ep.printStackTrace();
                    JOptionPane.showMessageDialog(mainWindow,
                            "Please select a specific row",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        //vaccinate button to send vaccinated doses to choosen settlement
        vaccinate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selction = model.getCurrentRow(table);
                    VaccinateDialog vacc = new VaccinateDialog(selction);
                    model.fireTableDataChanged();
                } catch (Exception ep) {
                    ep.printStackTrace();
                    JOptionPane.showMessageDialog(mainWindow,
                            "Please select a specific row",
                            "ERROR", JOptionPane.ERROR_MESSAGE);

                }
            }
        });


        JLabel s1 = new JLabel(space1);
        JLabel s2 = new JLabel(space2);


        downPanel.setLayout(new BoxLayout(downPanel, BoxLayout.LINE_AXIS));
        downPanel.add(s1);
        downPanel.add(this.save);
        downPanel.add(s2);
        downPanel.add(this.addSick);
        downPanel.add(this.vaccinate);
        downPanel.add(s2);


        this.add(upPanel, BorderLayout.NORTH);
        this.add(statsTable, BorderLayout.CENTER);
        this.add(downPanel, BorderLayout.SOUTH);

        this.pack();

    }

    //Save a statistics file
    public static File saveFileFunc(String fileName, JFrame staticticsWindow) {
        // Instead of "(Frame) null" use a real frame, when GUI is learned
        FileDialog fd = new FileDialog(staticticsWindow, "Please choose a file:", FileDialog.SAVE);
        fd.setFile(fileName);
        fd.setVisible(true);

        if (fd.getFile() == null)
            return null;
        File f = new File(fd.getDirectory(), fd.getFile());
        System.out.println(f.getPath());
        return f;
    }

    public static long getVaccinatedDoses() {
        return StatisticsWindow.vaccinatedDoses;
    }

    //Static variable change of vaccination dose quantity
    public static void setVaccinatedDoses(long vc) {
        StatisticsWindow.vaccinatedDoses = vc;
    }

    public static void setModel(StatsSettlement m) {
        model = m;
    }

    //Function for getting original line number even after filtering
    public int getCurrentRow(JTable table) {
        return table.getRowSorter().convertRowIndexToModel(table.getSelectedRow());
    }

    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = StatisticsWindow.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    //Function for filtering by regular expression
    private void newFilter() {
        try {
            sorter.setRowFilter(RowFilter.regexFilter(filterText.getText(), FilterComboBox.getColNumber()));
        } catch (java.util.regex.PatternSyntaxException e) {
            // If current expression doesn't parse, don't update.
        }
    }

    //Filter a row by settlement name
    public void filterChoosenSettlement(int index) {
        table.setRowSelectionInterval(index, index);
    }

    //Real-time update of a table
    public void fireTableDataChanged() {
        // TODO Auto-generated method stub
        model.fireTableDataChanged();
    }

    //inner class for model table
    private class StatsSettlement extends AbstractTableModel {
        public final String[] columnNames = {"Settlement Type", "Settlement Name", "Ramzor Color",
                "Population Size", "Number of sick", "Sick Percentage", "Vaccinated", "Number of Deceased"};
        public Map mapData;

        public StatsSettlement(Map m) {
            this.mapData = m;
        }


        @Override
        public int getRowCount() {
            return mapData.getLength();
        }

        @Override
        public int getColumnCount() {
            return 8;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Settlement settlement = mapData.at(rowIndex);
            return switch (columnIndex) {
                case 0 -> settlement.getType();
                case 1 -> settlement.getName();
                case 2 -> settlement.getRamzorColor();
                case 3 -> settlement.getPopulation().size();
                case 4 -> settlement.calcSick();
                case 5 -> settlement.sickPrecentString();
                case 6 -> settlement.calcVaccinated();
                case 7 -> settlement.getDeadCounter();
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex > 0;
        }

        @Override
        public void fireTableDataChanged() {
            fireTableChanged(new TableModelEvent(this, 0, getRowCount() - 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
        }

        public void setValueAt(String aValue, int row, int col) {
            Settlement settlement = mapData.at(row);
            System.out.println(row);
            System.out.println(col);
            int i = Integer.parseInt(aValue);
            if (col == 4) {
                settlement.setVaccinationDoses(i);
                this.fireTableDataChanged();
            }
            //System.out.println(map.getSettlement()[row].getVaccineDose());
        }

        public int getCurrentRow(JTable table) {
            return table.getRowSorter().convertRowIndexToModel(table.getSelectedRow());
        }
    }

    //inner class for send vaccinated doses window
    private class VaccinateDialog extends JFrame {
        public VaccinateDialog(int indexSelect) {

            super("Vaccinate Window");
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

            JPanel up = new JPanel();
            up.setLayout(new BoxLayout(up, BoxLayout.LINE_AXIS));

            JLabel title = new JLabel("Sending vaccines");
            title.setFont(new Font("Arial", Font.ITALIC, 20));
            title.setForeground(Color.red);


            JLabel img = new JLabel();
            img.setIcon(new ImageIcon("src/UI/Icons/vaccin.jpg"));

            up.add(title);
            up.add(img);

            JLabel text = new JLabel("Enter positive number of Vaccinate Doses :");
            text.setFont(new Font("Serif", Font.BOLD, 20));
            text.setForeground(Color.red);

            NumberFormat longFormat = NumberFormat.getIntegerInstance();
            NumberFormatter numberFormatter = new NumberFormatter(longFormat);
            numberFormatter.setValueClass(Long.class);
            numberFormatter.setAllowsInvalid(false);
            numberFormatter.setMinimum(0l);


            JFormattedTextField filed = new JFormattedTextField(numberFormatter);
            filed.setToolTipText("Only positive number aceptable");

            JPanel setPanel = new JPanel();
            setPanel.setLayout(new BoxLayout(setPanel, BoxLayout.LINE_AXIS));
            setPanel.add(new JLabel("Doses: "));
            setPanel.add(filed);

            JButton send = new JButton("Send", new ImageIcon("src/UI/Icons/send.png"));
            send.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (filed.getValue() == null) {
                        JOptionPane.showMessageDialog(new JFrame(),
                                "Please enter number ",
                                "Error", JOptionPane.ERROR_MESSAGE);

                    }
                    long value = (long) (filed.getValue());
                    StatisticsWindow.setVaccinatedDoses(value);
                    map.getSettlement().get(indexSelect).setVaccinationDoses(StatisticsWindow.vaccinatedDoses);
                    setVisible(false);
                }
            });

            JButton cancel = new JButton("Cancel", new ImageIcon("src/UI/Icons/error.png"));
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });

            JPanel down = new JPanel();
            down.setLayout(new BoxLayout(down, BoxLayout.LINE_AXIS));


            down.add(new JLabel("     "));
            down.add(cancel);
            down.add(send);


            this.add(up);
            this.add(text, BorderLayout.CENTER);
            this.add(setPanel);
            this.add(down);

            this.pack();
            this.setVisible(true);
        }
    }
}




