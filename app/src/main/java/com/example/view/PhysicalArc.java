package com.example.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;

import com.example.angel.R;

/**
 * 圆形计分控件
 *
 */
public class PhysicalArc extends View {
    private Paint paint_black, paint_white, paint_text;
    private RectF rectf;//矩形
    private float tb;
    private int blackColor = 0x30000000; // 底色
    private int whiteColor = 0xffFE6895; // 前色
    private int score;
    private float arc_y = 0f;
    private int score_text = 90;

    public PhysicalArc(Context context, int score) {
        super(context);
        this.score = score;
        this.score_text = score;
        init();
    }

    public void init() {
        Resources res = getResources();
        tb = res.getDimension(R.dimen.historyscore_tb);

        paint_black = new Paint();
        paint_black.setAntiAlias(true);
        paint_black.setColor(blackColor);
        paint_black.setStrokeWidth(tb * 0.7f);
        paint_black.setStyle(Style.STROKE);
        paint_black.setShadowLayer(6, 0, 0, blackColor);

        paint_white = new Paint();
        paint_white.setAntiAlias(true);
        paint_white.setColor(whiteColor);
        paint_white.setTextSize(tb * 6.0f);
        paint_white.setStrokeWidth(tb * 0.7f);
        paint_white.setTextAlign(Align.CENTER);
        paint_white.setStyle(Style.STROKE);
        paint_white.setShadowLayer(6, 0, 0, 0xff00ffff);

        paint_text = new Paint();
        paint_text.setAntiAlias(true);
        paint_text.setColor(whiteColor);
        paint_text.setTextSize(tb * 6.0f);
        paint_text.setStrokeWidth(tb * 0.2f);
        paint_text.setTextAlign(Align.CENTER);
        paint_text.setStyle(Style.FILL);

        rectf = new RectF();
        rectf.set(tb * 3f, tb * 3f, tb * 17, tb * 17f);

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        this.getViewTreeObserver().addOnPreDrawListener(
                new OnPreDrawListener() {
                    public boolean onPreDraw() {
                        new thread();
                        getViewTreeObserver().removeOnPreDrawListener(this);
                        return false;
                    }
                });
    }

    protected void onDraw(Canvas c) {
        super.onDraw(c);
        c.drawArc(rectf, -80, 360, false, paint_black);
        paint_white.setStyle(Style.STROKE);
        c.drawArc(rectf, -80, arc_y, false, paint_white);

        paint_text.setTextSize(tb * 6.0f);
        c.drawText("" + score_text, tb * 10.0f, tb * 12.0f, paint_text);
    }

    class thread implements Runnable {
        private Thread thread;
        private int statek;
        int count;

        public thread() {
            thread = new Thread(this);
            thread.start();
        }

        public void run() {
            while (true) {
                switch (statek) {
                    case 0:
                        try {
                            Thread.sleep(200);
                            statek = 1;
                        } catch (InterruptedException e) {
                        }
                        break;
                    case 1:
                        try {
                            Thread.sleep(15);
                            arc_y += 3.6f;

                            count++;
                            postInvalidate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                if (count >= score)
                    break;
            }
        }
    }

}
