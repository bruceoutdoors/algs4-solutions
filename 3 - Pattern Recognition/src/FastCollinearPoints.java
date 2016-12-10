
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.Quick;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bruceoutdoors
 */
public class FastCollinearPoints {

    private LineSegment[] segmentsArr;

    public FastCollinearPoints(Point[] points) // finds all line segments containing 4 or more points
    {
        if (points == null) {
            throw new java.lang.NullPointerException();
        }

        for (Point p : points) {
            if (p == null) {
                throw new java.lang.NullPointerException();
            }
        }
        
        Point[] pointsCopy = points.clone();

        ArrayList<LineSegment> segs = new ArrayList<>();
        Quick.sort(pointsCopy);

        // all points must be unique:
        for (int i = 0; i < pointsCopy.length - 1; i++) {
            if (pointsCopy[i].compareTo(pointsCopy[i + 1]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        HashMap<Point, ArrayList<Double>> pointToSlopes = new HashMap<>();

        for (int i = 0; i < pointsCopy.length - 1; i++) {
            Point origin = pointsCopy[i];
            ArrayList<Point> pts = new ArrayList<>();

            for (int j = i + 1; j < pointsCopy.length; j++) {
                pts.add(pointsCopy[j]);
            }

            Point[] ptsArr = pts.toArray(new Point[pts.size()]);
            MergeX.sort(ptsArr, origin.slopeOrder());

            /* print all slopes
            for (int j = 0; j < ptsArr.length; j++) {
                Double s1 = origin.slopeTo(ptsArr[j]);               
                StdOut.println(s1);
            } StdOut.println("===");
             */
            for (int j = 0; j < ptsArr.length - 1; j++) {
                double s1 = origin.slopeTo(ptsArr[j]);
                Point endPoint = null;
                int counter = 1;

                for (int k = j + 1; k < ptsArr.length; k++) {
                    double s2 = origin.slopeTo(ptsArr[k]);

                    if (Objects.equals(s1, s2)
                            || Math.abs(s1 - s2) < 0.000001) {
                        counter++;
                        if (counter >= 3) {
                            endPoint = ptsArr[k];
                            j = k;
                        }
                    } else {
                        break;
                    }
                }

                if (endPoint != null) {
                    // ensure that you only have the maximal line
                    if (pointToSlopes.containsKey(endPoint)) {
                        ArrayList<Double> slopes = pointToSlopes.get(endPoint);
                        if (slopes.contains(s1)) {
                            continue;
                        } else {
                            slopes.add(s1);
                        }
                    } else {
                        pointToSlopes.put(endPoint, new ArrayList<>());
                        pointToSlopes.get(endPoint).add(s1);
                    }

                    segs.add(new LineSegment(origin, endPoint));
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
        return segmentsArr.clone();
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }
}
