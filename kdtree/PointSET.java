import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import java.util.Iterator;

public class PointSET {
    
    private final SET<Point2D> pointSet;
    
    /**
     *  construct an empty set of points 
     */
    public PointSET() {
        pointSet = new SET<Point2D>();
    }
    
    /**
     * is the set empty?
     */
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }
    
    /**
     * number of points in the set 
     */
    public int size() {
        return pointSet.size();
    }
    
    /**
     * add the point to the set (if it is not already in the set)
     */
    public void insert(Point2D p) {
        if (!contains(p)) {
            pointSet.add(p);
        }
    }
    
    /**
     * does the set contain point p? 
     */
    public boolean contains(Point2D p) {
        if (p == null)  throw new java.lang.IllegalArgumentException();
        return pointSet.contains(p);
    }
    
    /**
     * draw all points to standard draw 
     */
    public void draw() {
        for (Iterator<Point2D> it = pointSet.iterator(); it.hasNext();) {
            Point2D p = it.next();
            p.draw();
        }
    }
    
    /**
     * all points that are inside the rectangle (or on the boundary) 
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)  throw new java.lang.IllegalArgumentException();
        Queue<Point2D> queue = new Queue<Point2D>();
        if (!isEmpty()) {
            for (Iterator<Point2D> it = pointSet.iterator(); it.hasNext();) {
                Point2D p = it.next();
                if (rect.contains(p)) queue.enqueue(p);
            }
        }
        return queue;
    }
    
    /**
     * a nearest neighbor in the set to point p; null if the set is empty 
     */
    public Point2D nearest(Point2D p) {
        if (p == null)  throw new java.lang.IllegalArgumentException();
        if (isEmpty()) return null;
        double minDistance =  Double.POSITIVE_INFINITY;
        Point2D result = null;
        for (Iterator<Point2D> it = pointSet.iterator(); it.hasNext();) {
              Point2D p1 = it.next();
              double distance = p1.distanceSquaredTo(p);
              if (distance < minDistance) {
                  result = p1;
                  minDistance = distance;
              }
       }
        return result;
    }
    
    public static void main(String[] args) {
        
    }
}