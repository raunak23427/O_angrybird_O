package io.github.project;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;

public class MainScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    private Sprite titleSprite;
    private Sprite playButtonSprite;
    private Sprite exitButtonSprite;
    private Sprite backgroundSprite;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Music backgroundMusic;

    private static final float VIRTUAL_WIDTH = 1600;
    private static final float VIRTUAL_HEIGHT = 900;

    public MainScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();
        Texture backgroundTexture = new Texture(Gdx.files.internal("MainMenu.jpg"));
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        Texture titleTexture = new Texture(Gdx.files.internal("Angry_Birds.png"));
        titleSprite = new Sprite(titleTexture);
        titleSprite.setPosition(VIRTUAL_WIDTH / 2 - titleSprite.getWidth() / 2, VIRTUAL_HEIGHT - 300);
        Texture playButtonTexture = new Texture(Gdx.files.internal("play.png"));
        playButtonSprite = new Sprite(playButtonTexture);
        playButtonSprite.setPosition(VIRTUAL_WIDTH / 2 - playButtonSprite.getWidth() / 2 , VIRTUAL_HEIGHT / 2 - 150);
        Texture exitButtonTexture = new Texture(Gdx.files.internal("quit.png"));
        exitButtonSprite = new Sprite(exitButtonTexture);
        exitButtonSprite.setPosition(VIRTUAL_WIDTH / 2 - exitButtonSprite.getWidth() / 2, VIRTUAL_HEIGHT / 2 - 340);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("play.mp3"));
        backgroundMusic.setLooping(true);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touchPos = new Vector3(screenX, screenY, 0);
                camera.unproject(touchPos);
                if (playButtonSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    System.out.println("Play button pressed");
                    game.setScreen(new LevelSelector(game));
                }
                if (exitButtonSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    System.out.println("Exit button pressed");
                    Gdx.app.exit();
                }
                return super.touchDown(screenX, screenY, pointer, button);
            }
        });
    }

    @Override
    public void show() {
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
        playButtonSprite.draw(batch);
        exitButtonSprite.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        batch.dispose();
        backgroundSprite.getTexture().dispose();
        titleSprite.getTexture().dispose();
        playButtonSprite.getTexture().dispose();
        exitButtonSprite.getTexture().dispose();
        backgroundMusic.dispose();
    }
}
