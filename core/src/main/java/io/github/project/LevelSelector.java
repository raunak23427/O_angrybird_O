package io.github.project;

import Screens.Gamescreen;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;

public class LevelSelector implements Screen {

    private Main game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Sprite backgroundSprite;
    private Sprite exitButtonSprite;
    private Sprite level1Sprite;
    private Sprite level2Sprite;
    private Sprite level3Sprite;
    private Sprite titleSprite;
    private Music backgroundMusic;

    private static final float VIRTUAL_WIDTH = 1600;
    private static final float VIRTUAL_HEIGHT = 900;

    public LevelSelector(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();
        Texture backgroundTexture = new Texture(Gdx.files.internal("background_levelselector.png"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        Texture level1Texture = new Texture(Gdx.files.internal("level1.png"));
        level1Sprite = new Sprite(level1Texture);
        level1Sprite.setPosition(VIRTUAL_WIDTH / 2 - 500, VIRTUAL_HEIGHT / 2 - level1Sprite.getHeight() / 2);
        Texture level2Texture = new Texture(Gdx.files.internal("level2.png"));
        level2Sprite = new Sprite(level2Texture);
        level2Sprite.setPosition(VIRTUAL_WIDTH / 2 - 100, VIRTUAL_HEIGHT / 2 - level2Sprite.getHeight() / 2);
        Texture level3Texture = new Texture(Gdx.files.internal("level3.png"));
        level3Sprite = new Sprite(level3Texture);
        level3Sprite.setPosition(VIRTUAL_WIDTH / 2 + 300 , VIRTUAL_HEIGHT / 2 - level3Sprite.getHeight() / 2);
        Texture exitButtonTexture = new Texture(Gdx.files.internal("exit_button.png"));
        exitButtonSprite = new Sprite(exitButtonTexture);
        exitButtonSprite.setPosition(20, 20);
        Texture titleTexture = new Texture(Gdx.files.internal("Select_Level.png"));
        titleSprite = new Sprite(titleTexture);
        titleSprite.setPosition(VIRTUAL_WIDTH / 2 - titleSprite.getWidth() / 2, VIRTUAL_HEIGHT - titleSprite.getHeight() - 150);

        // Load background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("level.mp3"));
        backgroundMusic.setLooping(true); // Loop the music

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touchPos = new Vector3(screenX, screenY, 0);
                camera.unproject(touchPos);
                if (level1Sprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    game.setScreen(new Gamescreen(game));
                    System.out.println("Level 1 selected");
                } else if (level2Sprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    game.setScreen(new Gamescreen(game));
                    System.out.println("Level 2 selected");
                } else if (level3Sprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    game.setScreen(new Gamescreen(game));
                    System.out.println("Level 3 selected");
                }
                if (exitButtonSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    System.out.println("Exit button pressed");
                    game.setScreen(new MainScreen(game));
                }
                return super.touchDown(screenX, screenY, pointer, button);
            }
        });
    }

    @Override
    public void show() {
        // Play background music when the screen is shown
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        backgroundSprite.draw(batch);
        titleSprite.draw(batch);
        level1Sprite.draw(batch);
        level2Sprite.draw(batch);
        level3Sprite.draw(batch);
        exitButtonSprite.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        // Pause background music if needed
        backgroundMusic.pause();
    }

    @Override
    public void resume() {
        // Resume background music if needed
        backgroundMusic.play();
    }

    @Override
    public void hide() {
        // Stop background music when the screen is hidden
        backgroundMusic.stop();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundSprite.getTexture().dispose();
        exitButtonSprite.getTexture().dispose();
        level1Sprite.getTexture().dispose();
        level2Sprite.getTexture().dispose();
        level3Sprite.getTexture().dispose();
        titleSprite.getTexture().dispose();
        backgroundMusic.dispose();
    }
}
