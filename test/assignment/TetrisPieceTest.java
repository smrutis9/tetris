package assignment;

import java.awt.*;

import assignment.Piece.PieceType;

public class TetrisPieceTest {
    public static void main(String[] args){
        TetrisPiece piece = new TetrisPiece(PieceType.RIGHT_DOG, PieceType.RIGHT_DOG.getSpawnBody(), 3, 3, new Color(138, 43, 223), 0, true);
        //Piece clock = piece.clockwisePiece();
        Point[] body = piece.getBody();
        TetrisPiece piece_clock = (TetrisPiece) piece.clockwisePiece();
        TetrisPiece double_clock = (TetrisPiece) piece_clock.clockwisePiece();
        TetrisPiece piece_counter = (TetrisPiece) piece.counterclockwisePiece();
        System.out.println(bodyPrint(double_clock.getBody()));
        System.out.println(Print(piece.getSkirt()));
    }

    public static String bodyPrint(Point[] body){
        String output = "";
        for(int i = 0; i < body.length; i++){
            output += "( " + body[i].getX() + ", " + body[i].getY() + " )" + "\n";
        }
        return output;
    }

    public static String Print(int[] body){
        String output = "";
        for(int i = 0; i < body.length; i++){
            output += body[i] + " , ";
        }
        return output;
    }
}
