package com.csc205.project1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for the Point3D class.
 * 
 * <p>This test suite covers:
 * <ul>
 *   <li>Normal construction and basic operations</li>
 *   <li>Edge cases (zero coordinates, negative values, very large/small values)</li>
 *   <li>Invalid input handling (NaN, Infinity, null)</li>
 *   <li>Distance calculations (including special cases)</li>
 *   <li>Equality and hash code contracts</li>
 *   <li>Boundary conditions and numerical precision</li>
 * </ul>
 * 
 * <p><strong>Testing Principles Demonstrated:</strong>
 * <ul>
 *   <li><strong>Arrange-Act-Assert (AAA) Pattern:</strong> Each test clearly separates
 *       setup, execution, and verification phases</li>
 *   <li><strong>Test Isolation:</strong> Each test is independent and can run in any order</li>
 *   <li><strong>Descriptive Names:</strong> Test method names clearly describe what is being tested</li>
 *   <li><strong>Single Assertion Principle:</strong> Each test focuses on one specific behavior</li>
 *   <li><strong>Boundary Testing:</strong> Tests cover edge cases and boundary conditions</li>
 * </ul>
 *
 * @author Generated
 * @version 1.0
 */
@DisplayName("Point3D Unit Tests")
public class Point3DTest {
    
    // Epsilon for floating-point comparisons
    private static final double EPSILON = 1e-10;
    
    // Common test fixtures
    private Point3D origin;
    private Point3D unitX;
    private Point3D unitY;
    private Point3D unitZ;
    private Point3D allPositive;
    private Point3D allNegative;
    
    /**
     * Sets up common test fixtures before each test.
     * This follows the <strong>Test Fixture Pattern</strong> to reduce code duplication.
     */
    @BeforeEach
    void setUp() {
        origin = new Point3D(0.0, 0.0, 0.0);
        unitX = new Point3D(1.0, 0.0, 0.0);
        unitY = new Point3D(0.0, 1.0, 0.0);
        unitZ = new Point3D(0.0, 0.0, 1.0);
        allPositive = new Point3D(3.0, 4.0, 5.0);
        allNegative = new Point3D(-3.0, -4.0, -5.0);
    }
    
    // ========================================================================
    // Construction Tests
    // ========================================================================
    
    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create point with positive coordinates")
        void testConstructorWithPositiveCoordinates() {
            // Arrange & Act
            Point3D point = new Point3D(1.5, 2.5, 3.5);
            
            // Assert
            assertEquals(1.5, point.getX(), EPSILON);
            assertEquals(2.5, point.getY(), EPSILON);
            assertEquals(3.5, point.getZ(), EPSILON);
        }
        
        @Test
        @DisplayName("Should create point with negative coordinates")
        void testConstructorWithNegativeCoordinates() {
            // Arrange & Act
            Point3D point = new Point3D(-1.5, -2.5, -3.5);
            
            // Assert
            assertEquals(-1.5, point.getX(), EPSILON);
            assertEquals(-2.5, point.getY(), EPSILON);
            assertEquals(-3.5, point.getZ(), EPSILON);
        }
        
        @Test
        @DisplayName("Should create point with zero coordinates")
        void testConstructorWithZeroCoordinates() {
            // Arrange & Act
            Point3D point = new Point3D(0.0, 0.0, 0.0);
            
            // Assert
            assertEquals(0.0, point.getX(), EPSILON);
            assertEquals(0.0, point.getY(), EPSILON);
            assertEquals(0.0, point.getZ(), EPSILON);
        }
        
        @Test
        @DisplayName("Should create point with mixed positive and negative coordinates")
        void testConstructorWithMixedCoordinates() {
            // Arrange & Act
            Point3D point = new Point3D(1.0, -2.0, 3.0);
            
            // Assert
            assertEquals(1.0, point.getX(), EPSILON);
            assertEquals(-2.0, point.getY(), EPSILON);
            assertEquals(3.0, point.getZ(), EPSILON);
        }
        
