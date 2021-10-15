/*
  Authors: Neriya Zudi (ID:207073545)
  Email:  neriyazudi@Gmail.com
  Department of Computer Engineering - Assignment 1 - Advanced Object-Oriented Programming
*/
package Location;

public class Location {
    //Data members
    private final Point position;
    private final Size size;

    //ctros
    public Location() {
        this.position = new Point();
        this.size = new Size();
    }

    public Location(Point position, Size size) {
        this.position = position;
        this.size = size;
    }

    public Location(Location other) {
        this.position = other.getPosition();
        this.size = other.getSize();
    }

    //equals
    public boolean equals(Location other) {
        return (position == other.position) && (size == other.size);
    }

    @Override
    public String toString() {
        return "Location - " +
                "position:" + position.toString() +
                ", size=" + size.toString();
    }

    //getters
    public Point getPosition() {
        return this.position;
    }

    public Size getSize() {
        return this.size;
    }
}
