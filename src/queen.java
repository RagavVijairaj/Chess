import javax.swing.*;

class queen extends piece {

    queen (String c) {
        points = 9;
        if(c.equals("w")){
            color = "w";
            not = "Q";
            spwan = new int[][] {{7,3}};
            icon = new ImageIcon(getClass().getResource("/qw.png"));
        }else if(c.equals("b")){
            color = "b";
            not = "q";
            spwan = new int[][] {{0,3}};
            icon = new ImageIcon(getClass().getResource("/qb.png"));
        }
    }

    boolean isAllBoxesInDiagonalClear(String [][] grid, int[] from, int[] to){

		boolean isUp = to[0] < from[0];
		boolean isLeft = from[1] > to[1];
		//up left
		if(isUp && isLeft){
			int x = from[0] - 1;
			int y = from[1] - 1;
			while( x > to[0] && y > to[1] ){

				if(!grid[x][y].equals(" ")) return false;
				x--;
				y--;
			}
		}
		// down left
		if(!isUp && isLeft){
			int x = from[0] + 1;
			int y = from[1] - 1;
			while( x < to[0] && y > to[1] ){

				if(!grid[x][y].equals(" ")) return false;
				x++;
				y--;
			}
		}
		// up right
		if(isUp && !isLeft){
			int x = from[0] - 1;
			int y = from[1] + 1;
			while( x > to[0] && y < to[1] ){

				if(!grid[x][y].equals(" ")) return false;
				x--;
				y++;
			}
		}
		// down right
		if(!isUp && !isLeft){
			int x = from[0] + 1;
			int y = from[1] + 1;
			while( x  <to[0] && y < to[1] ){

				if(!grid[x][y].equals(" ")) return false;
				x++;
				y++;
			}
		}
		return true;
	}

    boolean isAllBoxesInFrontClear (String[][] grid, int[] from, int[] to) {
        boolean isHorizontal = from[0] == to[0] ? true : false;

        if(isHorizontal){
          
            boolean isLeft = (to[1] < from[1]) ? true : false;
    
            if(isLeft){
                
                 for(int i = from[1]-1; i > to[1]; i--){
                    if(!grid[from[0]][i].equals(" ")) return false;  
                 }
            }else{
               
                for(int i = from[1] + 1; i < to[1]; i ++){
                    if(!grid[from[0]][i].equals(" ")) return false;
                
                }
            }
            
            
            return true;

        }else{
           
            boolean isUp = from[0] > to[0] ? true : false;

            if(isUp){
              
                for(int i = from[0] - 1; i > to[0]; i-- ){

                   
                    if(!grid[i][from[1]].equals(" ")){

                        return false;
                    } else{
                      
                    }
                }

            }else{
               
                for(int i = from[0] + 1; i < to[0]; i++ ){
                    if(!grid[i][from[1]].equals(" ")) return false;
                }
            }
        }

        return true;
    }
    @Override
    boolean isValidMove (String[][] grid, int[] from, int[] to, String color) {

        boolean isDiagonal =  Math.abs(from[0] - to[0]) == Math.abs(from[1] - to[1]);
        boolean isXY = from[1] == to[1] || from[0] == to[0];
        if((isDiagonal && isAllBoxesInDiagonalClear(grid, from, to)) || isXY && isAllBoxesInFrontClear(grid, from, to)) return true;
        return false;
    }
}
