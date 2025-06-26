
package Logic;

import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    public List<BirdState> birds;
    public List<PigState> pigs;
    public List<BlockState> blocks;
    public boolean isPaused;

    public static class BirdState implements Serializable {
        private static final long serialVersionUID = 1L;
        public Vector2 position;
        public boolean isLaunched;
    }

    public static class PigState implements Serializable {
        private static final long serialVersionUID = 1L;
        public Vector2 position;
        public int hits;
    }

    public static class BlockState implements Serializable {
        private static final long serialVersionUID = 1L;
        public Vector2 position;
    }
}
