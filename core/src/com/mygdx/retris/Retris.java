package com.mygdx.retris;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import javax.swing.text.View;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Retris extends ApplicationAdapter implements ApplicationListener {
	float timer = 0;
	private SpriteBatch batch;
	boolean gamestate;
	Texture border,middle,locked,active;
	Texture waterBorder,waterFill;
	//Texture locked;
	//Texture active;
	tetris game = new tetris();
	private OrthographicCamera camera;
	private Rectangle square;

	String gameover = "Game Over";
	String Score;
	BitmapFont font;



	@Override
	public void create () {
		border =  new Texture(Gdx.files.internal("ground05.png"));
		middle =  new Texture(Gdx.files.internal("hollow middle.png"));
		locked =  new Texture(Gdx.files.internal("rocky01.png"));
		active =  new Texture(Gdx.files.internal("lava 2.png"));

		waterBorder = new Texture(Gdx.files.internal("water_full_2.png"));
		waterFill = new Texture(Gdx.files.internal("water_full_2_transp.png"));

		font = new BitmapFont(Gdx.files.internal("vcr_osd_mono.fnt"), Gdx.files.internal("vcr_osd_mono.png"), true); //must be set true to be flipped

		game.init(border,middle,locked,active,waterBorder,waterFill);
		gamestate = true;

		camera = new OrthographicCamera(760,920);
		camera.setToOrtho(true, 760, 920);
		//camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		square = new Rectangle();

		square.width = 40;
		square.height = 40;

		batch = new SpriteBatch();

	}

	@Override
	public void render () {
		Texture[][] board = game.getBoard();
		Gdx.gl.glClearColor(0.3f, 0.28f, 0.15f, 0.9f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();



		for (int i = 0; i <24; i++) {
			for (int j = 0; j <24; j++) {

				batch.draw(board[i][j],i*40,j*40,square.width,square.height);
			}
		}
		//font.setColor(Color.BLACK);
		font.getData().setScale(2);
		font.draw(batch, "Next",(Gdx.graphics.getWidth()/2)+160,265);
		font.getData().setScale(1);
		font.setColor(Color.WHITE);
		font.draw(batch, "  L/R: Move\n\n  U/D: Rotate\n\nSPACE: Down\n\nENTER: RESTART",(Gdx.graphics.getWidth()/2)+110,500);
		if(!game.getState()){
			font.setColor(Color.WHITE);
			font.getData().setScale(2);
			font.getData().
			Score = "Score: "+game.getScore();
			font.draw(batch, gameover, (Gdx.graphics.getWidth()/2)-290, 400);
			font.getData().setScale(1);
			font.draw(batch, Score,(Gdx.graphics.getWidth()/2)-290,450);


		}else{


		}

		batch.end();


		if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			game.move(1);
			camera.update();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP)&& game.getState()) {
			game.rotate(-1);
			camera.update();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && game.getState()) {
			game.rotate(+1);
			camera.update();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)&& game.getState()) {
			game.move(-1);
			camera.update();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)&& game.getState()) {
			game.dropDown();
			game.setScore(1);
			camera.update();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			game.init(border,middle,locked,active,waterBorder,waterFill);
		}
		if(game.getState()){
			if(timer>1 ) {
				timer = 0;
				game.dropDown();
			}else{
				timer += Gdx.graphics.getDeltaTime();
			}

		}else{

		}





	}


	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}

}

