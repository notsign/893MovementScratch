package com.mygdx.game;

import com.badlogic.gdx.Game;

public class Firing extends Game {

    @Override
    public void create() {
        setScreen(new FiringTest(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
