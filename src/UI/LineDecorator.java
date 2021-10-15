package UI;

import Country.Settlement;

import java.awt.*;

public class LineDecorator {
    private Color color;
    private Settlement source;
    private Settlement target;

    public LineDecorator(Settlement one,Settlement two){
        source=one;
        target=two;
    }
    //Method Averaging color calculation
    public void setAvgColor(){
        int r=(source.getColor().getRed()+target.getColor().getRed())/2;
        int g=(source.getColor().getGreen()+target.getColor().getGreen())/2;
        int b=(source.getColor().getBlue()+target.getColor().getBlue())/2;
        this.color=new Color(r,g,b);
    }

    //A method that draws a line in an average color between the colors of the settlements
    public void repaintLine(Graphics2D gr) {
        setAvgColor();//Average color calculation
        int x_start_line = (int) (source.getLocation().getPosition().getX() + (0.5 * source.getLocation().getSize().getWidth()));
        int y_start_line = (int) (source.getLocation().getPosition().getY() + (0.5 * source.getLocation().getSize().getHeight()));
        int x_final_line = (int) (target.getLocation().getPosition().getX() + (0.5 * target.getLocation().getSize().getWidth()));
        int y_final_line = (int) (target.getLocation().getPosition().getY() + (0.5 * target.getLocation().getSize().getHeight()));
        gr.setColor(color);
        gr.drawLine(x_start_line, y_start_line, x_final_line, y_final_line);
    }

    public Settlement getSource(){return this.source;}

    public Settlement getTarget() {
        return target;
    }
}
