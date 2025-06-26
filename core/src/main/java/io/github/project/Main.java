package io.github.project;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    @Override
    public void create() {
        batch = new SpriteBatch();
        System.out.println("Game Started");
        setScreen(new LoadScreen(this));
    }
}
