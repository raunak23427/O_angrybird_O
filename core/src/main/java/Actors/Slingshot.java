package Actors;

import Logic.GameEntities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Slingshot extends Actor {

    private Texture slingshotTexture;
    private Vector2 slingshotPosition;
    private Bird bird;
    private boolean isDragging;
    private Vector2 initialTouchPos;
    private Vector2 birdInitialPosition;
    private Vector2 launchVelocity;
    private World world;
    private Sprite trajectorySprite;
    private GameEntities gameEntities;
    private ShapeRenderer shapeRenderer;

    // Slingshot anchor points for the rubber bands
    private Vector2 slingshotAnchorLeft;
    private Vector2 slingshotAnchorRight;

    private float power;
    private float angle;
    private final float gravity = -5f;
    private float t = 1.1f;
    public Slingshot(World world, Vector2 position, Bird bird, GameEntities gameEntities) {
        this.world = world;
        this.slingshotPosition = position;
        this.bird = bird;
        this.gameEntities = gameEntities;

        slingshotTexture = new Texture(Gdx.files.internal("slingshot.png"));
        trajectorySprite = new Sprite(new Texture(Gdx.files.internal("white-balls.png")));
        trajectorySprite.setSize(10, 10);// Lower the bird's initial position
        birdInitialPosition = new Vector2(2.5f, 2.0f + t ); // Lowered the Y value

        // Lower the anchor points for the rubber bands
        slingshotAnchorLeft = new Vector2(slingshotPosition.x - 0.2f, slingshotPosition.y + 0.7f + t); // Lowered the Y value
        slingshotAnchorRight = new Vector2(slingshotPosition.x + 0.2f, slingshotPosition.y + 0.7f + t); // Lowered the Y value

        shapeRenderer = new ShapeRenderer();

        isDragging = false;
    }


    public void setBird(Bird bird) {
        this.bird = bird;
    }

    public Vector2 getPosition() {
        return slingshotPosition;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(slingshotTexture, slingshotPosition.x * 100, slingshotPosition.y * 100 + 150, 80, 160);

        if (isDragging) {
            batch.end();
            drawRubberBand();
            batch.begin();
            drawTrajectory(batch);
        }
    }

    private void drawRubberBand() {
        shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.3f, 0.1f, 0.1f, 1f);
        Vector2 birdPosition = bird.getBody().getPosition();
        shapeRenderer.line(birdInitialPosition.x * 100-20, birdInitialPosition.y * 100,
            birdPosition.x * 100, birdPosition.y * 100);
        shapeRenderer.line(birdInitialPosition.x * 100+20, birdInitialPosition.y * 100,
            birdPosition.x * 100, birdPosition.y * 100);

        shapeRenderer.end();
    }

    private void drawTrajectory(Batch batch) {
        if (launchVelocity == null) {
            return;
        }

        float t = 0f;
        int pointCount = 30;
        float timeStep = 0.1f;

        for (int i = 0; i < pointCount; i++) {
            float x = birdInitialPosition.x + launchVelocity.x * t;
            float y = birdInitialPosition.y + launchVelocity.y * t + 0.5F * gravity * t * t;

            if (y < 0) {
                break;
            }

            batch.draw(trajectorySprite, x * 100, y * 100, 10, 10);
            t += timeStep;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (Gdx.input.isTouched()) {
            Vector2 touchPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 worldTouchPos = new Vector2(
                touchPosition.x / 100f,
                (Gdx.graphics.getHeight() - touchPosition.y) / 100f
            );

            if (!isDragging && bird.getBody().getPosition().dst(worldTouchPos) < 0.5f) {
                isDragging = true;
                initialTouchPos = worldTouchPos;
            }

            if (isDragging) {
                bird.setDynamic();
                bird.setBodyPosition(worldTouchPos.x, worldTouchPos.y);
                Vector2 dragDistance = new Vector2(birdInitialPosition).sub(worldTouchPos);
                angle = dragDistance.angle();
                power = dragDistance.len() * 8;
                launchVelocity = new Vector2(power * (float) Math.cos(Math.toRadians(angle)),
                    power * (float) Math.sin(Math.toRadians(angle)));
            }
        } else if (isDragging) {
            launchBird();
            isDragging = false;
        }
    }

    private void launchBird() {
        bird.getBody().setLinearVelocity(launchVelocity);
        if (gameEntities != null) {
            gameEntities.onBirdLaunched();
        }
    }

    public void dispose() {
        bird.dispose();
        slingshotTexture.dispose();
        trajectorySprite.getTexture().dispose();
        shapeRenderer.dispose();
    }
}
