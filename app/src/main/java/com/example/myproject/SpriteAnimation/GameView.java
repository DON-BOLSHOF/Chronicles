package com.example.myproject.SpriteAnimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.View;

import com.example.myproject.R;


public class GameView extends View{

    private Sprite CubeAnim;
    private Sprite CubeValue;

    private final int timerInterval = 30;
    private final int timerValue = 220;
    private int rollValue; //ДА огромное вложение((

    private int timer = 0;

    private int viewWidth;
    private int viewHeight;

    public GameView(Context context, int rollValue) {
        super(context);

        this.rollValue = rollValue;

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.cube);
        int w = b.getWidth()/5;
        int h = b.getHeight();
        Rect firstFrame = new Rect(0, 0, w, h);
        CubeAnim = new Sprite(viewHeight/2, viewWidth/2
                , firstFrame, b);

        for (int i = 1; i < 5; i++) {
            CubeAnim.addFrame(new Rect(i*w, 0, i*w+w, h));
        }

        CubeAnim.addFrame(new Rect(0,0,0,0)); // Пустота после проигрывания анимации будет ставится

        b = BitmapFactory.decodeResource(getResources(), R.drawable.cube_value);
        w = b.getWidth()/5;
        h = b.getHeight()/4;
        firstFrame = new Rect(0, 0, w, h);
        CubeValue = new Sprite(getHeight()/2, getWidth()/2,  firstFrame, b);

        for (int i = 0; i < 4; i++) {
            for(int j = 0; j<5; j++) {
                if (i == 0 && j == 0)
                    continue;
                CubeValue.addFrame(new Rect(j * w, i * h, j * w + w, i*h+h));
            }
        }

        Timer t = new Timer();
        t.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);;
        if(timer < timerValue)
            CubeAnim.draw(canvas);
        else
            CubeValue.draw(canvas);
    }


    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    protected void update () {
        if(timer < timerValue) {
            CubeAnim.update(timerInterval);
            timer += 5;
        }else {
            CubeAnim.setCurrentFrame(CubeAnim.getFramesCount() - 1);
            CubeValue.setCurrentFrame(rollValue);
        }
        invalidate();
    }


    class Timer extends CountDownTimer {

        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            update ();
        }

        @Override
        public void onFinish() {

        }
    }
}