package com.csc205.project1;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Represents a line segment in three-dimensional space defined by two endpoints.
 * This class provides geometric operations such as calculating length, checking
 * parallelism, and finding the shortest distance between lines.
 * 
 * <p>The Line3D class demonstrates several object-oriented design patterns:
 * <ul>
 *   <li><strong>Value Object Pattern:</strong> Instances are immutable after construction,
 *       ensuring thread-safety and predictable behavior</li>
 *   <li><strong>Builder Pattern (implicit):</strong> Construction requires two points,
 *       ensuring a line is always in a valid state</li>
 *   <li><strong>Strategy Pattern (potential):</strong> Distance calculations could be
 *       extended to support different metrics (Euclidean, Manhattan, etc.)</li>
 * </ul>
 * 
 * <p>Foundational Principles Demonstrated:
 * <ul>
 *   <li><strong>Encapsulation:</strong> Internal state (points) is private and immutable</li>
 *   <li><strong>Composition:</strong> Line3D is composed of Point3D objects</li>
 *   <li><strong>Defensive Programming:</strong> Null checks and validation throughout</li>
 *   <li><strong>Single Responsibility:</strong> Each method has one clear purpose</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>
 * Point3D p1 = new Point3D(0.0, 0.0, 0.0);
 * Point3D p2 = new Point3D(3.0, 4.0, 0.0);
 * Line3D line1 = new Line3D(p1, p2);
 * 
 * double length = line1.length();
 * System.out.println("Line length: " + length); // 5.0
 * 
 * Point3D p3 = new Point3D(0.0, 0.0, 1.0);
 * Point3D p4 = new Point3D(3.0, 4.0, 1.0);
 * Line3D line2 = new Line3D(p3, p4);
 * 
 * double distance = line1.shortestDistanceBetweenLines(line2);
 * System.out.println("Distance between lines: " + distance); // 1.0
 * </pre>
 *
 * @author Generated
 * @version 1.0
 */
public class Line3D {
    
    private static final Logger logger = Logger.getLogger(Line3D.class.getName());
    
    // Tolerance for floating-point comparisons
    private static final double EPSILON = 1e-10;
    
    private final Point3D startPoint;
    private final Point3D endPoint;
    
    /**
     * Constructs a new Line3D from two points in 3D space.
     * 
     * <p>This constructor enforces the invariant that a line must have two
     * distinct points. It validates that neither point is null and that the
     * points are not coincident (same location), as this would not define a line.
     * 
     * <p><strong>Design Pattern:</strong> This demonstrates the Factory Method pattern
     * variant where the constructor acts as a factory, ensuring objects are created
     * in a valid state.
     *
     * @param startPoint the starting point of the line segment
     * @param endPoint the ending point of the line segment
     * @throws IllegalArgumentException if either point is null or if both points are identical
     */
    public Line3D(Point3D startPoint, Point3D endPoint) {
        if (startPoint == null || endPoint == null) {
            logger.severe("Attempted to create Line3D with null point(s)");
            throw new IllegalArgumentException("Start and end points cannot be null");
        }
        
        if (startPoint.equals(endPoint)) {
            logger.severe(String.format(
                "Attempted to create Line3D with coincident points: %s", startPoint));
            throw new IllegalArgumentException(
                "Start and end points must be distinct to define a line");
        }
        
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        
        logger.info(String.format("Created Line3D from %s to %s", startPoint, endPoint));
    }
    
    /**
     * Calculates the length of this line segment.
     * 
     * <p>The length is computed as the Euclidean distance between the start and end points.
     * This method demonstrates the <strong>Delegation Pattern</strong>, where the Line3D
     * delegates the distance calculation to the Point3D class, promoting code reuse
     * and maintaining single responsibility.
     * 
     * <p><strong>Algorithm Complexity:</strong> O(1) - constant time operation involving
     * basic arithmetic operations.
     * 
     * <p><strong>Data Structure Principle:</strong> This demonstrates how composite data
     * structures (Line3D composed of Point3D objects) can provide higher-level operations
     * by leveraging their component parts.
     *
     * @return the length of the line segment
     */
    public double length() {
        logger.info("Calculating length of line");
        double length = startPoint.distanceTo(endPoint);
        logger.info(String.format("Line length calculated: %.2f", length));
        return length;
    }
    
    /**
     * Computes the direction vector of this line.
     * 
     * <p>The direction vector is calculated as (end - start), representing the
     * vector from the start point to the end point. This is fundamental for many
     * geometric calculations.
     * 
     * <p><strong>Design Pattern:</strong> This is a helper method that demonstrates
     * the <strong>Template Method Pattern</strong> concept, as it provides a common
     * operation used by other methods in the class.
     *
     * @return a double array of length 3 containing the direction vector [dx, dy, dz]
     */
    private double[] getDirectionVector() {
        double dx = endPoint.getX() - startPoint.getX();
        double dy = endPoint.getY() - startPoint.getY();
        double dz = endPoint.getZ() - startPoint.getZ();
        
        logger.info(String.format("Direction vector: [%.2f, %.2f, %.2f]", dx, dy, dz));
        return new double[]{dx, dy, dz};
    }
    
