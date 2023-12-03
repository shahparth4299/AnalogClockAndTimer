package com.example.androidassignment2;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;

public class ClockSurfaceView implements Runnable {
    private float length;
    public Thread thread;
    private SurfaceHolder holder;
    private boolean running = false;
    private SurfaceView surfaceView;
    public ClockSurfaceView (SurfaceView surfaceView, float length) {
        this.surfaceView = surfaceView;
        this.holder = surfaceView.getHolder();
        this.length = length;
        thread = new Thread(this);
        thread.start ();
        running = true;
    }

    @Override
    public void run() {
        int sec = 0, hour = 0, min = 0;
        while (running) {
            if(holder.getSurface().isValid()){
                Canvas canvas = holder.lockCanvas();

                int height = surfaceView.getHeight();
                int width = surfaceView.getWidth();
                Paint paint = new Paint();
                canvas.drawColor(Color.BLACK);
                paint.setColor(Color.WHITE);
                // RegPoly objs for marks
                RegPoly secMarks = new RegPoly(60, width/2, height/2, this.length, canvas, paint);
                RegPoly hourMarks = new RegPoly(12, width/2, height/2, this.length-60, canvas, paint);

                Calendar calendar = Calendar.getInstance();
                int amPm = calendar.get(Calendar.AM_PM);
                hour = calendar.get(Calendar.HOUR);
                min = calendar.get(Calendar.MINUTE);
                sec = calendar.get(Calendar.SECOND);

                float hourAngle = (hour % 12 + min/60.0f) * 360 / 12;

                paint.setStrokeWidth(5);
                RegPoly secHand = new RegPoly(60, width/2, height/2, this.length-50, canvas, paint);
                secHand.drawRadius(sec+45);

                paint.setStrokeWidth(8);
                RegPoly minHand = new RegPoly(60, width/2, height/2, this.length-70, canvas, paint);
                minHand.drawRadius(min+45);

                paint.setStrokeWidth(12);
                RegPoly hourHand = new RegPoly(60, width/2, height/2, this.length-100, canvas, paint);
                hourHand.drawHand(width/2, height/2, this.length-100, hourAngle);

                //  draw marks
                secMarks.drawNodes();
                hourMarks.drawNodes();

                Paint borderCirclePaint = new Paint();
                borderCirclePaint.setColor(Color.WHITE);
                borderCirclePaint.setStyle(Paint.Style.STROKE);
                borderCirclePaint.setStrokeWidth(5); // Adjust the border thickness
                canvas.drawCircle(width/2, height/2, this.length + 20, borderCirclePaint);

                sec++;
                if (sec % 60 == 0) {
                    min++;
                    if (min % 60 == 0) {
                        hour++;
                    }
                }
                // wait a sec
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {}

                try {
                    holder.unlockCanvasAndPost(canvas);
                }catch(Exception e) {}
            }
        }
    }

    public void onResumeClock () {
        thread = new Thread(this);
        thread.start ();
        running = true;
    }

    public void onPauseClock () {
        running = false;
        boolean reentry = true;
        while (reentry) {
            try {
                thread.join();
                reentry = false;
            } catch (Exception e) {

            }
        }
    }
}