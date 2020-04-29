package com.example.view;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.angel.DateCardModel;
import com.example.angel.R;

/**
 * 大姨妈日历控件卡片类
 * @author Administrator zxm
 *
 */
public class DateCard extends FrameLayout{
	//颜色
    private String safety = "#9DF96F"; //3
    private String risk = "#FFF370"; //4
	
	private TextView tvNumber, tvToday;
	private ImageView iv;
	private LinearLayout ll, layout;
	
	public DateCard(Context context) {
		super(context);
		
		initView();
	}
	
	private void initView(){
		layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundResource(R.drawable.date_view_bg_witle);
		LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lps.setMargins(5, 5, 0, 0);
		layout.setLayoutParams(lps);
		layout.setPadding(1, 1, 1, 1);
		addView(layout);
		
		ll = new LinearLayout(getContext());
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundResource(R.drawable.menstruation_date_view_item_white);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ll.setLayoutParams(lp);
		layout.addView(ll, lp);
		
		//显示日期
		tvNumber = new TextView(getContext());
		tvNumber.setTextSize(14);
		tvNumber.setTextColor(Color.parseColor("#BBBBBB"));
		tvNumber.setPadding(5, 0, 0, 0);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ll.addView(tvNumber, params);
		
		LinearLayout l = new LinearLayout(getContext());
		l.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		l.setPadding(5, 0, 0, 0);
		l.setGravity(Gravity.CENTER_VERTICAL);
		l.setLayoutParams(lparams);
		ll.addView(l);
		
		//显示姨妈来了、走了图标
		iv = new ImageView(getContext());
		LayoutParams ivParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		iv.setImageResource(R.drawable.dym_nian_r);
		iv.setLayoutParams(ivParams);
		iv.setVisibility(View.GONE);
		l.addView(iv);
		
		//显示今天
		tvToday = new TextView(getContext());
		LayoutParams tvParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tvToday.setLayoutParams(tvParams);
		tvToday.setTextColor(Color.parseColor("#FE8FB0"));
		tvToday.setTextSize(10);
		tvToday.setText("今天");
		tvToday.setVisibility(View.GONE);
		tvToday.setGravity(Gravity.RIGHT);
		l.addView(tvToday);
	}
	
	/**
	 * 初始化数据
	 * @param dateCard
	 */
	public void initData(DateCardModel dateCard){
		tvNumber.setText(dateCard.date +"");
		
		setToMonth(dateCard.istoMonth);
		setToDay(dateCard.isToday, dateCard.type);
		setStart(dateCard.isStart);
		setType(dateCard.type);
		setOnClick(dateCard.isClick);
	}
	
	/**
	 * 是否显示边框（点击显示的边框）
	 * @param isClick true显示
	 */
	public void setOnClick(boolean isClick){
		if(isClick){
			layout.setBackgroundResource(R.drawable.date_view_bg);
		}else {
			layout.setBackgroundResource(R.drawable.date_view_bg_witle);
		}
	}
	
	/**
	 * 是否显示内容（在本月中显示的上下月时间不用显示内容）
	 * @param istoMonth
	 */
	public void setToMonth(boolean istoMonth){
		if(!istoMonth){
			iv.setVisibility(View.GONE);
			tvNumber.setVisibility(View.GONE);
			tvToday.setVisibility(View.GONE);
		}else {
			iv.setVisibility(View.VISIBLE);
			tvNumber.setVisibility(View.VISIBLE);
			tvToday.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 显示今天
	 * @param isToday
	 */
	public void setToDay(boolean isToday, int type){
		if(isToday){
			tvToday.setVisibility(View.VISIBLE);
		}else {
			tvToday.setVisibility(View.GONE);
		}
		if(type == 1 || type == 2){
			tvToday.setTextColor(Color.WHITE);
		}else {
			tvToday.setTextColor(Color.parseColor("#FE8FB0"));
		}
	}
	
	/**
	 * 设置数字字体颜色与背景颜色
	 * @param type
	 */
	public void setType(int type){
		switch (type) {
		case 0:
			tvNumber.setTextColor(Color.parseColor("#BBBBBB"));
			ll.setBackgroundResource(R.drawable.menstruation_date_view_item_white);
			break;
		case 1:
			tvNumber.setTextColor(Color.WHITE);
			ll.setBackgroundResource(R.drawable.menstruation_date_view_item_red);
			break;
		case 2:
			tvNumber.setTextColor(Color.WHITE);
			ll.setBackgroundResource(R.drawable.menstruation_date_view_item_pink);
			break;
		case 3:
			tvNumber.setTextColor(Color.parseColor(safety));
			ll.setBackgroundResource(R.drawable.menstruation_date_view_item_white);
			break;
		case 4:
			tvNumber.setTextColor(Color.parseColor(risk));
			ll.setBackgroundResource(R.drawable.menstruation_date_view_item_white);
			break;
		}
	}
	
	/**
	 * 显示开始图片或者结束图片
	 * @param isStart
	 */
	public void setStart(int isStart){
		if(isStart == 1){
			iv.setVisibility(View.VISIBLE);
			iv.setImageResource(R.drawable.dym_kaishi);
		}else if (isStart == 2) {
			iv.setVisibility(View.VISIBLE);
			iv.setImageResource(R.drawable.dym_jieshu);
		}else {
			iv.setVisibility(View.GONE);
		}
	}
	
}
