import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> s; //a red black tree to store elements
    
    public PointSET() {
        s = new SET<Point2D>();
    }
    
    public boolean isEmpty() {
        return s.isEmpty();
    }

    public int size() {
        return s.size();
    }
    
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("null pointer!");        
        s.add(p);                
    }
    
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("null pointer!");
        return s.contains(p);
    }
    
    public void draw() {
        for (Point2D p : s) p.draw();
    }
        
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("null pointer!");
        
        Queue<Point2D> q = new Queue<Point2D>();        
        for (Point2D p : s) {
            if (rect.contains(p)) 
                q.enqueue(p);
        }
        
        return q;
    }
    
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("null pointer!");
        
        double min = Double.MAX_VALUE; 
        Point2D pmin = null; //must initialize!
        for (Point2D p1 : s) {
            if (p1.distanceTo(p) < min) {
                min = p1.distanceTo(p);
                pmin = p1;
            }
        }
        
        return pmin;
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