    /**
     * Calculates the midpoint of this line segment.
     * 
     * <p>The midpoint is the point equidistant from both endpoints, calculated
     * as the average of the coordinates: ((x1+x2)/2, (y1+y2)/2, (z1+z2)/2).
     * 
     * <p><strong>Algorithm Complexity:</strong> O(1) - constant time operation.
     * 
     * <p><strong>Data Structure Principle:</strong> This demonstrates how derived
     * data can be computed on-demand rather than stored, following the principle
     * of lazy evaluation to save memory.
     *
     * @return a new Point3D representing the midpoint of this line
     */
    public Point3D getMidpoint() {
        logger.info("Calculating midpoint of line");
        
        double midX = (startPoint.getX() + endPoint.getX()) / 2.0;
        double midY = (startPoint.getY() + endPoint.getY()) / 2.0;
        double midZ = (startPoint.getZ() + endPoint.getZ()) / 2.0;
        
        Point3D midpoint = new Point3D(midX, midY, midZ);
        logger.info(String.format("Midpoint calculated: %s", midpoint));
        
        return midpoint;
    }
    
    /**
     * Determines whether this line is parallel to another line.
     * 
     * <p>Two lines are parallel if their direction vectors are scalar multiples
     * of each other. This is tested using the cross product: if the cross product
     * is the zero vector (within floating-point tolerance), the lines are parallel.
     * 
     * <p><strong>Algorithm Complexity:</strong> O(1) - constant time with fixed
     * number of operations regardless of input size.
     * 
     * <p><strong>Numerical Stability:</strong> Uses EPSILON tolerance for floating-point
     * comparisons to handle precision errors inherent in double arithmetic.
     *
     * @param other the other line to check for parallelism
     * @return true if the lines are parallel, false otherwise
     * @throws IllegalArgumentException if other is null
     */
    public boolean isParallel(Line3D other) {
        if (other == null) {
            logger.severe("Attempted to check parallelism with null line");
            throw new IllegalArgumentException("Cannot check parallelism with null line");
        }
        
        logger.info("Checking if lines are parallel");
        
        double[] dir1 = this.getDirectionVector();
        double[] dir2 = other.getDirectionVector();
        
        // Calculate cross product
        double[] cross = crossProduct(dir1, dir2);
        
        // Lines are parallel if cross product is zero vector
        boolean parallel = Math.abs(cross[0]) < EPSILON && 
                          Math.abs(cross[1]) < EPSILON && 
                          Math.abs(cross[2]) < EPSILON;
        
        logger.info(String.format("Lines are %sparallel", parallel ? "" : "not "));
        return parallel;
    }
    
    /**
     * Calculates the cross product of two 3D vectors.
     * 
     * <p>The cross product a × b produces a vector perpendicular to both input vectors.
     * Formula: [a_y*b_z - a_z*b_y, a_z*b_x - a_x*b_z, a_x*b_y - a_y*b_x]
     * 
     * <p><strong>Algorithm Principle:</strong> This is a fundamental linear algebra
     * operation used in many geometric algorithms. The implementation demonstrates
     * the importance of mathematical foundations in algorithm design.
     *
     * @param a the first vector as a double array [x, y, z]
     * @param b the second vector as a double array [x, y, z]
     * @return the cross product as a double array [x, y, z]
     */
    private double[] crossProduct(double[] a, double[] b) {
        return new double[]{
            a[1] * b[2] - a[2] * b[1],
            a[2] * b[0] - a[0] * b[2],
            a[0] * b[1] - a[1] * b[0]
        };
    }
    
    /**
     * Calculates the dot product of two 3D vectors.
     * 
     * <p>The dot product a · b = a_x*b_x + a_y*b_y + a_z*b_z is used to measure
     * the similarity of direction between vectors and to project one vector onto another.
     * 
     * <p><strong>Algorithm Principle:</strong> The dot product is O(n) for n-dimensional
     * vectors, but O(1) for fixed-dimension vectors like 3D, demonstrating how
     * dimensionality affects complexity analysis.
     *
     * @param a the first vector as a double array [x, y, z]
     * @param b the second vector as a double array [x, y, z]
     * @return the dot product as a double
     */
    private double dotProduct(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }
    
