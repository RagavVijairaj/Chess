import javax.swing.*;

class knight extends piece{
     
    knight (String c) {
        points = 3;
        if(c.equals("w")){
            color = "w";
            not = "N";
            spwan = new int[][] {{7,1}, {7,6}};
            icon = new ImageIcon(getClass().getResource("/nw.png"));
        }else if(c.equals("b")){
            color = "b";
            not = "n";
            spwan = new int[][] {{0,1}, {0,6}};
            icon = new ImageIcon(getClass().getResource("/nb.png"));
        }
    }

    @Override 
    boolean isValidMove (String[][] grid, int[] from, int[] to, String color){
        return (Math.abs(from[0] - to[0]) == 2 && Math.abs(from[1] - to[1]) == 1) || (Math.abs(from[0] - to[0]) == 1 && Math.abs(from[1] - to[1]) == 2);
    }
}
