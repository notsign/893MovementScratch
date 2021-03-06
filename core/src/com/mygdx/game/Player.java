package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by k9sty on 2016-03-12.
 */

public class Player {
	Body body;
	Fixture footSensor;
	TextureAtlas taIdle;
	TextureAtlas taRun;
	Sprite[] sprIdle;
	Sprite[] sprRun;
	Animation aniIdle, aniRun;
	World world;

	float animationTime;
	boolean bRight, isIdle;

	Player(World world, Vector2 spawnpoint) {
		this.world = world;

		taIdle = new TextureAtlas(Gdx.files.internal("player/idle/idle.pack"));
		taRun = new TextureAtlas(Gdx.files.internal("player/run/run.pack"));
		animationTime = 0f;

		sprIdle = new Sprite[9];
		sprRun = new Sprite[9];

		for(int i = 1; i < 10; i++) {
			sprIdle[i - 1] = new Sprite(taIdle.findRegion("idle (" + i + ")"));
			sprRun[i - 1] = new Sprite(taRun.findRegion("run (" + i + ")"));
		}

		aniIdle = new Animation(10, sprIdle);
		aniRun = new Animation(5, sprRun);

		bRight = true;
		isIdle = true;

		createBody(spawnpoint);
		createFootSensor();
	}

	private void createBody(Vector2 spawnpoint) {
		BodyDef bodyDef = new BodyDef();
		FixtureDef fdefPlayer = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		bodyDef.position.set(new Vector2(spawnpoint.x / 2, spawnpoint.y / 2));
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bodyDef);
		body.setFixedRotation(true);

		shape.setAsBox(sprIdle[0].getWidth() / 4, sprIdle[0].getHeight() / 4);
		fdefPlayer.shape = shape;
		fdefPlayer.friction = 1;
		body.setSleepingAllowed(false);
		body.createFixture(fdefPlayer);
		shape.dispose();
	}

	private void createFootSensor() {
		PolygonShape shape = new PolygonShape();
		FixtureDef fdefFoot = new FixtureDef();

		// TODO: Figure this out
		shape.setAsBox(sprIdle[0].getWidth() / 4, 0.2f, new Vector2(body.getLocalCenter().x, body.getLocalCenter().y - sprIdle[0].getHeight() / 4), 0);
		//shape.setAsBox(1f, 1f, new Vector2(body.getLocalCenter().x, body.getPosition().y), 0f);

		fdefFoot.isSensor = true;
		fdefFoot.shape = shape;

		footSensor = body.createFixture(fdefFoot);

		shape.dispose();
	}

	Vector3 getPosition() {
		return new Vector3(body.getPosition().x, body.getPosition().y, 0);
	}

	void draw(SpriteBatch sb) {
		animationTime++;
		if(isIdle) {
			TextureRegion trIdle = aniIdle.getKeyFrame(animationTime, true);
			if(bRight) {
				sb.draw(trIdle, body.getPosition().x - sprIdle[0].getWidth() / 4, body.getPosition().y - sprIdle[0].getHeight() / 4, sprIdle[0].getWidth() / 2, sprIdle[0].getHeight() / 2);
			} else {
				sb.draw(trIdle, body.getPosition().x + sprIdle[0].getWidth() / 4, body.getPosition().y - sprIdle[0].getHeight() / 4, -sprIdle[0].getWidth() / 2, sprIdle[0].getHeight() / 2);
			}
		} else {
			TextureRegion trRun = aniRun.getKeyFrame(animationTime, true);
			if(bRight) {
				sb.draw(trRun, body.getPosition().x - sprIdle[0].getWidth() / 4, body.getPosition().y - sprIdle[0].getHeight() / 4, sprRun[0].getWidth() / 2, sprRun[0].getHeight() / 2);
			} else {
				sb.draw(trRun, body.getPosition().x + sprIdle[0].getWidth() / 4, body.getPosition().y - sprIdle[0].getHeight() / 4, -sprRun[0].getWidth() / 2, sprRun[0].getHeight() / 2);
			}
		}
	}

	void move() {
		if(Gdx.input.isTouched()) {
			int height = Gdx.graphics.getHeight();
			int width = Gdx.graphics.getWidth();
			int touchX = Gdx.input.getX();
			int touchY = Gdx.input.getY(); // Don't get near this guy ;}

			if(touchX > width - (width / 3f) && touchY > height - (height / 3f)) { // Bottom right
				body.setLinearVelocity(100f, body.getLinearVelocity().y);
				isIdle = false;
				bRight = true;
			} else if(touchX < (width / 3f) && touchY > height - (height / 3f)) { // Bottom left
				body.setLinearVelocity(-100f, body.getLinearVelocity().y);
				isIdle = false;
				bRight = false;
			} else if(isGrounded && touchY > height - (height / 3)) { // Bottom middle
				jump();
			}
		} else {
			isIdle = true;
			stop();
		}
	}

	void stop() {
		isIdle = true;
		body.setLinearVelocity(0, body.getLinearVelocity().y);
	}

	boolean isGrounded = true;

	void jump() {
		isGrounded = false;
		body.setLinearVelocity(body.getLinearVelocity().x, 500);
	}

	Vector2 getLinearVelocity() {
		return body.getLinearVelocity();
	}

	void setLinearVelocity(Vector2 velocity) {
		body.setLinearVelocity(velocity);
	}
}
