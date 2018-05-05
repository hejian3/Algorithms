/*************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    none
 *  Dependencies: Point.java LineSegment.java
 *
 *
 *************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class BruteCollinearPoints {
   
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
    public BruteCollinearPoints(Point[] points) {
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
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i+1; j < points.length - 2; j++) {
                double slope = points[i].slopeTo(points[j]);
                for (int k = j+1; k < points.length - 1; k++) {
                    if (points[i].slopeTo(points[k]) == slope) {
                        for (int n = k+1; n < points.length; n++) {
                            if (points[i].slopeTo(points[n]) == slope) {
                                if (result == null) result = new LineSegment[2];
                                result[lineSegmentCount++]  
                                    = new LineSegment(points[i], points[n]);
                                if (lineSegmentCount == result.length) {
                                    result =  resizeArr(result, result.length * 2);
                                }      
                                break;
                            }
                        }
                    }
                }
            }
        }
        result = resizeArr(result, lineSegmentCount);
        return result;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}