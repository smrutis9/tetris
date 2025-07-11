package assignment;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;

import assignment.Piece.PieceType;


/**
 * JTetris presents a tetris game in a window.
 * It handles the GUI and the animation.
 * The Piece and Board classes handle the
 * lower-level computations.
 */
public class JBrainTetris extends JTetris {
    private CoolAlgo brain;
    private javax.swing.Timer timer2;
    
    public JBrainTetris(){
        super();
        brain = new CoolAlgo();
        timer2 = new javax.swing.Timer(0, new ActionListener() { 
            public void actionPerformed(ActionEvent e){
                tick(Board.Action.NOTHING);
            }
        });
    }

    public static void main(String[] args) {
        createGUI(new JBrainTetris());
    }


    /**
     * Called to change the position of the current piece.
     * Each key press calls this once with a Board.Action
     * and the timer calls it with the verb DOWN to move
     * the piece down one square.
     */
    @Override
    public void tick(Board.Action verb) {
        if (!gameOn) {
            return;
        }

        if(verb != Board.Action.DOWN){
            verb = brain.nextMove(board);
        }

        super.tick(verb);
    }

    @Override
    public void startGame(){
        super.startGame();
        timer2.start();
    }

    @Override
    public void stopGame(){
        super.stopGame();
        timer2.stop();
    }

    public int getCount(){
        return count;
    }
}
