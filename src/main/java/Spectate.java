import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoCollection;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mongodb.client.model.changestream.FullDocument;
import static com.mongodb.client.model.changestream.FullDocument.UPDATE_LOOKUP;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Spectate {


    static MongoClient client;
    static MongoDatabase gameDatabase;
    static MongoCollection<Document> games;
    static MongoCollection<Document> counter;
    myButton[] viewingButtons = new myButton[64];
    JTextField spectateTextArea;
    JTextArea spectateAStats;
    JTextArea spectateBStats;
    JPanel panel =  new JPanel(new BorderLayout());
    JButton goBack = new JButton("GO BACK");
    int spectatingGameId;
    int lastVersion = -1;

    String[][] grid = {
        {"r", "n", "b", "q", "k", "b", "n", "r"},
        {"p", "p", "p", "p", "p", "p", "p", "p"},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {" ", " ", " ", " ", " ", " ", " ", " "},
        {"P", "P", "P", "P", "P", "P", "P", "P"},
        {"R", "N", "B", "Q", "K", "B", "N", "R"}
    };


    Spectate() {
        String dataBaseLink = "mongodb+srv://admin:admin@duppischessserver.1esvovi.mongodb.net/?retryWrites=true&w=majority&appName=DuppischessServer";
        client = MongoClients.create(dataBaseLink);
        gameDatabase = client.getDatabase("gameDatabase");
        games = gameDatabase.getCollection("games");
        counter = gameDatabase.getCollection("counter");
        guiInit();
    }



    static void increaseCounter(){
        counter.updateOne(Filters.eq("type", "counter"), Updates.inc("count", 1));
    }

    static public void ressetCounter(){
        counter.updateOne(Filters.eq("type", "counter"), Updates.set("count", 0));
    }

    public piece getPiece(String[][] grid, int[] cor) {
        piece p;
        piece d = new piece();
        d.setColor("e");
        for (String id : board.initList) {
            if (grid[cor[0]][cor[1]].equals(board.pieceMap.get(id).not)) {
                p = board.pieceMap.get(id);
                return p;
            }
        }
        return d;
    }


    public void updateGame(int gameId) {

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
        Document query = new Document("gameNumber", gameId);
        Document updatedDoc = games.find(query).first();

        if (updatedDoc != null) {
            int currentVersion = updatedDoc.getInteger("version", -1);
            System.out.println(currentVersion);

            if (currentVersion > lastVersion) {
                lastVersion = currentVersion;
                updatePanel();
            }
        }
            System.out.println("5 sec has passed");
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void updatePanel() {
        Document query = new Document("gameNumber", spectatingGameId);
        Document updatedDoc = games.find(query).first();
        System.out.println("GameUpdated");
        String whitePlayerName = updatedDoc.getString("whitePlayer");
        String blackPlayerName = updatedDoc.getString("blackPlayer");
        String blackPlayerStats = updatedDoc.getString("blackStats");
        String whitePlayerStats = updatedDoc.getString("whiteStats");
        String gridString = updatedDoc.getString("currentState");

        spectateTextArea.setText(whitePlayerName + " vs " + blackPlayerName);
        spectateAStats.setText(blackPlayerStats);
        spectateBStats.setText(whitePlayerStats);
        updateSpectatorGrid(gridString);
    }



    public void updateSpectatorGrid(String grid){
        String[] peiceArr = grid.split(",");
        for (int i = 0; i < 64; i++) {
            if(peiceArr[i] != " ") {
                for (String id : board.initList) {
                    if (peiceArr[i].equals(board.pieceMap.get(id).not)) {

                        viewingButtons[i].setIcon(board.pieceMap.get(id).icon);

                    }
                }

            }
        }

    }

    public int getLastCounter() {
        Document lastCounter = counter.find(new Document("type", "counter"))
                .projection(new Document("count", 1).append("_id", 0))
                .first();

        if (lastCounter != null) {
            return lastCounter.getInteger("count", -1);
        }
        return -1;
    }
    public void guiInit(){



        spectateTextArea = new JTextField();
        spectateTextArea.setPreferredSize(new Dimension(470, 90));
        spectateTextArea.setFont(board.turnFont);
        spectateTextArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 1, 5));
        spectateTextArea.setForeground(board.white);
        spectateTextArea.setBackground(board.bg);
        spectateTextArea.setEditable(true);
        spectateTextArea.setText("Enter code:");
        ActionListener gameCodeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField textOut = (JTextField) e.getSource();
                String gameIdText = textOut.getText().substring(textOut.getText().indexOf(":") + 1);

                try {
                    int gameId = Integer.parseInt(gameIdText);

                    if (gameId > getLastCounter()) {
                        textOut.setText("Invalid, Enter Code:");
                        return;
                    }

                    spectatingGameId = gameId;
                    textOut.setText("Connecting...");
                    updateGame(gameId);
                    textOut.removeActionListener(this);
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid game ID format.");
                }

            }

        };
        spectateTextArea.addActionListener(gameCodeListener);


        spectateAStats = new JTextArea();// player A stats
        spectateAStats.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        spectateAStats.setLineWrap(true);
        spectateAStats.setWrapStyleWord(true);
        spectateAStats.setFont(board.playerFont);
        spectateAStats.setEditable(true);
        spectateAStats.setPreferredSize(new Dimension(100, 260));
        spectateAStats.setBackground(board.bg);
        spectateAStats.setForeground(board.white);
        spectateAStats.setEditable(false);


        spectateBStats = new JTextArea();// player B stats
        spectateBStats.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        spectateBStats.setLineWrap(true);
        spectateBStats.setWrapStyleWord(true);
        spectateBStats.setFont(board.playerFont);
        spectateBStats.setEditable(true);
        spectateBStats.setPreferredSize(new Dimension(100, 260));
        spectateBStats.setBackground(board.bg);
        spectateBStats.setForeground(board.white);
        spectateBStats.setEditable(false);

        JPanel spectatePlayerPanel = new JPanel(new GridLayout(2, 1, 20, 0)); // holds all the stats
        spectatePlayerPanel.setBackground(board.bg);
        spectatePlayerPanel.setPreferredSize(new Dimension(100, 600));
        spectatePlayerPanel.add(spectateAStats, BorderLayout.NORTH);
        spectatePlayerPanel.add(spectateBStats, BorderLayout.SOUTH);

        JPanel spectateControls = new JPanel(new GridLayout(4, 1, 0, 0));
        spectateControls.setBackground(board.bg);
        spectateControls.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 5));
        spectateControls.setPreferredSize(new Dimension(110, 100));


        goBack.setBorder(null);
        goBack.setFocusPainted(false);
        goBack.setForeground(new Color(237, 222, 121));
        goBack.setBackground(board.bg);
        goBack.setFont(board.playerFont);
        spectateControls.add(goBack);

        JPanel spectateTextAndControlPanel = new JPanel(new BorderLayout());
        spectateTextAndControlPanel.setBackground(board.bg);
        spectateTextAndControlPanel.add(spectateControls, BorderLayout.EAST);
        spectateTextAndControlPanel.add(spectateTextArea, BorderLayout.CENTER);


        JPanel spectatePiecePanel = new JPanel();//holds all the pieces
        spectatePiecePanel.setLayout(new GridLayout(8, 8, 0, 0));
        spectatePiecePanel.setPreferredSize(new Dimension(500, 500));
        spectatePiecePanel.setBorder(BorderFactory.createEmptyBorder(9, 9, 9, 16));
        spectatePiecePanel.setBackground(board.bg);

        int i = 0;
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                viewingButtons[i] = new myButton();
                viewingButtons[i].cor = new int[]{j, k};
                viewingButtons[i].ind = i;



                viewingButtons[i].setBorder(null);
                viewingButtons[i].setFocusPainted(false);
                spectatePiecePanel.add(viewingButtons[i]);


                if ((j + k) % 2 == 0) {
                    viewingButtons[i].setBackground(board.white);
                    viewingButtons[i].bg = board.white;

                } else {
                    viewingButtons[i].setBackground(board.pastel);
                    viewingButtons[i].bg = board.pastel;
                }


                if (!grid[j][k].equals(" ")) {
                    ImageIcon pieceIcon = getPiece(grid,new int[]{j, k}).icon;
                    Image scaledImage = pieceIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    ImageIcon resizedIcon = new ImageIcon(scaledImage);
                    viewingButtons[i].setIcon(resizedIcon);
                }

                i++;
            }
        }


        spectateBStats.setText("----");
        spectateAStats.setText("----");
        panel.add(spectatePiecePanel, BorderLayout.WEST);
        panel.add(spectatePlayerPanel, BorderLayout.EAST);
        panel.add(spectateTextAndControlPanel, BorderLayout.NORTH);


    }

}