    /**
     * Computes the magnitude (length) of a 3D vector.
     * 
     * <p>The magnitude is calculated as: sqrt(x² + y² + z²)
     * 
     * <p><strong>Algorithm Complexity:</strong> O(1) - constant time for fixed dimensions.
     *
     * @param vector the vector as a double array [x, y, z]
     * @return the magnitude of the vector
     */
    private double magnitude(double[] vector) {
        return Math.sqrt(vector[0] * vector[0] + 
                        vector[1] * vector[1] + 
                        vector[2] * vector[2]);
    }
    
    /**
     * Calculates the shortest distance between this line and another line in 3D space.
     * 
     * <p>This method handles three cases:
     * <ol>
     *   <li><strong>Parallel lines:</strong> Distance is the perpendicular distance
     *       between the lines, calculated by projecting the vector between any two
     *       points (one on each line) onto the plane perpendicular to the lines.</li>
     *   <li><strong>Intersecting lines:</strong> Distance is zero.</li>
     *   <li><strong>Skew lines:</strong> Distance is calculated using the formula:
     *       |((P2-P1) · (d1 × d2))| / |d1 × d2|, where P1 and P2 are points on the
     *       lines and d1, d2 are direction vectors.</li>
     * </ol>
     * 
     * <p><strong>Algorithm Complexity:</strong> O(1) - all operations are constant time
     * vector calculations with fixed dimensionality.
     * 
     * <p><strong>Geometric Algorithm:</strong> This demonstrates how computational geometry
     * problems can be solved by decomposing them into cases and applying appropriate
     * formulas. The algorithm shows the importance of handling special cases (parallel lines)
     * separately to avoid division by zero.
     * 
     * <p><strong>Numerical Robustness:</strong> Uses EPSILON tolerance throughout to
     * handle floating-point precision issues, a critical consideration in geometric algorithms.
     *
     * @param other the other line to calculate distance to
     * @return the shortest distance between the two lines
     * @throws IllegalArgumentException if other is null
     */
    public double shortestDistanceBetweenLines(Line3D other) {
        if (other == null) {
            logger.severe("Attempted to calculate distance to null line");
            throw new IllegalArgumentException("Cannot calculate distance to null line");
        }
        
        logger.info("Calculating shortest distance between lines");
        
        double[] dir1 = this.getDirectionVector();
        double[] dir2 = other.getDirectionVector();
        
        // Vector from start of line1 to start of line2
        double[] w = new double[]{
            other.startPoint.getX() - this.startPoint.getX(),
            other.startPoint.getY() - this.startPoint.getY(),
            other.startPoint.getZ() - this.startPoint.getZ()
        };
        
        // Calculate cross product of direction vectors
        double[] cross = crossProduct(dir1, dir2);
        double crossMagnitude = magnitude(cross);
        
        // Check if lines are parallel
        if (crossMagnitude < EPSILON) {
            logger.info("Lines are parallel, calculating perpendicular distance");
            
            // For parallel lines, find distance between the lines
            // Project w onto the plane perpendicular to dir1
            double[] wCrossDir1 = crossProduct(w, dir1);
            double distance = magnitude(wCrossDir1) / magnitude(dir1);
            
            logger.info(String.format("Distance between parallel lines: %.2f", distance));
            return distance;
        }
        
        // For skew or intersecting lines, use the formula:
        // distance = |w · (dir1 × dir2)| / |dir1 × dir2|
        double distance = Math.abs(dotProduct(w, cross)) / crossMagnitude;
        
        if (distance < EPSILON) {
            logger.info("Lines intersect or are extremely close (distance ≈ 0)");
        } else {
            logger.info(String.format("Shortest distance between skew lines: %.2f", distance));
        }
        
        return distance;
    }
    
    /**
     * Checks if a given point lies on this line segment.
     * 
     * <p>A point lies on the line segment if:
     * <ol>
     *   <li>It is collinear with the start and end points (cross product is zero)</li>
     *   <li>It falls between the start and end points (not beyond either endpoint)</li>
     * </ol>
     * 
     * <p><strong>Algorithm Complexity:</strong> O(1) - constant time operations.
     * 
     * <p><strong>Design Principle:</strong> This method demonstrates defensive programming
     * by validating both geometric conditions (collinearity and boundedness) before
     * confirming point containment.
     *
     * @param point the point to check
     * @return true if the point lies on this line segment, false otherwise
     * @throws IllegalArgumentException if point is null
     */
    public boolean containsPoint(Point3D point) {
        if (point == null) {
            logger.severe("Attempted to check if line contains null point");
            throw new IllegalArgumentException("Cannot check containment of null point");
        }
        
        logger.info(String.format("Checking if line contains point %s", point));
        
        // Vector from start to point
        double[] toPoint = new double[]{
            point.getX() - startPoint.getX(),
            point.getY() - startPoint.getY(),
            point.getZ() - startPoint.getZ()
        };
        
        double[] dir = getDirectionVector();
        
        // Check if point is collinear (cross product should be zero)
        double[] cross = crossProduct(toPoint, dir);
        if (magnitude(cross) > EPSILON) {
            logger.info("Point is not collinear with line");
            return false;
        }
        
        // Check if point is between start and end
        double dotProductValue = dotProduct(toPoint, dir);
        double lineLengthSquared = dotProduct(dir, dir);
        
        boolean contained = dotProductValue >= -EPSILON && 
                           dotProductValue <= lineLengthSquared + EPSILON;
        
        logger.info(String.format("Point is %son the line segment", 
                                 contained ? "" : "not "));
        return contained;
    }
    
