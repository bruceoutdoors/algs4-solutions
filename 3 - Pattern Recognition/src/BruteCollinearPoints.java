
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Quick;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;

/*
 * inputs that will work with BruteCollinearPoints: input8.txt and input40.txt
 */
/**
 *
 * @author bruceoutdoors
 */
public class BruteCollinearPoints {

    private LineSegment[] segmentsArr;

    public BruteCollinearPoints(Point[] points) // finds all line segments containing 4 points
    {
        if (points == null) {
            throw new java.lang.NullPointerException();
        }

        for (Point p : points) {
            if (p == null) {
                throw new java.lang.NullPointerException();
            }
        }

        ArrayList<LineSegment> segs = new ArrayList<>();
        Quick.sort(points);

        // all points must be unique:
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = 1; j < points.length; j++) {
                for (int k = 2; k < points.length; k++) {
                    for (int l = 3; l < points.length; l++) {
                        if (i < j && j < k && k < l) {
                            Point p1 = points[i];
                            Point p2 = points[j];
                            Point p3 = points[k];
                            Point p4 = points[l];

                            double s1 = p1.slopeTo(p2);
                            if (p1.slopeTo(p3) == s1
                                    && p1.slopeTo(p4) == s1) {
                                segs.add(new LineSegment(p1, p4));
                            }
                        }
                    }
                }
            }
        }

        segmentsArr = segs.toArray(new LineSegment[segs.size()]);
    }

    public int numberOfSegments() // the number of line segments
    {
        return segmentsArr.length;
    }

    public LineSegment[] segments() // the line segments
    {
        return segmentsArr;
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

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }
}
