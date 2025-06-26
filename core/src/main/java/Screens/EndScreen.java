package Screens;

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
import io.github.project.LevelSelector;
import io.github.project.Main;

import java.util.ArrayList;
import java.util.List;

public class EndScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    private Sprite playButtonSprite;
    private Sprite exitButtonSprite;
    private Sprite backgroundSprite;
    private OrthographicCamera camera;
    private Viewport viewport;
    private List<Texture> textures;
    private List<Sprite> stars;
    private Music backgroundMusic;

    private static final float VIRTUAL_WIDTH = 1600;
    private static final float VIRTUAL_HEIGHT = 900;

    public EndScreen(Main game, int screenType) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        // Initialize textures list
        textures = new ArrayList<>();
        textures.add(new Texture(Gdx.files.internal("win.jpg")));
        textures.add(new Texture(Gdx.files.internal("lose.png")));

        // Set background texture based on screenType
        backgroundSprite = new Sprite(textures.get(screenType - 1));
        backgroundSprite.setSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // Load appropriate button texture based on screenType
        Texture playButtonTexture = new Texture(Gdx.files.internal(screenType == 1 ? "play_finish.png" : "replay_finish.png"));
        playButtonSprite = new Sprite(playButtonTexture);
        playButtonSprite.setSize(300, 100); // Set size of play button
        playButtonSprite.setPosition(VIRTUAL_WIDTH / 2 - playButtonSprite.getWidth() - 20, VIRTUAL_HEIGHT / 2 - 300);

        Texture exitButtonTexture = new Texture(Gdx.files.internal("exit_finish.png"));
        exitButtonSprite = new Sprite(exitButtonTexture);
        exitButtonSprite.setSize(300, 100); // Set size of exit button
        exitButtonSprite.setPosition(VIRTUAL_WIDTH / 2 + 20, VIRTUAL_HEIGHT / 2 - 300);

        // Load star textures and create sprites
        stars = new ArrayList<>();
        if (screenType == 1) { // Only add stars for win screen
            for (int i = 0; i < 3; i++) {
                Texture starTexture = new Texture(Gdx.files.internal("star.png"));
                Sprite starSprite = new Sprite(starTexture);
                starSprite.setSize(200, 200); // Set size of stars
                starSprite.setPosition(VIRTUAL_WIDTH / 2 - 200 + i * 150, VIRTUAL_HEIGHT / 2); // Move stars a bit to the left
                stars.add(starSprite);
            }
        }

        // Load background music based on screenType
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(screenType == 1 ? "win.mp3" : "lose.mp3"));
        backgroundMusic.setLooping(true); // Loop the music

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touchPos = new Vector3(screenX, screenY, 0);
                camera.unproject(touchPos);
                if (playButtonSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    System.out.println("Play/replay button pressed");
                    game.setScreen(new Gamescreen(game));
                }
                if (exitButtonSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    System.out.println("Exit button pressed");
                    game.setScreen(new LevelSelector(game));
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
        playButtonSprite.draw(batch);
        exitButtonSprite.draw(batch);
        for (Sprite star : stars) {
            star.draw(batch);
        }
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
        for (Texture texture : textures) {
            texture.dispose();
        }
        playButtonSprite.getTexture().dispose();
        exitButtonSprite.getTexture().dispose();
        for (Sprite star : stars) {
            star.getTexture().dispose();
        }
        backgroundMusic.dispose();
    }
}
