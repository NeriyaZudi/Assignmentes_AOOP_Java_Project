package UI;

import Country.Map;
import Country.Settlement;
import IO.LogFile;
import IO.RestoreLogFile;
import IO.SimulationFile;
import Location.Location;
import Simulation.Clock;
import Simulation.Main;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CyclicBarrier;


public class MainWindow extends JFrame {


    // data members
    public int sleepingTime = 50;
    protected Map map;
    private final ChangeListener val;
    private final MyMenuBar menuBar;
    private final DrawingMap drawMap;
    private final JSlider speedSlider;
    private final JLabel speedValue;
    private final JLabel daysValue;
    private StatisticsWindow statisticsWindow;
    private LogFile logFile;
    private final RestoreLogFile restoreLogFile=new RestoreLogFile();

    public MainWindow() throws Exception {

        super("Main Window");
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        //statisticsWindow=new StatisticsWindow(this);
        //statisticsWindow.setVisible(false);

        //menu bar object
        this.menuBar = new MyMenuBar();
        this.setJMenuBar(menuBar);
        //map panel.
        drawMap = new DrawingMap();


        speedValue = new JLabel("  Simulation Speed : ");
        speedValue.setFont(new Font("Serif", Font.BOLD, 18));
        speedValue.setForeground(Color.RED);

        //dafine slider for simulation speed
        this.speedSlider = new JSlider();
        this.speedSlider.setMajorTickSpacing(10);
        this.speedSlider.setMinorTickSpacing(5);
        this.speedSlider.setPaintTicks(true);
        this.speedSlider.setPaintLabels(true);

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.LINE_AXIS));
        sliderPanel.add(speedValue);
        sliderPanel.add(speedSlider);

        this.daysValue= new JLabel("Day : ",new ImageIcon("src/UI/Icons/calendar.png"),SwingConstants.RIGHT);
        daysValue.setFont(new Font("Arial", Font.BOLD, 18));
        daysValue.setForeground(Color.BLUE);
        this.daysValue.setText("Day : "+ Clock.now());

        sliderPanel.add(daysValue);
        this.add(drawMap);
        this.add(sliderPanel);

        //getting value from slider to sleeping time
        speedSlider.addChangeListener(val = new ChangeListener() {
            int speed;

            @Override
            public void stateChanged(ChangeEvent e) {
                speed = speedSlider.getValue();
                speedValue.setText("  Simulation Speed : " + speed);
                setSleepingTime(speed);

            }

        });


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700, 700);
        this.setVisible(true);

    }

    public void maPanel() throws Exception{
        return;
    }

    //************** setters **************

    public void setSleepingTime(int time) {
        sleepingTime = time;
        maPanel();
    }

    public void setLogFile(File file) {
        try {
            logFile=new LogFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(map!=null){
            Map.setLogFile(logFile);
        }
    }


    //************** getters **************

    public Map getMap(){return this.map;}

    public LogFile getLogFile() {
        return logFile;
    }

    //update gui
    public void updateAll() {
        this.daysValue.setText("Day: "+ Clock.now());
        drawMap.repaint();
        statisticsWindow.fireTableDataChanged();

    }

    private MainWindow getThis() {
        return this;
    }

    //inner class for the menu bar
    private class MyMenuBar extends JMenuBar {

        JMenu File, Simulation, Help;
        JMenuItem load, statistics, editMutations, logFile, exit, play, pause, stop, setTicks, help, about,restore;
        File fileLog;

        public MyMenuBar() {

            // File menu
            File = new JMenu("File");
            File.setIcon(new ImageIcon("src/UI/Icons/file.png"));

            //load
            load = new JMenuItem("Load", new ImageIcon("src/UI/Icons/loading.png"));
            load.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //load map from file
                    File file = loadFileFunc();
                    SimulationFile s = null;//Read data from the file
                    try {
                        s = new SimulationFile(file);

                        //Initialize a map object for the entire program
                        map=new Map(s.getMap().getSettlement(),s.getMap().getLength());
                        map.setupMap();
                        map.setupDecorators();
                        map.setIsMapLoaded(true);
                        map.printMap();

                        Map.setLogFile(getLogFile());

                        //Update menu buttons
                        load.setEnabled(false);
                        play.setEnabled(true);
                        pause.setEnabled(true);
                        stop.setEnabled(true);

                        //Defining a scheduling mechanism
                        //The last thread that comes after synchronization performs the following steps
                        map.setCyclicBarrier( new CyclicBarrier(map.getLength(), new Runnable() {
                            @Override
                            public void run() {
                                // GUI Update
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateAll();
                                    }
                                });

                                //Promoting a clock
                                Clock.nextTick();

                                //Sleep time
                                try {
                                    Thread.sleep(100L * (100 - sleepingTime));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }));

                        //Create a Statistics window and GUI update
                        statisticsWindow = new StatisticsWindow(getThis(),map);
                        updateAll();

                        //Defining and activating Threads
                        map.executeThreads();

                    } catch (Exception exception) {
                        //custom title, error icon
                        JOptionPane.showMessageDialog(getThis(),
                                "Invalid file format",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        load.setEnabled(true);
                    }
                }

                //Function for loading a file
                private java.io.File loadFileFunc() {
                    FileDialog fd = new FileDialog((Frame) null, "Please choose a file:", FileDialog.LOAD);
                    fd.setVisible(true);

                    if (fd.getFile() == null)
                        return null;
                    File f = new File(fd.getDirectory(), fd.getFile());
                    System.out.println(f.getPath());
                    return f;
                }
            });
            File.add(load);
            File.addSeparator();

            //statistics
            statistics = new JMenuItem("Statistics", new ImageIcon("src/UI/Icons/trend.png"));
            statistics.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    try {
                        //Open the Statistics window
                        statisticsWindow.setVisible(true);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        //custom title, error icon
                        JOptionPane.showMessageDialog(getThis(),
                                "No loaded map ",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }
            });
            File.add(statistics);
            File.addSeparator();

            // editMutations
            editMutations = new JMenuItem("Edit Mutations", new ImageIcon("src/UI/Icons/virus.png"));
            editMutations.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Open a mutation editing window
                    EditMutations window = EditMutations.getInstance(getThis());
                    window.setVisible(true);
                }
            });
            File.add(editMutations);
            File.addSeparator();

            //log file
            LocalDate date = LocalDate.now();
            String text = date.format(DateTimeFormatter.BASIC_ISO_DATE);
            LocalDate parsedDate = LocalDate.parse(text, DateTimeFormatter.BASIC_ISO_DATE);
            logFile = new JMenuItem("Log File", new ImageIcon("src/UI/Icons/log-file-format.png"));
            logFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Select a location to save a log file
                    File f = saveFileFunc("Log File " + parsedDate + ".log", getThis());
                    setLogFile(f);
                    restoreLogFile.save();
                    //If the file was created successfully
                    if(setFile(f)){
                        restore.setEnabled(true);
                        JOptionPane.showMessageDialog(getThis(),
                                "The file was created successfully",
                                "File",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(getThis(),
                                "Problem with creating file ",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }
                //save log file
                private java.io.File saveFileFunc(String fileName,Frame frame) {
                    // Instead of "(Frame) null" use a real frame, when GUI is learned
                    FileDialog fd = new FileDialog(frame, "Please choose a file:", FileDialog.SAVE);
                    fd.setFile(fileName);
                    fd.setVisible(true);

                    if (fd.getFile() == null)
                        return null;
                    File f = new File(fd.getDirectory(), fd.getFile());
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(f.getPath());
                    return f;
                }
            });
            File.add(logFile);
            File.addSeparator();

            restore=new JMenuItem("Restore Log",new ImageIcon("src/UI/Icons/undo.png"));
            restore.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(restoreLogFile.undo()) {
                        JOptionPane.showMessageDialog(getThis(),
                                "The file was successfully restored",
                                "Restore",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(getThis(),
                                "No file to restore",
                                "Restore",
                                JOptionPane.ERROR_MESSAGE);

                    }
                }
            });
            restore.setEnabled(false);
            File.add(restore);
            File.addSeparator();

            //exit
            exit = new JMenuItem("Exit", new ImageIcon("src/UI/Icons/on-off-button.png"));
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(map!=null)
                        map.getLogFileWriter().closeAll();
                    System.exit(0);
                }
            });
            File.add(exit);


            //  Simulation menu
            Simulation = new JMenu("Simulation");
            Simulation.setIcon(new ImageIcon("src/UI/Icons/simulate.png"));


            play = new JMenuItem("Play", new ImageIcon("src/UI/Icons/play-button.png"));
            play.setEnabled(false);
            play.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Run only after loading a map
                    if (!map.getIsMapLoaded()) {
                        JOptionPane.showMessageDialog(getThis(),
                                "Please load map first ",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (!map.getIsInAction()) {
                        //Change a static variable of a session
                        map.setIsInAction(true);
                       //Releasing threads from waiting
                        synchronized (map) {
                            map.notifyAll();
                        }

                        play.setEnabled(false);
                        pause.setEnabled(true);
                        stop.setEnabled(true);

                    } else {
                        JOptionPane.showMessageDialog(getThis(),
                                "Simulation in action ",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            Simulation.add(play);
            Simulation.addSeparator();

            // pause
            pause = new JMenuItem("Pause", new ImageIcon("src/UI/Icons/pause.png"));
            pause.setEnabled(false);
            pause.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Run only after loading a map
                    if (!map.getIsMapLoaded()) {
                        JOptionPane.showMessageDialog(getThis(),
                                "Please load map first ",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (map.getIsInAction()) {
                        //Change a static variable of a session
                        map.setIsInAction(false);

                        // Update menu buttons
                        play.setEnabled(true);
                        pause.setEnabled(false);
                        stop.setEnabled(false);

                    }//No simulation run at the moment
                    else {
                        JOptionPane.showMessageDialog(getThis(),
                                "No simulation run right now ",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);

                    }
                }
            });
            Simulation.add(pause);
            Simulation.addSeparator();

            //stop
            stop = new JMenuItem("Stop", new ImageIcon("src/UI/Icons/stop-button.png"));
            stop.setEnabled(false);
            Simulation.add(stop);
            stop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Run only after loading a map
                    if (!map.getIsMapLoaded()) {
                        JOptionPane.showMessageDialog(getThis(),
                                "Please load map first ",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (map.getIsInAction()) {
                        //Change a variable of a session
                        map.setIsInAction(false);
                        //Change the map to null
                        map.setIsStopped(true);
                        map = null;

                        // Update menu buttons
                        pause.setEnabled(false);
                        stop.setEnabled(false);
                        load.setEnabled(true);
                        play.setEnabled(false);
                        logFile.setEnabled(true);
                        drawMap.repaint();

                    }//No simulation run at the moment
                    else {
                        JOptionPane.showMessageDialog(getThis(),
                                "No simulation run right now ",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);

                    }
                }
            });
            Simulation.addSeparator();

            //set ticks
            setTicks = new JMenuItem("Set Ticks Per Day", new ImageIcon("src/UI/Icons/clock.png"));
            setTicks.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Open a window to change ticks
                    TicksPerDayDialog t = new TicksPerDayDialog(getThis());
                }
            });
            Simulation.add(setTicks);

            //Help nemu
            Help = new JMenu("Help");
            Help.setIcon(new ImageIcon("src/UI/Icons/information-button.png"));

            //help
            help = new JMenuItem("Help", new ImageIcon("src/UI/Icons/help.png"));
            help.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Open a  help window
                    HelpDialog help = new HelpDialog(getThis());
                }
            });
            Help.add(help);
            Help.addSeparator();

            //about
            about = new JMenuItem("About", new ImageIcon("src/UI/Icons/about.png"));
            about.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Open a  help window
                    AboutDialog about = new AboutDialog(getThis());
                }
            });
            Help.add(about);



            this.add(File);
            this.add(Simulation);
            this.add(Help);



        }
        public boolean setFile(File log){
            if(log!=null) {
                this.fileLog = log;
                return true;
            }
            return false;
        }

        public File getFileLog(){return this.fileLog;}

        //inner class for about window
        private class AboutDialog extends JDialog {
            private final JLabel matanPic;
            private final JLabel neriyaPic;

            public AboutDialog(JFrame MainWindow) {

                //Define dialogue not model
                super(MainWindow, "About Window", false);

                BorderLayout myBorderLayout = new BorderLayout();
                myBorderLayout.setHgap(20);
                myBorderLayout.setVgap(20);
                this.setLayout(myBorderLayout);

                JLabel title = new JLabel(" This program has been designed and developed During a Advanced OOP Programming ");
                JLabel title1 = new JLabel("               Department of Software Engineering ,SCE College of Engineering ");
                title.setForeground(Color.BLUE);
                title1.setForeground(Color.BLUE);
                title.setFont(new Font("Serif", Font.BOLD, 20));
                title1.setFont(new Font("Serif", Font.BOLD, 20));
                JPanel titlePanel = new JPanel();
                titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.PAGE_AXIS));
                titlePanel.setBackground(Color.LIGHT_GRAY);
                titlePanel.add(title);  // adds to center of panel's default BorderLayout.
                titlePanel.add(title1);

                ImageIcon matan = new ImageIcon("src/UI/Icons/matan.jpg");
                ImageIcon neriya = new ImageIcon("src/UI/Icons/neriya.jpg");

                Image image = matan.getImage(); // transform it
                Image newimg = image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                matan = new ImageIcon(newimg);  // transform it back

                image = neriya.getImage(); // transform it
                newimg = image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                neriya = new ImageIcon(newimg);  // transform it back

                matanPic = new JLabel();
                neriyaPic = new JLabel();
                matanPic.setIcon(matan);
                neriyaPic.setIcon(neriya);

                JPanel center = new JPanel();
                center.setLayout(new BoxLayout(center, BoxLayout.LINE_AXIS));
                center.add(new JLabel("                                      "));
                center.add(matanPic);
                center.add(new JLabel("                                      "));
                center.add(neriyaPic);

                JPanel down = new JPanel();
                down.setLayout(new BoxLayout(down, BoxLayout.PAGE_AXIS));
                down.add(new JLabel("Contact Details:"));
                String[][] details = {
                        {"Name", "Age", "Email", "Phone"},
                        {"Matan Ohayon", "26", "matan15595m@gmail.com", "0534203045"},
                        {"Neriya Zudi", "25", "neriyazudi@gmail.com", "0523112891"},

                };
                String[] cols = {"Name", "Age", "Email", "Phone"};
                JTable info = new JTable(details, cols);
                info.setBounds(100, 200, 500, 500);
                JScrollPane jsp = new JScrollPane(info);
                info.setBackground(Color.LIGHT_GRAY);

                JLabel rights = new JLabel();
                rights.setIcon(new ImageIcon("src/UI/Icons/copy rights.jpg"));

                JLabel copy = new JLabel("All  Rights Reserved ");
                copy.setForeground(Color.red);
                copy.setFont(new Font("David", Font.ITALIC, 20));

                JPanel copyright = new JPanel();
                copyright.setLayout(new BoxLayout(copyright, BoxLayout.LINE_AXIS));
                copyright.add(copy);
                copyright.add(rights);

                down.add(info);
                down.add(copyright);


                this.add(titlePanel, BorderLayout.NORTH);
                this.add(center, BorderLayout.CENTER);
                this.add(down, BorderLayout.SOUTH);


                pack();
                setVisible(true);


            }

        }

        //inner class for ticks per day window
        private class TicksPerDayDialog extends JDialog {
            private final JSpinner spinner;
            private final JLabel statusLabel = new JLabel();

            public TicksPerDayDialog(JFrame MainWindow) {
                super(MainWindow, "Ticks per day window", true);
                JPanel northPanel = new JPanel();
                JPanel CenterPanel = new JPanel();
                JPanel southPanel = new JPanel();

                northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));
                this.spinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
                spinner.setBounds(5, 5, 5, 5);
                JLabel label = new JLabel("Ticks: ");
                northPanel.add(label);
                northPanel.add(this.spinner);

                CenterPanel.setLayout(new BoxLayout(CenterPanel, BoxLayout.LINE_AXIS));
                JLabel clock = new JLabel();
                clock.setIcon(new ImageIcon("src/UI/Icons/back-in-time.png"));
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                    }
                });

                JButton Setbutton = new JButton("Set");
                Setbutton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String command = e.getActionCommand();
                        if (command.equals("Set")) {
                            statusLabel.setText(" new ticks per day is: " + spinner.getValue());
                        }
                        Clock.setTick_per_day((int) spinner.getValue());

                    }


                });

                CenterPanel.add(clock);
                CenterPanel.add(new JLabel("        "));
                CenterPanel.add(Setbutton);
                CenterPanel.add(cancelButton);

                southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.LINE_AXIS));
                southPanel.add(this.statusLabel);

                this.add(northPanel, BorderLayout.NORTH);
                this.add(CenterPanel, BorderLayout.CENTER);
                this.add(southPanel, BorderLayout.SOUTH);
                this.setSize(200, 200);
                setVisible(true);

            }
        }

        //inner class for help window
        private class HelpDialog extends JDialog {

            private final JLabel title;

            public HelpDialog(JFrame MainWindow) {
                super(MainWindow, "Help Window", true);
                BorderLayout myBorderLayout = new BorderLayout();
                myBorderLayout.setHgap(0);
                myBorderLayout.setVgap(0);
                this.setLayout(myBorderLayout);
                title = new JLabel("Welcome to Help window !");
                title.setForeground(Color.BLUE);
                title.setFont(new Font("Serif", Font.BOLD, 20));
                JPanel titlePanel = new JPanel();
                titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.PAGE_AXIS));
                titlePanel.setBackground(Color.WHITE);
                titlePanel.add(title, BorderLayout.NORTH);


                ImageIcon helpPic = new ImageIcon("src/UI/Icons/helpPic.jpeg");
                Image image = helpPic.getImage();
                image.getScaledInstance(500, 500, java.awt.Image.SCALE_SMOOTH);
                helpPic = new ImageIcon(image);
                JLabel helppic = new JLabel();
                helppic.setIcon(helpPic);

                JPanel body = new JPanel();
                body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
                body.add(helppic);

                this.add(titlePanel, BorderLayout.NORTH);
                this.add(body, BorderLayout.CENTER);
                pack();
                setVisible(true);

            }
        }
    }

    //inner class for painting map
    private class DrawingMap extends JPanel {

        // data members
        private Image simMap;
        private LineDecorator lineDecorator;

        public DrawingMap() {
            super();
            this.addMouseListener(new CustomMouseClick());
        }

        public void paintComponent(Graphics g) {

            if (map != null) {//After loading a map

                super.paintComponent(g);
                if (map == null)
                    return;

                Graphics2D gr = (Graphics2D) g;
                gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gr.setBackground(Color.lightGray);

                //Drawing connecting lines between settlements
                for(LineDecorator dec:map.getDecorators()){
                    dec.repaintLine(gr);
                }

                //Draw rectangles by size and location of settlements
                for (Settlement s: map) {
                    gr.setColor(s.getColor());//Settlement traffic light color
                    Location settlementLocation = s.getLocation();
                    gr.fill3DRect(settlementLocation.getPosition().getX(), settlementLocation.getPosition().getY(), settlementLocation.getSize().getWidth(), settlementLocation.getSize().getHeight(), true);
                    gr.setColor(Color.BLACK);
                    int x_name = settlementLocation.getPosition().getX();
                    int y_name = settlementLocation.getPosition().getY();
                    gr.drawString(s.getName(), x_name, y_name);

                    //repaint();

                }

                this.setVisible(true);
            } else//Before loading a map
            {
                simMap = new ImageIcon(Objects.requireNonNull(DrawingMap.class.getResource("simMap.jpg"))).getImage();
                super.paintComponent(g);
                Graphics2D gr = (Graphics2D) g;
                gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gr.drawImage(simMap, 0, 0, 700, 700, null);
                this.setVisible(true);
            }

        }

        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        //inner class for mouse click
        private class CustomMouseClick implements MouseListener {
            @Override
            public void mouseClicked(MouseEvent e) {

                Point press = e.getPoint();
                int xSettlement;
                int ySettlement;
                int widthSettlement;
                int heightSettlement;

                int index=0;
                //Find the location of the settlement on the map
                for (Settlement s:map) {
                    xSettlement = s.getLocation().getPosition().getX();
                    ySettlement = s.getLocation().getPosition().getY();
                    widthSettlement = s.getLocation().getSize().getWidth();
                    heightSettlement = s.getLocation().getSize().getHeight();

                    //Check if the click was performed over the settlement
                    if ((press.getX() >= xSettlement && press.getX() <= widthSettlement + xSettlement) && (press.getY() >= ySettlement && press.getY() <= heightSettlement + ySettlement)) {
                        //Open the statistics window for the selected settlement
                        statisticsWindow.filterChoosenSettlement(index);
                        statisticsWindow.setVisible(true);
                    }
                    index++;
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        }

    }


}


