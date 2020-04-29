package com.example.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
   通过手势滑动可以完成View的切换
   继承ViewPager类
   setPageTransformer()方法为我们的ViewPager 设置切换时的动画效果
 */
public class NoScrollViewpager extends ViewPager {
    private boolean enabled;
    //  AttributeSet 是接收xml中定义的属性信息
    public NoScrollViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.enabled = false;
    }

    //触摸没有反应就可以
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.enabled){
            return super.onTouchEvent(ev);
        }
        return false;
    }

    //用于拦截手势事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }
    public void setPagingEnabled(boolean enabled){
        this.enabled = enabled;
    }
}
