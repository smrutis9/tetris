package assignment;

import java.util.*;

/**
 * A Lame Brain implementation for JTetris; tries all possible places to put the
 * piece (but ignoring rotations, because we're lame), trying to minimize the
 * total height of pieces on the board.
 */
public class CoolAlgo implements Brain {

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;
    private int count;

    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);

        double best = 0;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            double score = scoreBoard(options.get(i));
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }
        // We want to return the first move on the way to the best Board
        return firstMoves.get(bestIndex);
    }

    /**
     * Test all of the places we can put the current Piece.
     * Since this is just a Lame Brain, we aren't going to do smart
     * things like rotating pieces.
     */
    private void enumerateOptions(Board currentBoard) {
        // We can always drop our current Piece
        options.add(currentBoard.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.DROP);
        Board.Action rotation_index = null;
        for(int i = 0; i < 4; i++){
            if(i==3){
                rotation_index = Board.Action.COUNTERCLOCKWISE;
            }

            Board left = currentBoard.testMove(Board.Action.LEFT);
            while (left.getLastResult() == Board.Result.SUCCESS) {
                options.add(left.testMove(Board.Action.DROP));
                if(rotation_index == null){
                    firstMoves.add(Board.Action.LEFT);
                }
                else{
                    firstMoves.add(rotation_index);
                }
                left.move(Board.Action.LEFT);
            }

        // And then the same thing to the right
            Board right = currentBoard.testMove(Board.Action.RIGHT);
            while (right.getLastResult() == Board.Result.SUCCESS) {
                options.add(right.testMove(Board.Action.DROP));
                if(rotation_index == null){
                    firstMoves.add(Board.Action.RIGHT);
                }
                else{
                    firstMoves.add(rotation_index);
                }
                right.move(Board.Action.RIGHT);
            } 
            rotation_index = Board.Action.CLOCKWISE;
            currentBoard = currentBoard.testMove(Board.Action.CLOCKWISE);
            }
    }

    /**
     * Since we're trying to avoid building too high,
     * we're going to give higher scores to Boards with
     * MaxHeights close to 0.
     */
    private double scoreBoard(Board newBoard) {
        int score = 100;
        score -= (1.39532 * relativeHeight(newBoard));
        score -= (1.075 * sumHeights(newBoard));
        score -= (2.79 * holes(newBoard));
        score += (175.5 * clears(newBoard));
        score -= (1.495*newBoard.getMaxHeight());
        score -= (0.37 * roughness(newBoard));
        return score;
    }

    private int sumHeights(Board board){
        int sum = 0;
        for(int x = 0; x < board.getWidth(); x++){
            sum += board.getColumnHeight(0);
        }
        return sum;
    }

    private int holes(Board board){
        int holes = 0;
        for(int x = 0; x < board.getWidth(); x++){
            for(int y = 0; y < board.getHeight() -1; y++){
                if(board.getGrid(x,y) == null){
                    for(int j = y+1; j < board.getHeight(); j++){
                        if(board.getGrid(x, j) != null){
                            holes++;
                            break;
                        }
                    }
                }
            }
        }
        return holes;
    }

    private int clears(Board board){
        return board.getRowsCleared();
    }

    private int roughness(Board board){
        int sum = 0;
        for(int x = 0; x < board.getWidth()-1; x++){
            sum += Math.abs(board.getColumnHeight(x) - board.getColumnHeight(x+1));
        }
        return sum;
    }

    private int relativeHeight(Board board){
        int min = board.getColumnHeight(0);
        for(int x = 0; x < board.getWidth(); x++){
            if(board.getColumnHeight(x) < min){
                min = board.getColumnHeight(x);
            }
        }
        return board.getMaxHeight() - min;
    }

    private int sumLengths(Board board){
        int sum = 0;
        for(int y = 0; y < board.getHeight(); y++){
            sum += board.getRowWidth(y);
        }
        return sum;
    }

}
