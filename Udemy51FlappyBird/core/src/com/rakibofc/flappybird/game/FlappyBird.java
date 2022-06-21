package com.rakibofc.flappybird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background, topTube, bottomTube;
	Texture[] bird;
	Texture gameOver;
	BitmapFont font;
	float birdX, birdY;
	int flapState;

	int gameState;
	boolean isGameOver = false;
	int gravity;

	Random randTube;
	int tubeWidthGap, tubeHeightGap, firstTubeHeightGap, secondTubeHeightGap;
	int velocity1, velocity2;
	int score, lastScore;

	Circle birdCircle;
	Rectangle topTubeRect1, bottomTubeRect1, topTubeRect2, bottomTubeRect2;

	// Test
	// ShapeRenderer shapeRenderer;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");

		// Bird Image
		bird = new Texture[3];
		bird[0] = new Texture("bird1.png");
		bird[1] = new Texture("bird2.png");
		bird[2] = new Texture("bird3.png");
		gameOver = new Texture("gameover.png");

		// Score in display
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);

		birdX = Gdx.graphics.getWidth() / 2f - bird[0].getWidth() / 2f;
		birdY = Gdx.graphics.getHeight() / 2f - bird[0].getHeight() / 2f;
		flapState = 0;
		gameState = 0;
		gravity = 2;

		// Tube or Pipe
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");

		// Tube maintain
		randTube = new Random();
		tubeWidthGap = Gdx.graphics.getWidth() / 2;
		tubeHeightGap = 90;
		firstTubeHeightGap = 50;
		secondTubeHeightGap = 0;
		velocity2 = velocity1 = Gdx.graphics.getWidth();

		// Collision
		birdCircle = new Circle();
		topTubeRect1 = bottomTubeRect1 = topTubeRect2 = bottomTubeRect2 = new Rectangle();

		lastScore = score = 0;

		// Test
		// shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render () {

		// Batch Begin
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Bird Flap
		delay();
		if (flapState >= 3) {

			 flapState = 0;
		}

		// Check Game State
		if (gameState == 1) {

			if (birdY > 0) {

				birdY -= gravity;
			}

		} else if (gameState == 2) {

			batch.draw(gameOver, Gdx.graphics.getWidth() / 2f - gameOver.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + gameOver.getHeight());

			flapState = 0;
			birdY = Gdx.graphics.getHeight() / 2f - bird[0].getHeight() / 2f;
			velocity2 = velocity1 = Gdx.graphics.getWidth();
		}

		// Touch On the screen
		if (Gdx.input.justTouched()) {

			if (gameState == 0) {

				gameState = 1;

			} else if (gameState == 2) {

				score = 0;
				gameState = 1;

			} else {
				birdY += 80;
			}
		}

		// Test
		// shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		// shapeRenderer.setColor(Color.RED);

		if (gameState == 1) {

			// Showing Tube in display
			batch.draw(topTube, velocity1, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2f + tubeHeightGap + firstTubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
			batch.draw(bottomTube, velocity1, - tubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + firstTubeHeightGap);

			// Create Tube Shape for collision
			topTubeRect1 = new Rectangle(velocity1, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2f + tubeHeightGap + firstTubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
			bottomTubeRect1 = new Rectangle(velocity1, - tubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + firstTubeHeightGap);

			// Test
			// shapeRenderer.rect(velocity1, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2f + tubeHeightGap + firstTubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
			// shapeRenderer.rect(velocity1, - tubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + firstTubeHeightGap);

			velocity1 -= 6;

			if ((velocity1 + topTube.getWidth() / 2) < 0) {

				velocity1 = Gdx.graphics.getWidth();

				if (randTube.nextBoolean()) {

					firstTubeHeightGap = 120;

				} else {

					firstTubeHeightGap = 0;
				}
			}

			/*
			if (velocity1 <= tubeWidthGap || velocity2 <= velocity1 - tubeWidthGap) {

				batch.draw(topTube, velocity2, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2f + tubeHeightGap + secondTubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
				batch.draw(bottomTube, velocity2, - tubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + secondTubeHeightGap);

				// Create Tube Rectangle for collision
				topTubeRect2 = new Rectangle(velocity2, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2f + tubeHeightGap + secondTubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
				bottomTubeRect2 = new Rectangle(velocity2, - tubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + secondTubeHeightGap);

				// Test
				shapeRenderer.rect(velocity2, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2f + tubeHeightGap + secondTubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
				shapeRenderer.rect(velocity2, - tubeHeightGap, topTube.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + secondTubeHeightGap);

				velocity2 -= 3;

				if ((velocity2 + topTube.getWidth() / 2) < 0) {

					velocity2 = Gdx.graphics.getWidth();

					if (firstTubeHeightGap == 50) {

						secondTubeHeightGap = 0;

					} else {

						secondTubeHeightGap = 50;
					}
				}
			}
			**/
		}

		batch.draw(bird[flapState], birdX, birdY);
		birdCircle.set(birdX + bird[flapState].getWidth() / 2f, birdY + bird[flapState].getHeight() / 2f, bird[flapState].getWidth() / 2.5f);

		// Test
		// shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		flapState++;

		// If The bird touch with tube then game over
		// if (Intersector.overlaps(birdCircle, topTubeRect1) || Intersector.overlaps(birdCircle, bottomTubeRect1) || Intersector.overlaps(birdCircle, topTubeRect2) || Intersector.overlaps(birdCircle, bottomTubeRect2)) {
		if (Intersector.overlaps(birdCircle, topTubeRect1) || Intersector.overlaps(birdCircle, bottomTubeRect1)) {

			// Game Over
			gameState = 2;
			lastScore = score;
		}

		// Score Update
		if (velocity1 == birdX - bird[0].getWidth() / 2f) {

			score++;
		}

		if (gameState == 1) {

			// Display Score
			displayScore(score, 30, Gdx.graphics.getHeight() - 100);

		}

		if (gameState == 2) {

			// Display Score
			displayScore(lastScore, Gdx.graphics.getWidth() / 2 - (int) font.getCapHeight() / 2, Gdx.graphics.getHeight() / 2 - bird[0].getHeight() - (int) font.getCapHeight() / 2);
		}
		batch.end();
		// shapeRenderer.end();
	}

	private void displayScore(int score, int cordX, int cordY) {

		font.draw(batch, String.valueOf(score), cordX, font.getXHeight() + cordY);
	}

	@Override
	public void dispose () {
		// batch.dispose();
	}

	private void delay() {

		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
