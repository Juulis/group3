package sample;

import models.GameEngine;

import java.io.IOException;

public class ABS {
    GameEngine gameEngine;
    private static ABS ourInstance;

    static {
        try {
            ourInstance = new ABS();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ABS getInstance() {
        return ourInstance;
    }

    private ABS() throws IOException {
        gameEngine = new GameEngine();
        gameEngine.startGame();
    }
}