        @Test
        @DisplayName("Should create point with very large coordinates")
        void testConstructorWithVeryLargeCoordinates() {
            // Arrange & Act
            double large = 1e100;
            Point3D point = new Point3D(large, large, large);
            
            // Assert
            assertEquals(large, point.getX(), EPSILON);
            assertEquals(large, point.getY(), EPSILON);
            assertEquals(large, point.getZ(), EPSILON);
        }
        
        @Test
        @DisplayName("Should create point with very small coordinates")
        void testConstructorWithVerySmallCoordinates() {
            // Arrange & Act
            double small = 1e-100;
            Point3D point = new Point3D(small, small, small);
            
            // Assert
            assertEquals(small, point.getX(), small * 0.01);
            assertEquals(small, point.getY(), small * 0.01);
            assertEquals(small, point.getZ(), small * 0.01);
        }
        
        @Test
        @DisplayName("Should throw exception when X is NaN")
        void testConstructorThrowsExceptionForNaNX() {
            // Arrange, Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Point3D(Double.NaN, 1.0, 1.0);
            });
            
            assertTrue(exception.getMessage().contains("NaN"));
        }
        
        @Test
        @DisplayName("Should throw exception when Y is NaN")
        void testConstructorThrowsExceptionForNaNY() {
            // Arrange, Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                new Point3D(1.0, Double.NaN, 1.0);
            });
        }
        
        @Test
        @DisplayName("Should throw exception when Z is NaN")
        void testConstructorThrowsExceptionForNaNZ() {
            // Arrange, Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                new Point3D(1.0, 1.0, Double.NaN);
            });
        }
        
        @Test
        @DisplayName("Should throw exception when X is positive infinity")
        void testConstructorThrowsExceptionForPositiveInfinityX() {
            // Arrange, Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Point3D(Double.POSITIVE_INFINITY, 1.0, 1.0);
            });
            
            assertTrue(exception.getMessage().contains("infinite"));
        }
        
        @Test
        @DisplayName("Should throw exception when Y is negative infinity")
        void testConstructorThrowsExceptionForNegativeInfinityY() {
            // Arrange, Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                new Point3D(1.0, Double.NEGATIVE_INFINITY, 1.0);
            });
        }
        
        @Test
        @DisplayName("Should throw exception when Z is positive infinity")
        void testConstructorThrowsExceptionForPositiveInfinityZ() {
            // Arrange, Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                new Point3D(1.0, 1.0, Double.POSITIVE_INFINITY);
            });
        }
        
        @Test
        @DisplayName("Should throw exception when all coordinates are NaN")
        void testConstructorThrowsExceptionForAllNaN() {
            // Arrange, Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                new Point3D(Double.NaN, Double.NaN, Double.NaN);
            });
        }
    }
    
    // ========================================================================
    // Getter Tests
    // ========================================================================
    
    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {
        
        @Test
        @DisplayName("Should return correct X coordinate")
        void testGetX() {
            // Arrange
            Point3D point = new Point3D(5.5, 0.0, 0.0);
            
            // Act
            double x = point.getX();
            
            // Assert
            assertEquals(5.5, x, EPSILON);
        }
        
        @Test
        @DisplayName("Should return correct Y coordinate")
        void testGetY() {
            // Arrange
            Point3D point = new Point3D(0.0, 7.7, 0.0);
            
            // Act
            double y = point.getY();
            
            // Assert
            assertEquals(7.7, y, EPSILON);
        }
        
        @Test
        @DisplayName("Should return correct Z coordinate")
        void testGetZ() {
            // Arrange
            Point3D point = new Point3D(0.0, 0.0, 9.9);
            
            // Act
            double z = point.getZ();
            
            // Assert
            assertEquals(9.9, z, EPSILON);
        }
    }
    
    // ========================================================================
    // Distance Calculation Tests
    // ========================================================================
    
    @Nested
    @DisplayName("Distance Calculation Tests")
    class DistanceTests {
        
        @Test
        @DisplayName("Should calculate distance from origin to unit point on X-axis")
        void testDistanceFromOriginToUnitX() {
            // Arrange
            // (using setUp fixtures: origin and unitX)
            
            // Act
            double distance = origin.distanceTo(unitX);
            
            // Assert
            assertEquals(1.0, distance, EPSILON);
        }
        
        @Test
        @DisplayName("Should calculate distance from origin to unit point on Y-axis")
        void testDistanceFromOriginToUnitY() {
            // Arrange & Act
            double distance = origin.distanceTo(unitY);
            
            // Assert
            assertEquals(1.0, distance, EPSILON);
        }
        
        @Test
        @DisplayName("Should calculate distance from origin to unit point on Z-axis")
        void testDistanceFromOriginToUnitZ() {
            // Arrange & Act
            double distance = origin.distanceTo(unitZ);
            
            // Assert
            assertEquals(1.0, distance, EPSILON);
        }
        
        @Test
        @DisplayName("Should calculate distance from origin to 3-4-5 right triangle point")
        void testDistanceFromOriginTo345Triangle() {
            // Arrange
            Point3D point345 = new Point3D(3.0, 4.0, 0.0);
            
            // Act
            double distance = origin.distanceTo(point345);
            
            // Assert
            assertEquals(5.0, distance, EPSILON);
        }
        
        @Test
        @DisplayName("Should calculate distance for 3D Pythagorean triple")
        void testDistance3DPythagoreanTriple() {
            // Arrange
            // Distance from origin to (3, 4, 5) should be sqrt(9 + 16 + 25) = sqrt(50) ≈ 7.071
            
            // Act
            double distance = origin.distanceTo(allPositive);
            
            // Assert
            double expected = Math.sqrt(50);
            assertEquals(expected, distance, EPSILON);
        }
        
        @Test
        @DisplayName("Should return zero distance from point to itself")
        void testDistanceToSelf() {
            // Arrange & Act
            double distance = allPositive.distanceTo(allPositive);
            
            // Assert
            assertEquals(0.0, distance, EPSILON);
        }
        
        @Test
        @DisplayName("Should calculate symmetric distance (distance from A to B equals B to A)")
        void testDistanceSymmetry() {
            // Arrange & Act
            double distanceAB = allPositive.distanceTo(allNegative);
            double distanceBA = allNegative.distanceTo(allPositive);
            
            // Assert
            assertEquals(distanceAB, distanceBA, EPSILON);
        }
        
        @Test
        @DisplayName("Should calculate distance between opposite signed points")
        void testDistanceBetweenOppositePoints() {
            // Arrange
            Point3D p1 = new Point3D(1.0, 2.0, 3.0);
            Point3D p2 = new Point3D(-1.0, -2.0, -3.0);
            
            // Act
            double distance = p1.distanceTo(p2);
            
            // Assert
            // Distance should be 2 * sqrt(1 + 4 + 9) = 2 * sqrt(14)
            double expected = 2 * Math.sqrt(14);
            assertEquals(expected, distance, EPSILON);
        }
        
        @Test
        @DisplayName("Should calculate distance with very small difference")
        void testDistanceWithVerySmallDifference() {
            // Arrange
            Point3D p1 = new Point3D(1.0, 1.0, 1.0);
            Point3D p2 = new Point3D(1.0 + 1e-10, 1.0, 1.0);
            
            // Act
            double distance = p1.distanceTo(p2);
            
            // Assert
            assertEquals(1e-10, distance, 1e-11);
        }
        
        @Test
        @DisplayName("Should calculate distance with very large coordinates")
        void testDistanceWithVeryLargeCoordinates() {
            // Arrange
            double large = 1e50;
            Point3D p1 = new Point3D(large, 0, 0);
            Point3D p2 = new Point3D(0, 0, 0);
            
            // Act
            double distance = p1.distanceTo(p2);
            
            // Assert
            assertEquals(large, distance, large * EPSILON);
        }
        
        @Test
        @DisplayName("Should throw exception when calculating distance to null point")
        void testDistanceToNullThrowsException() {
            // Arrange & Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                origin.distanceTo(null);
            });
            
            assertTrue(exception.getMessage().contains("null"));
        }
        
        @Test
        @DisplayName("Should satisfy triangle inequality")
        void testTriangleInequality() {
            // Arrange
            Point3D p1 = new Point3D(0, 0, 0);
            Point3D p2 = new Point3D(3, 4, 0);
            Point3D p3 = new Point3D(3, 0, 0);
            
            // Act
            double d12 = p1.distanceTo(p2);
            double d23 = p2.distanceTo(p3);
            double d13 = p1.distanceTo(p3);
            
            // Assert - Triangle inequality: d(A,C) <= d(A,B) + d(B,C)
            assertTrue(d13 <= d12 + d23 + EPSILON);
            assertTrue(d12 <= d13 + d23 + EPSILON);
            assertTrue(d23 <= d12 + d13 + EPSILON);
        }
    }
    
    // ========================================================================
    // Equality Tests
    // ========================================================================
    
    @Nested
    @DisplayName("Equality and Hash Code Tests")
    class EqualityTests {
        
        @Test
        @DisplayName("Should be equal to itself (reflexive)")
        void testEqualityReflexive() {
            // Arrange & Act & Assert
            assertEquals(allPositive, allPositive);
        }
        
        @Test
        @DisplayName("Should be equal to point with same coordinates")
        void testEqualityWithSameCoordinates() {
            // Arrange
            Point3D p1 = new Point3D(1.5, 2.5, 3.5);
            Point3D p2 = new Point3D(1.5, 2.5, 3.5);
            
            // Act & Assert
            assertEquals(p1, p2);
        }
        
        @Test
        @DisplayName("Should be symmetric (if A equals B, then B equals A)")
        void testEqualitySymmetric() {
            // Arrange
            Point3D p1 = new Point3D(1.0, 2.0, 3.0);
            Point3D p2 = new Point3D(1.0, 2.0, 3.0);
            
            // Act & Assert
            assertEquals(p1, p2);
            assertEquals(p2, p1);
        }
        
        @Test
        @DisplayName("Should be transitive (if A equals B and B equals C, then A equals C)")
        void testEqualityTransitive() {
            // Arrange
            Point3D p1 = new Point3D(1.0, 2.0, 3.0);
            Point3D p2 = new Point3D(1.0, 2.0, 3.0);
            Point3D p3 = new Point3D(1.0, 2.0, 3.0);
            
            // Act & Assert
            assertEquals(p1, p2);
            assertEquals(p2, p3);
            assertEquals(p1, p3);
        }
        
        @Test
        @DisplayName("Should not be equal to null")
        void testNotEqualToNull() {
            // Arrange & Act & Assert
            assertNotEquals(null, allPositive);
        }
        
        @Test
        @DisplayName("Should not be equal to different type")
        void testNotEqualToDifferentType() {
            // Arrange & Act & Assert
            assertNotEquals(allPositive, "Not a Point3D");
        }
        
        @Test
        @DisplayName("Should not be equal when X coordinate differs")
        void testNotEqualWhenXDiffers() {
            // Arrange
            Point3D p1 = new Point3D(1.0, 2.0, 3.0);
            Point3D p2 = new Point3D(1.1, 2.0, 3.0);
            
            // Act & Assert
            assertNotEquals(p1, p2);
        }
        
        @Test
        @DisplayName("Should not be equal when Y coordinate differs")
        void testNotEqualWhenYDiffers() {
            // Arrange
            Point3D p1 = new Point3D(1.0, 2.0, 3.0);
            Point3D p2 = new Point3D(1.0, 2.1, 3.0);
            
            // Act & Assert
            assertNotEquals(p1, p2);
        }
        
        @Test
        @DisplayName("Should not be equal when Z coordinate differs")
        void testNotEqualWhenZDiffers() {
            // Arrange
            Point3D p1 = new Point3D(1.0, 2.0, 3.0);
            Point3D p2 = new Point3D(1.0, 2.0, 3.1);
            
            // Act & Assert
            assertNotEquals(p1, p2);
        }
        
        @Test
        @DisplayName("Should have same hash code when equal")
        void testHashCodeConsistentWithEquals() {
            // Arrange
            Point3D p1 = new Point3D(1.5, 2.5, 3.5);
            Point3D p2 = new Point3D(1.5, 2.5, 3.5);
            
            // Act & Assert
            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }
        
        @Test
        @DisplayName("Should have consistent hash code across multiple calls")
        void testHashCodeConsistency() {
            // Arrange
            Point3D point = new Point3D(1.0, 2.0, 3.0);
            
            // Act
            int hash1 = point.hashCode();
            int hash2 = point.hashCode();
            int hash3 = point.hashCode();
            
            // Assert
            assertEquals(hash1, hash2);
            assertEquals(hash2, hash3);
        }
        
        @Test
        @DisplayName("Should likely have different hash codes for different points")
        void testHashCodeDifferentForDifferentPoints() {
            // Arrange
            Point3D p1 = new Point3D(1.0, 2.0, 3.0);
            Point3D p2 = new Point3D(4.0, 5.0, 6.0);
            
            // Act
            int hash1 = p1.hashCode();
            int hash2 = p2.hashCode();
            
            // Assert
            // Note: Different objects CAN have same hash code (collision),
            // but it should be unlikely for very different coordinates
            assertNotEquals(hash1, hash2);
        }
        
        @Test
        @DisplayName("Should handle zero coordinates in equality")
        void testEqualityWithZeroCoordinates() {
            // Arrange
            Point3D origin1 = new Point3D(0.0, 0.0, 0.0);
            Point3D origin2 = new Point3D(0.0, 0.0, 0.0);
            
            // Act & Assert
            assertEquals(origin1, origin2);
        }
        
        @Test
        @DisplayName("Should handle negative coordinates in equality")
        void testEqualityWithNegativeCoordinates() {
            // Arrange
            Point3D p1 = new Point3D(-1.0, -2.0, -3.0);
            Point3D p2 = new Point3D(-1.0, -2.0, -3.0);
            
            // Act & Assert
            assertEquals(p1, p2);
        }
    }
    
    // ========================================================================
    // toString Tests
    // ========================================================================
    
    @Nested
    @DisplayName("toString Tests")
    class ToStringTests {
        
        @Test
        @DisplayName("Should return non-null string")
        void testToStringNotNull() {
            // Arrange & Act
            String result = allPositive.toString();
            
            // Assert
            assertNotNull(result);
        }
        
        @Test
        @DisplayName("Should contain all coordinate values")
        void testToStringContainsCoordinates() {
            // Arrange
            Point3D point = new Point3D(1.5, 2.5, 3.5);
            
            // Act
            String result = point.toString();
            
            // Assert
            assertTrue(result.contains("1.5") || result.contains("1.50"));
            assertTrue(result.contains("2.5") || result.contains("2.50"));
            assertTrue(result.contains("3.5") || result.contains("3.50"));
        }
        
        @Test
        @DisplayName("Should handle zero coordinates in string")
        void testToStringWithZeroCoordinates() {
            // Arrange & Act
            String result = origin.toString();
            
            // Assert
            assertNotNull(result);
            assertTrue(result.contains("0") || result.contains("0.0") || result.contains("0.00"));
        }
        
        @Test
        @DisplayName("Should handle negative coordinates in string")
        void testToStringWithNegativeCoordinates() {
            // Arrange & Act
            String result = allNegative.toString();
            
            // Assert
            assertNotNull(result);
            assertTrue(result.contains("-"));
        }
    }
    
    // ========================================================================
    // Immutability Tests
    // ========================================================================
    
    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {
        
        @Test
        @DisplayName("Should not have setter methods (compile-time check documented)")
        void testNoSetters() {
            // This is a compile-time guarantee - Point3D has no setters
            // We document this with a test that verifies coordinates don't change
            
            // Arrange
            Point3D point = new Point3D(1.0, 2.0, 3.0);
            double originalX = point.getX();
            double originalY = point.getY();
            double originalZ = point.getZ();
            
            // Act - Try to use the point in various ways
            point.distanceTo(origin);
            point.toString();
            point.hashCode();
            point.equals(origin);
            
            // Assert - Coordinates should be unchanged
            assertEquals(originalX, point.getX(), EPSILON);
            assertEquals(originalY, point.getY(), EPSILON);
            assertEquals(originalZ, point.getZ(), EPSILON);
        }
        
        @Test
        @DisplayName("Should maintain consistent state across multiple operations")
        void testConsistentStateAcrossOperations() {
            // Arrange
            Point3D point = new Point3D(5.5, 6.6, 7.7);
            
            // Act - Perform multiple operations
            for (int i = 0; i < 100; i++) {
                point.distanceTo(origin);
                point.hashCode();
                point.toString();
            }
            
            // Assert - State should be unchanged
            assertEquals(5.5, point.getX(), EPSILON);
            assertEquals(6.6, point.getY(), EPSILON);
            assertEquals(7.7, point.getZ(), EPSILON);
        }
    }
    
    // ========================================================================
    // Edge Case and Boundary Tests
    // ========================================================================
    
    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Should handle points with very small coordinate differences")
        void testVerySmallCoordinateDifferences() {
            // Arrange
            Point3D p1 = new Point3D(1.0, 1.0, 1.0);
            Point3D p2 = new Point3D(1.0000000001, 1.0, 1.0);
            
            // Act & Assert
            // These should NOT be equal (different coordinates)
            assertNotEquals(p1, p2);
        }
        
        @Test
        @DisplayName("Should handle maximum double value")
        void testMaxDoubleValue() {
            // Arrange
            double max = Double.MAX_VALUE;
            
            // Act
            Point3D point = new Point3D(max, max, max);
            
            // Assert
            assertEquals(max, point.getX());
            assertEquals(max, point.getY());
            assertEquals(max, point.getZ());
        }
        
        @Test
        @DisplayName("Should handle minimum positive double value")
        void testMinPositiveDoubleValue() {
            // Arrange
            double min = Double.MIN_VALUE;
            
            // Act
            Point3D point = new Point3D(min, min, min);
            
            // Assert
            assertEquals(min, point.getX());
            assertEquals(min, point.getY());
            assertEquals(min, point.getZ());
        }
        
        @Test
        @DisplayName("Should handle mixture of zero and non-zero coordinates")
        void testMixtureOfZeroAndNonZero() {
            // Arrange & Act
            Point3D p1 = new Point3D(0.0, 5.0, 0.0);
            Point3D p2 = new Point3D(5.0, 0.0, 0.0);
            Point3D p3 = new Point3D(0.0, 0.0, 5.0);
            
            // Assert
            assertEquals(5.0, p1.distanceTo(origin), EPSILON);
            assertEquals(5.0, p2.distanceTo(origin), EPSILON);
            assertEquals(5.0, p3.distanceTo(origin), EPSILON);
        }
        
        @Test
        @DisplayName("Should handle collinear points on X-axis")
        void testCollinearPointsOnXAxis() {
            // Arrange
            Point3D p1 = new Point3D(1.0, 0.0, 0.0);
            Point3D p2 = new Point3D(2.0, 0.0, 0.0);
            Point3D p3 = new Point3D(3.0, 0.0, 0.0);
            
            // Act
            double d12 = p1.distanceTo(p2);
            double d23 = p2.distanceTo(p3);
            double d13 = p1.distanceTo(p3);
            
            // Assert - For collinear points: d13 = d12 + d23
            assertEquals(d13, d12 + d23, EPSILON);
        }
        
        @Test
        @DisplayName("Should handle collinear points on Y-axis")
        void testCollinearPointsOnYAxis() {
            // Arrange
            Point3D p1 = new Point3D(0.0, 1.0, 0.0);
            Point3D p2 = new Point3D(0.0, 2.0, 0.0);
            Point3D p3 = new Point3D(0.0, 3.0, 0.0);
            
            // Act
            double d12 = p1.distanceTo(p2);
            double d23 = p2.distanceTo(p3);
            double d13 = p1.distanceTo(p3);
            
            // Assert
            assertEquals(d13, d12 + d23, EPSILON);
        }
        
        @Test
        @DisplayName("Should handle collinear points on Z-axis")
        void testCollinearPointsOnZAxis() {
            // Arrange
            Point3D p1 = new Point3D(0.0, 0.0, 1.0);
            Point3D p2 = new Point3D(0.0, 0.0, 2.0);
            Point3D p3 = new Point3D(0.0, 0.0, 3.0);
            
            // Act
            double d12 = p1.distanceTo(p2);
            double d23 = p2.distanceTo(p3);
            double d13 = p1.distanceTo(p3);
            
            // Assert
            assertEquals(d13, d12 + d23, EPSILON);
        }
        
        @Test
        @DisplayName("Should handle points forming a right angle")
        void testRightAnglePoints() {
            // Arrange
            Point3D p1 = new Point3D(0.0, 0.0, 0.0);
            Point3D p2 = new Point3D(3.0, 0.0, 0.0);
            Point3D p3 = new Point3D(0.0, 4.0, 0.0);
            
            // Act
            double d12 = p1.distanceTo(p2);  // 3
            double d13 = p1.distanceTo(p3);  // 4
            double d23 = p2.distanceTo(p3);  // 5
            
            // Assert - Pythagorean theorem: 3² + 4² = 5²
            assertEquals(3.0, d12, EPSILON);
            assertEquals(4.0, d13, EPSILON);
            assertEquals(5.0, d23, EPSILON);
            assertEquals(d12 * d12 + d13 * d13, d23 * d23, EPSILON);
        }
        
        @Test
        @DisplayName("Should handle negative zero")
        void testNegativeZero() {
            // Arrange
            double negativeZero = -0.0;
            double positiveZero = 0.0;
            
            // Act
            Point3D p1 = new Point3D(negativeZero, negativeZero, negativeZero);
            Point3D p2 = new Point3D(positiveZero, positiveZero, positiveZero);
            
            // Assert - In Java, -0.0 == 0.0, so these should be equal
            assertEquals(p1, p2);
        }
    }
    
    // ========================================================================
    // Thread Safety Tests (Immutability ensures thread-safety)
    // ========================================================================
    
    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {
        
        @Test
        @DisplayName("Should be safely shareable across multiple threads")
        void testThreadSafety() throws InterruptedException {
            // Arrange
            Point3D sharedPoint = new Point3D(1.0, 2.0, 3.0);
            final int numThreads = 10;
            final int operationsPerThread = 1000;
            
            Thread[] threads = new Thread[numThreads];
            
            // Act - Multiple threads reading from the same point
            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < operationsPerThread; j++) {
                        double x = sharedPoint.getX();
                        double y = sharedPoint.getY();
                        double z = sharedPoint.getZ();
                        sharedPoint.distanceTo(origin);
                        sharedPoint.hashCode();
                        sharedPoint.toString();
                        
                        // Verify values haven't changed
                        assertEquals(1.0, x, EPSILON);
                        assertEquals(2.0, y, EPSILON);
                        assertEquals(3.0, z, EPSILON);
                    }
                });
                threads[i].start();
            }
            
            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }
            
            // Assert - Point should still have original values
            assertEquals(1.0, sharedPoint.getX(), EPSILON);
            assertEquals(2.0, sharedPoint.getY(), EPSILON);
            assertEquals(3.0, sharedPoint.getZ(), EPSILON);
        }
    }
}