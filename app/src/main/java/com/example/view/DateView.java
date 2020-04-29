package com.example.view;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

import com.example.angel.DateCardModel;
import com.example.angel.MenstruationModel;
import com.example.util.DateChange;

/**
 * 大姨妈日历控件
 * @author Administrator zxm
 *
 */
public class DateView extends GridLayout {
	private boolean is = true;
	private Date curDate; // 当前日历显示的月
	private Calendar calendar;
	private int number = 42;
	private int[] date = new int[number]; // 日历显示数字
	private DateCard[] dateCard = new DateCard[number];
	private DateCardModel[] dateList = new DateCardModel[number];
	private int lastNumber, toNumber; //这个月显示上月天数， 这个月天数
	private String dateClick = "";//记录点击的日期
	
	public DateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initView();
	}

	public DateView(Context context) {
		super(context);
		
		initView();
	}

	public DateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView();
	}

	private void initView() {
		setColumnCount(7);
		
		calendar = Calendar.getInstance();
		curDate = new Date();
	}
	
	public void initData(List<MenstruationModel> mtmList){
		calculateDate();
		calculateType(mtmList);
		onItemListener.onClick(DateChange.dateTimeStamp(dateClick, "yyyy/MM/dd"), dateList[lastNumber+getNowTime("dd")]);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		if(is){
			int cardWidth = w/7;
			addCards(cardWidth,cardWidth);
			is = false;
		}
		
	}
	
	private void addCards(int cardWidth,int cardHeight){
		
		DateCard c;
		
		int i = 0;
		for (int y = 0; y < number/7; y++) {
			for (int x = 0; x < 7; x++) {
				c = new DateCard(getContext());
				c.initData(dateList[i]);
				addView(c, cardWidth, cardHeight);
				dateCard[i] = c;
				i++;
			}
		}
		setListener();
	}
	
	private void setListener(){
		//设置点击事件（上个月与下个月的不能点击）
		for(int i=0; i<=lastNumber; i++){
			dateCard[i].setOnClickListener(null);
		}
		for(int i=lastNumber+1; i<=lastNumber + toNumber; i++){
			final int position = i;
			dateCard[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					for(int j=lastNumber+1; j<=lastNumber + toNumber; j++){
						if(position==j){
							dateList[j].isClick = true;
							dateClick= calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+dateList[j].date;
							//进行回调处理
							onItemListener.onClick(DateChange.dateTimeStamp(dateClick, "yyyy/MM/dd"), dateList[j]);
						}else {
							dateList[j].isClick = false;
						}
						dateCard[j].setOnClick(dateList[j].isClick);
					}
				}
			});
		}
		for(int i=lastNumber + toNumber+1; i<dateList.length; i++){
			dateCard[i].setOnClickListener(null);
		}
		
		//开头空白超过一行去掉一行
		if(lastNumber >= 6){
			for(int i=0; i<7; i++){
				dateCard[i].setVisibility(View.GONE);
			}
		}else {
			for(int i=0; i<7; i++){
				dateCard[i].setVisibility(View.VISIBLE);
			}
		}
		//结尾空白超过一行去掉一行
		if(lastNumber + toNumber > 34){
			for(int i=35; i<42; i++){
				dateCard[i].setVisibility(View.VISIBLE);
			}
		}else {
			for(int i=35; i<42; i++){
				dateCard[i].setVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * 计算日期
	 */
	private void calculateDate() {
		calendar.setTime(curDate);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int monthStart = dayInWeek;
		if (monthStart == 1) {
			monthStart = 8;
		}
		monthStart -= 1;  //以日为开头-1，以星期一为开头-2
		//月初1号
		if(dateClick.equals(calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+1)){
			if(getNowTime("yyyy") == calendar.get(Calendar.YEAR) 
					&& getNowTime("MM") == calendar.get(Calendar.MONTH)+1 
				 	&& getNowTime("dd") == 1){
				dateList[monthStart] = new DateCardModel(1, true, 0, 0, true, true);
			}else {
				dateList[monthStart] = new DateCardModel(1, false, 0, 0, true, true);
			}
		}else {
			if(getNowTime("yyyy") == calendar.get(Calendar.YEAR) 
					&& getNowTime("MM") == calendar.get(Calendar.MONTH)+1 
				 	&& getNowTime("dd") == 1){
				dateList[monthStart] = new DateCardModel(1, true, 0, 0, true, false);
			}else {
				dateList[monthStart] = new DateCardModel(1, false, 0, 0, true, false);
			}
		}
		date[monthStart] = 1;
		// last month 上个月
		if (monthStart > 0) {
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);
			lastNumber = monthStart - 1;
			
			for (int i = monthStart - 1; i >= 0; i--) {
				dateList[i] = new DateCardModel(dayInmonth, false, 0, 0, false, false);
				date[i] = dayInmonth;
				dayInmonth--;
			}
			calendar.set(Calendar.DAY_OF_MONTH, date[0]);
		}
		// this month 当月
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
		
		toNumber = monthDay;
		for (int i = 1; i < monthDay; i++) {
			date[monthStart + i] = i + 1;
			calendar.setTime(curDate);
			if(dateClick.equals(calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+(i+1))){
				if(getNowTime("yyyy") == calendar.get(Calendar.YEAR) 
						&& getNowTime("MM") == calendar.get(Calendar.MONTH)+1 
						 	&& getNowTime("dd") == (i+1)){
					dateList[monthStart+i] = new DateCardModel(i+1, true, 0, 0, true, true);
				}else {
					dateList[monthStart+i] = new DateCardModel(i+1, false, 0, 0, true, true);
				}
			}else if(dateClick.equals("")){
				if(getNowTime("yyyy") == calendar.get(Calendar.YEAR) 
						&& getNowTime("MM") == calendar.get(Calendar.MONTH)+1 
						 	&& getNowTime("dd") == (i+1)){
					//=========初始化点击
					dateList[monthStart+i] = new DateCardModel(i+1, true, 0, 0, true, true);
					dateClick = calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+(i+1);
				}else {
					dateList[monthStart+i] = new DateCardModel(i+1, false, 0, 0, true, false);
				}
			}else {
				if(getNowTime("yyyy") == calendar.get(Calendar.YEAR) 
						&& getNowTime("MM") == calendar.get(Calendar.MONTH)+1 
						 	&& getNowTime("dd") == (i+1)){
					dateList[monthStart+i] = new DateCardModel(i+1, true, 0, 0, true, false);
				}else {
					dateList[monthStart+i] = new DateCardModel(i+1, false, 0, 0, true, false);
				}
			}
		}
		// next month 下个月
		for (int i = monthStart + monthDay; i < number; i++) {
			dateList[i] = new DateCardModel(i - (monthStart + monthDay) + 1, false, 0, 0, false, false);
			date[i] = i - (monthStart + monthDay) + 1;
		}
//		calculateType(1, 12, 15, getNowTime("dd"));
	}
	
	private long getNowDate(int d){
		calendar.setTime(curDate);
		String date = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+d;
		return DateChange.dateTimeStamp(date, "yyyy-MM-dd");
	}
	
	/**
	 * 刷新UI
	 * @param mtmList
	 */
	public void refreshUI(List<MenstruationModel> mtmList){
		calendar.setTime(curDate);
		calculateDate();
		calculateType(mtmList);
		for(int i=0; i<number; i++){
			dateCard[i].initData(dateList[i]);
		}
		setListener();
		onItemListener.onClick(DateChange.dateTimeStamp(dateClick, "yyyy/MM/dd"), dateList[lastNumber+getNowTime("dd")]);
	}
	
	/**
	 * 计算当月显示的状态（1为月经期，2为预测期，3为安全期，4为易孕期, 0为其他）
	 * @param start 当月月经开始日期
	 * @param end 当月月经结束日期
	 */
	public void calculateType(List<MenstruationModel> mtmList){
		if(mtmList.size()!=0) {
			for (int i = lastNumber + 1; i <= lastNumber + toNumber; i++) {
				if (isType1(mtmList, i) != -1) {//月经期或者预测期
					int position = isType1(mtmList, i);
					if (mtmList.get(position).getBeginTime() > DateChange.dateTimeStamp(getYMD("yyyy-MM-dd"), "yyyy-MM-dd")) {
						dateList[i].type = 2;
					} else if (mtmList.get(position).getEndTime() < DateChange.dateTimeStamp(getYMD("yyyy-MM-dd"), "yyyy-MM-dd")) {
						dateList[i].type = 1;
						if (isStart(mtmList, i)) {
							dateList[i].isStart = 1;
						} else if (isEnd(mtmList, i)) {
							dateList[i].isStart = 2;
						} else {
							dateList[i].isStart = 3;
						}
					} else if (getNowDate(dateList[i].date) == mtmList.get(position).getBeginTime()) {
						if (mtmList.get(position).isCon()) {
							dateList[i].type = 1;
							if (isStart(mtmList, i)) {
								dateList[i].isStart = 1;
							} else if (isEnd(mtmList, i)) {
								dateList[i].isStart = 2;
							} else {
								dateList[i].isStart = 3;
							}
						} else {
							dateList[i].type = 2;
						}
					} else {
						if (getNowDate(dateList[i].date) > mtmList.get(position).getBeginTime() &&
								getNowDate(dateList[i].date) <= DateChange.dateTimeStamp(getYMD("yyyy-MM-dd"), "yyyy-MM-dd")) {
							dateList[i].type = 1;
							if (isStart(mtmList, i)) {
								dateList[i].isStart = 1;
							} else if (isEnd(mtmList, i)) {
								dateList[i].isStart = 2;
							} else {
								dateList[i].isStart = 3;
							}
						} else {
							dateList[i].type = 2;
						}
					}
				} else if (isType2(mtmList, i)) {
					dateList[i].type = 3;
				} else {
					dateList[i].type = 4;
				}

			}
		}
	}
	private int isType1(List<MenstruationModel> mtmList, int position){
		for(int i = 0; i<mtmList.size(); i++){
			if(getNowDate(dateList[position].date)>=mtmList.get(i).getBeginTime() && getNowDate(dateList[position].date)<=mtmList.get(i).getEndTime()){
				return i;
			}
		}
		return -1;
	}
	
	private boolean isType2(List<MenstruationModel> mtmList, int position){
		for(int i = 0; i<mtmList.size(); i++){
			if((getNowDate(dateList[position].date)<mtmList.get(i).getBeginTime() && getNowDate(dateList[position].date)>=mtmList.get(i).getBeginTime()-7*86400000)
					|| (getNowDate(dateList[position].date)>mtmList.get(i).getEndTime() && getNowDate(dateList[position].date)<mtmList.get(i).getEndTime()+9*86400000)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isStart(List<MenstruationModel> mtmList, int position){
		for(int i = 0; i<mtmList.size(); i++){
			if(getNowDate(dateList[position].date) == mtmList.get(i).getBeginTime()){
				return true;
			}
		}
		return false;
	}
	private boolean isEnd(List<MenstruationModel> mtmList, int position){
		for(int i = 0; i<mtmList.size(); i++){
			if(getNowDate(dateList[position].date) == mtmList.get(i).getEndTime()){
				return true;
			}
		}
		return false;
	}
	/**
	 *  获得当前应该显示的年月
	 * @return
	 */
	public String getYearAndmonth() {
		calendar.setTime(curDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		return year + "年" + month + "月";
	}

	private OnItemListener onItemListener;
	
	/**
	 * 点击回调事件
	 * @param onItemListener
	 */
	public void setOnItemClickListener(OnItemListener onItemListener){
		this.onItemListener = onItemListener;
	}
	
	public interface OnItemListener{
		public void onClick(long time, DateCardModel dcm);
	}
	
	/**
	 * 向上一月
	 * @return
	 */
	public String clickLeftMonth(List<MenstruationModel> mtmList){
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, -1);
		curDate = calendar.getTime();
		calculateDate();
		if(mtmList != null){
			calculateType(mtmList);
		}
		for(int i=0; i<number; i++){
			dateCard[i].initData(dateList[i]);
		}
		setListener();
		return getYearAndmonth();
	}
	/**
	 * 向下一月
	 * @return
	 */
	public String clickRightMonth(List<MenstruationModel> mtmList){
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, 1);
		curDate = calendar.getTime();
		calculateDate();
		if(mtmList!=null){
			calculateType(mtmList);
		}
		for(int i=0; i<number; i++){
			dateCard[i].initData(dateList[i]);
		}
		setListener();
		return getYearAndmonth();
	}
	
	
	/**
	 * 回今天
	 * @return
	 */
	public String recurToday(List<MenstruationModel> mtmList){
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, getNowTime("yyyy")*12 + getNowTime("MM") - (calendar.get(Calendar.MONTH)+1)-calendar.get(Calendar.YEAR)*12);
		curDate = calendar.getTime();
		calculateDate();
		calculateType(mtmList);
		int position = 1;
		for(int i=lastNumber+1; i<=lastNumber + toNumber; i++){
			if(getNowTime("yyyy") == calendar.get(Calendar.YEAR) 
					&& getNowTime("MM") == calendar.get(Calendar.MONTH)+1 
				 	&& getNowTime("dd") == position){
				dateList[i].isClick = true;
				dateClick= calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+dateList[i].date;
				onItemListener.onClick(DateChange.dateTimeStamp(dateClick, "yyyy/MM/dd"), dateList[i]);
			}else {
				dateList[i].isClick = false;
			}
			position++;
		}
		for(int i=0; i<number; i++){
			dateCard[i].initData(dateList[i]);
		}
		setListener();
		return getYearAndmonth();
	}
	/**
	 * 获取当天日期
	 * @param format
	 * @return
	 */
	public String getYMD(String format) { 
        Date now = new Date(); 
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);// 可以方便地修改日期格式  
        String hehe = dateFormat.format(now); 
        return hehe; 
    } 
	/**
	 * 获取当天日期
	 * @param format
	 * @return
	 */
	public int getNowTime(String format) { 
        Date now = new Date(); 
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);// 可以方便地修改日期格式  
        String hehe = dateFormat.format(now); 
        return Integer.parseInt(hehe); 
    } 
}
