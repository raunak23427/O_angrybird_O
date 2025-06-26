package io.github.project;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Sprite background;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Sprite titleSprite;
    private ShapeRenderer shapeRenderer;

    private static final float VIRTUAL_WIDTH = 1600;
    private static final float VIRTUAL_HEIGHT = 900;

    private float progress;

    public LoadScreen(Main game) {
        this.game = game;
        System.out.println("Loading Screen Loaded");
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();
        Texture backgroundTexture = new Texture(Gdx.files.internal("loadingscreen.png"));
        background = new Sprite(backgroundTexture);
        background.setSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        Texture title = new Texture(Gdx.files.internal("Angry_birds.png"));
        titleSprite = new Sprite(title);
        titleSprite.setPosition(VIRTUAL_WIDTH / 2 - 500, VIRTUAL_HEIGHT - 300);
        titleSprite.setScale(1f);

        shapeRenderer = new ShapeRenderer();
        progress = 0f;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Simulate loading progress
        if (progress < 1f) {
            progress += delta * 0.25f;
        } else {
            game.setScreen(new MainScreen(game));
            return;
        }


        batch.begin();
        background.draw(batch);
        titleSprite.draw(batch);
        batch.end();
        renderLoadingBar();
    }

    private void renderLoadingBar() {
        float barWidth = VIRTUAL_WIDTH * 0.5f;
        float barHeight = 30f;
        float barX = (VIRTUAL_WIDTH - barWidth) / 2f;
        float barY = 100f;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        // Progress of the loading bar
        shapeRenderer.setColor(1.0f, 0.84f, 0.0f, 1); // Golden color
        shapeRenderer.rect(barX, barY, barWidth * progress, barHeight);

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        background.getTexture().dispose();
        shapeRenderer.dispose();
    }
}
