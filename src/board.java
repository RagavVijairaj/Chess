import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextListener;
import java.util.*;

class board {

    //gui
    private final Color white = Color.decode("#f0d9b5");
    private final Color pastel = Color.decode("#b48963");
    private final Color bg = Color.decode("#181414");
    private final Font turnFont = new Font("IMPACT", Font.PLAIN, 35);
    private final Font playerFont = new Font("IMPACT", Font.PLAIN, 18);
    private final String[] initList = {"wR", "bR", "wN", "bN", "wB", "bB", "wQ", "bQ", "wK", "bK", "wP", "bP"};
    // pieces
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
    //players
    player wPlayer;
    player bPlayer;
    player playing;
    myButton[] button;
    JTextField textArea;
    JTextArea playerAStats;
    JTextArea playerBStats;
    String[][] grid;
    Map<String, piece> pieceMap;

    //action listener stuff
    int[] from;
    int[] to;
    int buttonPresses = 0;
    ActionListener textListener2;
    ActionListener textListener1;
    ActionListener restartGame;
    ActionListener buttonListener;
    Boolean isSameColorButtonClicked = true;

    //game Logic
    Boolean gameFirstMove = true;
    Boolean isPeiceLastCaptured = false;
    boolean isCheckMate = false;
    boolean isStalemate = false;
    boolean isCheckCurrently = false;

    //frame
    JFrame frame;


    board() {


        pieceMap = new HashMap<String, piece>();
        bPlayer = new player("b");
        wPlayer = new player("w");
        playing = wPlayer;
        //pieces
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


    void guiInit() {

        //action listener

        buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                myButton clickedButton = (myButton) e.getSource();
                buttonActionHandler(clickedButton.cor, clickedButton.ind);
            }


        };

