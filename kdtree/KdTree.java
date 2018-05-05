import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class  KdTree {
    
    private static final boolean RED = true;
    private static final boolean BLUE = false;

    private Node root;
    private Point2D nearest;
    private double nearestDistances;
    private int size;

    private class Node {
        Point2D p;
        RectHV rect;
        Node left, right;
        boolean color;
    }
    
    /**
     *  construct an empty set of points
     */
    public KdTree() {
        size = 0;
    }
    
    /**
     * is the set empty?
     */
    public boolean isEmpty() {
        return size() == 0;
    }
    
    /**
     * number of points in the set 
     */
    public int size() {
        return size;
    }
    
    /**
     * add the point to the set (if it is not already in the set)
     */
    public void insert(Point2D p) {
        if (contains(p)) return;
        Node node = new Node();
        node.p = p;
        if (isEmpty()) {
            node.color = RED;
            node.rect = new RectHV(0, 0, 1, 1);
            root = node;
        } 
        else {
            Node n = root;
            while (true) {
                if (n.color) {
                    if (p.x() > n.p.x()) {
                        if (n.right == null) {
                            n.right = node;
                            node.rect = new RectHV(n.p.x(), n.rect.ymin(),
                                    n.rect.xmax(), n.rect.ymax());
                            node.color = BLUE;
                            break;
                        }
                        else n = n.right;
                    }
                    else {
                        if (n.left == null) {
                            n.left = node;
                            node.rect = new RectHV(n.rect.xmin(), n.rect.ymin(),
                                    n.p.x(), n.rect.ymax());
                            node.color = BLUE;
                            break;
                        }
                        else n = n.left;
                    }
                } 
                else {
                    if (p.y() > n.p.y()) {
                        if (n.right == null) {
                            n.right = node;
                            node.rect = new RectHV(n.rect.xmin(), n.p.y(),
                                    n.rect.xmax(), n.rect.ymax());
                            node.color = RED;
                            break;
                        }
                        n = n.right;
                    }
                    else {
                        if (n.left == null) {
                            n.left = node;
                            node.rect = new RectHV(n.rect.xmin(), n.rect.ymin(),
                                    n.rect.xmax(), n.p.y());
                            node.color = RED;
                            break;
                        }
                        n = n.left;
                    }
                }
            }
        }       
         size++;
    }
    
    /**
     * does the set contain point p? 
     */
    public boolean contains(Point2D p) {
        if (p == null)  throw new java.lang.IllegalArgumentException();
        if (isEmpty()) return false;
        Node n = root;
        while (true) {
            if (n.p.equals(p)) return true;
            if (n.color) {
                if (p.x() > n.p.x()) {
                    if (n.right == null) break;
                    else n = n.right;
                }
                else {
                    if (n.left == null) break;
                    else n = n.left;
                } 
            }
            else {
                if (p.y() > n.p.y()) {
                    if (n.right == null) break;
                    else n = n.right;
                }
                else {
                    if (n.left == null) break;
                    else  n = n.left;
                }
            }
        }
        return false;
    }
    
    /**
     * draw all points to standard draw 
     */
    public void draw() {
       if (isEmpty()) return;
       drawPoint(root, null);
    }
    
    private void drawPoint(Node n, Node parent) {
        if (n == null) return;
        if (n.color) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.01);
            if (parent == null) {
                StdDraw.line(n.p.x(), 0, n.p.x(), 1);
            }
            else {
                if (parent.left == n) StdDraw.line(n.p.x(), n.rect.ymin(),
                        n.p.x(), n.rect.ymax());
                else StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
            }
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.01);
            if (parent == null) {
                StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
            }
            else {
                if (parent.left == n) StdDraw.line(n.rect.xmin(), n.p.y(),
                        n.rect.xmax(), n.p.y());
                else StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
            }
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        n.p.draw();
        drawPoint(n.left, n);
        drawPoint(n.right, n);
    }

    /**
     * all points that are inside the rectangle (or on the boundary) 
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.IllegalArgumentException();
        Queue<Point2D> queue = new Queue<Point2D>();
        if (!isEmpty()) rangeChildren(rect, root, queue);
        return queue;
    }
    
    private void rangeChildren(RectHV rect, Node node, Queue<Point2D> queue) {
        if (node == null) return;
        if (rect.intersects(node.rect)) {
            if (rect.contains(node.p)) queue.enqueue(node.p);
            rangeChildren(rect, node.left, queue);
            rangeChildren(rect, node.right, queue);
        }
    }
    
    /**
     * a nearest neighbor in the set to point p; null if the set is empty 
     */
    public Point2D nearest(Point2D p) {
        if (p == null)  throw new java.lang.IllegalArgumentException();
        if (isEmpty()) return null;
        nearest = root.p;
        nearestDistances = root.p.distanceSquaredTo(p);
        searchChildren(root.left, p);
        searchChildren(root.right, p);
        return nearest;
    }
    
    private void searchChildren(Node node, Point2D p) {
        if (node == null) return;
        if (node.rect.distanceSquaredTo(p) < nearestDistances) {
            double d = node.p.distanceSquaredTo(p);
            if (d < nearestDistances) {
                nearest = node.p;
                nearestDistances = d;
            }
            searchChildren(node.left, p);
            searchChildren(node.right, p);
        }
    }

    public static void main(String[] args) {
        KdTree k = new KdTree();
        k.insert(new Point2D(0.7, 0.2));
        k.insert(new Point2D(0.5, 0.4));
        k.insert(new Point2D(0.2, 0.3));
        k.insert(new Point2D(0.4, 0.7));
        k.insert(new Point2D(0.9, 0.6));
        k.draw();
        StdOut.println(k.nearest(new Point2D(0.348, 0.386)));

    }
}