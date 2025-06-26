package Screens;

import Logic.GameEntities;
import Logic.GameUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.project.Main;
import io.github.project.MainScreen;

public class Gamescreen implements Screen {

    private Main game;
    private GameUI gameUI;
    private GameEntities gameEntities;
    private Stage stage;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Music backgroundMusic;
    private ImageButton pauseButton;
    private ImageButton mainMenuButton;
    private boolean isPaused = false;

    public Gamescreen(Main game) {
        this.game = game;

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, 1366, 768);

        this.stage = new Stage(new ScreenViewport(camera));

        world = new World(new Vector2(0, -9.8f), true);

        debugRenderer = new Box2DDebugRenderer();

        gameUI = new GameUI(game, world, stage);
        gameEntities = new GameEntities(game, world, stage);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("game.mp3"));
        backgroundMusic.setLooping(true);

        Gdx.input.setInputProcessor(stage);

        // Load button textures
        TextureRegionDrawable pauseButtonUp = new TextureRegionDrawable(new Texture(Gdx.files.internal("pause.png")));
        TextureRegionDrawable pauseButtonDown = new TextureRegionDrawable(new Texture(Gdx.files.internal("pause.png")));
        TextureRegionDrawable mainMenuButtonUp = new TextureRegionDrawable(new Texture(Gdx.files.internal("exit_button.png")));
        TextureRegionDrawable mainMenuButtonDown = new TextureRegionDrawable(new Texture(Gdx.files.internal("exit_button.png")));

        // Create the pause button
        pauseButton = new ImageButton(pauseButtonUp, pauseButtonDown);
        pauseButton.setPosition(10, stage.getViewport().getWorldHeight() - pauseButton.getHeight() - 10);

        // Create the main menu button
        mainMenuButton = new ImageButton(mainMenuButtonUp, mainMenuButtonDown);
        mainMenuButton.setPosition(10, stage.getViewport().getWorldHeight() - pauseButton.getHeight() - mainMenuButton.getHeight() - 20);

        // Add listener to handle pause functionality
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = !isPaused;
                if (isPaused) {
                    pause();
                } else {
                    resume();
                }
            }
        });

        // Add listener to handle main menu transition
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(game));
            }
        });

        // Add the buttons to the stage
        stage.addActor(pauseButton);
        stage.addActor(mainMenuButton);
    }

    @Override
    public void show() {
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            world.step(1 / 60f, 8, 3);
            gameUI.renderBackground();
            debugRenderer.render(world, stage.getCamera().combined);
            gameEntities.update(delta);
            stage.act(delta);
        }
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        backgroundMusic.pause();
    }

    @Override
    public void resume() {
        backgroundMusic.play();
    }

    @Override
    public void hide() {
        backgroundMusic.stop();
    }

    @Override
    public void dispose() {
        stage.dispose();
        gameUI.dispose();
        gameEntities.dispose();
        world.dispose();
        debugRenderer.dispose();
        backgroundMusic.dispose();
    }
}
