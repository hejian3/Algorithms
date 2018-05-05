/*************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Execution:    none
 *  Dependencies: Point.java LineSegment.java
 *
 *
 *************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class FastCollinearPoints {

    private final LineSegment[] lineSegment;
    private int lineSegmentCount;
    
    /**
     * finds all line segments containing 4 points
     * Throw a java.lang.IllegalArgumentException 
     * if the argument to the constructor is null, 
     * if any point in the array is null, 
     * or if the argument to the constructor contains a repeated point
     * 
     */
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("array points is null");
        }
        
        Point [] p = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) 
                throw new IllegalArgumentException("array points is null");
            p[i] = points[i];
        }
        
        Arrays.sort(p);
        
        for (int i = 0; i < p.length -1; i++) {
            if (p[i].compareTo(p[i+1]) == 0) {
                throw new IllegalArgumentException("array points is null");
            }
        }
        lineSegment = getLineSegment(p).clone();
    }
    
    private LineSegment[] getLineSegment(Point[] points) {
        LineSegment[] result = null;
        Point [] a = new Point[points.length];
        Point [] aux = new Point[points.length];
        double [] p = new double[points.length];
        
        for (int i = 0; i < points.length - 3; i++) {
             Point origin = points[i];
             for (int k = 0; k < points.length; k++) {
                   a[k] = points[k];
              }
             sort(origin, a, aux, i+1, a.length-1); 
            for (int k = i + 1; k < a.length; k++) {
                p[k] = origin.slopeTo(a[k]);
            }
        
            for (int k = i + 1; k < p.length - 2; k++) {
                if (Double.compare(p[k], p[k + 2]) == 0) {
                    int last = k + 2;
                    for (int n = k + 3; n < p.length; n++) {
                        if (Double.compare(p[n], p[k]) != 0) {
                            last = n - 1;
                            break;
                        }
                        else {
                            last = n;
                        }
                    }
       
                     Point[] temp = new Point[last - k +2];
                     for (int n = 0; n < temp.length - 1; n++) {
                         temp[n] = a[k+n];
                     }
                     temp[temp.length-1] = origin;
                     Arrays.sort(temp);
                     if (result == null) {
                         result = new LineSegment[2];         
                     }
                   
                     int start = getPointIndex(points, temp[0]);
                     int end  = getPointIndex(points, temp[temp.length-1]);
                     if (end - start +1 == temp.length) {
                         i = end -1;
                     }
                     k = last;
                     result[lineSegmentCount++] 
                         = new LineSegment(temp[0], temp[temp.length-1]);
                     if (result.length == lineSegmentCount) {
                        result = resizeArr(result, result.length*2);
                    }
                }
            }
        }
        result = resizeArr(result, lineSegmentCount);
        return result;
    }
    
    private int getPointIndex(Point[] points, Point p) {
        for (int i = 0; i < points.length; i++) {
            if (p == points[i]) return i;
        }
        return -1;
    }
    
    private LineSegment[] resizeArr(LineSegment[] temp, int size) {
        if (temp == null) return new LineSegment[0];
        if (temp.length == size) return temp;
        LineSegment [] result =  new LineSegment[size];
        int arrLength = Math.min(temp.length, size);
        for (int i = 0; i <  arrLength; i++) {
            result[i] = temp[i];
        }
        return result;
    }
    
    private static void sort(Point origin, Point[] a, Point[] aux, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(origin, a, aux, lo, mid);
        sort(origin, a, aux, mid+1, hi);
        merge(origin, a, aux, lo, mid, hi);
    }
    
    private static void merge(Point origin, Point[] a, 
                              Point[] aux, int lo, int mid, int hi) {     
        assert isSorted(origin, a, lo, mid); // precondition: a[lo..mid] sorted
        assert isSorted(origin, a, mid+1, hi); // precondition: a[mid+1..hi] sorted
        
        for (int k = lo; k <= hi; k++) aux[k] = a[k];
        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)                  a[k] = aux[j++];
            else if (j > hi)              a[k] = aux[i++];
            else if (less(origin, aux[j], aux[i]))  a[k] = aux[j++];
            else                           a[k] = aux[i++];
        }
        assert isSorted(origin, a, lo, hi); // postcondition: a[lo..hi] sorted
    }
    
    private static boolean isSorted(Point origin, Point[] a, int lo, int hi) {
        for (int i = lo; i < hi; i++) {
            if (less(origin, a[i+1], a[i])) return false;
        }
        return true;
    }
    
    private static boolean less(Point origin, Point p1, Point p2) {
        return origin.slopeOrder().compare(p1, p2) == -1;
    }
    
    /**
     * the number of line segments
     */
    public int numberOfSegments() {
        return lineSegmentCount;
    }
    
    /**
     * the line segments
     */
    public LineSegment[] segments() {
        return lineSegment;
    }
    
    public static void main(String[] args) {
       // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}