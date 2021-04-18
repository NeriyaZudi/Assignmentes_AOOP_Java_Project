/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Location;

public class Size {

    //ctors
    public Size() {this.width=0; this.height=0;}
    public Size(int width,int height) {
        this.width=width;
        this.height=height;
    }
   //equals
    public boolean equals(Size other) {
        return (width==other.width)&&(height==other.height);
    }
    public String toString() {
        return "width = "+this.width+","+"height = "+this.height;
    }
    //getters
    public int getWidth() {return this.width;}
    public int getHeight() {return this.height;}

    //Data members
    private final int width;
    private final int height;
}
