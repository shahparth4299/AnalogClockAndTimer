package com.example.androidassignment2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TimerSurfaceView implements Runnable {
    private SurfaceHolder holder;
    private Thread thread;
    private boolean running = false;
    private int totalSeconds;
    private int remainingSeconds;
    private SurfaceView surfaceView;

    public TimerSurfaceView(SurfaceView surfaceView, int totalSeconds) {
        this.surfaceView = surfaceView;
        this.holder = surfaceView.getHolder();
        this.totalSeconds = totalSeconds;
        thread = new Thread(this);
        thread.start ();
        running = true;
    }

    @Override
    public void run() {
        int elapsedSeconds = 0;
        while (running && elapsedSeconds <= totalSeconds) {
            if (elapsedSeconds == totalSeconds) {
                running = false;
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.BLACK);
                    Paint paint = new Paint();
                    paint.setStyle(Paint.Style.FILL);
                    paint.setAntiAlias(true);
                    paint.setTextSize(150);
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setColor(Color.WHITE);
                    int width = surfaceView.getWidth();
                    int height = surfaceView.getHeight();
                    canvas.drawText("Time's Up!", width / 2, height / 2, paint);
                    holder.unlockCanvasAndPost(canvas);
                }
            } else {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawTimer(canvas, elapsedSeconds);
                    holder.unlockCanvasAndPost(canvas);
                    try {
                        Thread.sleep(1000); // Update every second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    elapsedSeconds++;
                }
            }
        }
    }

    private void drawTimer(Canvas canvas, int elapsedSeconds) {
        int width = surfaceView.getWidth();
        int height = surfaceView.getHeight();
        canvas.drawColor(Color.BLACK); // Clear the canvas

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(centerX, centerY);

        int degreesPerSecond = 360 / totalSeconds;
        int currentDegrees = elapsedSeconds * degreesPerSecond;

        // Draw the filled arcs for each second
        for (int i = 0; i < elapsedSeconds; i++) {
            paint.setColor(Color.CYAN); // Change color based on your requirements
            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                    i * degreesPerSecond, degreesPerSecond, true, paint);
        }

        // Draw the remaining unfilled arcs
        paint.setColor(Color.GRAY); // Change color based on your requirements
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                currentDegrees, 360 - currentDegrees, true, paint);

        // Draw the text in the center
        paint.setColor(Color.BLACK);
        paint.setTextSize(130); // Adjust the text size as needed
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(totalSeconds - elapsedSeconds),
                centerX, centerY + paint.getTextSize() / 3, paint);
        remainingSeconds = totalSeconds - elapsedSeconds;
    }

    public void onRestartTimer () {
        if (remainingSeconds > 0) {
            if (!thread.isAlive()) {
                thread = new Thread(this);
                thread.start();
                running = true;
            }
        }
    }

    public void onPauseTimer () {
        running = false;
        boolean reentry = true;
        while (reentry) {
            try {
                thread.join();
                reentry = false;
            } catch (Exception e) {}
        }
    }
}
