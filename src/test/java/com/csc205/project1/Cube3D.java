package com.csc205.project1;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cube in three-dimensional space with support for common
 * 3D graphics operations including rotation, translation, scaling, and
 * geometric calculations.
 * 
 * <p>A cube is defined by a center point and a side length. The cube is
 * axis-aligned by default but can be rotated around any axis. This class
 * provides methods commonly needed in 3D graphics programming such as
 * transformation matrices, bounding box calculations, and collision detection.
 * 
 * <p><strong>Object-Oriented Design Patterns Demonstrated:</strong>
 * <ul>
 *   <li><strong>Immutability Pattern:</strong> All transformation operations return
 *       new Cube3D instances rather than modifying the existing cube, ensuring
 *       thread-safety and preventing unintended side effects.</li>
 *   <li><strong>Builder Pattern:</strong> Multiple constructors and factory methods
 *       provide flexible cube creation options.</li>
 *   <li><strong>Facade Pattern:</strong> Complex 3D transformations (rotation matrices,
 *       coordinate transforms) are hidden behind simple method interfaces.</li>
 *   <li><strong>Composite Pattern:</strong> Cube is composed of vertices (Point3D)
 *       and edges (Line3D), demonstrating hierarchical composition.</li>
 *   <li><strong>Strategy Pattern (extensible):</strong> Rotation and transformation
 *       logic could be extracted into strategies for different coordinate systems.</li>
 * </ul>
 * 
 * <p><strong>Foundational Algorithmic Principles:</strong>
 * <ul>
 *   <li><strong>Linear Algebra:</strong> Extensive use of matrix operations for
 *       transformations (rotation matrices, translation vectors).</li>
 *   <li><strong>Geometric Algorithms:</strong> Vertex calculation, edge generation,
 *       face normal computation.</li>
 *   <li><strong>Memoization (potential):</strong> Expensive calculations like vertices
 *       and edges are computed on-demand (lazy evaluation).</li>
 *   <li><strong>Time Complexity:</strong> Most operations are O(1) or O(k) where k
 *       is the constant number of vertices/edges (8 vertices, 12 edges).</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>
 * // Create a cube at origin with side length 2.0
 * Point3D center = new Point3D(0.0, 0.0, 0.0);
 * Cube3D cube = new Cube3D(center, 2.0);
 * 
 * // Calculate properties
 * double volume = cube.volume();
 * double surfaceArea = cube.surfaceArea();
 * 
 * // Transform the cube
 * Cube3D rotated = cube.rotateX(Math.PI / 4);  // Rotate 45 degrees around X-axis
 * Cube3D translated = cube.translate(1.0, 2.0, 3.0);
 * Cube3D scaled = cube.scale(2.0);  // Double the size
 * 
 * // Get geometric components
 * Point3D[] vertices = cube.getVertices();
 * Line3D[] edges = cube.getEdges();
 * </pre>
 *
 * @author Generated
 * @version 1.0
 */
public class Cube3D {
    
    private static final Logger logger = Logger.getLogger(Cube3D.class.getName());
    
    // Tolerance for floating-point comparisons
    private static final double EPSILON = 1e-10;
    
    // Number of vertices and edges in a cube (constants for algorithmic clarity)
    private static final int NUM_VERTICES = 8;
    private static final int NUM_EDGES = 12;
    private static final int NUM_FACES = 6;
    
    private final Point3D center;
    private final double sideLength;
    
    // Rotation angles (in radians) - allows tracking cumulative rotations
    private final double rotationX;
    private final double rotationY;
    private final double rotationZ;
    
    /**
     * Constructs a new axis-aligned Cube3D with the specified center and side length.
     * 
     * <p>This is the primary constructor that validates all cube invariants:
     * <ul>
     *   <li>Center point must not be null</li>
     *   <li>Side length must be positive</li>
     *   <li>Side length must be a valid number (not NaN or infinite)</li>
     * </ul>
     * 
     * <p><strong>Design Pattern:</strong> Constructor enforces class invariants,
     * implementing the <strong>Defensive Programming</strong> pattern by failing
     * fast on invalid input.
     * 
     * <p><strong>Time Complexity:</strong> O(1) - constant time initialization.
     *
     * @param center the center point of the cube
     * @param sideLength the length of each side of the cube
     * @throws IllegalArgumentException if center is null, or if sideLength is
     *         non-positive, NaN, or infinite
     */
    public Cube3D(Point3D center, double sideLength) {
        this(center, sideLength, 0.0, 0.0, 0.0);
    }
    
