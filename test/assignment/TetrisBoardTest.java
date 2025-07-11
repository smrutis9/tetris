package assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assignment.Board.Result;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

public class TetrisBoardTest{

    private TetrisBoard board;

    @BeforeEach
    public void setUp() {
        // Create a TetrisBoard instance for testing
        board = new TetrisBoard(10, 20);
    }

    @Test
    public void testConstructor() {
        // Test the constructor's initialization of the board
        assertEquals(10, board.getWidth());
        assertEquals(20, board.getHeight());
        assertEquals(0, board.getRowsCleared());
        assertNull(board.getCurrentPiece());
        assertEquals(Board.Result.NO_PIECE,board.getLastResult());
        assertEquals(Board.Action.NOTHING,board.getLastAction());

        // Add more assertions to validate other properties of the board
    }

@Test
public void testMove() {
    // Create a Tetris piece and set it as the current piece
    Piece piece = new TetrisPiece(Piece.PieceType.LEFT_L);
    Point initialPosition = new Point(4, 0);
    board.nextPiece(piece, initialPosition);

    // Move the piece left
    Board.Result leftResult = board.move(Board.Action.LEFT);
    // Verify the result is SUCCESS since the piece can move left
    assertEquals(Board.Result.SUCCESS, leftResult);

    // Move the piece right
    Board.Result rightResult = board.move(Board.Action.RIGHT);
    // Verify the result is SUCCESS since the piece can move right
    assertEquals(Board.Result.SUCCESS, rightResult);

    // Move the piece down
    Board.Result downResult = board.move(Board.Action.DOWN);
    // Verify the result is SUCCESS since the piece can move down
    assertEquals(Board.Result.SUCCESS, downResult);

    // Rotate the piece clockwise
    Board.Result rotateResult = board.move(Board.Action.CLOCKWISE);
    // Verify the result is SUCCESS since the piece can rotate clockwise
    assertEquals(Board.Result.SUCCESS, rotateResult);

    // Try to move the piece down until it's placed
    while (board.getLastResult() != Board.Result.PLACE) {
        downResult = board.move(Board.Action.DOWN);
    }

    // Verify the result is PLACE since the piece has been placed at the lowest position
    assertEquals(Board.Result.PLACE, downResult);
}

     @Test
    public void testMoveLeft() {
        Piece piece = new TetrisPiece(Piece.PieceType.RIGHT_L);
        Point initialPosition = new Point(5, 0);
        board.nextPiece(piece, initialPosition);

    	Board.Result result = board.move(Board.Action.LEFT);

     	assertEquals(Board.Result.SUCCESS, result);

    	Point newPosition = board.getCurrentPiecePosition();
    	assertEquals(initialPosition.x - 1, (int) newPosition.getX());
    	assertEquals(initialPosition.y, (int) newPosition.getY());

    	assertEquals(Board.Action.LEFT, board.getLastAction());
    }


    @Test
    public void testMoveRight() {
        Piece piece = new TetrisPiece(Piece.PieceType.RIGHT_L);
        Point initialPosition = new Point(5, 0);
        board.nextPiece(piece, initialPosition);

    	Board.Result result = board.move(Board.Action.RIGHT);

     	assertEquals(Board.Result.SUCCESS, result);

    	Point newPosition = board.getCurrentPiecePosition();
    	assertEquals(initialPosition.x + 1, (int) newPosition.getX());
    	assertEquals(initialPosition.y, (int) newPosition.getY());

    	assertEquals(Board.Action.RIGHT, board.getLastAction());
    }

    @Test
    public void testMoveDown() {
        Piece piece = new TetrisPiece(Piece.PieceType.RIGHT_L);
        Point initialPosition = new Point(5, 0);
        board.nextPiece(piece, initialPosition);

    	Board.Result result = board.move(Board.Action.DOWN);

     	assertEquals(Board.Result.SUCCESS, result);

    	Point newPosition = board.getCurrentPiecePosition();
    	assertEquals(initialPosition.x, (int) newPosition.getX());
    	assertEquals(initialPosition.y + 1, (int) newPosition.getY());

    	assertEquals(Board.Action.RIGHT, board.getLastAction());
    }

