import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Robert on 11/4/15.
 */
public class Point{
    private int x;
    private int y;
    private ArrayList<Dots> dots;
    private ArrayList<Integer> xInts;
    private ArrayList<Integer> yInts;
    private Color color;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.dots = new ArrayList<>();
        this.xInts = new ArrayList<>();
        this.yInts = new ArrayList<>();
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
        xInts.add(d.getX());
        yInts.add(d.getY());
        dots.add(d);
    }

    public ArrayList<Dots> getDots() {
        return dots;
    }
    public void resetPoints() {

        this.dots = new ArrayList<>();
    }

    private int[] getYVals() {
        ArrayList<Integer> vals = new ArrayList<>();
        int prev = -1;
        for (int x = 0; x < dots.size(); x ++) {
            if (dots.get(x).getY() != prev) {
                vals.add(dots.get(x).getY());
            }
            prev = dots.get(x).getY();
        }
        int[] yss = new int[vals.size()];
        for (int y = 0; y < vals.size(); y++) {
                yss[y] = vals.get(y);
        }
        return yss;
    }
    public ArrayList<Dots> getImportantPoints() {
        ArrayList<Dots> impo = new ArrayList<>();
        int[] yVals = getYVals();
        System.out.println("yVals: " + yVals.length);
        for (int y = 0; y < yVals.length; y++) {
            ArrayList<Dots> dy = getPointsWithY(yVals[y]);
            int min = dy.get(0).getX();
            int max = dy.get(0).getX();
            for (int x = 0; x < dy.size(); x++) {
                if (dy.get(x).getX() > max) max = dy.get(x).getX();
                if (dy.get(x).getX() < min) min = dy.get(x).getX();
            }
            for (int z = 0; z < dots.size(); z++) {
                Dots d = dots.get(z);
                if ((d.getY() == yVals[y]) && ((d.getX() == min) || (d.getX() == max))) {
                    impo.add(d);
                }
            }
        }
        return impo;
    }
    public Shape createShape() {
        ArrayList<Dots> list = getImportantPoints();
        ArrayList<Dots> sl;
        sl = sortPoints(list);
        Path2D p2d = new Path2D.Float();
        p2d.moveTo(sl.get(0).getX(), sl.get(0).getY());
        int prevY = sl.get(0).getY();
        for (int x = 1; x < sl.size(); x ++) {
            if (prevY != sl.get(x).getY()) p2d.lineTo(sl.get(x).getX(), sl.get(x).getY());
            else p2d.moveTo(sl.get(x).getX(), sl.get(x).getY());
            prevY = sl.get(x).getY();
        }
        p2d.closePath();
        resetPoints();
        return p2d.createTransformedShape(null);
    }
    private ArrayList<Dots> sortPoints( ArrayList<Dots> list) {
        ArrayList<Dots> sorted = new ArrayList<>();
        sorted.add(list.remove(0));
        while (list.size() > 0) {
            int index = findNearestIndex(sorted.get(sorted.size()-1), list);
            sorted.add(list.remove(index));
        }
        return sorted;
    }
    private int findNearestIndex (Dots thisPoint, ArrayList<Dots> listToSearch) {
        double nearestDistSquared=Double.POSITIVE_INFINITY;
        int nearestIndex = -1;
        for (int i=0; i< listToSearch.size(); i++) {
            Dots point2 = listToSearch.get(i);
            double distsq = getDistance(thisPoint.getX(), thisPoint.getY(), point2.getX(), point2.getY());
            if(distsq < nearestDistSquared) {
                nearestDistSquared = distsq;
                nearestIndex=i;
            }
        }
        return nearestIndex;
    }

    private double getDistance(int x1, int y1, int x2, int y2) {
        double val = Math.pow((x1 + x2), 2) + Math.pow((y1 + y2), 2);
        return Math.sqrt(val);
    }
    private ArrayList<Dots> getPointsWithY(int y) {
        ArrayList<Dots> dd = new ArrayList<>();
        for (Dots d : dots) {
            if (d.getY() == y) dd.add(d);
        }
        return dd;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
