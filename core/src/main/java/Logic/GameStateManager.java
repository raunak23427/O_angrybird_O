package Logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.*;

public class GameStateManager {

    public static void saveGameState(GameState gameState, String fileName) {
        try {
            FileHandle file = Gdx.files.local(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file.write(false));
            out.writeObject(gameState);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState loadGameState(String fileName) {
        try {
            FileHandle file = Gdx.files.local(fileName);
            ObjectInputStream in = new ObjectInputStream(file.read());
            GameState gameState = (GameState) in.readObject();
            in.close();
            return gameState;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
