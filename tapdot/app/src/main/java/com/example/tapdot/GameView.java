package com.example.tapdot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import java.util.Random;

public class GameView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random();
    private float targetX = 300, targetY = 500, radius = 75;
    private int score = 0;
    private long startTime = SystemClock.elapsedRealtime();
    private boolean gameOver = false;

    public GameView(Context context) {
        super(context);
        paint.setTextAlign(Paint.Align.CENTER);
        setBackgroundColor(Color.rgb(245, 245, 245));
    }

    private void moveTarget() {
        int w = Math.max(getWidth(), 1);
        int h = Math.max(getHeight(), 1);
        float margin = radius + 20;
        targetX = margin + random.nextFloat() * Math.max(1, w - margin * 2);
        targetY = margin + 130 + random.nextFloat() * Math.max(1, h - margin * 2 - 180);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long elapsed = SystemClock.elapsedRealtime() - startTime;
        int secondsLeft = Math.max(0, 30 - (int)(elapsed / 1000));
        if (secondsLeft == 0) gameOver = true;

        paint.setColor(Color.DKGRAY);
        paint.setTextSize(54);
        canvas.drawText("Score: " + score, getWidth()/2f, 75, paint);
        paint.setTextSize(38);
        canvas.drawText("Time: " + secondsLeft, getWidth()/2f, 125, paint);

        if (!gameOver) {
            paint.setColor(Color.rgb(40, 140, 255));
            canvas.drawCircle(targetX, targetY, radius, paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(36);
            canvas.drawText("TAP", targetX, targetY + 12, paint);
            postInvalidateDelayed(100);
        } else {
            paint.setColor(Color.DKGRAY);
            paint.setTextSize(64);
            canvas.drawText("Game Over", getWidth()/2f, getHeight()/2f - 40, paint);
            paint.setTextSize(42);
            canvas.drawText("Final score: " + score, getWidth()/2f, getHeight()/2f + 25, paint);
            paint.setTextSize(32);
            canvas.drawText("Tap anywhere to restart", getWidth()/2f, getHeight()/2f + 85, paint);
        }
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) return true;
        float x = event.getX(), y = event.getY();
        if (gameOver) {
            score = 0;
            radius = 75;
            gameOver = false;
            startTime = SystemClock.elapsedRealtime();
            moveTarget();
            invalidate();
            return true;
        }
        float dx = x - targetX, dy = y - targetY;
        if (dx*dx + dy*dy <= radius*radius) {
            score++;
            radius = Math.max(42, 75 - score * 1.2f);
            moveTarget();
            invalidate();
        }
        return true;
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);
        moveTarget();
    }
}
