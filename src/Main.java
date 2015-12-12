import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private int width = 800;
    private int height = 822;
    public static void main(String[] args) {
        EventQueue.invokeLater(
                () -> new Main()
        );
    }

    public Main() {
        super ("Voronoi");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        add(new VoronoiDiagram());
    }
}
