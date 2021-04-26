/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Country;

import java.awt.*;

public enum RamzorColor {
    Green(0.4, Color.green),
    Yellow(0.6,Color.yellow),
    Orange(0.8,Color.orange),
    Red(1,Color.red);

    RamzorColor(double precent,Color color)
    {
        if(color==Color.green)
            this.precent=1;
        else if(color==Color.yellow)
            this.precent=0.8;
        else if(color==Color.orange)
            this.precent=0.6;
        else
            this.precent=0.4;

        this.passProbability=precent;
    }
    public static RamzorColor CalculateColor(double precent)
    {
        if(precent <= 0.4)
            return RamzorColor.Green;
        else if(precent <= 0.6)
            return RamzorColor.Yellow;
        else if(precent <= 0.8)
            return RamzorColor.Orange;
        else
            return RamzorColor.Red;
    }

    public double getPassProbability(){return this.passProbability;}


    private final double passProbability;
    private Color color;
    private double precent;
}
