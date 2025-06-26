package Logic;

import Actors.Block;
import Actors.Pig;
import Actors.Bird;
import Actors.Slingshot;
import Screens.EndScreen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import io.github.project.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEntities {
    private List<Bird> birds; // List of birds
    private List<Pig> pigs; // List of pigs
    private int currentBirdIndex = 0; // Tracks the bird mounted on the slingshot
    private Slingshot slingshot;
    private Stage stage;
    private Map<Body, Actor> bodyActorMap = new HashMap<>();
    private List<Body> bodiesToRemove = new ArrayList<>();
    private Main game;
    public GameEntities(Main game,World world, Stage stage) {
        this.stage = stage;
        this.game = game;
        world.setGravity(new Vector2(0, -5f));
        world.setContactListener(new GameContactListener());
        birds = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Bird bird = new Bird(world, i); // Pass the index to select the texture
            bird.setStatic();
            bodyActorMap.put(bird.getBody(), bird);
            if (i == 0) {
                bird.setPosition(2.5f, 3f); // Set initial position
            } else {
                bird.setPosition(2f - i * 0.1f, 2f); // Set initial position
            }

            birds.add(bird);
        }

        slingshot = new Slingshot(world, new Vector2(2f, 0.2f + 0.15f), birds.get(currentBirdIndex), this);
        stage.addActor(slingshot);
        for (Bird bird : birds) {
            stage.addActor(bird);
        }

        updateBirdQueue();
        float startX = 800;
        float startY = 350;
        pigs = new ArrayList<>();
        int numberOfTowers = MathUtils.random(3, 6);
        for (int i = 0; i < numberOfTowers; i++) {
            int numberOfBlocks = MathUtils.random(2, 7);
            boolean addPigOnTop = pigs.size() < 3 || MathUtils.randomBoolean();
            createTower(world, stage, startX += 35, startY, numberOfBlocks, addPigOnTop);
        }
    }


    private void createTower(World world, Stage stage, float startX, float startY, int height, boolean addPigOnTop) {
        float blockWidth = 60f;
        float blockHeight = 60f;

        float towerTopY = startY;

        for (int i = 0; i < height; i++) {
            float blockY = startY + i * blockHeight;

            Block block = new Block(world, startX, blockY, blockWidth, blockHeight);
            bodyActorMap.put(block.getBody(), block);
            block.setDensity(0.2f);
            block.setFriction(1f);
            stage.addActor(block);

            towerTopY = blockY;
        }

        if (addPigOnTop && height > 0) {
            Pig pig = new Pig(world, startX, towerTopY + blockHeight);
            bodyActorMap.put(pig.getBody(), pig);
            pig.setSize(50f, 50f); // Match updated pig size
            pigs.add(pig);
            stage.addActor(pig);
        }
    }

    public void update(float delta) {
        for (Pig pig : pigs) {
            pig.update(delta);
        }
        for (Body body : bodiesToRemove) {
            Actor actor = bodyActorMap.get(body);
            if (actor != null) {
                stage.getActors().removeValue(actor, true);
                bodyActorMap.remove(body);
            }
            body.getWorld().destroyBody(body);
        }
        bodiesToRemove.clear();
    }

    public int getRemainingBirds() {
        int remainingBirds = 0;
        for (Bird bird : birds) {
            if (!bird.isLaunched()) {
                remainingBirds++;
            }
        }
        return remainingBirds;
    }

    public int getRemainingPigs() {
        return pigs.size();
    }

    public void checkWinOrLose() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (getRemainingPigs() == 0) {
                    System.out.println("You win! All pigs are eliminated.");
                    game.setScreen(new EndScreen(game, 1));
                } else if (getRemainingBirds() == 0) {
                    System.out.println("You lose! No birds left.");
                    game.setScreen(new EndScreen(game, 2));
                } else {
                    System.out.println("Game still in progress.");
                }
            }
        }, 2); // 5 seconds delay
    }


    public void onBirdLaunched() {
        birds.get(currentBirdIndex).setLaunched(true);

        while (currentBirdIndex < birds.size() - 1 && birds.get(currentBirdIndex).isLaunched()) {
            currentBirdIndex++;
        }

        if (currentBirdIndex < birds.size() && !birds.get(currentBirdIndex).isLaunched()) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    updateBirdQueue();
                    birds.get(currentBirdIndex).setBodyPosition(2.5f, 3f); // Reset bird position
                    slingshot.setBird(birds.get(currentBirdIndex));
                }
            }, 0.5f); // 0.5 seconds delay
        } else {
            System.out.println("No more birds left!");
        }
        System.out.println("Remaining birds: " + getRemainingBirds());
        System.out.println("Remaining pigs: " + getRemainingPigs());
        checkWinOrLose();
    }

    private void updateBirdQueue() {
        if (currentBirdIndex < birds.size() && !birds.get(currentBirdIndex).isLaunched()) {
            birds.get(currentBirdIndex).setBodyPosition(2.5f, 3f); // Reset bird position
        }
    }

    public void dispose() {
        for (Bird bird : birds) {
            bird.dispose();
        }
        for (Pig pig : pigs) {
            pig.dispose();
        }
        slingshot.dispose();
    }

    private class GameContactListener implements ContactListener {
        @Override
        public void beginContact(Contact contact) {
            Fixture fixtureA = contact.getFixtureA();
            Fixture fixtureB = contact.getFixtureB();

            handleCollision(fixtureA, fixtureB);
        }

        @Override
        public void endContact(Contact contact) {
            // Not used
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
            // Not used
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }

        private void handleCollision(Fixture fixtureA, Fixture fixtureB) {
            Object userDataA = fixtureA.getBody().getUserData();
            Object userDataB = fixtureB.getBody().getUserData();

            if ((userDataA instanceof Bird && userDataB instanceof Pig) ||
                (userDataB instanceof Bird && userDataA instanceof Pig)) {
                Body pigBody = userDataA instanceof Pig ? fixtureA.getBody() : fixtureB.getBody();
                bodiesToRemove.add(pigBody);
                Pig pig = (Pig) (userDataA instanceof Pig ? userDataA : userDataB);
                pigs.remove(pig);
                System.out.println("Pig hit by RedBird. Added to removal queue.");
            }

            if ((userDataA instanceof Bird && userDataB instanceof Block) ||
                (userDataB instanceof Bird && userDataA instanceof Block)) {
                Body blockBody = userDataA instanceof Block ? fixtureA.getBody() : fixtureB.getBody();
                bodiesToRemove.add(blockBody);
                System.out.println("Block hit by RedBird. Added to removal queue.");
            }
        }
    }
}
