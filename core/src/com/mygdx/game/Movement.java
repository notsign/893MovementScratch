package com.mygdx.game;

import com.badlogic.gdx.Game;

public class Movement extends Game {

    @Override
    public void create() {
        setScreen(new ScrMovement(this));
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
