import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

/*
 * In my Ubuntu environment, when i click once, java receives twice click
 * and input twice click, so the program behave abnormaly
 */

public class KdTreeVisualtxt {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String filename = args[0];
        In in = new In(filename);

        StdDraw.show(0);

        // initialize the data structures with N points from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        
        StdDraw.clear();
        StdDraw.show(0);
        kdtree.draw();
        StdDraw.show(50);
    }

}
