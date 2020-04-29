package com.example.angel;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.db.MenstruationDao;
import com.example.view.NoScrollViewpager;
import com.example.view.PhysicalArc;

public class MenstruationAnalyze extends FragmentActivity{
	private LinearLayout llArc;
	private TextView tvAnalyze, tvSuggest, tvDetail, tvCycle, tvDateNumber;
	private NoScrollViewpager pager;
	private MenstruationDao mtDao;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menstruation_analyze);
		
		mContext = this;
		initView();
		setListener();
	}

	private void setListener() {
		tvAnalyze.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tvAnalyze.setTextColor(getResources().getColor(R.color.white));
				tvAnalyze.setBackgroundResource(R.drawable.menstruation_left);
				
				tvDetail.setTextColor(getResources().getColor(R.color.theme_color));
				tvDetail.setBackgroundResource(R.drawable.menstruation_centre_while);
				
				tvSuggest.setTextColor(getResources().getColor(R.color.theme_color));
				tvSuggest.setBackgroundResource(R.drawable.menstruation_right_while);
				
				pager.setCurrentItem(0);
			}
		}); 
		tvDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tvAnalyze.setTextColor(getResources().getColor(R.color.theme_color));
				tvAnalyze.setBackgroundResource(R.drawable.menstruation_left_while);
				
				tvDetail.setTextColor(getResources().getColor(R.color.white));
				tvDetail.setBackgroundResource(R.color.theme_color);
				
				tvSuggest.setTextColor(getResources().getColor(R.color.theme_color));
				tvSuggest.setBackgroundResource(R.drawable.menstruation_right_while);
				
				pager.setCurrentItem(1);
			}
		});
		tvSuggest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tvAnalyze.setTextColor(getResources().getColor(R.color.theme_color));
				tvAnalyze.setBackgroundResource(R.drawable.menstruation_left_while);
				
				tvDetail.setTextColor(getResources().getColor(R.color.theme_color));
				tvDetail.setBackgroundResource(R.drawable.menstruation_centre_while);
				
				tvSuggest.setTextColor(getResources().getColor(R.color.white));
				tvSuggest.setBackgroundResource(R.drawable.menstruation_right);
				
				pager.setCurrentItem(2);
			}
		});
	}

	private void initView() {
		mtDao = new MenstruationDao(mContext);
		MenstruationCycle mtCycle = mtDao.getMTCycle();
		
		//圆形百分
		llArc = (LinearLayout) findViewById(R.id.ll_arc);
		PhysicalArc dk = new PhysicalArc(this, 90);
		llArc.addView(dk);
		
		tvAnalyze = (TextView) findViewById(R.id.tv_analyze);
		tvSuggest = (TextView) findViewById(R.id.tv_suggest);
		tvDetail = (TextView) findViewById(R.id.tv_detail);
		tvCycle = (TextView) findViewById(R.id.tv_cycle);
		tvDateNumber = (TextView) findViewById(R.id.tv_date_number);
		tvCycle.setText(mtCycle.getCycle()+"天");
		tvDateNumber.setText(mtCycle.getNumber()+"天");
		
		pager = (NoScrollViewpager) findViewById(R.id.view_pager);
		pager.setAdapter(new VPAdapter(getSupportFragmentManager()));
	}
	
	private class VPAdapter extends FragmentStatePagerAdapter{

		public VPAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment f = null;
			switch (position) {
			case 0:
				f = new AnalyzeFrament();
				break;
			case 1:
				f = new DetailFragment();
				break;
			case 2:
				f = new SuggestFragment();
				break;
			}
			
			return f;
		}

		@Override
		public int getCount() {
			return 3;
		}

	}
}
