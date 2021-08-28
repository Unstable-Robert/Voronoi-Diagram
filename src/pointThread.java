
import java.util.ArrayList;
import java.util.List;

/**
 * PointThread
 * Assigns each dot within the assigned region to a Point
 * Dots are assigned to the closest Point
 */
public class pointThread implements Runnable {
    private int startX;
    private int endX;
    private int startY;
    private int endY;
    private List<Point> list;
    private VoronoiDiagram jPanel;
    private long startTime;

    public pointThread(int startX, int endX, int startY, int endY, List<Point> pnt, VoronoiDiagram panel) {
        this.startTime = System.nanoTime();
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.list = pnt;
        this.jPanel = panel;
    }

    @Override
    public void run() {
        sortPoints();
        jPanel.repaint();
    }

    /**
     * Sorts Dots by finding the closest to point to it
     */
    private void sortPoints(){
        ArrayList<Double> distances = new ArrayList<>();
//        System.out.println("Starting the thing");
        for (int x = startX; x < endX; x ++) {
            for (int y = startY; y < endY; y++) {
                Dots dot = new Dots(x, y);
                for (int z = 0; z < list.size(); z++) {
                    distances.add(getDistance(dot.getX(), dot.getY(), list.get(z).getX(), list.get(z).getY()));
                }
                int index = findSmallestValue(distances);
                distances = new ArrayList<>();
                list.get(index).addDot(dot);
            }
        }
        long endTime = System.nanoTime();
        long dur = (endTime - startTime) / 1000000;
//        System.out.println( name() + "ThreadEnd:  " + dur);
    }

    private double getDistance(int x1, int y1, int x2, int y2) {
        double val = Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2);
        return Math.sqrt(val);
    }
    private int findSmallestValue(ArrayList<Double> list) {
        double min = list.get(0);
        for (int x = 0; x < list.size(); x ++) {
            if (list.get(x) < min) {
                min = list.get(x);
            }
        }
        for (int y = 0; y < list.size(); y ++) {
            if (list.get(y) == min) return y;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "pointThread{" +
                "startX=" + startX +
                ", endX=" + endX +
                ", startY=" + startY +
                ", endY=" + endY +
                ", points=" + list +
                '}';
    }
    public String name() {
        return "pointThread{" +
                "startX=" + startX +
                ", endX=" + endX +
                ", startY=" + startY +
                ", endY=" + endY +
                '}';
    }

}
