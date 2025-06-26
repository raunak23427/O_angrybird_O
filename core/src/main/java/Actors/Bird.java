package Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bird extends Actor {

    private static final String[] BIRD_TEXTURES = {
        "RED.png",
        "WHITE.png",
        "BLUE.png",
        "YELLOW.png"
    };

    private static final float PIXELS_TO_METERS = 100f;
    private static final float BIRD_RADIUS = 25f;

    private Texture birdTexture;
    private Body birdBody;
    private boolean isLaunched = false;

    public Bird(World world, int textureIndex) {
        birdTexture = loadBirdTexture(textureIndex);
        birdBody = createBirdBody(world);
        setSize(BIRD_RADIUS * 2, BIRD_RADIUS * 2); // Set size based on the radius
    }

    // Load the texture for the bird based on the index
    private Texture loadBirdTexture(int textureIndex) {
        String texturePath = BIRD_TEXTURES[textureIndex % BIRD_TEXTURES.length];
        return new Texture(Gdx.files.internal(texturePath));
    }
    private Body createBirdBody(World world) {
        BodyDef bodyDef = createBodyDef();
        birdBody = world.createBody(bodyDef);

        CircleShape shape = createBirdShape();
        FixtureDef fixtureDef = createFixtureDef(shape);
        birdBody.createFixture(fixtureDef);
        birdBody.setUserData(this);
        shape.dispose();
        return birdBody;
    }
    private BodyDef createBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(2f, -1f);
        bodyDef.fixedRotation = false;
        bodyDef.linearDamping = 0.4f;
        return bodyDef;
    }

    private CircleShape createBirdShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(BIRD_RADIUS / PIXELS_TO_METERS); // Convert radius to Box2D units
        return shape;
    }
    private FixtureDef createFixtureDef(CircleShape shape) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f; // Density of the bird
        fixtureDef.restitution = 0.6f; // Bounciness (elasticity)
        fixtureDef.friction = 0.5f; // Surface friction
        return fixtureDef;
    }
    public void setBodyPosition(float x, float y) {
        birdBody.setTransform(x, y, birdBody.getAngle()); // Set the position while preserving the angle
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(birdTexture,
            birdBody.getPosition().x * PIXELS_TO_METERS - getWidth() / 2,
            birdBody.getPosition().y * PIXELS_TO_METERS - getHeight() / 2,
            getWidth(), getHeight());
    }



    @Override
    public void act(float delta) {
        super.act(delta); // Call the parent class' act method
    }

    public void dispose() {
        birdTexture.dispose(); // Dispose the bird texture when no longer needed
    }


    public boolean isLaunched() {
        return isLaunched; // Getter for the launched state
    }

    public void setLaunched(boolean launched) {
        isLaunched = launched; // Setter for the launched state
    }

    public void setStatic() {
        birdBody.setType(BodyDef.BodyType.StaticBody); // Set the bird body as static
    }

    public void setDynamic() {
        birdBody.setType(BodyDef.BodyType.DynamicBody); // Set the bird body as dynamic
    }
    public Body getBody() {
        return birdBody; // Getter for the bird body
    }
    public float getRadius() {
        return BIRD_RADIUS / PIXELS_TO_METERS; // Getter for the bird radius in Box2D units
    }
}
