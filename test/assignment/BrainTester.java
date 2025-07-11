package assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import assignment.Board;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import assignment.CoolAlgo;
import assignment.TetrisBoard;

public class BrainTester
{
    private CoolAlgo test;
    private TetrisBoard board;
    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;

    @BeforeEach
    public void setUp() {
        test = new CoolAlgo();
        board = new TetrisBoard(10, 20);
    }
    public void enumerateOptions(Board currentBoard) {
        // We can always drop our current Piece
        options.add(currentBoard.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.DROP);
        Board.Action rotation = null;
        for(int i = 0; i < 4; i++){
            if(i==3){
                rotation = Board.Action.COUNTERCLOCKWISE;
            }

            Board left = currentBoard.testMove(Board.Action.LEFT);
            while (left.getLastResult() == Board.Result.SUCCESS) {
                options.add(left.testMove(Board.Action.DROP));
                if(rotation == null){
                    firstMoves.add(Board.Action.LEFT);
                }
                else{
                    firstMoves.add(rotation);
                }
                left.move(Board.Action.LEFT);
            }

        // And then the same thing to the right
            Board right = currentBoard.testMove(Board.Action.RIGHT);
            while (right.getLastResult() == Board.Result.SUCCESS) {
                options.add(right.testMove(Board.Action.DROP));
                if(rotation == null){
                    firstMoves.add(Board.Action.RIGHT);
                }
                else{
                    firstMoves.add(rotation);
                }
                right.move(Board.Action.RIGHT);
            } 
            rotation = Board.Action.CLOCKWISE;
            currentBoard = currentBoard.testMove(Board.Action.CLOCKWISE);
            }
    }
    @Test
    public void testEnumerateOptions(Board currentBoard) {
        enumerateOptions(currentBoard);
        assertEquals(0, options.size());
        assertEquals(0, firstMoves.size());
    }

}

