package com.flappybird.game;

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
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;
	Texture[] birds;
	Texture topTube;
	Texture bottomTube;
	Texture gameOver;
	int flapState=0;
	int pause=0;
	float birdY=0;
	float velocity=0;
	int gameState=0;
	float gravity=  1.5f;
	float gap=400;
	Circle birdOfCircle;
	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;
	BitmapFont font;

	Random random;
	int score=0;
	int scoringTube=0;

	float maxTubeOfSet;
	float tubeVelocity=4;
	int numberrOfTube=4;
	float[] tubeX=new float[numberrOfTube];
	float[] tubeOfSet=new float[numberrOfTube];
	float distanceBetweenTubes;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		birds=new Texture[2];
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");

		topTube=new Texture("toptube.png");
		bottomTube=new Texture("bottomtube.png");
		//maxTubeOfSet=Gdx.graphics.getHeight()/2-gap/2-100;
		random=new Random();
		distanceBetweenTubes=Gdx.graphics.getWidth()*3/4;
		birdOfCircle=new Circle();
		//shapeRenderer=new ShapeRenderer();
		topTubeRectangle=new Rectangle[numberrOfTube];
		bottomTubeRectangle=new Rectangle[numberrOfTube];
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		gameOver=new Texture("gameover.png");
		startGame();

	}
	public void startGame(){
		birdY= Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		for (int i=0;i<numberrOfTube;i++){
			tubeOfSet[i]=(random.nextFloat()*0.5f)*(Gdx.graphics.getHeight()-gap-200);
			tubeX[i]=Gdx.graphics.getWidth()/2-topTube.getWidth()/2+Gdx.graphics.getWidth()+i*distanceBetweenTubes;
			topTubeRectangle[i]=new Rectangle();
			bottomTubeRectangle[i]=new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if(gameState==1){
			if (tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
				score++;
				Gdx.app.log("score",String.valueOf(score));
				if(scoringTube<numberrOfTube-1){
					scoringTube++;
				}else
					scoringTube=0;
			}

			if(Gdx.input.justTouched()) {
				velocity = -25;

			}
			for (int i=0;i<numberrOfTube;i++) {
				if(tubeX[i]<-topTube.getWidth()){
					tubeX[i]+=numberrOfTube*distanceBetweenTubes;
					tubeOfSet[i]=(random.nextFloat()*0.5f)*(Gdx.graphics.getHeight()-gap-200);
				}else {
					tubeX[i] -= tubeVelocity;

				}
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOfSet[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOfSet[i]);

				topTubeRectangle[i]=new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2  + tubeOfSet[i],topTube.getWidth(),topTube.getHeight());
				bottomTubeRectangle[i]=new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOfSet[i],bottomTube.getWidth(),bottomTube.getHeight());
			}
			if (birdY>0){
				velocity+=gravity;
				birdY-=velocity;
			}else {
				gameState=2;
			}

		}
		else if(gameState==0){

			if(Gdx.input.justTouched()){
				gameState=1;
			}

		}else if(gameState==2){
			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
			if(Gdx.input.justTouched()){
				gameState=1;
				startGame();
				score=0;
				scoringTube=0;
				velocity=0;
			}


		}
		if (pause < 10) {
			pause++;
		} else {
			pause = 0;
			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}
	}

		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2,birdY);
		font.draw(batch,String.valueOf(score),100,200);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		birdOfCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);

		//shapeRenderer.circle(birdOfCircle.x,birdOfCircle.y,birdOfCircle.radius);
		for (int i=0;i<numberrOfTube;i++) {
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2  + tubeOfSet[i],topTube.getWidth(),topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOfSet[i],bottomTube.getWidth(),bottomTube.getHeight());
			if (Intersector.overlaps(birdOfCircle,topTubeRectangle[i])||Intersector.overlaps(birdOfCircle,bottomTubeRectangle[i])){

				gameState=2;
			}
		}
		batch.end();
        //shapeRenderer.end();
	}


	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
