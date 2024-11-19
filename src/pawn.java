import javax.swing.*;

class pawn extends piece {

	pawn(String c) {
		points =1;
		if (c.equals("b")) {
			color = "b";
			not = "p";
			spwan = new int[][] { { 1, 0 }, { 1, 1 }, { 1, 2 }, { 1, 3 }, { 1, 4 }, { 1, 5 }, { 1, 6 }, { 1, 7 } };
			icon = new ImageIcon(getClass().getResource("/pb.png"));
		} else if (c.equals("w")) {
			color = "w";
			not = "P";
			spwan = new int[][] { { 6, 0 }, { 6, 1 }, { 6, 2 }, { 6, 3 }, { 6, 4 }, { 6, 5 }, { 6, 6 }, { 6, 7 } };
			icon = new ImageIcon(getClass().getResource("/pw.png"));
		}
	}

	@Override
	boolean isValidMove(String[][] grid, int[] from, int[] to, String pieceColor) {
		// if pawn in starting position and is one or two move away and nothing is in
		// front of it, it's valid

		if ((pieceColor.equals("w") && from[0] == 6) || (pieceColor.equals("b") && from[0] == 1)) {

			if (pieceColor.equals("w") && to[1] == from[1]) {
				
				if ((to[0] == from[0] - 2) && (grid[to[0]][to[1]]).equals(" ")
						&& (grid[to[0] + 1][to[1]]).equals(" ")) {
					return true;
				} else if ((to[0] == from[0] - 1) && (grid[to[0]][to[1]]).equals(" ")) {
					return true;
				}
			}

			if (pieceColor.equals("b") && to[1] == from[1]) {
		
				if ((to[0] == from[0] + 2) && (grid[to[0]][to[1]]).equals(" ")
						&& (grid[to[0] - 1][to[1]]).equals(" ")) {
					return true;
				} else if ((to[0] == from[0] + 1) && (grid[to[0]][to[1]]).equals(" ")) {
					return true;
				}
			}
		}
		
		// one move front
		if(pieceColor.equals("w") && (from[0] == to[0] + 1 && from[1] == to[1]) && grid[to[0]][to[1]].equals(" ")) {
			return true;
		}else if (pieceColor.equals("b") && (from[0] == to[0] - 1 && from[1] == to[1]) && grid[to[0]][to[1]].equals(" ")) {
			return true;
		}
		
		// if pawn wants to cut a piece in front left or right its valid
		if (pieceColor.equals("w") && ((to[1] == from[1] + 1 && to[0] == from[0] - 1) || (to[1] == from[1] - 1 && to[0] == from[0] - 1))) {
            return !grid[to[0]][to[1]].equals(" ");
		} else if (pieceColor.equals("b") && ((from[1] == to[1] - 1 && from[0] == to[0] - 1) || (from[1] == to[1] + 1 && from[0] == to[0] - 1))) {
            return !grid[to[0]][to[1]].equals(" ");
		}

		return false;
	}

}
