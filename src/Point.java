import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Point
 * Points of user defined and what Dots are sorted by
 * Each point is assigned a random Color
 */
public class Point{
    private int x;
    private int y;
    private ArrayList<Dots> dots;
    private Color color;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.dots = new ArrayList<>();
        Random rn = new Random();
        int max = 255;
        int min = 0;
        int r = rn.nextInt(max - min + 1) + min;
        int g = rn.nextInt(max - min + 1) + min;
        int b = rn.nextInt(max - min + 1) + min;
        this.color = new Color(r, g ,b);
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public Ellipse2D getShape() {
        return new Ellipse2D.Double(this.x, this.y, 5, 5);
    }
    public synchronized void addDot(Dots d) {
            dots.add(d);
    }

    public void addDots(Point p) {
        System.out.println("Point: " + p.getDots().size());
        dots.addAll(p.getDots());
    }

    public ArrayList<Dots> getDots() {
//        System.out.println(dots);
        return dots;
    }
    public void resetPoints() {

        this.dots = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
