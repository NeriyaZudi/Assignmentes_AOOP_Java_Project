package Interface;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private MyMenuBar menuBar;
    private JPanel mapPanel;
    private JSlider speedSlider;

    public MainWindow(){

        super("Main Window");
        super.setSize(5000,5000);
        BorderLayout myBorderLayout = new BorderLayout();
        myBorderLayout.setHgap(20);
        myBorderLayout.setVgap(20);
        this.setLayout(myBorderLayout);


        this.menuBar=new MyMenuBar();
        this.mapPanel=new JPanel();
        this.speedSlider= new JSlider();
        this.speedSlider.setMajorTickSpacing(10);
        this.speedSlider.setMinorTickSpacing(5);
        this.speedSlider.setPaintTicks(true);
        this.speedSlider.setPaintLabels(true);


        this.add(menuBar, BorderLayout.NORTH);
        this.add(mapPanel, BorderLayout.CENTER);
        this.add(speedSlider, BorderLayout.SOUTH);






        this.pack();
        this.setVisible(true);

    }
    public static void main(String args[]) {
        MainWindow m=new MainWindow();
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    }


