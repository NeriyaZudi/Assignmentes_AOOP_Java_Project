package Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MyMenuBar extends JMenuBar {

    JMenu Menu,File, Simulation, Help;
    JMenuItem load, statistics, editMutations, exit, play,pause,stop,setTicks,help,about;

    public MyMenuBar(){

        //JFrame f= new JFrame();
        //JMenuBar menuBar=new JMenuBar();
        File=new JMenu("File");
        Simulation = new JMenu("Simulation");
        Help=new JMenu("Help");
        load=new JMenuItem("Load");
        statistics = new JMenuItem("Statistics");
        editMutations =new JMenuItem("Edit Mutations");
        exit =new JMenuItem("Exit");
        play= new JMenuItem("Play");
        pause = new JMenuItem("Pause");
        stop = new JMenuItem("Stop");
        setTicks = new JMenuItem("Set Ticks Per Day");
        help=new JMenuItem("Help");
        about=new JMenuItem("About");

        File.add(load);
        File.add(statistics);
        File.add(editMutations);
        File.add(exit);

        Simulation.add(play);
        Simulation.add(pause);
        Simulation.add(stop);
        Simulation.add(setTicks);

        Help.add(help);
        Help.add(about);

        this.add(File);
        this.add(Simulation);
        this.add(Help);

        //f.setJMenuBar(menuBar);
        //f.setSize(500,500);
        //f.setVisible(true);

        }

}
