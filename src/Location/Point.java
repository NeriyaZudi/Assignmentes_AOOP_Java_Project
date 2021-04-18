/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Location;

public class Point {

    //ctors
    public Point() {this.x=0; this.y=0;}
    public Point(int x,int y) {
        this.x=x;
        this.y=y;
    }
    public Point(Point other)
    {
        this.x=other.x;
        this.y=other.y;
    }
    //equals
    public boolean equals(Point other) {
        return (x==other.x)&&(y==other.y);
    }
    public String toString() {
        return "["+this.x+","+this.y+"]";
    }
    //getters
    public int getX() {return this.x;}
    public int getY() {return this.y;}

    //Data members
    private final int x;
    private final int y;

}
