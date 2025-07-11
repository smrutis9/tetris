package assignment;

import java.awt.*;
import java.util.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for it's
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do precomputation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {

    private PieceType piece;
    private int height; // done
    private int width; //done
    private int[] skirt;
    private Point[] body; //done
    private int index;
    private Color color; //done
    private Dimension box; //done
    private LinkedList<TetrisPiece> list;
    //private Piece next - what does this mean?

    /**
     * Construct a tetris piece of the given type. The piece should be in it's spawn orientation,
     * i.e., a rotation index of 0.
     * 
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    public TetrisPiece(PieceType type) {
        this(type, type.getSpawnBody(), type.getBoundingBox().width, type.getBoundingBox().height, type.getColor(), 0, true);
    }

    public TetrisPiece(PieceType piece2, Point[] b, int width, int height, Color color, int ind, boolean spawnBody) {
        piece = piece2;
        body = b;
        this.width = width;
        this.height = height;
        this.color = color;
        index = ind;
        list = new LinkedList<TetrisPiece>();
        if(spawnBody){
            makeList();
            for(int i = 1; i < 4; i++){
                list.get(i).list = this.list;
            }
        }
        Point point;
        skirt = new int[width];
        for(int i = 0; i < skirt.length; i++){
            skirt[i] = width;
        }
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                point = new Point(i,j);
                if(Arrays.asList(body).contains(point)){
                    if(point.y < skirt[point.x]){
                        skirt[point.x] = point.y;
                    }
                }
            }
        }
        for(int i = 0; i < width; i++){
            if(skirt[i] == width){
                skirt[i] = Integer.MAX_VALUE;
            }
        }
    }

    public LinkedList<TetrisPiece> makeList(){
        if(index == 0){
            list.add(this);
            list.add((TetrisPiece)clockwisePiece());
            list.add((TetrisPiece)clockwisePiece().clockwisePiece());
            list.add((TetrisPiece)counterclockwisePiece());
        }
        else if(index == 1){
            list.add((TetrisPiece)counterclockwisePiece());
            list.add(this);
            list.add((TetrisPiece)clockwisePiece());
            list.add((TetrisPiece)counterclockwisePiece().counterclockwisePiece());
        }
        else if(index == 2){
            list.add((TetrisPiece)counterclockwisePiece().counterclockwisePiece());
            list.add((TetrisPiece)counterclockwisePiece());
            list.add(this);
            list.add((TetrisPiece)clockwisePiece());
        }
        else if(index == 3){
            list.add((TetrisPiece)clockwisePiece());
            list.add((TetrisPiece)counterclockwisePiece().counterclockwisePiece());
            list.add((TetrisPiece)counterclockwisePiece());
            list.add(this);
        }
        return list;
    }

    @Override
    public PieceType getType() {
       return piece;
    }

    @Override
    public int getRotationIndex() {
        return index;
    }

    public Point[] getClockwisePiece(Point[] bodyPoints){
        Point[] newBody = new Point[bodyPoints.length];
            for(int i = 0; i < width; i++){
                for(int j = 0; j < height; j++){
                    if(i == bodyPoints[0].getX() && j == bodyPoints[0].getY()){
                        newBody[0] = new Point((int) bodyPoints[0].getY(), (int) (width - 1 - bodyPoints[0].getX()));
                    }
                    else if(i == bodyPoints[1].getX() && j == bodyPoints[1].getY()){
                        newBody[1] = new Point((int) bodyPoints[1].getY(), (int) (width - 1 - bodyPoints[1].getX()));
                    }
                    else if(i == bodyPoints[2].getX() && j == bodyPoints[2].getY()){
                        newBody[2] = new Point((int) bodyPoints[2].getY(), (int) (width - 1 - bodyPoints[2].getX()));
                    }
                    else if(i == bodyPoints[3].getX() && j == bodyPoints[3].getY()){
                        newBody[3] = new Point((int) bodyPoints[3].getY(), (int) (width - 1 - bodyPoints[3].getX()));
                    }
                }
            }
        return newBody;
    }

    public Point[] getCounterclockwisePiece(Point[] bodyPoints){
        Point[] newBody = new Point[bodyPoints.length];
            for(int i = 0; i < width; i++){
                for(int j = 0; j < height; j++){
                    if(i == bodyPoints[0].getX() && j == bodyPoints[0].getY()){
                        newBody[0] = new Point((int) (width - 1 - bodyPoints[0].getY()), (int) bodyPoints[0].getX());
                    }
                    else if(i == bodyPoints[1].getX() && j == bodyPoints[1].getY()){
                        newBody[1] = new Point((int) (width - 1 - bodyPoints[1].getY()), (int) bodyPoints[1].getX());
                    }
                    else if(i == bodyPoints[2].getX() && j == body[2].getY()){
                        newBody[2] = new Point((int) (width - 1 - bodyPoints[2].getY()), (int) bodyPoints[2].getX());
                    }
                    else if(i == bodyPoints[3].getX() && j == bodyPoints[3].getY()){
                        newBody[3] = new Point((int) (width - 1 - bodyPoints[3].getY()), (int) bodyPoints[3].getX());
                    }
                }
            } // to be changed
        return newBody;
    }

    @Override
    public Piece clockwisePiece() {
        int newIndex = (index+1) % 4;
        Point[] newBody = getClockwisePiece(body);
        return new TetrisPiece(piece, newBody, width, height, color, newIndex, false);
    }
    @Override
    public Piece counterclockwisePiece() {
        int newIndex = (index + 3) % 4;
        Point[] newBody = getCounterclockwisePiece(body);
        return new TetrisPiece(piece, newBody, width, height, color, newIndex, false);
    }

    @Override
    public int getWidth() { // or should I return the 
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Point[] getBody() { // need to change so this is not the spawn body
        return body;
    }

    @Override
    public int[] getSkirt() {
        return skirt;
    }

    @Override
    public boolean equals(Object other) {
        // Ignore objects which aren't also tetris pieces.
        if(!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;
        if((this.getType() == otherPiece.getType()) && (this.getRotationIndex() == otherPiece.getRotationIndex())){
            return true;
        }
        return false;
    }

    public Point getLeftMost(){
        int leftmost = width;
        Point p = null;
        for(int x = 0; x < body.length; x++){
            if((int)body[x].getX() < leftmost){
                leftmost = (int) body[x].getX();
                p = body[x];
            }
        }
        return p;
    }

    public Point getRightMost(){
        int rightmost = 0;
        Point p = null;
        for(int x = 0; x < body.length; x++){
            if((int)body[x].getX() > rightmost){
                rightmost = (int) body[x].getX();
                p = body[x];
            }
        }
        return p;
    }
}
