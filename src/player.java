import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
public class player {
    int TotalPoints;
    String color;
    String playerName = "----";
    String stats;
    HashMap<String, Integer> capturedPieces  = new HashMap<String, Integer>();


    player(String c){
        color = c;
        TotalPoints = 0;

        if(c.equals("b")){
            capturedPieces.put("P", 0);
            capturedPieces.put("B", 0);
            capturedPieces.put("Q", 0);
            capturedPieces.put("R", 0);
            capturedPieces.put("N", 0);
        }else{
            capturedPieces.put("p", 0);
            capturedPieces.put("b", 0);
            capturedPieces.put("q", 0);
            capturedPieces.put("r", 0);
            capturedPieces.put("n", 0);
        }
    }

    void capturePiece (piece p){




        TotalPoints += p.points;


        if(capturedPieces.containsKey(p.not)){
            int currentVal = capturedPieces.get(p.not);
            capturedPieces.replace(p.not, currentVal, currentVal + 1);
        }
    }

    void printCaptureMessage(JTextArea statsArea){

        if(color.equals("w")){
            stats = (playerName + "(" + color + ")" + "\n\n" + capturedPieces.get("p") + " Pawn(s)\n" + capturedPieces.get("n")  + " Knight(s)\n" +capturedPieces.get("b") + " Bishop(s)\n"+ capturedPieces.get("q") + " Queen(s)\n"+capturedPieces.get("r") + " Rook(s)");
        }else{
            stats = (playerName + "(" + color + ")" + "\n\n" +capturedPieces.get("P") + " Pawn(s)\n" + capturedPieces.get("N") + " Knight(s)\n" +capturedPieces.get("B") + " Bishop(s)\n"+ capturedPieces.get("Q") + " Queen(s)\n"+capturedPieces.get("R") + " Rook(s)");
        }


        if(statsArea != null){

            statsArea.setText(stats);

        }
    }
}