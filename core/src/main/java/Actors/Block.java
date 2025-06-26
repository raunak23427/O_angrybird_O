package Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Block extends Actor {
    public static final String[] BLOCK_TEXTURES = {
        "ice3.png",
        "wood3.png",
        "stone3.png"
    };

    private Texture blockTexture;
    private Body blockBody;

    public Block(World world, float posX, float posY, float blockWidth, float blockHeight) {
        super();
        blockTexture = loadRandomTexture();
        setBlockPositionAndSize(posX, posY, blockWidth, blockHeight);
        createBlockBody(world, posX, posY, blockWidth, blockHeight);
    }

    private Texture loadRandomTexture() {
        int textureIndex = MathUtils.random(BLOCK_TEXTURES.length - 1);
        return new Texture(Gdx.files.internal(BLOCK_TEXTURES[textureIndex]));
    }

    private void setBlockPositionAndSize(float posX, float posY, float blockWidth, float blockHeight) {
        setPosition(posX, posY);
        setSize(blockWidth, blockHeight);
    }

    private void createBlockBody(World world, float posX, float posY, float blockWidth, float blockHeight) {
        BodyDef bodyDef = createBodyDef(posX, posY);
        blockBody = world.createBody(bodyDef);
        PolygonShape shape = createBlockShape(blockWidth, blockHeight);
        FixtureDef fixtureDef = createFixtureDef(shape);
        blockBody.createFixture(fixtureDef);
        blockBody.setUserData(this);
        shape.dispose();
    }

    private BodyDef createBodyDef(float posX, float posY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX / 100f, posY / 100f); // Scale position to meters
        return bodyDef;
    }

    private PolygonShape createBlockShape(float blockWidth, float blockHeight) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(blockWidth / 200f, blockHeight / 200f); // Scale size to meters
        return shape;
    }

    private FixtureDef createFixtureDef(PolygonShape shape) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 100f;
        fixtureDef.restitution = 0f; // No bounce
        return fixtureDef;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(blockTexture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updatePositionAndRotation();
    }

    private void updatePositionAndRotation() {
        setPosition(blockBody.getPosition().x * 100 - getWidth() / 2, blockBody.getPosition().y * 100 - getHeight() / 2);
        setRotation(blockBody.getAngle() * MathUtils.radiansToDegrees);
    }

    public Body getBody() {
        return blockBody;
    }

    public void dispose() {
        disposeTexture();
        disposeBlockBody();
    }

    private void disposeTexture() {
        if (blockTexture != null) {
            blockTexture.dispose();
        }
    }

    private void disposeBlockBody() {
        if (blockBody != null) {
            blockBody.getWorld().destroyBody(blockBody);
        }
    }

    public void setFriction(float friction) {
        blockBody.getFixtureList().get(0).setFriction(friction);
    }

    public void setDensity(float density) {
        blockBody.getFixtureList().get(0).setDensity(density);
    }
}
