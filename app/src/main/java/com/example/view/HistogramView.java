package com.example.view;
/*
大姨妈柱状图
 */
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.angel.MenstruationModel;
import com.example.angel.R;
import com.example.util.DateChange;

import java.util.ArrayList;
import java.util.List;

public class HistogramView extends View {
    private Paint paint_text,paint_line,paint_small,paint_big,paint_base;
    private float tb;//基准
    private int characterColor = 0xff6E6E6E;//字体颜色
    private int lineColor = 0xffBBBBBB; // 线的颜色
    private int smallColor = 0xffFE6895; // 最后一个矩形的颜色
    private int bigColor = 0xffFAB0C7;//矩形的颜色
    private int baseColor = 0xffF8F8F8;//底色
    private float x,y;
    private float rowX = 0;
    private float startX;

    //天数集合
    private List<MenstruationModel> hm = new ArrayList<MenstruationModel>();
    public HistogramView(Context context) {
        super(context);
        init(context);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setHistogramList(List<MenstruationModel> hm){
        this.hm = hm;
        invalidate();//UI刷新
    }
    private void init(Context context){
        Resources res = this.getResources();
        tb = res.getDimension(R.dimen.histogram_tb);

        x = tb*3.0f;//便于后期维护
        y = tb*3.0f;

        //
        paint_text = new Paint();
        paint_text.setAntiAlias(true);//抗锯齿功能绘制曲线
        paint_text.setColor(characterColor);
        paint_text.setTextSize(tb*1.2f);
        paint_text.setStrokeWidth(tb * 0.1f);//描边宽度
        paint_text.setTextAlign(Paint.Align.CENTER);
        paint_text.setStyle(Paint.Style.FILL);

        //线
        paint_line = new Paint();
        paint_line.setAntiAlias(true);
        paint_line.setColor(lineColor);
        paint_line.setTextSize(tb * 1.2f);
        paint_line.setStrokeWidth(tb * 0.1f);
        paint_line.setTextAlign(Paint.Align.CENTER);
        paint_line.setStyle(Paint.Style.FILL);

        //最后的矩形
        paint_small = new Paint();
        paint_small.setAntiAlias(true);
        paint_small.setColor(smallColor);
        paint_small.setTextSize(tb * 1.2f);
        paint_small.setStrokeWidth(tb * 0.1f);
        paint_small.setTextAlign(Paint.Align.CENTER);
        paint_small.setStyle(Paint.Style.FILL);

        //矩形
        paint_big = new Paint();
        paint_big.setAntiAlias(true);
        paint_big.setColor(bigColor);
        paint_big.setTextSize(tb * 1.2f);
        paint_big.setStrokeWidth(tb * 0.1f);
        paint_big.setTextAlign(Paint.Align.CENTER);
        paint_big.setStyle(Paint.Style.FILL);

        //底色
        paint_base = new Paint();
        paint_base.setAntiAlias(true);
        paint_base.setColor(baseColor);
        paint_base.setTextSize(tb * 1.2f);
        paint_base.setStrokeWidth(tb * 0.1f);
        paint_base.setTextAlign(Paint.Align.CENTER);
        paint_base.setStyle(Paint.Style.FILL);
    }

    //触摸事件
    public boolean onTouchEvent(MotionEvent event){
        float endX = 0;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN://手指初次接触到屏幕 时触发
            startX = event.getX();//获得触摸点在当前 View 的 X 轴坐标
                break;
            case MotionEvent.ACTION_UP://手指离开屏幕时触发。
                endX = event.getX();
                if(rowX < 0){
//				rowX = 0;
                    new thread2();
                }else if(getWidth()-2*hm.size()*x+rowX > 3*x) {
//				rowX = 3*x - (getWidth()-2*hm.size()*x);
                    new thread();
                }
//			invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                float f = endX-startX;
                rowX += f;
                startX = event.getX();
                invalidate();
                break;

        }
        return true;
    }