    @Test
    public void testRotateClockwise() {
    	Piece piece = new TetrisPiece(Piece.PieceType.RIGHT_L);
    	Point initialPosition = new Point(4, 0);
    	board.nextPiece(piece, initialPosition);

    	Board.Result result = board.move(Board.Action.CLOCKWISE);

    	assertEquals(Board.Result.SUCCESS, result);

    	Point newPosition = board.getCurrentPiecePosition();
   	assertEquals(initialPosition.x, (int) newPosition.getX());
    	assertEquals(initialPosition.y, (int) newPosition.getY());

    	assertEquals(1, board.getCurrentPiece().getRotationIndex());

    	assertEquals(Board.Action.CLOCKWISE, board.getLastAction());
     }


    @Test
    public void testRotateCounterclockwise() {
    	Piece piece = new TetrisPiece(Piece.PieceType.RIGHT_L);
    	Point initialPosition = new Point(4, 0);
    	board.nextPiece(piece, initialPosition);

    	Board.Result result = board.move(Board.Action.COUNTERCLOCKWISE);

    	assertEquals(Board.Result.SUCCESS, result);

    	Point newPosition = board.getCurrentPiecePosition();
   	assertEquals(initialPosition.x, (int) newPosition.getX());
    	assertEquals(initialPosition.y, (int) newPosition.getY());

    	assertEquals(3, board.getCurrentPiece().getRotationIndex());

    	assertEquals(Board.Action.COUNTERCLOCKWISE, board.getLastAction());
    }

    @Test
    public void testDrop() {
    	Piece piece = new TetrisPiece(Piece.PieceType.SQUARE);
    	Point initialPosition = new Point(4, 0);
    	board.nextPiece(piece, initialPosition);

    	Board.Result result = board.move(Board.Action.DROP);

    	assertEquals(Board.Result.PLACE, result);

    	Point newPosition = board.getCurrentPiecePosition();
    	assertEquals(initialPosition.x, (int) newPosition.getX());

    	assertTrue(board.getRowsCleared() >= 0);

    	assertEquals(Board.Action.DROP, board.getLastAction());
    }

@Test
public void testGetCurrentPiece() {
    // Create a Tetris piece (e.g., L piece) and set it as the current piece
    Piece initialPiece = new TetrisPiece(Piece.PieceType.RIGHT_L);
    Point initialPosition = new Point(4, 0);
    board.nextPiece(initialPiece, initialPosition);

    // Get the current piece from the board
    Piece currentPiece = board.getCurrentPiece();

    // Verify that the retrieved current piece is equal to the initially set piece
    assertEquals(initialPiece, currentPiece);
}

@Test
public void testGetCurrentPiecePosition() {
    // Create a Tetris piece (e.g., I piece) and set it as the current piece
    Piece initialPiece = new TetrisPiece(Piece.PieceType.STICK);
    Point initialPosition = new Point(4, 0);
    board.nextPiece(initialPiece, initialPosition);

    // Get the current piece's position from the board
    Point currentPosition = board.getCurrentPiecePosition();

    // Verify that the retrieved current piece's position matches the initially set position
    assertEquals(initialPosition, currentPosition);
}

    @Test
    public void testNextPiece() {
    	Piece initialPiece = new TetrisPiece(Piece.PieceType.RIGHT_DOG);
    	Point initialPosition = new Point(4, 0);
    	board.nextPiece(initialPiece, initialPosition);

   	Piece nextPiece = new TetrisPiece(Piece.PieceType.RIGHT_L);
    	Point spawnPosition = new Point(0, 0);
    	board.nextPiece(nextPiece, spawnPosition);

    	assertEquals(nextPiece, board.getCurrentPiece());

    	assertEquals(spawnPosition, board.getCurrentPiecePosition());
    }


