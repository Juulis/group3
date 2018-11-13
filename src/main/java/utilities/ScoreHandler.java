package utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import models.Highscore;
import models.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class ScoreHandler {

    private Highscore[] highscores;

    public Highscore[] readHighscoresFromJSON(){
        try {
            JsonReader reader = new JsonReader( new FileReader("src/main/java/json/Highscore.json"));
            highscores = new Gson().fromJson(reader, Highscore[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Arrays.sort(highscores, Collections.reverseOrder((Highscore h1, Highscore h2) -> h1.getScore()-h2.getScore()));
        return highscores;
    }

    public boolean isScoreAHighscore( Player p){
        int score = p.getScore();
        readHighscoresFromJSON();
        int minHighscore = highscores[highscores.length-1].getScore();
        if( score > minHighscore)
            return true;
        return false;
    }

    public void saveHighscore(Player p){
        Highscore highscore = new Highscore(p.getName(), p.getScore());
        highscores[highscores.length-1] = highscore;
        Arrays.sort(highscores, Collections.reverseOrder((Highscore h1, Highscore h2) -> h1.getScore()-h2.getScore()));
        try {
            FileWriter writer = new FileWriter("src/main/java/json/Highscore.json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(highscores, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerScore( Player p){
        int health = p.getHealth();
        int nrCardsLeft = p.getCurrentDeck().size() + p.getPlayerHand().size()+ p.getTableCards().size();
        int score = health * nrCardsLeft;
        p.setScore(score);
    }

    public void checkScore( Player p){
        setPlayerScore(p);
        if( isScoreAHighscore(p))
            saveHighscore(p);
    }
}
