package com.example.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.angel.R;

public class WFYTitle extends RelativeLayout {
    private Context context;
    private TextView tvTitle,tvLeft,tvRight;
    private ImageView ivTitle,ivLeft,ivRight;
    private int titleColor,rightTextColor;

    public WFYTitle(Context context) {
        super(context);
        this.context = context;
    }

    public WFYTitle(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    //deStyleAttr,当前主题的属性
    public WFYTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //获取资源文件,obtaiStyledAttributes()方法创建出来。如果不在使用了，recycle()方法把它释放
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WEYTitle,defStyleAttr,0);
        String title = ta.getString(R.styleable.WEYTitle_centerText);//中间文字
        int titleImage = ta.getResourceId(R.styleable.WEYTitle_centerImage,-1);//标题图片
        //添加标题图片
        if (titleImage != -1){
            addTitleImage(titleImage);
        }
        titleColor = ta.getColor(R.styleable.WEYTitle_centerTextColor, Color.parseColor("#ffffff"));
        //添加标题文字
        if (! TextUtils.isEmpty(title)){
            addTitleText(title,titleColor);//
        }
        int leftImage = ta.getResourceId(R.styleable.WEYTitle_leftImage, -1);
        if (leftImage != -1) {
            addLeftImage(leftImage);
        }
        String leftText = ta.getString(R.styleable.WEYTitle_leftText);
        if (!TextUtils.isEmpty(leftText)) {
            int leftTextColor = ta.getColor(R.styleable.WEYTitle_leftTextColor, Color.parseColor("#ffffff"));
            addLeftText(leftText, leftTextColor);
        }

