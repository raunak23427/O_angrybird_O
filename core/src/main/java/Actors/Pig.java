package Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pig extends Actor {

    private static final String[] PIG_TEXTURES = {
        "normal.png",
        "king.png",
        "soldier.png"
    };

    private static List<String> remainingTextures = new ArrayList<>();

    private Texture pigTexture;
    private Body pigBody;

    private static final float PIXELS_TO_METERS = 100f;
    private static final float PIG_RADIUS = 25f;

    public Pig(World world, float posX, float posY) {
        if (remainingTextures.isEmpty()) {
            loadAndShuffleTextures();
        }
        pigTexture = loadTextureForPig();
        createPigPhysicsBody(world, posX / PIXELS_TO_METERS, posY / PIXELS_TO_METERS);
        setSize(PIG_RADIUS * 2, PIG_RADIUS * 2);  // Size adjusted to match pig radius
        update(0);  // Initially update the position
    }

    private void loadAndShuffleTextures() {
        Collections.addAll(remainingTextures, PIG_TEXTURES);
        Collections.shuffle(remainingTextures);
    }

    private Texture loadTextureForPig() {
        String texturePath = remainingTextures.remove(0);
        return new Texture(Gdx.files.internal(texturePath));
    }

    private void createPigPhysicsBody(World world, float posX, float posY) {
        BodyDef bodyDef = createBodyDef(posX, posY);
        pigBody = world.createBody(bodyDef);

        CircleShape shape = createPigShape();
        pigBody.createFixture(shape, 1.0f).setFriction(0.8f);
        shape.dispose();
        pigBody.setUserData(this);
    }

    private BodyDef createBodyDef(float posX, float posY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX, posY);
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 0.5f;
        return bodyDef;
    }

    private CircleShape createPigShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(PIG_RADIUS / PIXELS_TO_METERS); // Adjusted radius to match visual size
        return shape;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(pigTexture, getX(), getY(), getWidth(), getHeight());
    }

    public void update(float deltaTime) {
        Vector2 bodyPosition = pigBody.getPosition();
        setPosition(bodyPosition.x * PIXELS_TO_METERS - getWidth() / 2, bodyPosition.y * PIXELS_TO_METERS - getHeight() / 2);
    }

    public void dispose() {
        disposeTexture();
    }

    private void disposeTexture() {
        if (pigTexture != null) {
            pigTexture.dispose();
        }
    }

    public Body getBody() {
        return pigBody;
    }
    public void setBodyPosition(float x, float y) {
        pigBody.setTransform(x, y, pigBody.getAngle());
    }
    public void setDensity(float density) {
        pigBody.getFixtureList().get(0).setDensity(density);
        pigBody.resetMassData();
    }
    public void setFriction(float friction) {
        pigBody.getFixtureList().get(0).setFriction(friction);
    }
    public float getRadius() {
        return PIG_RADIUS / PIXELS_TO_METERS;
    }

}
