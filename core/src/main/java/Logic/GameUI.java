package Logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.project.Main;

public class GameUI {
    private Main game;
    private Stage stage;
    private Texture buttonUpTexture;
    private Texture buttonDownTexture;
    private ImageButton pauseButton;
    private boolean isPaused;

    private Texture groundTexture;
    private Body groundBody;
    private Texture backgroundTexture;


    private Body leftBoundary;
    private Body rightBoundary;

    public GameUI(Main game, World world, Stage stage) {
        this.game = game;
        this.stage = stage;

        groundTexture = new Texture(Gdx.files.internal("ground.png"));
        backgroundTexture = new Texture(Gdx.files.internal("MainMenu.jpg"));

        createGround(world);
        createBoundaries(world);
    }

    private void createGround(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(stage.getViewport().getWorldWidth() / 2 / 100f, groundTexture.getHeight() / 200f + 1.5f );  // Centered X, near bottom Y
        bodyDef.type = BodyDef.BodyType.StaticBody;
        groundBody = world.createBody(bodyDef);
        PolygonShape groundShape = new PolygonShape();
        float groundWidth = stage.getViewport().getWorldWidth() / 100f;
        float groundHeight = groundTexture.getHeight() / 100f;
        groundShape.setAsBox(groundWidth / 2, groundHeight / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundShape;
        fixtureDef.friction = 2f;
        fixtureDef.restitution = 0.0f;  // No bounce
        groundBody.createFixture(fixtureDef);
        groundShape.dispose();
    }

    private void createBoundaries(World world) {
        float screenWidth = stage.getViewport().getWorldWidth() / 100f;
        float screenHeight = stage.getViewport().getWorldHeight() / 100f;
        BodyDef boundaryDef = new BodyDef();
        boundaryDef.type = BodyDef.BodyType.StaticBody;
        boundaryDef.position.set(0, screenHeight / 2);
        leftBoundary = world.createBody(boundaryDef);
        EdgeShape leftEdge = new EdgeShape();
        leftEdge.set(0, -screenHeight / 2, 0, screenHeight / 2);
        leftBoundary.createFixture(leftEdge, 0.0f);
        leftEdge.dispose();
        boundaryDef.position.set(screenWidth, screenHeight / 2);
        rightBoundary = world.createBody(boundaryDef);
        EdgeShape rightEdge = new EdgeShape();
        rightEdge.set(0, -screenHeight / 2, 0, screenHeight / 2);
        rightBoundary.createFixture(rightEdge, 0.0f);
        rightEdge.dispose();
    }

    public float groundHeight() {
        return groundTexture.getHeight() / 100f;
    }

    public void renderBackground() {
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, -70, stage.getViewport().getWorldWidth(), 1000);
        float groundHeight = stage.getViewport().getWorldHeight() / 3;
        game.batch.draw(groundTexture, 0, -80 -30, stage.getViewport().getWorldWidth(), groundHeight);
        game.batch.end();
    }

    public void dispose() {
        buttonUpTexture.dispose();
        buttonDownTexture.dispose();
        groundTexture.dispose();
        backgroundTexture.dispose();
    }

    public ImageButton getPauseButton() {
        return pauseButton;
    }

    public Body getGroundBody() {
        return groundBody;
    }
}
