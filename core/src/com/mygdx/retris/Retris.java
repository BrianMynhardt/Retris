package com.mygdx.retris;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Retris extends ApplicationAdapter implements ApplicationListener {
	float timer = 0;
	private SpriteBatch batch;
	Texture border;
	Texture middle;
	Texture locked;
	Texture active;
	tetris game = new tetris();
	private OrthographicCamera camera;
	private Rectangle square;


	@Override
	public void create () {
		border =  new Texture(Gdx.files.internal("ground05.png"));
		middle =  new Texture(Gdx.files.internal("hollow middle.png"));
		locked =  new Texture(Gdx.files.internal("rocky01.png"));
		active =  new Texture(Gdx.files.internal("lava 2.png"));

		game.init(border,middle,locked,active);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		System.out.println(w);
		System.out.println(h);
		camera = new OrthographicCamera(480,920);
		camera.setToOrtho(true, 480, 920);
		//camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		square = new Rectangle();

		square.width = 40;
		square.height = 40;

		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Texture[][] well = game.getWell();
		Gdx.gl.glClearColor(0.3f, 0.28f, 0.15f, 0.9f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		for (int i = 0; i <12; i++) {
			for (int j = 0; j <23; j++) {

				batch.draw(well[i][j],i*40,j*40,square.width,square.height);
			}
		}
		batch.end();

		if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			game.move(1);
			camera.update();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			game.rotate(-1);
			camera.update();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			game.rotate(+1);
			camera.update();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			game.move(-1);
			camera.update();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			game.dropDown();
			game.setScore(1);
			camera.update();
		}
		if(timer>1){
			timer = 0;
			game.dropDown();

		}else{
			timer += Gdx.graphics.getDeltaTime();
		}

		// Make the falling piece drop every second



	}


	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}

}

