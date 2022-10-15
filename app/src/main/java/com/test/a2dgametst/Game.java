package com.test.a2dgametst;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private final GameLoop gameLoop;
    private final Player player;
    private final Joystick joystick;

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);


        gameLoop = new GameLoop(this,surfaceHolder);

        //initialize Game Objects
        joystick = new Joystick(400, 750, 70, 40);
        player = new  Player(getContext(),2*500,500,30);


        setFocusable(true);


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //handle Touch event actions
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (joystick.isPressed((double)event.getX(),(double)event.getY()))
                {
                    joystick.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed())
                {
                    joystick.setActuator((double)event.getX(),(double)event.getY());
                }
                return true;
                case MotionEvent.ACTION_UP:
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    return true;

        }

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);

        joystick.draw(canvas);
        player.draw(canvas);
    }

    public void drawUPS(Canvas canvas)
    {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(),R.color.purple_200);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS:"+averageUPS,100,100,paint);
    }
    public void drawFPS(Canvas canvas)
    {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(),R.color.purple_200);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS:"+averageFPS,100,200,paint);
    }

    public void update() {
        joystick.update();
        player.update(joystick);
    }
}
