
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import java.util.ArrayList;

/**
 *
 * @author bruceoutdoors
 */
public class PointSET {

    private final SET<Point2D> points;

    // construct an empty set of points 
    public PointSET() {
        points = new SET<>();
    }

    // is the set empty? 
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set 
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new java.lang.NullPointerException();
        }
        
        points.add(p);
    }

    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new java.lang.NullPointerException();
        }
        
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new java.lang.NullPointerException();
        }
        
        ArrayList<Point2D> inrange = new ArrayList<>();
        
        for (Point2D p : points) {
            if (rect.contains(p)) {
                inrange.add(p);
            }
        }
        
        return inrange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new java.lang.NullPointerException();
        }
        
        Point2D closest = null;
        double closestDist = 9999;
        
        for (Point2D pt : points) {
            double dist = pt.distanceSquaredTo(p);
            if (dist < closestDist) {
                closest = pt;
                closestDist = dist;
            }
        }
        
        return closest;
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
    }
}
