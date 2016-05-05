import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root; //the top node
    private int N;  // the number of Nodes
    private class Node {
        private final Point2D point;
        private final boolean cycle; //true compares left, false compares right
        private Node left, right;
       
        public Node (Point2D p, boolean d){
            point = p;
            cycle = d;
            left = null;
            right = null;
        }      
    }
    
    public KdTree() {
       N = 0;
    }
    
    public boolean isEmpty() {
       return N == 0;
    }

    public int size() {
        return N;
    }
    
    // the second insert simulates the BST code of package algs4
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("null pointer!");   
        
        root = insert(root, p, true);  // true is for initialize
    }
    // p: point x: node c: cycle of parent
    private Node insert(Node n, Point2D p, boolean c) {
        if (n == null) {
            N++;
            return new Node(p, c);        
        }
        int cmp;
        if (n.cycle) cmp = Point2D.X_ORDER.compare(p, n.point);
        else         cmp = Point2D.Y_ORDER.compare(p, n.point);    
        if (cmp < 0)    //less 
            n.left  = insert(n.left, p,  !n.cycle);  
        else            //greater or equal 
            n.right = insert(n.right, p, !n.cycle);          
        return n;        
    }
    
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("null pointer!");         
            
        Node n = root;
        int cmp;
        while (n != null) {
            if(n.cycle) cmp = Point2D.X_ORDER.compare(p, n.point);
            else        cmp = Point2D.Y_ORDER.compare(p, n.point);
            if (cmp < 0)      n = n.left;
            else if (cmp > 0) n = n.right;
            else              return true;
        }    
        return false;
    }
    
    public void draw() {
        draw(null, root, 0.0, 1.0, 0.0, 1.0);
    }
    
    private void draw(Node nUp, Node n, double xmin, double xmax, 
            double ymin, double ymax) {
        //draw current node
        StdDraw.setPenRadius(.01);
        StdDraw.setPenColor(StdDraw.BLACK);        
        n.point.draw();
        
        //draw line
        StdDraw.setPenRadius();
        if (nUp ==null) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.point.x(), 0, n.point.x(), 1);
        }
        else if (n.cycle) {
            StdDraw.setPenColor(StdDraw.RED);
            if (n.point.y() < nUp.point.y())
                StdDraw.line(n.point.x(), ymin, n.point.x(), nUp.point.y());        
            else 
                StdDraw.line(n.point.x(), nUp.point.y(), n.point.x(), ymax); 
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            if (n.point.x() < nUp.point.x())
                StdDraw.line(xmin, n.point.y(), nUp.point.x(), n.point.y()); 
            else 
                StdDraw.line(nUp.point.x(), n.point.y(), xmax, n.point.y());                 
        }        
        //draw leaves
        if (n.left != null)  {
            if (n.cycle) draw(n, n.left,  xmin, n.point.x(), ymin, ymax);
            else         draw(n, n.left,  xmin, xmax, ymin, n.point.y());
        }
        if (n.right != null) {
            if (n.cycle) draw(n, n.right, n.point.x(), xmax, ymin, ymax);
            else         draw(n, n.right, xmin, xmax, n.point.y(), ymax);
        }
    }
    

    // <Node> is for the cycle field to draw
    private Iterable<Node> keys() {
        Queue<Node> q = new Queue<Node>();
        return keys(root, q);
    }
    
    private Iterable<Node> keys(Node n, Queue<Node> q) {
        if (n.left != null) keys(n.left, q); //forget n.left != null !!
        q.enqueue(n);
        if (n.right != null) keys(n.right, q);
        return q;
    }
        
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("null pointer!");
        
        
        return null;
    }
    
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("null pointer!");
                
        return null;
    }
    

    
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