    /**
     * Calculates the point on this line closest to a given external point.
     * 
     * <p>The closest point is found by projecting the external point onto the line.
     * The projection parameter t is calculated as: t = ((P-A) · (B-A)) / |B-A|²
     * where A is the start point, B is the end point, and P is the external point.
     * 
     * <p>The value of t is clamped to [0, 1] to ensure the result lies on the segment:
     * <ul>
     *   <li>t = 0: closest point is the start point</li>
     *   <li>0 < t < 1: closest point is interior to the segment</li>
     *   <li>t = 1: closest point is the end point</li>
     * </ul>
     * 
     * <p><strong>Algorithm Complexity:</strong> O(1) - constant time vector operations.
     * 
     * <p><strong>Geometric Insight:</strong> This demonstrates the vector projection
     * algorithm, a fundamental technique in computational geometry used for point
     * localization, collision detection, and proximity queries.
     *
     * @param point the external point to project onto the line
     * @return the closest point on this line segment to the given point
     * @throws IllegalArgumentException if point is null
     */
    public Point3D closestPointTo(Point3D point) {
        if (point == null) {
            logger.severe("Attempted to find closest point to null");
            throw new IllegalArgumentException("Cannot find closest point to null");
        }
        
        logger.info(String.format("Finding closest point on line to %s", point));
        
        double[] dir = getDirectionVector();
        
        // Vector from start to the external point
        double[] toPoint = new double[]{
            point.getX() - startPoint.getX(),
            point.getY() - startPoint.getY(),
            point.getZ() - startPoint.getZ()
        };
        
        // Project toPoint onto dir to find parameter t
        double dirDotDir = dotProduct(dir, dir);
        double t = dotProduct(toPoint, dir) / dirDotDir;
        
        // Clamp t to [0, 1] to stay on the line segment
        t = Math.max(0.0, Math.min(1.0, t));
        
        // Calculate the closest point
        double closestX = startPoint.getX() + t * dir[0];
        double closestY = startPoint.getY() + t * dir[1];
        double closestZ = startPoint.getZ() + t * dir[2];
        
        Point3D closestPoint = new Point3D(closestX, closestY, closestZ);
        logger.info(String.format("Closest point: %s (t=%.2f)", closestPoint, t));
        
        return closestPoint;
    }
    
    /**
     * Returns the starting point of this line segment.
     * 
     * <p><strong>Immutability Pattern:</strong> Returns the actual Point3D object
     * rather than a copy because Point3D itself is immutable. This is safe and
     * more efficient than defensive copying.
     *
     * @return the start point
     */
    public Point3D getStartPoint() {
        return startPoint;
    }
    
    /**
     * Returns the ending point of this line segment.
     * 
     * <p><strong>Immutability Pattern:</strong> Returns the actual Point3D object
     * rather than a copy because Point3D itself is immutable.
     *
     * @return the end point
     */
    public Point3D getEndPoint() {
        return endPoint;
    }
    
    /**
     * Compares this line to another object for equality.
     * 
     * <p>Two lines are considered equal if they have the same start and end points.
     * Note that this implementation considers direction: a line from A to B is
     * different from a line from B to A.
     * 
     * <p><strong>Design Pattern:</strong> Properly implements the equals contract
     * as specified in Object.equals(), including reflexivity, symmetry, transitivity,
     * and consistency.
     *
     * @param obj the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Line3D other = (Line3D) obj;
        return Objects.equals(startPoint, other.startPoint) &&
               Objects.equals(endPoint, other.endPoint);
    }
    
    /**
     * Generates a hash code for this line.
     * 
     * <p><strong>Design Pattern:</strong> Implements hashCode() consistently with
     * equals(), ensuring that equal objects have equal hash codes, as required
     * by the hashCode contract.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(startPoint, endPoint);
    }
    
    /**
     * Returns a string representation of this line.
     * 
     * <p><strong>Design Pattern:</strong> Provides a human-readable representation
     * for debugging and logging purposes, following the toString() convention.
     *
     * @return a string describing this line
     */
    @Override
    public String toString() {
        return String.format("Line3D[%s -> %s, length=%.2f]", 
                           startPoint, endPoint, length());
    }
}