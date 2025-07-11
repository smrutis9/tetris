 package assignment;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import static org.junit.Assert.assertEquals;

// /*
//  * Any comments and methods here are purely descriptions or suggestions.
//  * This is your test file. Feel free to change this as much as you want.
//  */

// public class TetrisTest {

//     // This will run ONCE before all other tests. It can be useful to setup up
//     // global variables and anything needed for all of the tests.

//     @BeforeAll
//     static void setupAll() {

//     }

//     // This will run before EACH test.
//     @BeforeEach
//     void setupEach() {
//     }

//     // You can test execute critter here. You may want to make additional tests and
//     // your own testing harness. See spec section 2.5 for more details.
//     @Test
//     void testTetrisPiece() {

//     }

//     // Test load species. You may want to make more tests for different cases here.
//     @Test
//     void testTetrisBoard() {

//     }

// }
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assignment.Piece.PieceType;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.Arrays;

public class TetrisTest {

    private TetrisPiece piece;

    @BeforeEach
    public void setUp() {
        // Create a TetrisPiece instance for testing
        piece = new TetrisPiece(PieceType.STICK);
    }

    @Test
    public void testGetType() {
        assertEquals(PieceType.STICK, piece.getType());
    }

    @Test
    public void testGetRotationIndex() {
        assertEquals(0, piece.getRotationIndex());
    }

    @Test
    public void testGetWidth() {
        assertEquals(4, piece.getWidth());
    }

    @Test
    public void testGetHeight() {
        assertEquals(4, piece.getHeight());
    }

    @Test
    public void testGetBody() {
        // Test the body points for the initial piece configuration
        Point[] body = piece.getBody();
        assertEquals(4, body.length);
        assertTrue(Arrays.asList(body).contains(new Point(0, 2)));
        assertTrue(Arrays.asList(body).contains(new Point(1, 2)));
        assertTrue(Arrays.asList(body).contains(new Point(2, 2)));
        assertTrue(Arrays.asList(body).contains(new Point(3, 2)));
    }

    @Test
    public void testGetSkirt() {
        // Test the skirt values for the initial piece configuration
        int[] skirt = piece.getSkirt();
        assertEquals(4, skirt.length);
        assertArrayEquals(new int[]{2, 2, 2, 2}, skirt);
    }

    @Test
    public void testClockwisePiece() {
        // Test clockwise rotation
        Piece rotatedPiece = piece.clockwisePiece();
        assertEquals(1, rotatedPiece.getRotationIndex());
        // Add more assertions to validate the rotated piece's properties
    }

    // @Test
    // public void testCounterclockwisePiece() {
    //     // Test counterclockwise rotation
    //     Piece rotatedPiece = piece.counterclockwise();
    //     assertEquals(3, rotatedPiece.getRotationIndex());
    //     // Add more assertions to validate the rotated piece's properties
    // }

    // @Test
    // public void testEquals() {
    //     // Test equals method
    //     Piece rotatedPiece = piece.counterclockwisePiece.counterclockwise();
    //     assertEquals(3, rotatedPiece.getRotationIndex());
    //     // Add more assertions to validate the rotated piece's properties
    // }
}
