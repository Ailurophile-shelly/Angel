package com.example.angel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.util.DateChange;

import java.util.List;

public class SlvSuggestAdapter extends BaseAdapter{
	private Context mContext;
	private List<MenstruationModel> mtmList;
	
	public SlvSuggestAdapter(Context context, List<MenstruationModel> mtmList) {
		this.mContext = context;
		this.mtmList = mtmList;
	}
	
	@Override
	public int getCount() {
		return mtmList.size();
	}

	@Override
	public Object getItem(int position) {
		return mtmList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View viewHolder, ViewGroup arg2) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.slv_suggest_item, null);
		TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
		TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
		TextView tvCycle = (TextView) view.findViewById(R.id.tv_cycle);
		tvDate.setText(DateChange.timeStamp2Date(mtmList.get(mtmList.size()-1-position).getBeginTime()+"", "yyyy-MM-dd"));
		tvNumber.setText(mtmList.get(mtmList.size()-1-position).getDurationDay()+"");
		tvCycle.setText(mtmList.get(mtmList.size()-1-position).getCycle()+"");
		return view;
	}

}
