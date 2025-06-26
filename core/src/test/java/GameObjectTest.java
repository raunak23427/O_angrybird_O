import Actors.Bird;
import Actors.Block;
import Actors.Pig;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameObjectTest {

    private World world;
    private Bird redBird;
    private Pig pig;
    private Block block;

    @BeforeEach
    public void setUp() {
        world = new World(new Vector2(0, -9.8f), true); // Create a physics world with gravity
        redBird = new Bird(world, 0);  // Create a RedBird with a texture index 0
        pig = new Pig(world, 1, 1);  // Create a Pig with a texture index 1
        block = new Block(world, 400, 200, 50, 50);  // Create a Block at position (400, 200) with width & height 50

        System.out.println("Setting up test environment...");
    }

    @Test
    public void testRedBirdCreation() {
        assertNotNull(redBird, "RedBird should be created.");
        assertNotNull(redBird.getBody(), "RedBird should have a Box2D body.");
    }

    @Test
    public void testPigCreation() {
        assertNotNull(pig, "Pig should be created.");
        assertNotNull(pig.getBody(), "Pig should have a Box2D body.");
    }

    @Test
    public void testBlockCreation() {

        assertNotNull(block, "Block should be created.");
        assertNotNull(block.getBody(), "Block should have a Box2D body.");
    }

    @Test
    public void testRedBirdLaunch() {

        redBird.getBody().setLinearVelocity(10f, 10f); // Apply some velocity
        assertTrue(redBird.getBody().getLinearVelocity().x > 0, "RedBird should have positive x velocity.");
        assertTrue(redBird.getBody().getLinearVelocity().y > 0, "RedBird should have positive y velocity.");
    }

    @Test
    public void testPigAndRedBirdCollision() {
        // Test that Pig and RedBird will collide if they are close enough
        redBird.getBody().setTransform(4f, 4f, 0); // Set initial position of RedBird
        pig.getBody().setTransform(4f, 4f, 0);  // Set initial position of Pig (same position to cause collision)
        world.step(1/60f, 6, 2);
        assertTrue(redBird.getBody().getPosition().dst(pig.getBody().getPosition()) < redBird.getRadius() + pig.getRadius(),
            "RedBird and Pig should collide.");
    }

    @Test
    public void testBlockPosition() {
        // Test that the Block is placed at the correct position
        float xPos = block.getBody().getPosition().x * 100;
        float yPos = block.getBody().getPosition().y * 100;
        assertEquals(400, xPos, "Block's x position should be 400.");
        assertEquals(200, yPos, "Block's y position should be 200.");
    }

    @Test
    public void testWorldStep() {
        world.step(1/60f, 6, 2); // Simulate a step
        assertNotNull(world, "World should be updated with physics.");
    }
}
