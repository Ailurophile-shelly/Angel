package com.example.angel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.MenstruationDao;
import com.example.util.DateChange;
import com.example.view.CompositePopupWindow;

public class SetMenstruationDate extends FragmentActivity {
	private EditText etLast, etNumber, etCycle;
	private TextView tvSubmit;
	private CompositePopupWindow popComposite;
	private MenstruationDao mtDao;
	private String date;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_menstruation_date);
		
		mContext = this;
		initView();
		setListener();
	}
	private void initView() {
		mtDao = new MenstruationDao(mContext);
		etLast = (EditText) findViewById(R.id.et_last_menstruation);
		etNumber = (EditText) findViewById(R.id.et_number);
		etCycle = (EditText) findViewById(R.id.et_cycle);
		tvSubmit = (TextView) findViewById(R.id.tv_submit);
		popComposite = new CompositePopupWindow(mContext);
        popComposite.setMaxDate();
		popComposite.setSelectComposite(new CompositePopupWindow.SelectComposite() {
            @Override
            public void onSelectComposite(int year, int monthOfYear,
					int dayOfMonth) {
            	etLast.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
            	date = year+"-"+monthOfYear+"-1";
//            	DateChange.dateTimeStamp(year+"-"+monthOfYear+"-"+dayOfMonth, "yyyy-MM-dd");
            }
		});
	}
	
	private void setListener(){
		etLast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (popComposite.isShow()) {
					popComposite.dismiss();
				} else {
					popComposite.show(etLast);
				}
			}
		});
		tvSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(etLast.getText().toString().equals("")){
					Toast.makeText(mContext, "请先选择最后一次月经时间",Toast.LENGTH_LONG).show();
					return;
				}
				if(etNumber.getText().toString().equals("")){
					Toast.makeText(mContext, "请先填写月经天数",Toast.LENGTH_LONG).show();
					return;
				}
				if(etCycle.getText().toString().equals("")){
					Toast.makeText(mContext, "请先填写月经周期",Toast.LENGTH_LONG).show();
					return;
				}
				SharedPreferences.Editor editor = getSharedPreferences("mensttuation", MODE_PRIVATE).edit();
            	editor.putBoolean("menstruation", true);
            	editor.commit();
            	MenstruationCycle mc = new MenstruationCycle();
            	mc.setNumber(Integer.parseInt(etNumber.getText().toString()));
            	mc.setCycle(Integer.parseInt(etCycle.getText().toString()));
            	mtDao.setMTCycle(mc);
            	
            	MenstruationModel mtm = new MenstruationModel();
            	mtm.setBeginTime(DateChange.dateTimeStamp(etLast.getText().toString(), "yyyy-MM-dd"));
            	mtm.setEndTime(DateChange.dateTimeStamp(etLast.getText().toString(), "yyyy-MM-dd")+86400000l*(Integer.parseInt(etNumber.getText().toString())-1));
            	mtm.setCycle(Integer.parseInt(etCycle.getText().toString()));
            	mtm.setDurationDay(Integer.parseInt(etNumber.getText().toString()));
            	mtm.setDate(DateChange.dateTimeStamp(date, "yyyy-MM-dd"));
            	mtDao.setMTModel(mtm);
            	startActivity(new Intent(mContext, MainActivity.class));
            	finish();
			}
		});
	}
}
