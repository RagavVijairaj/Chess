import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Timer;
import javax.swing.*;

class board {


	int[] from;
	int[] to;
	int buttonPresses;
	JTextArea playerAStats = new JTextArea();
	JTextArea playerBStats = new JTextArea();
	JTextField textArea = new JTextField();
	myButton[] button = new myButton[64];
	Font customFont = new Font("Oxanium Medium", Font.BOLD, 18);
	String[] initList = { "wR", "bR", "wN", "bN", "wB", "bB", "wQ", "bQ", "wK", "bK", "wP", "bP" };
	String[][] grid;
	Map<String, piece> pieceMap;
	player wPlayer;
	player bPlayer;
	rook wR;
	rook bR;
	pawn wP;
	pawn bP;
	knight wN;
	knight bN;
	bishop wB;
	bishop bB;
	king wK;
	king bK;
	queen wQ;
	queen bQ;
	player playing;
	Color white = Color.decode("#f0d9b5");
	Color pastel = Color.decode("#b48963");
	Color bg = Color.decode("#181414");
	ActionListener buttonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			myButton clickedButton = (myButton) e.getSource();
			buttonActionHandler(clickedButton.cor , clickedButton.ind);
		}


	};
	ActionListener textListener1= new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextField textOut = (JTextField) e.getSource();
			setwPlayerName(textOut.getText());
			textOut.setText("Enter Black Player's Name: ");
			textOut.removeActionListener(textListener1);
			textOut.addActionListener(textListener2);

		}


	};
	ActionListener textListener2 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextField textOut = (JTextField) e.getSource();
			setbPlayerName(textOut.getText());
			textOut.setText("Game Started");
			textOut.removeActionListener(textListener2);
			textOut.setEditable(false);


        }


	};

	board() {
		grid = new String[8][8];
		pieceMap = new HashMap<String, piece>();
		bPlayer = new player("b");
		wPlayer = new player("w");
		playing = wPlayer;
		wR = new rook("w");
		bR = new rook("b");
		wP = new pawn("w");
		bP = new pawn("b");
		wN = new knight("w");
		bN = new knight("b");
		wB = new bishop("w");
		bB = new bishop("b");
		wK = new king("w");
		bK = new king("b");
		wQ = new queen("w");
		bQ = new queen("b");

	}

	void init() {
		pieceMap.put("wR", wR);
		pieceMap.put("bR", bR);
		pieceMap.put("wN", wN);
		pieceMap.put("bN", bN);
		pieceMap.put("wB", wB);
		pieceMap.put("bB", bB);
		pieceMap.put("wQ", wQ);
		pieceMap.put("bQ", bQ);
		pieceMap.put("wK", wK);
		pieceMap.put("bK", bK);
		pieceMap.put("wP", wP);
		pieceMap.put("bP", bP);

		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				grid[j][i] = " ";
			}
		}

		for (String id : initList) {
			for (int[] j : pieceMap.get(id).spwan) {
				grid[j[0]][j[1]] = pieceMap.get(id).not;
			}
		}
	}

	void print() {
		for (int j = 0; j < 8; j++) {
			System.out.println();
			System.out.print("|");

			for (int i = 0; i < 8; i++) {
				System.out.print(grid[j][i] + "|");

			}

		}

		System.out.println();

	}

	void printCor() {
		for (int j = 0; j < 8; j++) {
			System.out.println();
			System.out.print("|");

			for (int i = 0; i < 8; i++) {
				System.out.print(String.valueOf(j) + " " + String.valueOf(i) + "|");

			}
			System.out.println();

		}
		System.out.println();
	}

	piece getPiece(int[] cordinates) {

		piece p;
		piece d = new piece();
		d.setColor("e");
		for (String id : initList) {
			if (grid[cordinates[0]][cordinates[1]].equals(pieceMap.get(id).not)) {
				p = pieceMap.get(id);
				return p;


			}

		}

		return d;

	}

	boolean isCheck(String[][] grid, String c){

		ArrayList<Integer[]> peicesToCheck= new ArrayList<>();
		int[] kingCor = new int[]{};
		String kingNot = c.equals("b") ? "k" : "K";


		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if(grid[i][j].equals(kingNot)) kingCor = new int[] {i,j};
			}
		}



		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				boolean isPeiceOnGrid = c.equals("b") ? grid[i][j].equals("B") || grid[i][j].equals("P") || grid[i][j].equals("R") || grid[i][j].equals("N") || grid[i][j].equals("Q")  : grid[i][j].equals("p") || grid[i][j].equals("r") || grid[i][j].equals("n") || grid[i][j].equals("b") || grid[i][j].equals("q");
				if(isPeiceOnGrid) {
					peicesToCheck.add(new Integer[]{i,j});

				}
			}
		}



		for(Integer[] isCheck : peicesToCheck){


			piece toCheck = getPiece(new int[] {isCheck[0], isCheck[1]});
			if(toCheck.isValidMove(grid, new int[] {isCheck[0], isCheck[1]}, kingCor ,c)){
				findButtonWihCor(kingCor).setBackground(Color.decode("#fa5050"));
				return true;
			}else{
				findButtonWihCor(kingCor).setBackground(findButtonWihCor(kingCor).bg);
			}
		}


		return false;
	}

	boolean isGameover(String c){

		return false;
	}

	boolean capture(int[] from, int to[]) {

		if (grid[from[0]][from[1]].equals(" ") || getPiece(to).color.equals(getPiece(from).color)) {
			return false;
		} else {
			piece fromPeiceType = getPiece(from);

			boolean isToNotEmpty = !grid[to[0]][to[1]].equals(" ");

			if (fromPeiceType.isValidMove(grid, from, to, fromPeiceType.color)) {

				if(!grid[to[0]][to[1]].equals(" ")) {
					playing.capturePeice(getPiece(to));
					playing.printCaptureMessage();
					playerAStats.setText(wPlayer.stats);
					playerBStats.setText(bPlayer.stats);
				}

				grid[from[0]][from[1]] = " ";
				grid[to[0]][to[1]] = fromPeiceType.not;
				return true;


			} else {
				return false;
			}
		}




	}

	void setbPlayerName(String wholeLine){
		bPlayer.playerName = wholeLine.substring(wholeLine.indexOf(":") +2);
		bPlayer.printCaptureMessage();
		playerBStats.setText(bPlayer.stats);
	}

	void setwPlayerName(String wholeLine){

		wPlayer.playerName = wholeLine.substring(wholeLine.indexOf(":") +2);
		wPlayer.printCaptureMessage();
		playerAStats.setText(wPlayer.stats);
	}

	void turnHandler(){
		if(playing == bPlayer){
			textArea.setText(bPlayer.playerName + "'s turn");

		}else{
			textArea.setText(wPlayer.playerName + "'s turn");

		}
	}

	void resetEveryButton(){
		int i = 0;
		for (int j = 0; j < 8; j++) {
			for (int k = 0; k < 8; k++) {

				if ((j + k) % 2 == 0) {
					button[i].setBackground(white);
				} else {
					button[i].setBackground(pastel);
				}

				// If there is a piece to add, set the icon
				if (!grid[j][k].equals(" ")) {
					ImageIcon pieceIcon = getPiece(new int[]{j, k}).icon;
					Image scaledImage = pieceIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
					ImageIcon resizedIcon = new ImageIcon(scaledImage);
					button[i].setIcon(resizedIcon);
				}else{
					button[i].setIcon(null);
				}
				button[i].addActionListener(buttonListener);
				i++;
			}
		}
	}

	void setOneColorTOActive(){
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if(!(getPiece(new int[]{i,j}).color.equals(playing.color))){

					for (int k = 0; k < 64; k++) {
						if(Arrays.equals(button[k].cor, new int[]{i, j})){
							button[k].removeActionListener(buttonListener);
							System.out.println(i + " " + j );
						}
					}
				}
 			}
		}
	}

	myButton findButtonWihCor(int[] cor){

		myButton retButton;

		for (int i = 0; i < 64; i++) {
			if(Arrays.equals(button[i].cor,cor)){
				return button[i];
			}
		}

		return null;
	}

	void setValidMovesActive(int[] cor, int ind){

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {



				if(getPiece(cor).isValidMove(grid, cor, new int[]{i, j}, playing.color) && !getPiece(cor).color.equals(getPiece(new int[]{i, j}).color)) {




						if(Arrays.equals(findButtonWihCor(new int[]{i, j}).cor, new int[]{i, j})){

							findButtonWihCor(new int[]{i, j}).addActionListener(buttonListener);
							findButtonWihCor(new int[]{i, j}).setBackground(Color.decode("#cdd26a"));


						}else{

							findButtonWihCor(new int[]{i, j}).removeActionListener(buttonListener);

						}

				}

			}
		}
	}

	void switchTurns() {
		if(playing == wPlayer) {
			playing = bPlayer;
		}else{
			playing = wPlayer;
		}
	}

	void buttonActionHandler(int[] cor, int ind){
		buttonPresses++;

		System.out.println("button press:"  + Arrays.toString(cor));


		if(buttonPresses == 1 ){
			System.out.println("button press is one");
			setOneColorTOActive();
			setValidMovesActive(cor , ind);
			from = cor;
		}else if(buttonPresses == 2){
			System.out.println("button press is two");
			buttonPresses = 0;
			to = cor;
			capture(from,to);
			print();
			printCor();
			switchTurns();
			resetEveryButton();
			setOneColorTOActive();
			turnHandler();
			isCheck(grid, playing.color);
		}


		
		}

	void startGui() {


		 Font defaultFont = new Font("Oxanium Medium", Font.BOLD, 35);
		 JFrame frame = new JFrame();
		 frame.setSize(600, 620);
		 frame.setLayout(new BorderLayout(0, 0));
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 frame.setTitle("Duppi's Chess");
		 frame.setResizable(false);

		 textArea.setPreferredSize(new Dimension(600, 90));
		 textArea.setFont(defaultFont);
		 textArea.setBorder(BorderFactory.createEmptyBorder(20, 15, -1, 5));
		 frame.add(textArea, BorderLayout.NORTH);

		 JPanel piecePanel = new JPanel();
		 piecePanel.setLayout(new GridLayout(8, 8, 0, 0));
		 piecePanel.setPreferredSize(new Dimension(500, 500));
		 piecePanel.setBorder(BorderFactory.createEmptyBorder(9, 10, 6, 10));
		 piecePanel.setBackground(Color.lightGray);

		 JPanel playerPanel = new JPanel(new GridLayout(2, 1));
		 playerPanel.setPreferredSize(new Dimension(100, 600));
		 playerPanel.setBackground(Color.lightGray);

		 playerAStats.setLineWrap(true);
		 playerAStats.setWrapStyleWord(true);
		 playerAStats.setEditable(true);
		 playerBStats.setFont(customFont);
		 playerAStats.setFont(customFont);
		 playerBStats.setLineWrap(true);
		 playerBStats.setWrapStyleWord(true);
		 playerBStats.setEditable(true);

		 playerAStats.setPreferredSize(new Dimension(100, 260));
		 playerBStats.setPreferredSize(new Dimension(100, 260));
		 playerAStats.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		 playerPanel.add(playerAStats, BorderLayout.SOUTH);
		 playerPanel.add(playerBStats, BorderLayout.NORTH);

		 JPanel containerPanel = new JPanel(new BorderLayout(0, 0));
		 containerPanel.setBorder(null);
		 containerPanel.add(piecePanel, BorderLayout.CENTER);
		 containerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
		 frame.add(containerPanel, BorderLayout.WEST);
		 frame.add(playerPanel, BorderLayout.EAST);

		 containerPanel.setBackground(bg);
		 playerPanel.setBackground(bg);
		 piecePanel.setBackground(bg);
		 textArea.setBackground(bg);
		 playerBStats.setBackground(bg);
		 playerAStats.setBackground(bg);

		 textArea.setForeground(white);
		 playerBStats.setForeground(white);
		 playerAStats.setForeground(white);


		 int i = 0;
		 for (int j = 0; j < 8; j++) {
			 for (int k = 0; k < 8; k++) {
				 button[i] = new myButton();
				 button[i].cor = new int[]{j, k};
				 button[i].ind = i;
				 button[i].addActionListener(buttonListener);
				 button[i].setBorder(null);
				 button[i].setFocusPainted(false);
				 piecePanel.add(button[i]);



				 if ((j + k) % 2 == 0) {
					 button[i].setBackground(white);
					 button[i].bg = white;

				 } else {
					 button[i].setBackground(pastel);
					 button[i].bg = pastel;
				 }


				 if (!grid[j][k].equals(" ")) {
					 ImageIcon pieceIcon = getPiece(new int[]{j, k}).icon;
					 Image scaledImage = pieceIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
					 ImageIcon resizedIcon = new ImageIcon(scaledImage);
					 button[i].setIcon(resizedIcon);
				 }

				 i++;
			 }
		 }


		 wPlayer.printCaptureMessage();
		 bPlayer.printCaptureMessage();

		 playerAStats.setText(wPlayer.stats);
		 playerBStats.setText(bPlayer.stats);

		 frame.setVisible(true);

		 playerBStats.setEditable(false);
		 playerAStats.setEditable(false);
		 textArea.setEditable(true);
		 textArea.setText("Enter White Player's Name: ");
		 textArea.addActionListener(textListener1);
	 }

}