        textListener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField textOut = (JTextField) e.getSource();
                setWPlayerName(textOut.getText());
                textOut.setText("Enter Black Player's Name: ");
                textOut.removeActionListener(textListener1);
                textOut.addActionListener(textListener2);

            }
        };

        textListener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField textOut = (JTextField) e.getSource();
                setBPlayerName(textOut.getText());
                textOut.setText("Game Started");
                textOut.removeActionListener(textListener2);
                textOut.setEditable(false);


            }
        };


        restartGame = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                board b = new board();
                b.init();
                b.guiInit();
                b.guiStart();
            }
        };

        //gui

        button = new myButton[64];

    }

    void init() {

        //for searcing later
        grid = new String[8][8];
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

        //sets all the boxes empty
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                grid[j][i] = " ";
            }
        }

        //set the peices in their start position
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


	void printWhite() {


        for (int j = 0; j < 8; j++) {
            System.out.println();
            System.out.print("|");

            for (int i = 0; i < 8; i++) {
                System.out.print(grid[j][i] + "|");

            }

        }

        System.out.println();

    }
    
    void printBlack(){
        for(int i = grid.length -1 ; i >= 0; i-- ){
            
            System.out.println();
            System.out.print("|");
            for(int j = grid[i].length -1 ; j >= 0; j-- ){
                System.out.print(grid[i][j] + "|");
                
            }
        }
        System.out.println();
    }
    //uses the peice map to get the peice from its not
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


    boolean isCheck(String c) {

        ArrayList<Integer[]> peicesToCheck = new ArrayList<>();
        int[] kingCor = new int[]{};
        String kingNot = c.equals("b") ? "k" : "K";


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j].equals(kingNot)) kingCor = new int[]{i, j};
            }
        }


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boolean isPeiceOnGrid = c.equals("b") ? grid[i][j].equals("B") || grid[i][j].equals("P") || grid[i][j].equals("R") || grid[i][j].equals("N") || grid[i][j].equals("Q") : grid[i][j].equals("p") || grid[i][j].equals("r") || grid[i][j].equals("n") || grid[i][j].equals("b") || grid[i][j].equals("q");
                if (isPeiceOnGrid) {
                    peicesToCheck.add(new Integer[]{i, j});

                }
            }
        }


        for (Integer[] isCheck : peicesToCheck) {


            piece toCheck = getPiece(new int[]{isCheck[0], isCheck[1]});


            if (toCheck.isValidMove(grid, new int[]{isCheck[0], isCheck[1]}, kingCor, toCheck.color)) {
                if (button != null) {

                    findButtonWihCor(kingCor).setBackground(Color.decode("#fa5050"));
                }
                return true;
            } else {
                if (button != null) {
                    findButtonWihCor(kingCor).setBackground(findButtonWihCor(kingCor).bg);
                }
            }
        }

        return false;
    }


    boolean isGameNotOver() {

//		if(isCheck(playing.color)){    //checkmate
//			//getAllpeicesFromeThePlayingColor
//			ArrayList<Integer[]> peicesToCheck = new ArrayList<>();
//
//			for (int i = 0; i < 8; i++) {
//				for (int j = 0; j < 8; j++) {
//					boolean isPeiceOnGrid = playing.color.equals("W") ? grid[i][j].equals("B") || grid[i][j].equals("P") || grid[i][j].equals("R") || grid[i][j].equals("N") || grid[i][j].equals("Q") : grid[i][j].equals("p") || grid[i][j].equals("r") || grid[i][j].equals("n") || grid[i][j].equals("b") || grid[i][j].equals("q");
//					if (isPeiceOnGrid) {
//						peicesToCheck.add(new Integer[]{i, j});
//
//					}
//				}
//			}
//
//
//			//Check If any moves can removeCheck
//
//			for (Integer[] isCheck : peicesToCheck) {
//				piece toCheck = getPiece(new int[]{isCheck[0], isCheck[1]});
//				for (int i = 0; i < 8; i++) {
//					for (int j = 0; j < 8; j++) {
//						if(checkIfMoveRemovesCheck(new int[]{isCheck[0], isCheck[1]}, new int[] {i,j})){
//							isCheckMate = true;
//							return false;
//						}
//					}
//				}
//
//
//			}
//
//
//
//		}else{                          //stale mate
//
//
//
//		}


//        return true;
        return false;
    }


    boolean capture(int[] from, int[] to) {
        if (grid[from[0]][from[1]].equals(" ") || getPiece(to).color.equals(getPiece(from).color)) {

            return false;
        } else {
            piece fromPieceType = getPiece(from);

            if (fromPieceType.isValidMove(grid, from, to, fromPieceType.color)) {
                // Handle capture
                if (!grid[to[0]][to[1]].equals(" ")) {


                    piece capturedPiece = getPiece(to);
                    playing.capturePiece(capturedPiece);

                    // Update the stats display
                    if (playing.color.equals("w")) {

                        playing.printCaptureMessage(playerAStats);

                    } else if (playing.color.equals("b")) {

                        playing.printCaptureMessage(playerBStats);

                    }
                }

                // Update the board
                grid[to[0]][to[1]] = fromPieceType.not;
                grid[from[0]][from[1]] = " ";
                isPeiceLastCaptured = true;

                // Handle game state
                switchTurns();
                if (isGameNotOver()) {


                    if (button != null) {
                        if (isCheckMate) {
                            resetEveryButton();
                            removeAllListeners();

                            textArea.setText(playing.playerName + " WON!" + " Press Enter To Restart");
                            textArea.addActionListener(restartGame);
                        } else if (isStalemate) {
                            resetEveryButton();
                            removeAllListeners();

                            textArea.setText("DRAW! Press Enter to Restart");
                            textArea.addActionListener(restartGame);
                        }
                    }
                } else {
                    switchTurns();
                }
                return true;
            } else {
                return false;
            }
        }
    }

	boolean safeCapture(int[] from, int[] to) {
		if (grid[from[0]][from[1]].equals(" ") || getPiece(to).color.equals(getPiece(from).color)) {

			return false;
		} else {
			piece fromPieceType = getPiece(from);

			if (fromPieceType.isValidMove(grid, from, to, fromPieceType.color)) {
				// Handle capture
				if (!grid[to[0]][to[1]].equals(" ")) {


					piece capturedPiece = getPiece(to);
					playing.capturePiece(capturedPiece);

					// Update the stats display
					if (playing.color.equals("w")) {

						playing.printCaptureMessage(playerAStats);

					} else if (playing.color.equals("b")) {

						playing.printCaptureMessage(playerBStats);

					}
				}

				// Update the board
				grid[to[0]][to[1]] = fromPieceType.not;
				grid[from[0]][from[1]] = " ";
				isPeiceLastCaptured = true;

				return true;
			} else {
				return false;
			}
		}
	}


    void setBPlayerName(String wholeLine) {
        bPlayer.playerName = wholeLine.substring(wholeLine.indexOf(":") + 2);
        bPlayer.printCaptureMessage(playerBStats);

    }

    void setWPlayerName(String wholeLine) {

        wPlayer.playerName = wholeLine.substring(wholeLine.indexOf(":") + 2);
        wPlayer.printCaptureMessage(playerAStats);

    }

    void turnHandler() {
        if (playing == bPlayer) {
            textArea.setText(bPlayer.playerName + "'s turn");

        } else {
            textArea.setText(wPlayer.playerName + "'s turn");

        }
    }

    void resetEveryButton() {

        for (int j = 0; j < 64; j++) {
            button[j].setBackground(button[j].bg);
            if (!grid[button[j].cor[0]][button[j].cor[1]].equals(" ")) {
                ImageIcon pieceIcon = getPiece(button[j].cor).icon;
                Image scaledImage = pieceIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(scaledImage);
                button[j].setIcon(resizedIcon);
            } else {
                button[j].setIcon(null);
            }
            button[j].removeActionListener(buttonListener);

        }
        isCheckCurrently = isCheck(playing.color);

    }

    void removeAllListeners() {
        for (int i = 0; i < 64; i++) {
            button[i].removeActionListener(buttonListener);
        }
    }

    void setOneColorTOActive() {
        removeAllListeners();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((getPiece(new int[]{i, j}).color.equals(playing.color))) {

                    for (int k = 0; k < 64; k++) {
                        if (Arrays.equals(button[k].cor, new int[]{i, j})) {
                            button[k].addActionListener(buttonListener);


                        }
                    }
                }
            }
        }
    }

    myButton findButtonWihCor(int[] cor) {

        myButton retButton;

        for (int i = 0; i < 64; i++) {
            if (Arrays.equals(button[i].cor, cor)) {
                return button[i];
            }
        }

        return null;
    }

    boolean setValidMovesActive(int[] cor, int ind) {
//		findButtonWihCor(cor).addActionListener(buttonListener);
        boolean validMoves = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {


                if (getPiece(cor).isValidMove(grid, cor, new int[]{i, j}, playing.color) && !getPiece(cor).color.equals(getPiece(new int[]{i, j}).color) && checkIfMoveRemovesCheck(cor, new int[]{i, j})) {
                    validMoves = true;
                    if (Arrays.equals(findButtonWihCor(new int[]{i, j}).cor, new int[]{i, j})) {

                        findButtonWihCor(new int[]{i, j}).addActionListener(buttonListener);

                        findButtonWihCor(new int[]{i, j}).setBackground(Color.decode("#cdd26a"));


                    } else {

                        findButtonWihCor(new int[]{i, j}).removeActionListener(buttonListener);

                    }


                } else if (getPiece(cor).color.equals(getPiece(new int[]{i, j}).color)) {
                    findButtonWihCor(new int[]{i, j}).addActionListener(buttonListener);
                }

            }
        }
        findButtonWihCor(cor).setBackground(new Color(250, 235, 145));
        return validMoves;
    }


    boolean checkIfMoveRemovesCheck(int[] from, int[] to) {
        board test = new board();
        test.init();

        test.grid = Arrays.copyOf(grid, grid.length);
        for (int i = 0; i < grid.length; i++) {
            test.grid[i] = Arrays.copyOf(grid[i], grid[1].length);
        }

        test.safeCapture(from, to);

        return !test.isCheck(playing.color);
    }


    void switchTurns() {

        if (playing == wPlayer) {
            playing = bPlayer;
        } else {
            playing = wPlayer;
        }
    }


    void buttonActionHandler(int[] cor, int ind) {

        buttonPresses++;



        if (buttonPresses == 1) {
            resetEveryButton();
            button[ind].removeActionListener(buttonListener);
            if (!isSameColorButtonClicked || gameFirstMove || isPeiceLastCaptured) {
                if (isPeiceLastCaptured) isPeiceLastCaptured = false;
                isSameColorButtonClicked = false;
                from = cor;
            }

            setOneColorTOActive();
            removeAllListeners();
            if (!setValidMovesActive(cor, ind)) {
                setOneColorTOActive();
                buttonPresses = 0;
            }


        } else if (buttonPresses == 2) {
            to = cor;
            buttonPresses = 0;

            if (getPiece(from).color.equals(getPiece(to).color)) {


                buttonPresses++;
                resetEveryButton();
                setOneColorTOActive();
                setValidMovesActive(cor, ind);
                from = cor;
                isSameColorButtonClicked = true;

            } else {

				capture(from, to);
                switchTurns();


                if (!isCheckMate && !isStalemate) {

                    resetEveryButton();
                    setOneColorTOActive();
                    turnHandler();

                }

                isCheck(playing.color);

            }


        }

    }


    void guiStart() {


        frame = new JFrame();  //the entire screen

        frame.setSize(600, 620);
        frame.setLayout(new BorderLayout(0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Duppi's Chess");
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/kb.png"))).getImage());
        frame.setResizable(false);
        frame.setBackground(bg);

        textArea = new JTextField(); // shows player turn

        textArea.setPreferredSize(new Dimension(600, 90));
        textArea.setFont(turnFont);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 1, 5));
        textArea.setForeground(white);
        textArea.setBackground(bg);
        textArea.setEditable(true);
        textArea.setText("Enter White Player's Name: ");
        textArea.addActionListener(textListener1);


        JPanel piecePanel = new JPanel();//holds all the peices

        piecePanel.setLayout(new GridLayout(8, 8, 0, 0));
        piecePanel.setPreferredSize(new Dimension(500, 500));
        piecePanel.setBorder(BorderFactory.createEmptyBorder(9, 9, 9, 16));
        piecePanel.setBackground(bg);

        JPanel playerPanel = new JPanel(new GridLayout(2, 1, 20, 0)); // holds all the stats

        playerPanel.setBackground(bg);
        playerPanel.setPreferredSize(new Dimension(100, 600));
        playerPanel.setBackground(bg);


        playerAStats = new JTextArea();// player A stats

        playerAStats.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        playerAStats.setLineWrap(true);
        playerAStats.setWrapStyleWord(true);
        playerAStats.setFont(playerFont);
        playerAStats.setEditable(true);
        playerAStats.setPreferredSize(new Dimension(100, 260));
        playerAStats.setBackground(bg);
        playerAStats.setForeground(white);
        playerAStats.setEditable(false);


        playerBStats = new JTextArea();// player B stats

        playerBStats.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        playerBStats.setLineWrap(true);
        playerBStats.setWrapStyleWord(true);
        playerBStats.setFont(playerFont);
        playerBStats.setEditable(true);
        playerBStats.setPreferredSize(new Dimension(100, 260));
        playerBStats.setBackground(bg);
        playerBStats.setForeground(white);
        playerBStats.setEditable(false);


        playerPanel.add(playerAStats, BorderLayout.NORTH);
        playerPanel.add(playerBStats, BorderLayout.SOUTH);


        frame.add(playerPanel, BorderLayout.EAST);
        frame.add(textArea, BorderLayout.NORTH);
        frame.add(piecePanel, BorderLayout.WEST);

        // inits the button's color and icon for the start of the game
        int i = 0;
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                button[i] = new myButton();
                button[i].cor = new int[]{j, k};
                button[i].ind = i;

                // only add white pieces to be active
                if (i > 47 && i < 64) {
                    button[i].addActionListener(buttonListener);

                }


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


        //inits player's stats
        wPlayer.printCaptureMessage(playerAStats);
        bPlayer.printCaptureMessage(playerBStats);
        //add the stats to text box
        playerAStats.setText(wPlayer.stats);
        playerBStats.setText(bPlayer.stats);

        frame.setVisible(true);

    }

}
