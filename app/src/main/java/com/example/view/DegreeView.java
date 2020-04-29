package com.example.view;
/*
痛经。流量程度控件
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.angel.R;

public class DegreeView extends LinearLayout {
    private Context mContext;
    private TextView tv;
    private ImageView leftIv;
    private ImageView[] ivArr;
    private int factImage,virtualImage;
    public DegreeView(Context context) {
        super(context);
        mContext = context;
    }

    public DegreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DegreeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(Color.parseColor("#FFFFFF"));
        setPadding(0, 0, dp2px(mContext, 12), 0);
        ivArr = new ImageView[5];

        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.DegreeView, defStyle, 0);
        int leftImage = type.getResourceId(R.styleable.DegreeView_leImage, -1);
        if(leftImage != -1){
            addLeftImageView(leftImage);
        }

        String textName = type.getString(R.styleable.DegreeView_textName);
        if(!"".equals(textName)){
            int textColor = type.getColor(R.styleable.DegreeView_textcolor, Color.parseColor("#BBBBBB"));
            addTextView(textName, textColor);
        }

        virtualImage = type.getResourceId(R.styleable.DegreeView_virtualImage, -1);
        factImage = type.getResourceId(R.styleable.DegreeView_factImage, -1);
        if(virtualImage != -1 && factImage != -1){
            addImageViewArr(virtualImage, factImage);
        }
        setListener();
    }

    private void addImageViewArr(int virtualImage, int factImage) {
        for(int i=0; i<ivArr.length; i++){
            ImageView iv = new ImageView(mContext);
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            iv.setLayoutParams(lp);
            iv.setImageResource(virtualImage);
            iv.setPadding(dp2px(mContext, 5), dp2px(mContext, 12), 0, dp2px(mContext, 12));
            addView(iv);
            ivArr[i] = iv;
        }
    }

    private void addLeftImageView(int leftImage){
        leftIv = new ImageView(mContext);
        LayoutParams lLeft = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftIv.setLayoutParams(lLeft);
        leftIv.setImageResource(leftImage);
        leftIv.setPadding(dp2px(mContext, 12), dp2px(mContext, 12), dp2px(mContext, 3), dp2px(mContext, 12));
        addView(leftIv);
    }

    private void addTextView(String textName, int textColor){
        tv = new TextView(mContext);
        LayoutParams lpLeft = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
        tv.setLayoutParams(lpLeft);
        tv.setText(textName);
        tv.setTextColor(textColor);
        tv.setTextSize(12);
        tv.setPadding(0, dp2px(mContext, 12), dp2px(mContext, 12), dp2px(mContext, 12));
        addView(tv);
    }
    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void setListener(){
        for(int i=0; i<ivArr.length; i++){
            final int position = i;
            ivArr[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    setNumber(position+1);
                    onNumberListener.onClick(position+1);
                }
            });
        }
    }

    private OnNumberListener onNumberListener;

    public void setOnNumberListener(OnNumberListener onNumberListener){
        this.onNumberListener = onNumberListener;
    };

    public interface OnNumberListener{
        public void onClick(int position);
    }


    /**
     * 设置程度几级
     * @param number 级别
     */
    public void setNumber(int number){
        if(number > 5){
            number = 5;
        }
        for(int i=0; i< number; i++){
            ivArr[i].setImageResource(factImage);
        }
        for(int i=number; i< 5; i++){
            ivArr[i].setImageResource(virtualImage);
        }
    }
}
