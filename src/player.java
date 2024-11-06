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
    
    void capturePeice (piece p){
        
        TotalPoints += p.points;

        

        if(capturedPieces.containsKey(p.not)){
            int currentVal = capturedPieces.get(p.not);
            capturedPieces.replace(p.not, currentVal, currentVal + 1);
        }
    }

    void printCaptureMessage(){
        System.out.println(playerName + " has " + TotalPoints + " point(s)." );

        if(color.equals("w")){
            stats = (playerName + "(" + color + ")" + "\n\n" + capturedPieces.get("p") + " Pawns\n" + capturedPieces.get("n")  + " Knights\n" +capturedPieces.get("b") + " Bishops\n"+ capturedPieces.get("q") + " Queen\n"+capturedPieces.get("r") + " Rooks");
        }else{
            stats = (playerName + "(" + color + ")" + "\n\n" +capturedPieces.get("P") + " Pawns\n" + capturedPieces.get("N") + " Knights\n" +capturedPieces.get("B") + " Bishops\n"+ capturedPieces.get("Q") + " Queen\n"+capturedPieces.get("R") + " Rooks");
        }
    }
}
