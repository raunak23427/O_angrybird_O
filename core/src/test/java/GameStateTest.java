import Logic.GameState;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class GameStateTest {

    @Test
    public void testDefaultValues() {
        GameState gameState = new GameState();
        assertFalse(gameState.isPaused);
        assertNull(gameState.birds);
        assertNull(gameState.pigs);
        assertNull(gameState.blocks);
    }

    @Test
    public void testMultipleEntities() {
        GameState gameState = new GameState();

        GameState.BirdState bird1 = new GameState.BirdState();
        bird1.position = new Vector2(1, 1);
        bird1.isLaunched = true;

        GameState.BirdState bird2 = new GameState.BirdState();
        bird2.position = new Vector2(2, 2);
        bird2.isLaunched = false;

        gameState.birds = List.of(bird1, bird2);

        GameState.PigState pig1 = new GameState.PigState();
        pig1.position = new Vector2(3, 3);
        pig1.hits = 1;

        GameState.PigState pig2 = new GameState.PigState();
        pig2.position = new Vector2(4, 4);
        pig2.hits = 2;

        gameState.pigs = List.of(pig1, pig2);

        GameState.BlockState block1 = new GameState.BlockState();
        block1.position = new Vector2(5, 5);

        GameState.BlockState block2 = new GameState.BlockState();
        block2.position = new Vector2(6, 6);

        gameState.blocks = List.of(block1, block2);

        assertEquals(2, gameState.birds.size());
        assertEquals(2, gameState.pigs.size());
        assertEquals(2, gameState.blocks.size());
    }

    @Test
    public void testEdgeCases() {
        GameState gameState = new GameState();
        gameState.birds = null;
        gameState.pigs = null;
        gameState.blocks = null;

        assertNull(gameState.birds);
        assertNull(gameState.pigs);
        assertNull(gameState.blocks);
    }

    @Test
    public void testPauseGame() {
        GameState gameState = new GameState();
        gameState.isPaused = true;
        assertTrue(gameState.isPaused, "Game should be paused.");
    }

    @Test
    public void testResumeGame() {
        GameState gameState = new GameState();
        gameState.isPaused = true;
        gameState.isPaused = false;
        assertFalse(gameState.isPaused, "Game should be resumed.");
    }

    @Test
    public void testAddBird() {
        GameState gameState = new GameState();
        GameState.BirdState bird = new GameState.BirdState();
        bird.position = new Vector2(1, 1);
        gameState.birds = List.of(bird);
        assertEquals(1, gameState.birds.size(), "There should be one bird.");
        assertEquals(new Vector2(1, 1), gameState.birds.get(0).position, "Bird position should be (1, 1).");
    }

    @Test
    public void testAddPig() {
        GameState gameState = new GameState();
        GameState.PigState pig = new GameState.PigState();
        pig.position = new Vector2(2, 2);
        gameState.pigs = List.of(pig);
        assertEquals(1, gameState.pigs.size(), "There should be one pig.");
        assertEquals(new Vector2(2, 2), gameState.pigs.get(0).position, "Pig position should be (2, 2).");
    }

    @Test
    public void testAddBlock() {
        GameState gameState = new GameState();
        GameState.BlockState block = new GameState.BlockState();
        block.position = new Vector2(3, 3);
        gameState.blocks = List.of(block);
        assertEquals(1, gameState.blocks.size(), "There should be one block.");
        assertEquals(new Vector2(3, 3), gameState.blocks.get(0).position, "Block position should be (3, 3).");
    }
}
