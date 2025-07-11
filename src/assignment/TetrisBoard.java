package assignment;

import java.awt.*;
import java.util.Arrays;

import assignment.Piece.PieceType;

/**
 * Represents a Tetris board -- essentially a 2-d grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {

    private Piece.PieceType[][] currentboard;
    private int height;
    private int width;
    private int[] columnHeights;
    private int[] rowLengths;
    private int maxHeight;
    private Piece currPiece;
    private Point currPosition;
    public int rowsCleared;
    private Result result;
    private Action action;
    private int dropHeight;

    // JTetris will use this constructor
    public TetrisBoard(int w, int h) {
        height = h;
        width = w;
        currentboard = new Piece.PieceType[width][height];
        columnHeights = new int[width];
        rowLengths = new int[height];
        for(int x = 0; x < columnHeights.length; x++){
            columnHeights[x] = 0;
        }
        maxHeight = columnHeights[0];
        for(int y = 0; y < rowLengths.length; y++){
            rowLengths[y] = 0;
        }
        currPiece = null;
        rowsCleared = 0;
        currPosition = null;
        result = Result.NO_PIECE;
        action = Action.NOTHING;
    }

    public TetrisBoard(int w, int h, Piece.PieceType[][] board, int[] columnheights, int[] rowlengths, Piece curr, int rowsClear, Result r, Action a, Point pos){
        width = w;
        height = h;
        currentboard = new Piece.PieceType[width][height];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                currentboard[x][y] = board[x][y];
            }
        }
        columnHeights = Arrays.copyOf(columnheights, columnheights.length);
        rowLengths = Arrays.copyOf(rowlengths, rowlengths.length);
        currPiece = curr;
        rowsCleared = rowsClear;
        result = r;
        action = a;
        currPosition = new Point(pos.x, pos.y);
    }

    @Override
    public Result move(Action act){ // variables updated so far: currPosition{
        if(act == Action.LEFT){
            action = Action.LEFT;
            if(checkLeft(currPiece) == false){
                result = Result.OUT_BOUNDS;
                clearRows();
                calculateColumnHeights();
                calculateMaxHeight();
                calculateRowLengths();
                return result;
            }
            else{
                currPosition.move((int)currPosition.getX() -1, (int)currPosition.getY());
                result = Result.SUCCESS;
                clearRows();
                calculateColumnHeights();
                calculateMaxHeight();
                calculateRowLengths();
                return result;
            }
        }
        else if(act == Action.RIGHT){
            action = Action.RIGHT;
            if(checkRight(currPiece) == false){
                result = Result.OUT_BOUNDS;
                clearRows();
                calculateColumnHeights();
                calculateMaxHeight();
                calculateRowLengths();
                return result;
            }
            else{
                currPosition.move((int)currPosition.getX() + 1, (int)currPosition.getY());
                result = Result.SUCCESS;
                clearRows();
                calculateColumnHeights();
                calculateMaxHeight();
                calculateRowLengths();
                return result;
            }
        }
        else if(act == Action.DOWN){
            action = Action.DOWN;
            if(checkDown(currPiece) == false){
                Point[] body = currPiece.getBody();
                for(int i = 0; i < 4; i++){
                    currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] = currPiece.getType();
                }
                result = Result.PLACE;
                clearRows();
                calculateColumnHeights();
                calculateMaxHeight();
                calculateRowLengths();
                return result;
            }
            else{
                currPosition.y = currPosition.y - 1;
                result = Result.SUCCESS;
                clearRows();
                calculateColumnHeights();
                calculateMaxHeight();
                calculateRowLengths();
                return result;
            }
        }
        else if(act == Action.CLOCKWISE){
            action = Action.CLOCKWISE;
            if(currPiece.getType() != PieceType.STICK){
                checkClockReg();
            }
            if(currPiece.getType() == PieceType.STICK){
                checkClockStick();
            }
            clearRows();
            calculateColumnHeights();
            calculateMaxHeight();
            calculateRowLengths();
            return result;
        }
        else if(act == Action.COUNTERCLOCKWISE){
            action = Action.COUNTERCLOCKWISE;
            if(currPiece.getType() != PieceType.STICK){
                checkCounterReg();
            }
            if(currPiece.getType() == PieceType.STICK){
                checkCounterStick();
            }
            clearRows();
            calculateColumnHeights();
            calculateMaxHeight();
            calculateRowLengths();
            return result;
        }
        else if(act == Action.DROP){
            action = Action.DROP;
            //currPosition.y = dropHeight(currPiece, currPosition.x);
            while(checkDown(currPiece) == true){
                currPosition.y = currPosition.y -1;
            }
            Point[] body = currPiece.getBody();
            for(int i = 0; i < 4; i++){
                currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] = currPiece.getType();
            }
            result = Result.PLACE;
            clearRows();
            calculateColumnHeights();
            calculateMaxHeight();
            calculateRowLengths();
            return result;
        }
        return Result.NO_PIECE;
    }

    @Override
    public Board testMove(Action act) { // need to implement
        TetrisBoard board = new TetrisBoard(width, height, currentboard, columnHeights, rowLengths, currPiece, rowsCleared, result, action, currPosition);
        board.move(act);
        return board;
    }

    @Override
    public Piece getCurrentPiece() { //good
        return currPiece;
    }

    @Override
    public Point getCurrentPiecePosition() { //good
        return currPosition;
    }

    @Override
    public void nextPiece(Piece p, Point spawnPosition) { 
        Point[] body = p.getBody();
        for(int i = 0; i < p.getBody().length; i++){
            if(!outOfBounds(spawnPosition.x + p.getBody()[i].x, spawnPosition.y + body[i].y) || currentboard[spawnPosition.x + body[i].x][spawnPosition.y + body[i].y] != null){
                throw new IllegalArgumentException("Your piece collides with something else, including any boundaries");
            }
        }
        currPiece = p;
        currPosition = spawnPosition;
    }

    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(currentboard);
        result = prime * result + ((currPiece == null) ? 0 : currPiece.hashCode());
        result = prime * result + ((currPosition == null) ? 0 : currPosition.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TetrisBoard other = (TetrisBoard) obj;
        if (!Arrays.deepEquals(currentboard, other.currentboard))
            return false;
        if (currPiece == null) {
            if (other.currPiece != null)
                return false;
        } else if (!currPiece.equals(other.currPiece))
            return false;
        if (currPosition == null) {
            if (other.currPosition != null)
                return false;
        } else if (!currPosition.equals(other.currPosition))
            return false;
        return true;
    }

    @Override
    public Result getLastResult() { 
        return result;
    }

    @Override
    public Action getLastAction() {
        return action;
     }

    @Override
    public int getRowsCleared() {
        return rowsCleared;
    }

    @Override
    public int getWidth() { 
        return width;
     }

    @Override
    public int getHeight() {
        return height;
     }
    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public int dropHeight(Piece piece, int x) { // to be written
        int[] skirt = piece.getSkirt();
        int[] array = new int[piece.getSkirt().length];
        for(int i = 0; i < array.length; i++){
            if(!(x + i < 0 || x + i >= width)){
                array[i] = columnHeights[x + i] - skirt[i];
            }
        }
        int maximum = array[0];
        for(int i = 0; i < array.length; i++){
            if(array[i] > maximum){
                maximum = array[i];
            }
        }
        return maximum;
     }

    @Override
    public int getColumnHeight(int x) {
        return columnHeights[x];
    }

    @Override
    public int getRowWidth(int y) {
        return rowLengths[y];
    }

    @Override
    public Piece.PieceType getGrid(int x, int y) { 
         if(currentboard[x][y] == null){
            return null;
         }
        return currentboard[x][y];
    }

    public boolean checkLeft(Piece p){
        Point[] body = p.getBody();
        for(int i = 0; i < 4; i++){
            if(currPosition.x + body[i].getX() <= 0){
                return false;
            }
            if(currPosition.x + body[i].getX() > 0 && currentboard[(int)(currPosition.x + body[i].getX() - 1)][(int)(currPosition.y + body[i].getY())] != null){
                return false;
            }
        }
        return true;
    }

    public boolean checkRight(Piece p){
        Point[] body = p.getBody();
        for(int i = 0; i < 4; i++){
            if(currPosition.x + body[i].getX() >= width -1){
                return false;
            }
            if(currPosition.x + body[i].getX() < width -1 && currentboard[(int)(currPosition.x + body[i].getX() + 1)][(int)(currPosition.y + body[i].getY())] != null){
                return false;
            }
        }
        return true;
    }

    public boolean checkDown(Piece p){
        Point[] body = p.getBody();
        for(int i = 0; i < 4; i++){
            if(currPosition.y + body[i].getY() <= 0){
                return false;
            }
            if(currPosition.y + body[i].getY() > 0 && currPosition.x + body[i].getX() < width && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY()) -1] != null){
                return false;
            }
        }
        return true;
    }

    public boolean checkClockReg(){
        if(currPiece.getRotationIndex() == 0){
            Piece clock = currPiece.clockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x - 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) +1) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) + 1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x -1;
                currPosition.y = currPosition.y +1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.y = currPosition.y -2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x -1;
                currPosition.y = currPosition.y -2;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 1){
            Piece clock = currPiece.clockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x + 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) -1) && currentboard[(int)(currPosition.x + body[i].getX()) + 1][(int)(currPosition.y + body[i].getY()) - 1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x + 1;
                currPosition.y = currPosition.y - 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.y = currPosition.y + 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x + 1;
                currPosition.y = currPosition.y + 2;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 2){
            Piece clock = currPiece.clockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x += 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) +1) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY()) + 1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x += 1;
                currPosition.y += 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.y -= 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x += 1;
                currPosition.y -= 2;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 3){
            Piece clock = currPiece.clockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x -= 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) -1) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) - 1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x -= 1;
                currPosition.y -= 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.y += 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x -= 1;
                currPosition.y += 2;
                result = Result.SUCCESS;
                return true;
            }
        }
        result = Result.OUT_BOUNDS;
        return false;
    }

    public boolean checkClockStick(){
        if(currPiece.getRotationIndex() == 0){
            Piece clock = currPiece.clockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -2, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) -2][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x - 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x +1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -2, (int)(currPosition.y + body[i].getY()) -1) && currentboard[(int)(currPosition.x + body[i].getX()) -2][(int)(currPosition.y + body[i].getY()) -1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x -= 2;
                currPosition.y = currPosition.y -1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x +1;
                currPosition.y = currPosition.y +2;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 1){
            Piece clock = currPiece.clockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x - 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +2, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) + 2][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x + 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x -= 1;
                currPosition.y = currPosition.y + 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +2, (int)(currPosition.y + body[i].getY()) -1) && currentboard[(int)(currPosition.x + body[i].getX()) +2][(int)(currPosition.y + body[i].getY()) -1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x = currPosition.x + 2;
                currPosition.y = currPosition.y - 1;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 2){
            Piece clock = currPiece.clockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +2, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) +2][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x += 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x -= 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +2, (int)(currPosition.y + body[i].getY()) +1) && currentboard[(int)(currPosition.x + body[i].getX()) + 2][(int)(currPosition.y + body[i].getY()) +1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x += 2;
                currPosition.y += 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x -= 1;
                currPosition.y -= 2;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 3){
            Piece clock = currPiece.clockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x += 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -2, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) -2][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x -= 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX()) + 1][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x += 1;
                currPosition.y -= 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -2, (int)(currPosition.y + body[i].getY()) +1) && currentboard[(int)(currPosition.x + body[i].getX()) -2][(int)(currPosition.y + body[i].getY()) +1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.clockwisePiece();
                currPosition.x -= 2;
                currPosition.y += 1;
                result = Result.SUCCESS;
                return true;
            }
        }
        result = Result.OUT_BOUNDS;
        return false;
    }
    public boolean checkCounterReg(){
        if(currPiece.getRotationIndex() == 0){
            Piece clock = currPiece.counterclockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x +1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) +1) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY()) + 1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x +1;
                currPosition.y += 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.y = currPosition.y -2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x +1;
                currPosition.y = currPosition.y -2;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 1){
            Piece clock = currPiece.counterclockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x + 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) -1) && currentboard[(int)(currPosition.x + body[i].getX()) + 1][(int)(currPosition.y + body[i].getY()) -1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x + 1;
                currPosition.y -= 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.y = currPosition.y + 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x + 1;
                currPosition.y = currPosition.y + 2;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 2){
            Piece clock = currPiece.counterclockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x -= 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) +1) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) + 1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x -= 1;
                currPosition.y += 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.y -= 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x -= 1;
                currPosition.y -= 2;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 3){
            Piece clock = currPiece.counterclockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x -= 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) -1) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) -1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x -= 1;
                currPosition.y -= 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.y += 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x -= 1;
                currPosition.y += 2;
                result = Result.SUCCESS;
                return true;
            }
        }
        result = Result.OUT_BOUNDS;
        return false;
    }
    public boolean checkCounterStick(){
        if(currPiece.getRotationIndex() == 0){
            Piece clock = currPiece.counterclockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x -1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +2, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) +2][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x +2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x -= 1;
                currPosition.y = currPosition.y +2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +2, (int)(currPosition.y + body[i].getY()) -1) && currentboard[(int)(currPosition.x + body[i].getX()) +2][(int)(currPosition.y + body[i].getY()) -1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x +2;
                currPosition.y = currPosition.y -1;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 1){
            Piece clock = currPiece.counterclockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +2, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) +2][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x + 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) - 1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x - 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +2, (int)(currPosition.y + body[i].getY()) +1) && currentboard[(int)(currPosition.x + body[i].getX()) + 2][(int)(currPosition.y + body[i].getY()) +1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x += 2;
                currPosition.y = currPosition.y + 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -1, (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX()) -1][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x = currPosition.x - 1;
                currPosition.y = currPosition.y - 2;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 2){
            Piece clock = currPiece.counterclockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x += 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -2, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) -2][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x -= 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) -2) && currentboard[(int)(currPosition.x + body[i].getX()) + 1][(int)(currPosition.y + body[i].getY()) -2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x += 1;
                currPosition.y -= 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -2, (int)(currPosition.y + body[i].getY()) +1) && currentboard[(int)(currPosition.x + body[i].getX()) -2][(int)(currPosition.y + body[i].getY()) +1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x -= 2;
                currPosition.y += 1;
                result = Result.SUCCESS;
                return true;
            }
        }
        else if(currPiece.getRotationIndex() == 3){
            Piece clock = currPiece.counterclockwisePiece();
            Point[] body = clock.getBody();
            int count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()), (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX())][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -2, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) -2][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x -= 2;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY())) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY())] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x += 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) -2, (int)(currPosition.y + body[i].getY()) -1) && currentboard[(int)(currPosition.x + body[i].getX()) -2][(int)(currPosition.y + body[i].getY()) -1] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x -= 2;
                currPosition.y -= 1;
                result = Result.SUCCESS;
                return true;
            }
            count = 0;
            for(int i = 0; i < clock.getBody().length; i ++){
                if(outOfBounds((int)(currPosition.x + body[i].getX()) +1, (int)(currPosition.y + body[i].getY()) +2) && currentboard[(int)(currPosition.x + body[i].getX()) +1][(int)(currPosition.y + body[i].getY()) +2] == null){
                    count++;
                }
            }
            if(count == 4){
                currPiece = currPiece.counterclockwisePiece();
                currPosition.x += 1;
                currPosition.y += 2;
                result = Result.SUCCESS;
                return true;
            }
        }
        result = Result.OUT_BOUNDS;
        return false;
    }


    public void calculateColumnHeights(){
        for(int x = 0; x < width; x++){
            for(int y = height -1; y >=0; y--){
                if(currentboard[x][y] != null){
                    columnHeights[x] = y + 1;
                    break;
                }
            }
        }
    }

    public boolean outOfBounds(int x, int y){
        if(x < 0 || x >= width || y < 0 || y >= height){
            return false;
        }
        return true;
    }

    public void calculateMaxHeight(){
        maxHeight = 0;
        for(int i = 0; i < columnHeights.length; i++){
            if(columnHeights[i] > maxHeight){
                maxHeight = columnHeights[i];
            }
        }
    }

    public int[] getColumnHeights(){
        return columnHeights;
    }

    public int[] getRowLengths(){
        return rowLengths;
    }

    public void calculateRowLengths(){
        for(int y = 0; y < height; y++){
            int count = 0;
            for(int x = 0; x < width; x++){
                if(currentboard[x][y] != null){
                    count++;
                }
            }
            rowLengths[y] = count;
        }
    }

    public void clearRows(){
        for(int y = 0; y < height; y++){
            int count = 0;
            for(int x = 0; x < width; x++){
                if(currentboard[x][y] != null){
                    count++;
                }
            }
            if(count == width){
                rowsCleared++;
                for(int r = 0; r < width; r++){
                    currentboard[r][y] = null;
                }
                for(int c = y; c < height -1; c++){
                    for(int b = 0; b < width; b++){
                        currentboard[b][c] = currentboard[b][c+1];
                    }
                }
                for(int x = 0; x < columnHeights.length; x++){
                    if(columnHeights[x] > 0){
                        columnHeights[x]--;
                    }
                }
                y = y-1;
            }
        }
    }
}
