import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root; //the top node
    private int N;  // the number of Nodes
    //private int cnt;
    private double min;
/*    public int getc() {
        return cnt;
    }*/
    
    private class Node {
        private final Point2D point;
        // cycle can be deleted!!!
        private final boolean cycle; //true compares left, false compares right
        private Node left, right;
       
        Node(Point2D p, boolean d) {
            point = p;
            cycle = d;
            left = null;
            right = null;
        }      
    }
    
    public KdTree() {
       //cnt = 0;
       min = Double.MAX_VALUE;
       N = 0;
       root = null;
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
        // can avoid contains() check to speed up
        if (!contains(p)) root = insert(root, p, true);  // true is for initialize
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
    
    // insert must be a recursion while contains can be a while loop
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("null pointer!");         

        if (N == 0) return false;    
        Node n = root;
        int cmp;
        while (n != null) {
            if (p.equals(n.point)) return true;
            if (n.cycle) cmp = Point2D.X_ORDER.compare(p, n.point);
            else         cmp = Point2D.Y_ORDER.compare(p, n.point);
            if (cmp < 0)  n = n.left;
            else          n = n.right;
        }    
        return false;
    }
    
    //it seems write two function drawH() and drawV() is better!!!
    public void draw() {
        if (N != 0)
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
        if (nUp == null) {
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
    
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("null pointer!");        
        Queue<Point2D> q = new Queue<Point2D>();    
        if (N != 0) // check if there is a node in tree
            range(q, rect, root,  0.0, 1.0, 0.0, 1.0);
        return q;
    }
    
    private void range(Queue<Point2D> q, RectHV rect, Node n, double xmin, 
            double xmax, double ymin, double ymax) {
        RectHV nR = new RectHV(xmin, ymin, xmax, ymax);
        if (!rect.intersects(nR)) return; 
        if (rect.contains(n.point)) q.enqueue(n.point);
        // go left
        if (n.left != null) {
            if (n.cycle) range(q, rect, n.left,  xmin, n.point.x(), ymin, ymax);
            else         range(q, rect, n.left,  xmin, xmax, ymin, n.point.y());
        }
        // go right
        if (n.right != null) {
            if (n.cycle) range(q, rect, n.right, n.point.x(), xmax, ymin, ymax);
            else         range(q, rect, n.right, xmin, xmax, n.point.y(), ymax);
        }

    }
    
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("null pointer!");
        if (N == 0) return null; // check if there is a node in tree
        min = Double.MAX_VALUE;
        Point2D pm = new Point2D(root.point.x(), root.point.y());
        pm = nearest(p, root, pm, 0, 1, 0, 1);    
        return pm;
    }
    
    private Point2D nearest(Point2D p, Node n, Point2D pm,  
            double xmin, double xmax, double ymin, double ymax) {
        
        // prune function 
        RectHV nR = new RectHV(xmin, ymin, xmax, ymax);
        if (min < nR.distanceSquaredTo(p)) return pm;   
        
        //cnt++;
        //refresh pm
        double mint = p.distanceSquaredTo(n.point);
        if (mint < min) {
            pm  = n.point;
            min = mint;
        }
                
        // recurse: first go to the side with closer distance
        int d = pointdist(p, n);
/*        if (d < 0) {
            if (n.left != null && n.cycle) {
                pm = nearest(p, n.left,  pm, xmin, n.point.x(), ymin, ymax);
                if
                pm = nearest(p, n.right, pm, n.point.x(), xmax, ymin, ymax);
            }
            else if (n.left != null) {
                pm = nearest(p, n.left,  pm, xmin, xmax, ymin, n.point.y());
                if
                pm = nearest(p, n.right, pm, xmin, xmax, n.point.y(), ymax);
            }
        }
        else {
            if (n.right != null && n.cycle) {
                pm = nearest(p, n.right, pm, n.point.x(), xmax, ymin, ymax);
                if
                pm = nearest(p, n.left,  pm, xmin, n.point.x(), ymin, ymax);
            }
            else if (n.right != null) {
                pm = nearest(p, n.right, pm, xmin, xmax, n.point.y(), ymax);
                if
                pm = nearest(p, n.left,  pm, xmin, xmax, ymin, n.point.y());
            }
        }
        */        
        double xTemp = Double.NaN;
        double yTemp = Double.NaN;

        if (d < 0) {
            if (n.left != null) {
                if (n.cycle) {
                    xTemp = n.point.x();
                    pm = nearest(p, n.left,  pm, xmin, xTemp, ymin, ymax);
                }
                else {        
                    yTemp = n.point.y();
                    pm = nearest(p, n.left,  pm, xmin, xmax, ymin, yTemp);
                }
            }
            if (n.right != null) {
                if (n.cycle) 
                    pm = nearest(p, n.right, pm, xTemp, xmax, ymin, ymax);
                else         
                    pm = nearest(p, n.right, pm, xmin, xmax, yTemp, ymax);
            }
        }
        else {
            if (n.right != null) {
                if (n.cycle) {
                    xTemp = n.point.x();
                    pm = nearest(p, n.right, pm, xTemp, xmax, ymin, ymax);
                }
                else {       
                    yTemp = n.point.y();
                    pm = nearest(p, n.right, pm, xmin, xmax, yTemp, ymax);
                }
            }
            if (n.left != null) {
                if (n.cycle) 
                    pm = nearest(p, n.left,  pm, xmin, xTemp, ymin, ymax);
                else         
                    pm = nearest(p, n.left,  pm, xmin, xmax, ymin, yTemp);
            }
        }
        return pm;
    }
    
    // when submitting , server throws no such method for distanceToOrder()
    private int pointdist(Point2D p, Node n) {
        if (n.left == null) return 1;
        else if (n.right == null) return -1;
        else {
            double dist1 = p.distanceSquaredTo(n.left.point);
            double dist2 = p.distanceSquaredTo(n.right.point);
            if      (dist1 < dist2) return -1;
            else if (dist1 > dist2) return +1;
            else                    return  0;
        }
    }
    
    // obsolete! draw don't use these function
    // <Node> is for the cycle field to draw
/*    private Iterable<Node> keys() {
        Queue<Node> q = new Queue<Node>();
        return keys(root, q);
    }
    
    private Iterable<Node> keys(Node n, Queue<Node> q) {
        if (n.left != null) keys(n.left, q); //forget n.left != null !!
        q.enqueue(n);
        if (n.right != null) keys(n.right, q);
        return q;
    }*/
    
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