    @Test
    public void testGetGrid() {
    	Piece piece = new TetrisPiece(Piece.PieceType.STICK);
    	Point initialPosition = new Point(3, 15);
    	board.nextPiece(piece, initialPosition);

    	board.move(Board.Action.DROP);

    	int pieceWidth = piece.getWidth();
   	int pieceHeight = piece.getHeight();

    	for (int x = 0; x < pieceWidth; x++) {
        	for (int y = 0; y < pieceHeight; y++) {
            	Point gridPosition = new Point(initialPosition.x + x, initialPosition.y - y);
            	Piece.PieceType gridValue = board.getGrid(gridPosition.x, gridPosition.y);
            
            	if (x < pieceWidth && y < pieceHeight) {
                	assertEquals(piece.getType(), gridValue);
            	} else {
                	assertNull(gridValue);
            	}
        	}
    	}

    }

@Test
public void testEquals() {
    // Create a Tetris board with an initial configuration
    TetrisBoard board1 = new TetrisBoard(10, 20);
    TetrisBoard board2 = new TetrisBoard(10, 20);

    // Verify that two newly created boards are equal
    assertTrue(board1.equals(board2));

    // Perform some actions on board1
    board1.move(Board.Action.LEFT);
    board1.move(Board.Action.DOWN);

    // Verify that the boards are no longer equal after actions
    assertFalse(board1.equals(board2));

    // Create another Tetris board with the same initial configuration as board1
    TetrisBoard board3 = new TetrisBoard(10, 20);
    assertTrue(board1.equals(board3));
}


@Test
public void testGetLastResult() {
    // Create a Tetris piece and set it as the current piece
    Piece piece = new TetrisPiece(Piece.PieceType.T);
    Point initialPosition = new Point(4, 0);
    board.nextPiece(piece, initialPosition);

    // Move the piece down until it's placed
    while (board.getLastResult() != Board.Result.PLACE) {
        board.move(Board.Action.DOWN);
    }

    // Verify that the last result is PLACE since the piece has been placed
    assertEquals(Board.Result.PLACE, board.getLastResult());
}

@Test
public void testGetLastAction() {
    Piece piece = new TetrisPiece(Piece.PieceType.SQUARE);
    Point initialPosition = new Point(4, 0);
    board.nextPiece(piece, initialPosition);

    board.move(Board.Action.LEFT);

    assertEquals(Board.Action.LEFT, board.getLastAction());

    board.move(Board.Action.DOWN);

    assertEquals(Board.Action.DOWN, board.getLastAction());

    board.move(Board.Action.CLOCKWISE);

    assertEquals(Board.Action.CLOCKWISE, board.getLastAction());
}


@Test
public void testGetRowsCleared() {
    Piece piece = new TetrisPiece(Piece.PieceType.LEFT_L);
    Point initialPosition = new Point(4, 0);
    board.nextPiece(piece, initialPosition);

    while (board.getLastResult() != Board.Result.PLACE) {
        board.move(Board.Action.DOWN);
    }

    assertEquals(2, board.getRowsCleared());
}


@Test
public void testGetWidth() {
    int width = 10;
    int height = 20;
    TetrisBoard board = new TetrisBoard(width, height);

    assertEquals(width, board.getWidth());
}

@Test
public void testGetHeight() {
    int width = 10;
    int height = 20;
    TetrisBoard board = new TetrisBoard(width, height);

    assertEquals(height, board.getHeight());
}

@Test
public void testGetMaxHeight() {
    int width = 10;
    int height = 20;
    TetrisBoard board = new TetrisBoard(width, height);

    board.nextPiece(new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(4, 0));
    board.move(Board.Action.DOWN);
    board.move(Board.Action.DOWN);

    assertEquals(2, board.getMaxHeight());
}


@Test
public void testDropHeight() {
    int width = 10;
    int height = 20;
    TetrisBoard board = new TetrisBoard(width, height);

    Piece piece = new TetrisPiece(Piece.PieceType.STICK);
    Point initialPosition = new Point(4, 0);
    board.nextPiece(piece, initialPosition);

    int calculatedDropHeight = board.dropHeight(piece, initialPosition.x);

    assertEquals(-2, calculatedDropHeight); 
}

@Test
public void testGetColumnHeight() {
    int width = 10;
    int height = 20;
    TetrisBoard board = new TetrisBoard(width, height);

    Piece piece = new TetrisPiece(Piece.PieceType.T);
    Point initialPosition = new Point(4, 0);
    board.nextPiece(piece, initialPosition);

    // Calculate the column height for column 4 (the column where the piece is)
    int columnHeight = board.getColumnHeight(4);

    // Verify that the calculated column height is correct
    assertEquals(0, columnHeight); // The piece is at the top

    // Add more assertions to validate other properties and edge cases if needed
}

@Test
public void testGetRowWidth() {
    // Create a Tetris board with a specified width and height
    int width = 10;
    int height = 20;
    TetrisBoard board = new TetrisBoard(width, height);

    // Create a Tetris piece (e.g., L piece) and set it as the current piece
    Piece piece = new TetrisPiece(Piece.PieceType.RIGHT_L);
    Point initialPosition = new Point(4, 0);
    board.nextPiece(piece, initialPosition);

    // Calculate the row width for row 0 (the top row where the piece is placed)
    int rowWidth = board.getRowWidth(0);

    // Verify that the calculated row width is correct
    assertEquals(0, rowWidth); // The L piece spans 4 columns in the top row

    // Add more assertions to validate other properties and edge cases if needed
}
}