        String rightText = ta.getString(R.styleable.WEYTitle_rightText);
        rightTextColor = ta.getColor(R.styleable.WEYTitle_rightTextColor, Color.parseColor("#ffffff"));
        if (!TextUtils.isEmpty(rightText)) {
            addRightText(rightText, rightTextColor);
        }
        int rightImage = ta.getResourceId(R.styleable.WEYTitle_rightImage, -1);
        if (rightImage != -1) {
            addRightImage(rightImage);
        }
        if (ta.getBoolean(R.styleable.WEYTitle_leftIsBack,false)){
            onBackPressed();
        }
        //释放
        ta.recycle();
    }

    //返回
    private void onBackPressed(){
        ivLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).onBackPressed();
            }
        });
    }

    //添加标题图片
    private void addTitleImage(int titleImage){
        ivTitle = new ImageView(context);
        //封装,标题中设置一个控件位置，标题Layout信息类，主要用来动态控制子view的摆放位置
        LayoutParams lpTitle = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        lpTitle.addRule(RelativeLayout.CENTER_IN_PARENT);//设置图片位置居中
        ivTitle.setLayoutParams(lpTitle);
        ivTitle.setImageResource(titleImage);
        ivTitle.setScaleType(ImageView.ScaleType.FIT_CENTER);//设置显示图片方式,等比缩放填充
        ivTitle.setPadding(0,dp2px(context,3),0,dp2px(context,3));
        addView(ivTitle);
    }

    //添加标题文字
    public void addTitleText(String title,int titleColor){
        tvTitle = new TextView(context);
        LayoutParams lpTitle = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        lpTitle.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvTitle.setLayoutParams(lpTitle);
        tvTitle.setGravity(Gravity.CENTER);//文字居中
        tvTitle.setText(title);
        tvTitle.setTextColor(titleColor);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        tvTitle.setTypeface(Typeface.MONOSPACE);//
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);//
        tvTitle.setSingleLine();//
        tvTitle.setEms(10);//
        addView(tvTitle);
    }

    //添加左边图片
    private void addLeftImage(int leftImage){
        ivLeft = new ImageView(context);
        LayoutParams lpLeft = new LayoutParams(dp2px(context, 50), LayoutParams.MATCH_PARENT);
        lpLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivLeft.setLayoutParams(lpLeft);
        ivLeft.setImageResource(leftImage);
        ivLeft.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ivLeft.setPadding(dp2px(context, 10), dp2px(context, 10), dp2px(context, 10), dp2px(context, 10));
        addView(ivLeft);
    }
    //添加左边图片文字
    private void addLeftText(String leftText, int leftTextColor) {
        tvLeft = new TextView(context);
        LayoutParams lpLeft = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lpLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//相对父类的左边
        tvLeft.setLayoutParams(lpLeft);
        tvLeft.setGravity(Gravity.CENTER_VERTICAL);
        tvLeft.setText(leftText);
        tvLeft.setTextColor(leftTextColor);
        tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tvLeft.setTypeface(Typeface.MONOSPACE);
        tvLeft.setLines(1);
        tvLeft.setPadding(dp2px(context, 8), 0, 0, 0);
        addView(tvLeft);
    }

    //添加右边图片
    private void addRightImage(int rightImage) {
        ivRight = new ImageView(context);
        LayoutParams lpLeft = new LayoutParams(dp2px(context, 40), LayoutParams.MATCH_PARENT);
        lpLeft.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ivRight.setLayoutParams(lpLeft);
        ivRight.setImageResource(rightImage);
        ivRight.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ivRight.setPadding(dp2px(context, 10), dp2px(context, 10), dp2px(context, 10), dp2px(context, 10));
        addView(ivRight);
    }

    //添加右边文字
    private void addRightText(String rightText, int rightTextColor) {
        tvRight = new TextView(context);
        LayoutParams lpRight = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lpRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvRight.setLayoutParams(lpRight);
        tvRight.setGravity(Gravity.CENTER_VERTICAL);
        tvRight.setText(rightText);
        tvRight.setTextColor(rightTextColor);
        tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tvRight.setTypeface(Typeface.MONOSPACE);
        tvRight.setLines(1);
        tvRight.setPadding(0, 0, dp2px(context, 8), 0);
        addView(tvRight);
    }

    //设置左边监听器
    public void setOnLeftClickListener(OnClickListener clickListener){
        if (ivLeft != null){
            ivLeft.setOnClickListener(clickListener);
        }
        if (tvLeft != null){
            tvLeft.setOnClickListener(clickListener);
        }
    }
    //设置右边监听器
    public void setOnRightClickListener(OnClickListener clickListener){
        if (ivRight != null) {
            ivRight.setOnClickListener(clickListener);
        }
        if (tvRight != null) {
            tvRight.setOnClickListener(clickListener);
        }
    }
    //设置标题监听器
    public void setOnTitleClickListener(OnClickListener clickListener){
        if (ivTitle != null) {
            ivTitle.setOnClickListener(clickListener);
        }
        if (tvTitle != null) {
            tvTitle.setOnClickListener(clickListener);
        }
    }

    //距离
    private int dp2px(Context context,float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    //隐藏左边
    public void hideLeft(){
        if(ivLeft != null){
            ivLeft.setVisibility(View.GONE);
        }
        if (tvLeft != null){
            tvLeft.setVisibility(View.GONE);
        }
    }
    //设置标题文字
    public void setTitleText(String title){
        if (tvTitle == null){
            addTitleText(title,titleColor);
        }else {
            tvTitle.setText(title);
        }
    }
    //设置标题
    public void setTitleText(int resId){
        if (tvTitle == null){
            addTitleText(context.getString(resId),titleColor);
        }else {
            tvTitle.setText(context.getString(resId));
        }
    }
    //设置右边图片,resId资源路径
    public void setRightImage(int resId){
        if (ivRight == null){
            addRightImage(resId);
        }else {
            ivRight.setImageResource(resId);
        }
    }
    public void setRightTextColor(int color){
        rightTextColor = color;
    }

    //设置右边文字
    public void setRightText(String right){
        if (tvRight == null){
            addRightText(right,rightTextColor);
        }else {
            tvRight.setText(right);
        }
    }
    //设置右边文字
    public void setRightText(int resId){
        if(tvRight == null){
            addRightText(context.getString(resId),titleColor);
        }else {
            tvRight.setText(context.getString(resId));
        }
    }
}
