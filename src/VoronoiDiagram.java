
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main JPanel
 * Handles coloring of the screen and points by setting the pixels of a BufferedImage that is the same size as
 * the JPanel
 */
@SuppressWarnings("Duplicates")
public class VoronoiDiagram extends JPanel implements MouseListener {
    private static final long serialVersionUID = 1L;
    private ArrayList<Point> points;
    private List<Point> list;
    private ExecutorService executorService;
    private static int numThreads = 8;

    public VoronoiDiagram() {
        this.executorService = Executors.newFixedThreadPool(numThreads);
        this.points = new ArrayList<>();
        this.list = Collections.synchronizedList(new ArrayList<>());
        addMouseListener(this);
    }

    /**
     * Goes through one pixel at a time comparing it to each dot(Pixel) currently in the view
     * Once the dot has been compared to each point, the closest dot is determined by the distances calculated
     * The point with the shortest distance has to the dot has it added to its list of dots
     */
    private void findShapes() {
        ArrayList<Double> distances = new ArrayList<>();
        for (int yy = 0; yy < getHeight(); yy++) {
            for (int xx = 0; xx < getWidth(); xx++) {
                Dots dot = new Dots(xx , yy );
                for (int x = 0; x < points.size(); x++) {
                    distances.add(getDistance(dot.getX(), dot.getY(), points.get(x).getX(), points.get(x).getY()));
                }
                int index = findSmallestValue(distances);
                distances = new ArrayList<>();
                points.get(index).addDot(dot);
            }
        }
    }

    /**
     * Breaks up the dots(Pixels) currently in view
     * Assigns each set up points to a thread and adds it to a Executor Service
     * Each thread is tasked with assigning each Dot to the user defined points
     */
    private void sortDotsMultiThreaded() {
//        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
//        long startTime = System.nanoTime();
        for (int x = 0; x < 4; x ++) {
            for (int y = 0; y < 4; y ++) {
                pointThread pt = new pointThread(
                                                    (x * (getWidth()/4)),
                                                    ((x + 1) * (getWidth()/4)),
                                                    (y * (getHeight()/4)),
                                                    ((y + 1) * (getHeight()/4)),
                                                    this.list,
                                                    this
                                                );
                executorService.execute(pt);
            }
        }
//        executorService.shutdown();
//        while (!executorService.isTerminated());
//        long endTime = System.nanoTime();
//        long dur = (endTime - startTime) / 1000000;
//        System.out.println("Dur FindShape:  " + dur);
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

    /**
     * Creates a buffered image with the dots sorted in sortDotsMultiThreaded
     * by assigning each pixel under a Point to a unique color
     * Plan on replacing this with Chan's Algorithm for Convex Hulls
     * @return colored image
     */
    private BufferedImage getColoredImage() {
        long startTime = System.nanoTime();
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < list.size(); x ++) {
                for (int z = 0; z < list.get(x).getDots().size(); z++) {
                    int xx = list.get(x).getDots().get(z).getX();
                    int yy = list.get(x).getDots().get(z).getY();
                    bi.setRGB(xx, yy, list.get(x).getColor().getRGB());
                }
            }
        long endTime = System.nanoTime();
        long dur = (endTime - startTime) / 1000000;
        System.out.println("GetImage Time:  " + dur);
        return bi;
    }

    @Override
    protected void paintComponent(Graphics g) {
        long startTime = System.nanoTime();
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setPaint(Color.black);
        g2d.drawImage(getColoredImage(), null, 0, 0);
            for (int x = 0; x < list.size(); x++) {
                g2d.fill(list.get(x).getShape());
            }
        long endTime = System.nanoTime();
        long dur = (endTime - startTime) / 1000000;
        System.out.println("PaintComponent Time:  " + dur);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        Point pnt = new Point(e.getX(), e.getY());
            list.add(pnt);
            if (list.size() > 0) sortDotsMultiThreaded();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