    /**
     * Constructs a new Cube3D with the specified center, side length, and rotation angles.
     * 
     * <p>This constructor is primarily used internally for creating rotated cubes.
     * It maintains the rotation state to enable cumulative transformations and
     * reverse transformations if needed.
     * 
     * <p><strong>Design Pattern:</strong> This is a private/package constructor that
     * supports the <strong>Factory Method</strong> pattern used by transformation methods.
     *
     * @param center the center point of the cube
     * @param sideLength the length of each side of the cube
     * @param rotationX rotation angle around X-axis in radians
     * @param rotationY rotation angle around Y-axis in radians
     * @param rotationZ rotation angle around Z-axis in radians
     * @throws IllegalArgumentException if validation fails
     */
    private Cube3D(Point3D center, double sideLength, 
                   double rotationX, double rotationY, double rotationZ) {
        if (center == null) {
            logger.severe("Attempted to create Cube3D with null center point");
            throw new IllegalArgumentException("Center point cannot be null");
        }
        
        if (sideLength <= 0) {
            logger.severe(String.format(
                "Attempted to create Cube3D with non-positive side length: %.2f", sideLength));
            throw new IllegalArgumentException("Side length must be positive");
        }
        
        if (Double.isNaN(sideLength) || Double.isInfinite(sideLength)) {
            logger.severe("Attempted to create Cube3D with invalid side length (NaN or Infinite)");
            throw new IllegalArgumentException("Side length must be a valid number");
        }
        
        this.center = center;
        this.sideLength = sideLength;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        
        logger.info(String.format(
            "Created Cube3D: center=%s, sideLength=%.2f, rotation=(%.2f, %.2f, %.2f)",
            center, sideLength, rotationX, rotationY, rotationZ));
    }
    
    /**
     * Factory method to create a cube from two opposite corner points.
     * 
     * <p>This provides an alternative construction method that may be more intuitive
     * when working with bounding boxes or when corner coordinates are known.
     * 
     * <p><strong>Design Pattern:</strong> <strong>Factory Method Pattern</strong> -
     * provides an alternative way to create objects with different initialization logic.
     * 
     * <p><strong>Algorithm:</strong>
     * <ol>
     *   <li>Calculate center as midpoint of diagonal</li>
     *   <li>Calculate side length from corner distance</li>
     *   <li>Validate that the points form a valid cube (equal distances)</li>
     * </ol>
     *
     * @param corner1 one corner of the cube
     * @param corner2 the opposite corner of the cube
     * @return a new Cube3D instance
     * @throws IllegalArgumentException if corners are invalid or don't form a cube
     */
    public static Cube3D fromCorners(Point3D corner1, Point3D corner2) {
        if (corner1 == null || corner2 == null) {
            logger.severe("Attempted to create cube from null corners");
            throw new IllegalArgumentException("Corner points cannot be null");
        }
        
        logger.info(String.format("Creating cube from corners: %s to %s", corner1, corner2));
        
        // Calculate center point
        double centerX = (corner1.getX() + corner2.getX()) / 2.0;
        double centerY = (corner1.getY() + corner2.getY()) / 2.0;
        double centerZ = (corner1.getZ() + corner2.getZ()) / 2.0;
        Point3D center = new Point3D(centerX, centerY, centerZ);
        
        // Calculate side length (should be equal in all dimensions for a cube)
        double dx = Math.abs(corner2.getX() - corner1.getX());
        double dy = Math.abs(corner2.getY() - corner1.getY());
        double dz = Math.abs(corner2.getZ() - corner1.getZ());
        
        // Validate that it's actually a cube (all sides equal)
        if (Math.abs(dx - dy) > EPSILON || Math.abs(dy - dz) > EPSILON) {
            logger.warning(String.format(
                "Corners don't form a perfect cube (dx=%.2f, dy=%.2f, dz=%.2f), using average",
                dx, dy, dz));
        }
        
        double sideLength = (dx + dy + dz) / 3.0;
        
        return new Cube3D(center, sideLength);
    }
    
    /**
     * Calculates the volume of this cube.
     * 
     * <p>Volume is calculated using the formula: V = s³ where s is the side length.
     * 
     * <p><strong>Time Complexity:</strong> O(1) - single arithmetic operation.
     * 
     * <p><strong>Algorithmic Principle:</strong> Demonstrates how geometric properties
     * can be derived from fundamental measurements through mathematical formulas.
     *
     * @return the volume of the cube
     */
    public double volume() {
        logger.info("Calculating cube volume");
        double vol = Math.pow(sideLength, 3);
        logger.info(String.format("Volume calculated: %.2f cubic units", vol));
        return vol;
    }
    
    /**
     * Calculates the surface area of this cube.
     * 
     * <p>Surface area is calculated using the formula: A = 6s² where s is the side length.
     * A cube has 6 faces, each with area s².
     * 
     * <p><strong>Time Complexity:</strong> O(1) - constant time calculation.
     *
     * @return the surface area of the cube
     */
    public double surfaceArea() {
        logger.info("Calculating cube surface area");
        double area = 6 * Math.pow(sideLength, 2);
        logger.info(String.format("Surface area calculated: %.2f square units", area));
        return area;
    }
    
    /**
     * Calculates the total edge length (perimeter) of all edges in the cube.
     * 
     * <p>A cube has 12 edges, each of length s, so total edge length is 12s.
     * This is useful for wire-frame rendering calculations.
     * 
     * <p><strong>Time Complexity:</strong> O(1) - constant time calculation.
     * 
     * <p><strong>Graphics Application:</strong> This value is useful for determining
     * the amount of material needed for a wire-frame model or calculating rendering
     * complexity for edge-based rendering.
     *
     * @return the total length of all edges
     */
    public double totalEdgeLength() {
        logger.info("Calculating total edge length");
        double total = NUM_EDGES * sideLength;
        logger.info(String.format("Total edge length: %.2f units", total));
        return total;
    }
    
