import javax.swing.*;

class king extends piece {


    king (String c) {
        points = 0;
        if(c.equals("w")){
            color = "w";
            not = "K";
            spwan = new int[][] {{7,4}};
            icon = new ImageIcon(getClass().getResource("/main/images/kw.png"));
        }else if(c.equals("b")){
            color = "b";
            not = "k";
            spwan = new int[][] {{0,4}};
            icon = new ImageIcon(getClass().getResource("/main/images/kb.png"));
        }
    }
    @Override
    boolean isValidMove(String[][] grid, int[] to, int[] from, String pieceColor){

        boolean isYOne = (from[0] == to[0] + 1 ||from[0] == to[0] -1) && from[1] == to[1];
        boolean isXone = (from[1] == to[1] + 1 ||from[1] == to[1] -1) && from[0] == to[0];
        boolean isOneDiagonal  = (from[0] == to[0] + 1 ||from[0] == to[0] -1) && (from[1] == to[1] + 1 ||from[1] == to[1] -1);
        return isOneDiagonal || isYOne || isXone;
       
    }
}
