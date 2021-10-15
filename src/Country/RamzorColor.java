package Country;

import java.awt.*;

public enum RamzorColor {
    Green(0.4, Color.green, 1),
    Yellow(0.6, Color.yellow, 0.8),
    Orange(0.8, Color.orange, 0.6),
    Red(1, Color.red, 0.4);

    private final double passProbability;
    private Color color;
    private double precent;


    RamzorColor(double precent, Color color, double passProbability) {
        this.precent = precent;
        this.color = color;
        this.passProbability = passProbability;

    }

    public static RamzorColor CalculateColor(double precent) {
        if (precent <= 0.4)
            return RamzorColor.Green;
        else if (precent <= 0.6)
            return RamzorColor.Yellow;
        else if (precent <= 0.8)
            return RamzorColor.Orange;
        else
            return RamzorColor.Red;
    }

    public double getPassProbability() {
        return this.passProbability;
    }
}