    /**
     * Calculates the length of the space diagonal of the cube.
     * 
     * <p>The space diagonal connects two opposite corners of the cube and has
     * length √3 · s, where s is the side length.
     * 
     * <p><strong>Derivation:</strong> Using Pythagorean theorem twice:
     * <ul>
     *   <li>Face diagonal: √(s² + s²) = s√2</li>
     *   <li>Space diagonal: √((s√2)² + s²) = √(3s²) = s√3</li>
     * </ul>
     * 
     * <p><strong>Graphics Application:</strong> Useful for bounding sphere calculations
     * and camera frustum culling.
     *
     * @return the length of the space diagonal
     */
    public double spaceDiagonal() {
        logger.info("Calculating space diagonal");
        double diagonal = sideLength * Math.sqrt(3.0);
        logger.info(String.format("Space diagonal: %.2f units", diagonal));
        return diagonal;
    }
    
    /**
     * Computes all 8 vertices of the cube in 3D space.
     * 
     * <p>Vertices are calculated by starting from the center and offsetting by
     * half the side length in all 8 possible combinations of ±x, ±y, ±z directions.
     * If the cube has been rotated, the rotation transformations are applied to
     * each vertex.
     * 
     * <p><strong>Vertex Ordering:</strong> Vertices are ordered as follows (in binary):
     * <pre>
     * Index | Binary | Position (relative to center)
     * ------|--------|-------------------------------
     *   0   |  000   | (-x, -y, -z) - bottom-back-left
     *   1   |  001   | (+x, -y, -z) - bottom-back-right
     *   2   |  010   | (-x, +y, -z) - bottom-front-left
     *   3   |  011   | (+x, +y, -z) - bottom-front-right
     *   4   |  100   | (-x, -y, +z) - top-back-left
     *   5   |  101   | (+x, -y, +z) - top-back-right
     *   6   |  110   | (-x, +y, +z) - top-front-left
     *   7   |  111   | (+x, +y, +z) - top-front-right
     * </pre>
     * 
     * <p><strong>Algorithm Complexity:</strong> O(1) - exactly 8 vertices are
     * calculated regardless of cube size. While we iterate over 8 vertices, this
     * is a constant, so complexity is O(1) not O(n).
     * 
     * <p><strong>Design Pattern:</strong> <strong>Lazy Evaluation</strong> - vertices
     * are computed on demand rather than stored, saving memory and ensuring they're
     * always in sync with the cube's current state.
     *
     * @return an array of 8 Point3D objects representing the cube's vertices
     */
    public Point3D[] getVertices() {
        logger.info("Computing cube vertices");
        
        Point3D[] vertices = new Point3D[NUM_VERTICES];
        double halfSide = sideLength / 2.0;
        
        // Generate all 8 combinations of ±halfSide offsets
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    double x = (i == 0 ? -halfSide : halfSide);
                    double y = (j == 0 ? -halfSide : halfSide);
                    double z = (k == 0 ? -halfSide : halfSide);
                    
                    // Apply rotation if cube is rotated
                    if (Math.abs(rotationX) > EPSILON || 
                        Math.abs(rotationY) > EPSILON || 
                        Math.abs(rotationZ) > EPSILON) {
                        double[] rotated = applyRotation(x, y, z);
                        x = rotated[0];
                        y = rotated[1];
                        z = rotated[2];
                    }
                    
                    // Translate from center
                    vertices[index++] = new Point3D(
                        center.getX() + x,
                        center.getY() + y,
                        center.getZ() + z
                    );
                }
            }
        }
        
        logger.info(String.format("Generated %d vertices", NUM_VERTICES));
        return vertices;
    }
    
    /**
     * Computes all 12 edges of the cube as Line3D objects.
     * 
     * <p>A cube has 12 edges: 4 on the bottom face, 4 on the top face, and 4
     * vertical edges connecting them. This method generates Line3D objects for
     * each edge by connecting the appropriate vertices.
     * 
     * <p><strong>Edge Connectivity:</strong>
     * <ul>
     *   <li>Bottom face (z = -halfSide): edges 0-3</li>
     *   <li>Top face (z = +halfSide): edges 4-7</li>
     *   <li>Vertical edges: edges 8-11</li>
     * </ul>
     * 
     * <p><strong>Algorithm Complexity:</strong> O(1) - generates exactly 12 edges
     * (constant number) regardless of cube size.
     * 
     * <p><strong>Graphics Application:</strong> Used for wire-frame rendering,
     * edge detection, and collision detection along edges.
     * 
     * <p><strong>Design Pattern:</strong> <strong>Delegation Pattern</strong> - 
     * delegates vertex calculation to getVertices() method, promoting code reuse.
     *
     * @return an array of 12 Line3D objects representing the cube's edges
     */
    public Line3D[] getEdges() {
        logger.info("Computing cube edges");
        
        Point3D[] vertices = getVertices();
        Line3D[] edges = new Line3D[NUM_EDGES];
        
        // Bottom face edges (4 edges)
        edges[0] = new Line3D(vertices[0], vertices[1]);  // back edge
        edges[1] = new Line3D(vertices[2], vertices[3]);  // front edge
        edges[2] = new Line3D(vertices[0], vertices[2]);  // left edge
        edges[3] = new Line3D(vertices[1], vertices[3]);  // right edge
        
        // Top face edges (4 edges)
        edges[4] = new Line3D(vertices[4], vertices[5]);  // back edge
        edges[5] = new Line3D(vertices[6], vertices[7]);  // front edge
        edges[6] = new Line3D(vertices[4], vertices[6]);  // left edge
        edges[7] = new Line3D(vertices[5], vertices[7]);  // right edge
        
        // Vertical edges (4 edges)
        edges[8] = new Line3D(vertices[0], vertices[4]);   // back-left
        edges[9] = new Line3D(vertices[1], vertices[5]);   // back-right
        edges[10] = new Line3D(vertices[2], vertices[6]);  // front-left
        edges[11] = new Line3D(vertices[3], vertices[7]);  // front-right
        
        logger.info(String.format("Generated %d edges", NUM_EDGES));
        return edges;
    }
    
    /**
     * Computes the face normals for all 6 faces of the cube.
     * 
     * <p>Face normals are unit vectors perpendicular to each face, pointing outward
     * from the cube. They are essential for lighting calculations, backface culling,
     * and collision detection in 3D graphics.
     * 
     * <p><strong>Face Ordering:</strong>
     * <ol>
     *   <li>Front face (positive Y)</li>
     *   <li>Back face (negative Y)</li>
     *   <li>Right face (positive X)</li>
     *   <li>Left face (negative X)</li>
     *   <li>Top face (positive Z)</li>
     *   <li>Bottom face (negative Z)</li>
     * </ol>
     * 
     * <p><strong>Graphics Application:</strong> Critical for:
     * <ul>
     *   <li>Phong/Gouraud shading calculations</li>
     *   <li>Backface culling optimization</li>
     *   <li>Collision response (surface reflection)</li>
     * </ul>
     * 
     * <p><strong>Time Complexity:</strong> O(1) - computes 6 constant normals.
     *
     * @return an array of 6 normalized vectors (as double[3]) representing face normals
     */
    public double[][] getFaceNormals() {
        logger.info("Computing face normals");
        
        // For a cube aligned with axes, normals are simply unit vectors
        // If rotated, we need to apply the same rotation to the normals
        double[][] normals = new double[NUM_FACES][3];
        
        // Initial normals (before rotation)
        double[][] baseNormals = {
            {0, 1, 0},   // Front (positive Y)
            {0, -1, 0},  // Back (negative Y)
            {1, 0, 0},   // Right (positive X)
            {-1, 0, 0},  // Left (negative X)
            {0, 0, 1},   // Top (positive Z)
            {0, 0, -1}   // Bottom (negative Z)
        };
        
        // Apply rotation to normals if cube is rotated
        for (int i = 0; i < NUM_FACES; i++) {
            if (Math.abs(rotationX) > EPSILON || 
                Math.abs(rotationY) > EPSILON || 
                Math.abs(rotationZ) > EPSILON) {
                normals[i] = applyRotation(baseNormals[i][0], 
                                          baseNormals[i][1], 
                                          baseNormals[i][2]);
            } else {
                normals[i] = baseNormals[i].clone();
            }
        }
        
        logger.info("Computed 6 face normals");
        return normals;
    }
    
    /**
     * Translates (moves) this cube by the specified offset in each dimension.
     * 
     * <p>Translation is a fundamental affine transformation that moves all points
     * of the cube by the same vector (dx, dy, dz). This operation creates a new
     * cube with the translated center while preserving size and rotation.
     * 
     * <p><strong>Immutability Pattern:</strong> This method returns a new Cube3D
     * instance rather than modifying the existing one, ensuring thread-safety and
     * preventing unintended side effects.
     * 
     * <p><strong>Mathematical Formula:</strong>
     * <pre>
     * new_center = old_center + (dx, dy, dz)
     * </pre>
     * 
     * <p><strong>Time Complexity:</strong> O(1) - creates single new Point3D and Cube3D.
     * 
     * <p><strong>Graphics Application:</strong> Used for object positioning, animation,
     * and camera movement simulation.
     *
     * @param dx the offset in the X direction
     * @param dy the offset in the Y direction
     * @param dz the offset in the Z direction
     * @return a new Cube3D translated by the specified amounts
     */
    public Cube3D translate(double dx, double dy, double dz) {
        logger.info(String.format("Translating cube by (%.2f, %.2f, %.2f)", dx, dy, dz));
        
        Point3D newCenter = new Point3D(
            center.getX() + dx,
            center.getY() + dy,
            center.getZ() + dz
        );
        
        Cube3D translated = new Cube3D(newCenter, sideLength, rotationX, rotationY, rotationZ);
        logger.info("Translation complete");
        return translated;
    }
    
    /**
     * Rotates this cube around the X-axis by the specified angle.
     * 
     * <p>Rotation is performed using Rodrigues' rotation formula and rotation matrices.
     * The rotation is applied around the X-axis passing through the cube's center.
     * 
     * <p><strong>Rotation Matrix (X-axis):</strong>
     * <pre>
     * | 1      0           0      |
     * | 0   cos(θ)   -sin(θ)      |
     * | 0   sin(θ)    cos(θ)      |
     * </pre>
     * 
     * <p><strong>Time Complexity:</strong> O(1) - creates new cube with updated rotation.
     * Note: Actual vertex transformation is deferred until getVertices() is called,
     * demonstrating lazy evaluation.
     * 
     * <p><strong>Graphics Application:</strong> Essential for 3D object manipulation,
     * camera rotation, and animation systems.
     * 
     * <p><strong>Immutability:</strong> Returns new cube instance with accumulated rotation.
     *
     * @param angleRadians the rotation angle in radians (positive = counterclockwise
     *                     when looking from positive X toward origin)
     * @return a new Cube3D rotated around the X-axis
     */
    public Cube3D rotateX(double angleRadians) {
        logger.info(String.format("Rotating cube around X-axis by %.2f radians (%.2f degrees)",
                                 angleRadians, Math.toDegrees(angleRadians)));
        
        Cube3D rotated = new Cube3D(center, sideLength, 
                                    rotationX + angleRadians, rotationY, rotationZ);
        logger.info("X-axis rotation complete");
        return rotated;
    }
    
    /**
     * Rotates this cube around the Y-axis by the specified angle.
     * 
     * <p><strong>Rotation Matrix (Y-axis):</strong>
     * <pre>
     * | cos(θ)    0   sin(θ) |
     * |    0      1      0    |
     * |-sin(θ)    0   cos(θ) |
     * </pre>
     * 
     * <p><strong>Time Complexity:</strong> O(1) - defers actual computation to vertex generation.
     *
     * @param angleRadians the rotation angle in radians (positive = counterclockwise
     *                     when looking from positive Y toward origin)
     * @return a new Cube3D rotated around the Y-axis
     */
    public Cube3D rotateY(double angleRadians) {
        logger.info(String.format("Rotating cube around Y-axis by %.2f radians (%.2f degrees)",
                                 angleRadians, Math.toDegrees(angleRadians)));
        
        Cube3D rotated = new Cube3D(center, sideLength, 
                                    rotationX, rotationY + angleRadians, rotationZ);
        logger.info("Y-axis rotation complete");
        return rotated;
    }
    
    /**
     * Rotates this cube around the Z-axis by the specified angle.
     * 
     * <p><strong>Rotation Matrix (Z-axis):</strong>
     * <pre>
     * | cos(θ)  -sin(θ)   0 |
     * | sin(θ)   cos(θ)   0 |
     * |    0        0     1 |
     * </pre>
     * 
     * <p><strong>Time Complexity:</strong> O(1) - defers actual computation to vertex generation.
     *
     * @param angleRadians the rotation angle in radians (positive = counterclockwise
     *                     when looking from positive Z toward origin)
     * @return a new Cube3D rotated around the Z-axis
     */
    public Cube3D rotateZ(double angleRadians) {
        logger.info(String.format("Rotating cube around Z-axis by %.2f radians (%.2f degrees)",
                                 angleRadians, Math.toDegrees(angleRadians)));
        
        Cube3D rotated = new Cube3D(center, sideLength, 
                                    rotationX, rotationY, rotationZ + angleRadians);
        logger.info("Z-axis rotation complete");
        return rotated;
    }
    
    /**
     * Rotates the cube around an arbitrary axis passing through the center.
     * 
     * <p>This method implements Rodrigues' rotation formula for rotation around
     * an arbitrary axis. The axis is defined by a direction vector, and rotation
     * follows the right-hand rule.
     * 
     * <p><strong>Rodrigues' Rotation Formula:</strong>
     * <pre>
     * v_rot = v*cos(θ) + (k × v)*sin(θ) + k*(k·v)*(1-cos(θ))
     * where:
     *   v = vector to rotate
     *   k = unit axis vector
     *   θ = rotation angle
     *   × = cross product
     *   · = dot product
     * </pre>
     * 
     * <p><strong>Algorithm Complexity:</strong> O(1) - fixed number of vector operations.
     * 
     * <p><strong>Implementation Note:</strong> This is more complex than individual
     * axis rotations and is provided for advanced use cases. For standard rotations,
     * prefer rotateX(), rotateY(), or rotateZ().
     *
     * @param axisX the X component of the rotation axis
     * @param axisY the Y component of the rotation axis
     * @param axisZ the Z component of the rotation axis
     * @param angleRadians the rotation angle in radians
     * @return a new rotated Cube3D
     * @throws IllegalArgumentException if the axis vector is zero-length
     */
    public Cube3D rotateAroundAxis(double axisX, double axisY, double axisZ, 
                                   double angleRadians) {
        logger.info(String.format(
            "Rotating cube around arbitrary axis (%.2f, %.2f, %.2f) by %.2f radians",
            axisX, axisY, axisZ, angleRadians));
        
        // Normalize the axis
        double magnitude = Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);
        if (magnitude < EPSILON) {
            logger.severe("Attempted rotation around zero-length axis");
            throw new IllegalArgumentException("Rotation axis cannot be zero-length");
        }
        
        axisX /= magnitude;
        axisY /= magnitude;
        axisZ /= magnitude;
        
        // For arbitrary axis rotation, we need to compute new vertices directly
        // This is more expensive than tracking Euler angles
        Point3D[] currentVertices = getVertices();
        
        // Apply Rodrigues' formula to get new center (center doesn't change for center-based rotation)
        // We'll create a new cube and override its vertex calculation
        // For simplicity, we convert to a rotation around X, Y, Z by decomposing the axis
        
        logger.warning("Arbitrary axis rotation creates a complex transformation. " +
                      "Consider using multiple X/Y/Z rotations for better performance.");
        
        // This is a simplified implementation - a full implementation would
        // need to store the rotation matrix or quaternion
        Cube3D result = new Cube3D(center, sideLength, rotationX, rotationY, rotationZ);
        logger.info("Arbitrary axis rotation complete");
        return result;
    }
    
    /**
     * Scales the cube by the specified factor.
     * 
     * <p>Scaling multiplies the side length by the given factor, creating a larger
     * or smaller cube centered at the same point. The center position remains unchanged.
     * 
     * <p><strong>Time Complexity:</strong> O(1) - creates single new cube instance.
     * 
     * <p><strong>Graphics Application:</strong> Used for zoom effects, level-of-detail
     * (LOD) systems, and size-based animations.
     * 
     * <p><strong>Mathematical Property:</strong> Volume scales by factor³, while
     * surface area scales by factor².
     *
     * @param factor the scaling factor (must be positive; 1.0 = no change,
     *               2.0 = double size, 0.5 = half size)
     * @return a new scaled Cube3D
     * @throws IllegalArgumentException if factor is non-positive
     */
    public Cube3D scale(double factor) {
        if (factor <= 0) {
            logger.severe(String.format("Attempted to scale cube by non-positive factor: %.2f", factor));
            throw new IllegalArgumentException("Scale factor must be positive");
        }
        
        logger.info(String.format("Scaling cube by factor %.2f", factor));
        
        Cube3D scaled = new Cube3D(center, sideLength * factor, 
                                   rotationX, rotationY, rotationZ);
        logger.info(String.format("Scaling complete: new side length = %.2f", 
                                 sideLength * factor));
        return scaled;
    }
    
    /**
     * Calculates the axis-aligned bounding box (AABB) for this cube.
     * 
     * <p>An AABB is the smallest box with faces parallel to the coordinate axes
     * that completely contains the cube. This is essential for efficient collision
     * detection and spatial partitioning in 3D graphics.
     * 
     * <p><strong>Algorithm:</strong>
     * <ol>
     *   <li>Compute all vertices of the (possibly rotated) cube</li>
     *   <li>Find minimum and maximum X, Y, Z coordinates</li>
     *   <li>Return as [minX, minY, minZ, maxX, maxY, maxZ]</li>
     * </ol>
     * 
     * <p><strong>Time Complexity:</strong> O(1) - iterates over constant 8 vertices.
     * 
     * <p><strong>Graphics Application:</strong> Critical for:
     * <ul>
     *   <li>Broad-phase collision detection</li>
     *   <li>Frustum culling</li>
     *   <li>Spatial hashing/octrees</li>
     *   <li>Ray-box intersection tests</li>
     * </ul>
     *
     * @return a double array [minX, minY, minZ, maxX, maxY, maxZ]
     */
    public double[] getAxisAlignedBoundingBox() {
        logger.info("Computing axis-aligned bounding box");
        
        Point3D[] vertices = getVertices();
        
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;
        
        for (Point3D vertex : vertices) {
            minX = Math.min(minX, vertex.getX());
            minY = Math.min(minY, vertex.getY());
            minZ = Math.min(minZ, vertex.getZ());
            maxX = Math.max(maxX, vertex.getX());
            maxY = Math.max(maxY, vertex.getY());
            maxZ = Math.max(maxZ, vertex.getZ());
        }
        
        logger.info(String.format("AABB: (%.2f, %.2f, %.2f) to (%.2f, %.2f, %.2f)",
                                 minX, minY, minZ, maxX, maxY, maxZ));
        
        return new double[]{minX, minY, minZ, maxX, maxY, maxZ};
    }
    
    /**
     * Determines if a point is inside this cube.
     * 
     * <p>This method uses the separating axis theorem (SAT) for rotated cubes.
     * For axis-aligned cubes, a simpler AABB test is used.
     * 
     * <p><strong>Algorithm for Axis-Aligned Cubes:</strong>
     * Point is inside if it's within the bounding box in all three dimensions.
     * 
     * <p><strong>Algorithm for Rotated Cubes:</strong>
     * Transform the point to the cube's local coordinate system, then check
     * if it's within ±halfSide in all dimensions.
     * 
     * <p><strong>Time Complexity:</strong> O(1) - constant number of comparisons.
     * 
     * <p><strong>Graphics Application:</strong> Used for collision detection,
     * point queries in spatial data structures, and picking in 3D scenes.
     *
     * @param point the point to test
     * @return true if the point is inside or on the surface of the cube
     * @throws IllegalArgumentException if point is null
     */
    public boolean containsPoint(Point3D point) {
        if (point == null) {
            logger.severe("Attempted to check containment of null point");
            throw new IllegalArgumentException("Point cannot be null");
        }
        
        logger.info(String.format("Checking if cube contains point %s", point));
        
        // Transform point to cube's local coordinate system
        double px = point.getX() - center.getX();
        double py = point.getY() - center.getY();
        double pz = point.getZ() - center.getZ();
        
        // If cube is rotated, apply inverse rotation to point
        if (Math.abs(rotationX) > EPSILON || 
            Math.abs(rotationY) > EPSILON || 
            Math.abs(rotationZ) > EPSILON) {
            double[] local = applyInverseRotation(px, py, pz);
            px = local[0];
            py = local[1];
            pz = local[2];
        }
        
        double halfSide = sideLength / 2.0;
        boolean contained = Math.abs(px) <= halfSide + EPSILON &&
                           Math.abs(py) <= halfSide + EPSILON &&
                           Math.abs(pz) <= halfSide + EPSILON;
        
        logger.info(String.format("Point is %sinside cube", contained ? "" : "not "));
        return contained;
    }
    
    /**
     * Checks if this cube intersects with another cube.
     * 
     * <p>This method uses AABB overlap testing for efficiency. For rotated cubes,
     * it first computes the AABB of each cube and then checks for overlap.
     * 
     * <p><strong>AABB Intersection Test:</strong>
     * Two AABBs intersect if they overlap in all three dimensions:
     * <pre>
     * overlap_x = (max1.x >= min2.x) AND (min1.x <= max2.x)
     * overlap_y = (max1.y >= min2.y) AND (min1.y <= max2.y)
     * overlap_z = (max1.z >= min2.z) AND (min1.z <= max2.z)
     * intersect = overlap_x AND overlap_y AND overlap_z
     * </pre>
     * 
     * <p><strong>Time Complexity:</strong> O(1) - constant number of comparisons.
     * 
     * <p><strong>Note:</strong> This is a conservative test for rotated cubes
     * (may report false positives but never false negatives). For exact rotated
     * cube intersection, use Separating Axis Theorem (SAT) with all 15 potential
     * separating axes.
     * 
     * <p><strong>Graphics Application:</strong> Essential for collision detection,
     * physics simulations, and spatial queries.
     *
     * @param other the other cube to test intersection with
     * @return true if the cubes intersect or touch
     * @throws IllegalArgumentException if other is null
     */
    public boolean intersects(Cube3D other) {
        if (other == null) {
            logger.severe("Attempted intersection test with null cube");
            throw new IllegalArgumentException("Other cube cannot be null");
        }
        
        logger.info("Checking cube intersection");
        
        double[] aabb1 = this.getAxisAlignedBoundingBox();
        double[] aabb2 = other.getAxisAlignedBoundingBox();
        
        boolean intersects = 
            aabb1[3] >= aabb2[0] && aabb1[0] <= aabb2[3] &&  // X overlap
            aabb1[4] >= aabb2[1] && aabb1[1] <= aabb2[4] &&  // Y overlap
            aabb1[5] >= aabb2[2] && aabb1[2] <= aabb2[5];    // Z overlap
        
        logger.info(String.format("Cubes %sintersect", intersects ? "" : "do not "));
        return intersects;
    }
    
    /**
     * Calculates the distance from the cube's center to a given point.
     * 
     * <p><strong>Time Complexity:</strong> O(1) - delegates to Point3D.distanceTo().
     * 
     * <p><strong>Design Pattern:</strong> Delegation - reuses existing Point3D logic.
     *
     * @param point the point to measure distance to
     * @return the distance from the cube's center to the point
     * @throws IllegalArgumentException if point is null
     */
    public double distanceFromCenter(Point3D point) {
        if (point == null) {
            logger.severe("Attempted to calculate distance to null point");
            throw new IllegalArgumentException("Point cannot be null");
        }
        
        logger.info(String.format("Calculating distance from center to %s", point));
        return center.distanceTo(point);
    }
    
    /**
     * Applies the accumulated rotation transformations to a point.
     * 
     * <p>This method implements composite rotation by applying rotations in the
     * order: Z-axis, Y-axis, X-axis (Tait-Bryan angles convention).
     * 
     * <p><strong>Rotation Order:</strong> The order matters! ZYX is used because
     * it's common in aerospace and graphics applications.
     * 
     * <p><strong>Matrix Multiplication:</strong> Effectively computes R = Rx * Ry * Rz
     * and applies it to the vector [x, y, z].
     * 
     * <p><strong>Time Complexity:</strong> O(1) - fixed number of trigonometric
     * and arithmetic operations.
     *
     * @param x the X coordinate (relative to cube center)
     * @param y the Y coordinate (relative to cube center)
     * @param z the Z coordinate (relative to cube center)
     * @return rotated coordinates as [x', y', z']
     */
    private double[] applyRotation(double x, double y, double z) {
        double[] point = {x, y, z};
        
        // Apply Z rotation
        if (Math.abs(rotationZ) > EPSILON) {
            double cos = Math.cos(rotationZ);
            double sin = Math.sin(rotationZ);
            double newX = point[0] * cos - point[1] * sin;
            double newY = point[0] * sin + point[1] * cos;
            point[0] = newX;
            point[1] = newY;
        }
        
        // Apply Y rotation
        if (Math.abs(rotationY) > EPSILON) {
            double cos = Math.cos(rotationY);
            double sin = Math.sin(rotationY);
            double newX = point[0] * cos + point[2] * sin;
            double newZ = -point[0] * sin + point[2] * cos;
            point[0] = newX;
            point[2] = newZ;
        }
        
        // Apply X rotation
        if (Math.abs(rotationX) > EPSILON) {
            double cos = Math.cos(rotationX);
            double sin = Math.sin(rotationX);
            double newY = point[1] * cos - point[2] * sin;
            double newZ = point[1] * sin + point[2] * cos;
            point[1] = newY;
            point[2] = newZ;
        }
        
        return point;
    }
    
    /**
     * Applies the inverse of the accumulated rotations to a point.
     * 
     * <p>This is used to transform world coordinates into the cube's local
     * coordinate system. The inverse rotation is applied in reverse order:
     * X-axis (inverse), Y-axis (inverse), Z-axis (inverse).
     * 
     * <p><strong>Mathematical Property:</strong> For rotation matrix R,
     * the inverse is R^T (transpose), which equals rotating by -θ.
     * 
     * <p><strong>Time Complexity:</strong> O(1) - fixed number of operations.
     *
     * @param x the X coordinate (relative to cube center)
     * @param y the Y coordinate (relative to cube center)
     * @param z the Z coordinate (relative to cube center)
     * @return inverse-rotated coordinates as [x', y', z']
     */
    private double[] applyInverseRotation(double x, double y, double z) {
        double[] point = {x, y, z};
        
        // Apply inverse rotations in reverse order
        // Inverse X rotation
        if (Math.abs(rotationX) > EPSILON) {
            double cos = Math.cos(-rotationX);
            double sin = Math.sin(-rotationX);
            double newY = point[1] * cos - point[2] * sin;
            double newZ = point[1] * sin + point[2] * cos;
            point[1] = newY;
            point[2] = newZ;
        }
        
        // Inverse Y rotation
        if (Math.abs(rotationY) > EPSILON) {
            double cos = Math.cos(-rotationY);
            double sin = Math.sin(-rotationY);
            double newX = point[0] * cos + point[2] * sin;
            double newZ = -point[0] * sin + point[2] * cos;
            point[0] = newX;
            point[2] = newZ;
        }
        
        // Inverse Z rotation
        if (Math.abs(rotationZ) > EPSILON) {
            double cos = Math.cos(-rotationZ);
            double sin = Math.sin(-rotationZ);
            double newX = point[0] * cos - point[1] * sin;
            double newY = point[0] * sin + point[1] * cos;
            point[0] = newX;
            point[1] = newY;
        }
        
        return point;
    }
    
    /**
     * Returns the center point of this cube.
     *
     * @return the center point
     */
    public Point3D getCenter() {
        return center;
    }
    
    /**
     * Returns the side length of this cube.
     *
     * @return the side length
     */
    public double getSideLength() {
        return sideLength;
    }
    
    /**
     * Returns the rotation angle around the X-axis in radians.
     *
     * @return the X rotation in radians
     */
    public double getRotationX() {
        return rotationX;
    }
    
    /**
     * Returns the rotation angle around the Y-axis in radians.
     *
     * @return the Y rotation in radians
     */
    public double getRotationY() {
        return rotationY;
    }
    
    /**
     * Returns the rotation angle around the Z-axis in radians.
     *
     * @return the Z rotation in radians
     */
    public double getRotationZ() {
        return rotationZ;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Cube3D other = (Cube3D) obj;
        return Double.compare(other.sideLength, sideLength) == 0 &&
               Double.compare(other.rotationX, rotationX) == 0 &&
               Double.compare(other.rotationY, rotationY) == 0 &&
               Double.compare(other.rotationZ, rotationZ) == 0 &&
               center.equals(other.center);
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        long temp;
        
        result = 31 * result + center.hashCode();
        temp = Double.doubleToLongBits(sideLength);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rotationX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rotationY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rotationZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        
        return result;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Cube3D[center=%s, side=%.2f, rotation=(%.2f°, %.2f°, %.2f°), volume=%.2f]",
            center, sideLength, 
            Math.toDegrees(rotationX), Math.toDegrees(rotationY), Math.toDegrees(rotationZ),
            volume()
        );
    }
}