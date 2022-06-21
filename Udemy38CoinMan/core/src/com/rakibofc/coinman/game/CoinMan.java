package com.rakibofc.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture dizzy;
	int manState = 0;
	int pause = 0;
	float gravity = 0.2f;
	float velocity = 0;
	int manY = 0;
	Rectangle manRectangle;
	int score = 0;
	int gameState = 0;
	Random random;

	BitmapFont font;

	ArrayList<Integer> coinXs = new ArrayList<>();
	ArrayList<Integer> coinYs = new ArrayList<>();
	ArrayList<Rectangle> coinRectangle = new ArrayList<>();
	Texture coin;
	int coinCount;

	ArrayList<Integer> bombXs = new ArrayList<>();
	ArrayList<Integer> bombYs = new ArrayList<>();
	ArrayList<Rectangle> bombRectangle = new ArrayList<>();
	Texture bomb;
	int bombCount;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		dizzy = new Texture("dizzy-1.png");

		manY = Gdx.graphics.getHeight() / 2;

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		random = new Random();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);

	}

	public void makeCoin() {

		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int) height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makeBomb() {

		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int) height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {

			// Game is live
			// Bomb
			if (bombCount < 300) {
				bombCount++;

			} else {
				bombCount = 0;
				makeBomb();
			}

			bombRectangle.clear();
			for (int i = 0; i < bombYs.size(); i++) {

				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i) - 8);
				bombRectangle.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}

			// Coin
			if (coinCount < 100) {
				coinCount++;

			} else {
				coinCount = 0;
				makeCoin();
			}

			coinRectangle.clear();
			for (int i = 0; i < coinYs.size(); i++) {

				batch.draw(coin, coinXs.get(i), coinYs.get(i));
				coinXs.set(i, coinXs.get(i) - 4);
				coinRectangle.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
			}

			velocity += gravity;
			manY -= velocity;

			if (manY <= 0) {

				manY = 0;
			}

			if (Gdx.input.justTouched()) {
				velocity = -10;
			}
			if (pause < 6) {
				pause++;

			} else {
				pause = 0;

				if (manState < 3) {
					manState++;

				} else {
					manState = 0;
				}
			}

		} else if (gameState == 0) {

			// Wating to start
			if (Gdx.input.justTouched()) {

				gameState = 1;
			}

		} else if (gameState == 2){

			// Game over
			if (Gdx.input.justTouched()) {

				gameState = 1;
				manY = Gdx.graphics.getHeight() / 2;
				score = 0;
				velocity = 0;

				coinXs.clear();
				coinYs.clear();
				coinRectangle.clear();
				coinCount = 0;

				bombXs.clear();
				bombYs.clear();
				bombRectangle.clear();
				bombCount = 0;
			}
		}

		if (gameState == 2) {

			batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - man[manState].getHeight() / 2, manY);

		} else {

			batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}
		manRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY, man[manState].getWidth(), man[manState].getHeight());

		for (int i = 0; i < coinRectangle.size(); i++) {

			if (Intersector.overlaps(manRectangle, coinRectangle.get(i))) {
				score++;
				coinRectangle.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		for (int i = 0; i < bombRectangle.size(); i++) {

			if (Intersector.overlaps(manRectangle, bombRectangle.get(i))) {

				gameState = 2;
			}
		}

		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
