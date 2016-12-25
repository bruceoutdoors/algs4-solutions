
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

/**
 *
 * @author bruceoutdoors
 */
public class KdTree {

    private static class Node {

        public static final Boolean VERTICAL = false;
        public static final Boolean HORIZONTAL = true;

        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb = null;        // the left/bottom subtree
        private Node rt = null;        // the right/top subtree
        private Boolean orientation = VERTICAL;
    }

    private int size = 0;
    private Node root = null;

    // construct an empty set of points 
    public KdTree() {
    }

    // is the set empty? 
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set 
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new java.lang.NullPointerException();
        }

        size++;

        if (root == null) {
            root = new Node();
            root.p = p;
            root.rect = new RectHV(0, 0, 1, 1);
            root.orientation = Node.VERTICAL;
            return;
        }

        appendToNode(root, p);

    }

    private void appendToNode(Node nd, Point2D p) {
        if (nd.orientation == Node.VERTICAL) {
            // check point to left or right:
            double diff = p.x() - nd.p.x();
            if (diff >= 0) { // right
                if (nd.rt == null) {
                    Node child = new Node();
                    child.p = p;
                    child.orientation = Node.HORIZONTAL;
                    child.rect = new RectHV(nd.p.x(), nd.rect.ymin(), nd.rect.xmax(), nd.rect.ymax());

                    nd.rt = child;
                } else {
                    appendToNode(nd.rt, p);
                }
            } else // left
            {
                if (nd.lb == null) {
                    Node child = new Node();
                    child.p = p;
                    child.orientation = Node.HORIZONTAL;
                    child.rect = new RectHV(nd.rect.xmin(), nd.rect.ymin(), nd.p.x(), nd.rect.ymax());

                    nd.lb = child;
                } else {
                    appendToNode(nd.lb, p);
                }
            }
        } else {
            // check point to top or bottom:
            double diff = p.y() - nd.p.y();
            if (diff >= 0) { // top
                if (nd.rt == null) {
                    Node child = new Node();
                    child.p = p;
                    child.orientation = Node.VERTICAL;
                    child.rect = new RectHV(nd.rect.xmin(), nd.p.y(), nd.rect.xmax(), nd.rect.ymax());

                    nd.rt = child;
                } else {
                    appendToNode(nd.rt, p);
                }
            } else // bottom
            {
                if (nd.lb == null) {
                    Node child = new Node();
                    child.p = p;
                    child.orientation = Node.VERTICAL;
                    child.rect = new RectHV(nd.rect.xmin(), nd.rect.ymin(), nd.rect.xmax(), nd.p.y());

                    nd.lb = child;
                } else {
                    appendToNode(nd.lb, p);
                }
            }
        }
    }

    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new java.lang.NullPointerException();
        }

        return contains(root, p);
    }

    private boolean contains(Node nd, Point2D p) {
        if (nd.p.equals(p)) {
            return true;
        } else if (contains(nd.lb, p)) {
            return true;
        } else {
            return contains(nd.rt, p);
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node nd) {
        if (nd.orientation == Node.HORIZONTAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(nd.rect.xmin(), nd.p.y(), nd.rect.xmax(), nd.p.y());
        } else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(nd.p.x(), nd.rect.ymin(), nd.p.x(), nd.rect.ymax());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        nd.p.draw();

        // recursively draw the left and right nodes:
        if (nd.lb != null) {
            draw(nd.lb);
        }

        if (nd.rt != null) {
            draw(nd.rt);
        }
    }

    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new java.lang.NullPointerException();
        }

        ArrayList<Point2D> pointList = new ArrayList<>();

        if (root != null) {
            range(root, pointList, rect);
        }

        return pointList;
    }

    private void range(Node nd, ArrayList<Point2D> pointList, RectHV rect) {
        if (rect.contains(nd.p)) {
            pointList.add(nd.p);
        }

        // recursively search left/bottom
        if (nd.lb != null && rect.intersects(nd.lb.rect)) {
            range(nd.lb, pointList, rect);
        }

        // recursively search right/top
        if (nd.rt != null && rect.intersects(nd.rt.rect)) {
            range(nd.rt, pointList, rect);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new java.lang.NullPointerException();
        }

        if (root == null) {
            return null;
        }

        // at start, champion is always the first point
        return nearest(root, root.p, root.p.distanceSquaredTo(p), p);
    }

    private Point2D auxNearest(Node nd, Point2D champion, double minDist, Point2D p) {
        if (nd == null) {
            return champion;
        } else {
            double distSq = nd.rect.distanceSquaredTo(p);
            if (distSq > minDist) {
                // exclude from search if rectangle distance is
                // further from minimum distance
                return champion;
            }
            
            Point2D chp = champion;
            double mdist = minDist;
            double dist = nd.p.distanceSquaredTo(p);
            if (dist < minDist) {
                chp = nd.p;
                mdist = dist;
            }
            return nearest(nd, chp, mdist, p);
        }
    }

    private Point2D nearest(Node nd, Point2D champion, double minDist, Point2D p) {
        if (nd.orientation == Node.VERTICAL) {
            // check point to left or right:
            double diff = p.x() - nd.p.x();
            if (diff >= 0) { // right
                return auxNearest(nd.rt, champion, minDist, p);
            } else // left
            {
                return auxNearest(nd.lb, champion, minDist, p);
            }
        } else {
            // check point to top or bottom:
            double diff = p.y() - nd.p.y();
            if (diff >= 0) { // top
                return auxNearest(nd.rt, champion, minDist, p);
            } else // bottom
            {
                return auxNearest(nd.lb, champion, minDist, p);
            }
        }
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
    }
}
