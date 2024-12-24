import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

public class Stream {

    MongoClient client;
    MongoDatabase gameDatabase;
    MongoCollection<Document> games;
    MongoCollection<Document> counter;
    int version = -1;

    Stream(){
        String dataBaseLink = "mongodb+srv://admin:admin@duppischessserver.1esvovi.mongodb.net/?retryWrites=true&w=majority&appName=DuppischessServer";
        client = MongoClients.create(dataBaseLink);
        gameDatabase = client.getDatabase("gameDatabase");
        games = gameDatabase.getCollection("games");
        counter = gameDatabase.getCollection("counter");
    }

    public int getNewPlayerCode() {
        Spectate.increaseCounter();
        return getLastCounter();
    }

    public void makeNewGame(){

        String startState = "r,n,b,q,k,b,n,r,p,p,p,p,p,p,p,p, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,P,P,P,P,P,P,P,P,R,N,B,Q,K,B,N,R,";
        Document newGameDoc = new Document("gameNumber",getLastCounter())
                .append("whitePlayer", "--" )
                .append("blackPlayer", "--" )
                .append("currentState",startState)
                .append("whiteStats", "--" )
                .append("blackStats","--")
                .append("version", version);

        games.insertOne(newGameDoc);


        System.out.println("making new game");
    }

    public void updateGame(int gameId, String whitePlayer, String blackPlayer, String currentState, String whiteStats, String blackStats) {
        Document filter = new Document("gameNumber", gameId);
        Document update = new Document("$set", new Document()
                .append("whitePlayer", whitePlayer)
                .append("blackPlayer", blackPlayer)
                .append("currentState", currentState)
                .append("whiteStats", whiteStats)
                .append("blackStats", blackStats)
                .append("version", version + 1));

        games.updateOne(filter, update);

        System.out.println("updating game" + gameId + whitePlayer + blackPlayer + currentState + whiteStats + blackStats);
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
}