    //更新UI
    class thread implements Runnable{
        private Thread thread;
        private int statek;
        public thread(){
            thread = new Thread(this);
            thread.start();
        }
        @Override
        public void run() {
            while (true){
                switch (statek){
                    case 0:
                        try {
                            Thread.sleep(10);
                            statek = 1;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            Thread.sleep(1);
                            rowX -= 5f;//在整个界面的x坐标
                            postInvalidate();//非UI刷新
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (getWidth()-2 * hm.size()*x+rowX <= 6*x){
                            statek = 2;
                        }
                        break;
                    case 2:
                        try {
                            Thread.sleep(1);
                            rowX -= 1f;
                            postInvalidate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                if (getWidth()-2*hm.size()*x+rowX <= 3*x)
                    break;
                }
            }
        }

    class thread2 implements Runnable {
        private Thread thread;
        private int statek;

        public thread2() {
            thread = new Thread(this);
            thread.start();
        }

        public void run() {
            while (true) {
                switch (statek) {
                    case 0:
                        try {
                            Thread.sleep(10);
                            statek = 1;
                        } catch (InterruptedException e) {
                        }
                        break;
                    case 1:
                        try {
                            Thread.sleep(1);
                            rowX += 5f;
                            postInvalidate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(rowX >= -4*x){
                            statek = 2;
                        }

                        break;
                    case 2:
                        try {
                            Thread.sleep(1);
                            rowX += 1f;
                            postInvalidate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                if (rowX >= 0)
                    break;
            }
        }
    }

    //画线
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);//绘制图形
        //显示年份
        canvas.drawText("2019",3.5f*x,(12+0.5f)*y,paint_text);
        //显示标注
        canvas.drawLine(0.0f,12*y,getWidth(),12*y,paint_line);//getWidth(),获取View的最终大小
        canvas.drawLine(3.0f*x, 12.9f*y, 4.0f*x, 12.9f*y, paint_big);
        canvas.drawText("理想周期   （28天）",6f*x, 13f*y, paint_text);
        canvas.drawText("日/月",11f*x,13f*y,paint_big);
        canvas.drawText("来经时间",12.5f*x, 13f*y, paint_text);

        //28天临界线
        canvas.drawLine(0.0f, 8.4f*y, getWidth(), 8.4f*y, paint_big);

        //绘制矩形与背景
        if((getWidth()-3*x) / x / 2 <= hm.size()){
            for(int i = hm.size()-1; i >= 0; i--){
                canvas.drawRect(getWidth()-2*(hm.size()-i)*x+rowX, 2*y, getWidth()-2*(hm.size()-i)*x + x+rowX, 11*y, paint_base);//画矩形
                if(i==hm.size()-1){
                    if(hm.get(i).getCycle()<20){
                        canvas.drawRect(getWidth()-2*(hm.size()-i)*x+rowX, (11-hm.get(i).getCycle()/20.0f)*y, getWidth()-2*(hm.size()-i)*x+x+rowX, 11*y, paint_small);
                        canvas.drawText(""+hm.get(i).getCycle(), getWidth()-2*(hm.size()-i)*x+0.5f*x+rowX, ((11-hm.get(i).getCycle()/20.0f)-0.2f)*y, paint_small);
                    }else {
                        canvas.drawRect(getWidth()-2*(hm.size()-i)*x+rowX, (11-(hm.get(i).getCycle()-15)/5.0f)*y, getWidth()-2*(hm.size()-i)*x+x+rowX, 11*y, paint_small);
                        canvas.drawText(""+hm.get(i).getCycle(), getWidth()-2*(hm.size()-i)*x+0.5f*x+rowX, ((11-(hm.get(i).getCycle()-15)/5.0f)-0.2f)*y, paint_small);
                    }
                    canvas.drawText(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "MM/dd"), getWidth()-2*(hm.size()-i)*x+0.5f*x+rowX, (11.5f)*y, paint_text);
                }else {
                    if(hm.get(i).getCycle()<20){
                        canvas.drawRect(getWidth()-2*(hm.size()-i)*x+rowX, (11-hm.get(i).getCycle()/20.0f)*y, getWidth()-2*(hm.size()-i)*x+x+rowX, 11*y, paint_big);
                        canvas.drawText(""+hm.get(i).getCycle(), getWidth()-2*(hm.size()-i)*x+0.5f*x+rowX, ((11-hm.get(i).getCycle()/20.0f)-0.2f)*y, paint_small);
                    }else {
                        canvas.drawRect(getWidth()-2*(hm.size()-i)*x+rowX, (11-(hm.get(i).getCycle()-15)/5.0f)*y, getWidth()-2*(hm.size()-i)*x+x+rowX, 11*y, paint_big);
                        canvas.drawText(""+hm.get(i).getCycle(), getWidth()-2*(hm.size()-i)*x+0.5f*x+rowX, ((11-(hm.get(i).getCycle()-15)/5.0f)-0.2f)*y, paint_small);
                    }
                    canvas.drawText(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "MM/dd"), getWidth()-2*(hm.size()-i)*x+0.5f*x+rowX, (11.5f)*y, paint_text);
                }
                //显示年份
                if(i == 0){
                    canvas.drawText(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "yyyy"), getWidth()-2*(hm.size()-i)*x+0.5f*x+rowX, (12+0.5f)*y, paint_text);
                    if(Integer.parseInt(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "yyyy"))
                            != Integer.parseInt(DateChange.timeStamp2Date(hm.get(i+1).getBeginTime()+"", "yyyy"))){
                        canvas.drawText(DateChange.timeStamp2Date(hm.get(i+1).getBeginTime()+"", "yyyy"), getWidth()-2*(hm.size()-i-1)*x+0.5f*x+rowX, (12+0.5f)*y, paint_text);
                    }
                }else if(i != hm.size()-1 && Integer.parseInt(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "yyyy"))
                        != Integer.parseInt(DateChange.timeStamp2Date(hm.get(i+1).getBeginTime()+"", "yyyy"))){
                    canvas.drawText(DateChange.timeStamp2Date(hm.get(i+1).getBeginTime()+"", "yyyy"), getWidth()-2*(hm.size()-i-1)*x+0.5f*x+rowX, (12+0.5f)*y, paint_text);
                }
            }

        }else {
            for(int i=0; i<hm.size(); i++){
                canvas.drawRect((3+i*2)*x, 2*y, (4+i*2)*x, 11*y, paint_base);
                if(i==hm.size()-1){
                    if(hm.get(i).getCycle()<20){
                        canvas.drawRect((3+i*2)*x, (11-hm.get(i).getCycle()/20.0f)*y, (4+i*2)*x, 11*y, paint_small);
                        canvas.drawText(""+hm.get(i).getCycle(), (3.5f+i*2)*x, ((11-hm.get(i).getCycle()/20.0f)-0.2f)*y, paint_small);
                        canvas.drawText(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "MM/dd"), (3.5f+i*2)*x, (11.5f)*y, paint_text);
                    }else {
                        canvas.drawRect((3+i*2)*x, (11-(hm.get(i).getCycle()-15)/5.0f)*y, (4+i*2)*x, 11*y, paint_small);
                        canvas.drawText(""+hm.get(i).getCycle(), (3.5f+i*2)*x, ((11-(hm.get(i).getCycle()-15)/5.0f)-0.2f)*y, paint_small);
                        canvas.drawText(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "MM/dd"), (3.5f+i*2)*x, (11.5f)*y, paint_text);
                    }
                }else {
                    if(hm.get(i).getCycle()<20){
                        canvas.drawRect((3+i*2)*x, (11-hm.get(i).getCycle()/20.0f)*y, (4+i*2)*x, 11*y, paint_big);
                        canvas.drawText(""+hm.get(i).getCycle(), (3.5f+i*2)*x, ((11-hm.get(i).getCycle()/20.0f)-0.2f)*y, paint_big);
                        canvas.drawText(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "MM/dd"), (3.5f+i*2)*x, (11.5f)*y, paint_text);
                    }else {
                        canvas.drawRect((3+i*2)*x, (11-(hm.get(i).getCycle()-15)/5.0f)*y, (4+i*2)*x, 11*y, paint_big);
                        canvas.drawText(""+hm.get(i).getCycle(), (3.5f+i*2)*x, ((11-(hm.get(i).getCycle()-15)/5.0f)-0.2f)*y, paint_big);
                        canvas.drawText(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "MM/dd"), (3.5f+i*2)*x, (11.5f)*y, paint_text);
                    }

                }
                //显示年份
                if(i==0){
                    canvas.drawText(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "yyyy"), (3.5f+i*2)*x, (12+0.5f)*y, paint_text);
                }else if(Integer.parseInt(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "yyyy"))!=
                        Integer.parseInt(DateChange.timeStamp2Date(hm.get(i-1).getBeginTime()+"", "yyyy"))) {
                    canvas.drawText(DateChange.timeStamp2Date(hm.get(i).getBeginTime()+"", "yyyy"), (3.5f+i*2)*x, (12+0.5f)*y, paint_text);
                }
            }
        }
        //y轴坐标
        canvas.drawText("天", x, y, paint_text);
        int j=1;
        for(int i=60; i>=20; i=i-5){
            canvas.drawText(""+i, x, (j+1.1f)*y, paint_text);
            canvas.drawLine(1.5f*x, (j+1)*y, 2*x, (j+1)*y, paint_line);
            j++;
        }
        //x轴y轴
        canvas.drawLine(0.0f, 11*y, getWidth(), 11*y, paint_line);
        canvas.drawLine(2*x, 0.0f, 2*x, 11*y, paint_line);
    }
}
