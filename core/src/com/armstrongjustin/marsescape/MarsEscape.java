package com.armstrongjustin.marsescape;

import com.armstrongjustin.marsescape.Screens.TestLevel;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MarsEscape extends Game {
	public SpriteBatch batch;
	public AssetManager assetManager;

	@Override
	public void create () {
        assetManager = new AssetManager();
		batch = new SpriteBatch();
        this.setScreen(new TestLevel(this));
	}

	@Override
	public void render () {
        super.render();
	}
}
