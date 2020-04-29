package com.example.angel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.db.MenstruationDao;
import com.example.util.DateChange;
import com.example.view.DateView;
import com.example.view.DegreeView;
import com.example.view.WFYTitle;
import com.example.weather.WeatherActivity;
import com.example.weight.FirstActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    private WFYTitle title;
    private TextView tvDate;
    private DateView dateView;
    private DegreeView dvFlow, dvPain;
    private LinearLayout llMtCome, llMtBack;
    private MenstruationDao mtDao;
    private MenstruationCycle mCycle;
    private Date curDate; // 当前日历显示的月
    private Calendar calendar;
    private MenstruationModel mtmBass;//预测大姨妈的基础数据
    private long nowTime = 0;//点击的日期
    private DateCardModel dcm;//点击的月份
    private List<MenstruationModel> list;
    private Context mContext;

    //导航栏
    private RadioGroup radioGroup;
    private RadioButton indexButton;
    private RadioButton weightButton;
    private RadioButton weatherButton;
    //时间
    private TextView indexDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_main);
//导航
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        weightButton = (RadioButton)findViewById(R.id.weight);
        weatherButton = (RadioButton)findViewById(R.id.weather);
        indexDate = (TextView)findViewById(R.id.index);
        listenRgChange();

        mContext = this;
        initView();
        initData();
        setListener();
    }
    //设置底部导航栏
    public void listenRgChange(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.index:
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, GuideActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.weather:
                        Intent intent1 = new Intent();
                        intent1.setClass(MainActivity.this, WeatherActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.weight:
                        Intent intent2 = new Intent();
                        intent2.setClass(MainActivity.this, FirstActivity.class);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void initView() {
        tvDate = (TextView) findViewById(R.id.tv_date);
        dateView = (DateView) findViewById(R.id.date_view);
        dateView.setOnItemClickListener(new DateView.OnItemListener() {

            @Override
            public void onClick(long time, DateCardModel d) {
                nowTime = time;
                dcm = d;
                if(time> DateChange.getDate()){
                    llMtCome.setVisibility(View.GONE);
                    llMtBack.setVisibility(View.GONE);
                    dvFlow.setVisibility(View.GONE);
                    dvPain.setVisibility(View.GONE);
                    return;
                }else if(dcm.type == 1){
                    MenstruationMt mt = mtDao.getMTMT(nowTime);
                    llMtCome.setVisibility(View.GONE);
                    llMtBack.setVisibility(View.VISIBLE);
                    dvFlow.setVisibility(View.VISIBLE);
                    dvPain.setVisibility(View.VISIBLE);
                    if(mt != null){
                        dvFlow.setNumber(mt.getQuantity());
                        dvPain.setNumber(mt.getPain());
                    }else {
                        dvFlow.setNumber(0);
                        dvPain.setNumber(0);
                    }
                }else if (mtDao.getEndTimeNumber(nowTime) < 6) {
                    llMtCome.setVisibility(View.GONE);
                    llMtBack.setVisibility(View.VISIBLE);
                    dvFlow.setVisibility(View.GONE);
                    dvPain.setVisibility(View.GONE);
                }else if (dcm.type != 1) {
                    llMtCome.setVisibility(View.VISIBLE);
                    llMtBack.setVisibility(View.GONE);
                    dvFlow.setVisibility(View.GONE);
                    dvPain.setVisibility(View.GONE);
                }
            }
        });
        tvDate.setText(dateView.getYearAndmonth());
        dvFlow = (DegreeView) findViewById(R.id.dv_flow);
        dvPain = (DegreeView) findViewById(R.id.dv_pain);
        title = (WFYTitle) findViewById(R.id.wfy_title);
        llMtCome = (LinearLayout) findViewById(R.id.ll_mt_come);
        llMtBack = (LinearLayout) findViewById(R.id.ll_mt_back);


    }

    /**
     * 初始化大姨妈数据
     */
    private void initData(){
        calendar = Calendar.getInstance();
        curDate = new Date();
        calendar.setTime(curDate);
        mtDao = new MenstruationDao(mContext);
        mCycle= mtDao.getMTCycle();
        long nowDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-1","yyyy-MM-dd");
        long nextDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+2)+"-1","yyyy-MM-dd");
        //获取当月数据
        List<MenstruationModel> mtmList = mtDao.getMTModelList(nowDate, nextDate);
        //获取全部数据
        list = mtDao.getMTModelList(0, 0);
        //当数据库中没有本月记录时，根据上一次的记录预测本月记录
        for(int i=0; i<mtmList.size(); i++){
            mtmList.get(i).setCon(true);
        }
        if(mtmList.size()==0){
            MenstruationModel mtm = new MenstruationModel();
            mtm.setDate(nowDate);
            mtm.setBeginTime(list.get(list.size()-1).getBeginTime()+intervalTime(list.get(list.size()-1).getBeginTime(), nowDate));
            mtm.setEndTime(list.get(list.size()-1).getBeginTime()+intervalTime(list.get(list.size()-1).getBeginTime(), nowDate)+86400000l*(mCycle.getNumber()-1));
            mtm.setCon(false);
            //如果当月没有记录，就根据之前的数据来预测现在的来月经时间，如果根据之前预测的时间小于当天时间就从现在开始记录
            if(mtm.getBeginTime() > DateChange.getDate()){
                mtmList.add(mtm);
            }else {
                mtm.setBeginTime(DateChange.getDate());
                mtm.setEndTime(DateChange.getDate() + 86400000l*4);
                mtmList.add(mtm);
            }
            //记录预测的基准
            mtmBass = mtm;
        }else {
            //记录预测的基准
            mtmBass = mtmList.get(mtmList.size()-1);
        }
        //下一次的月经是否在当月
        MenstruationModel mtm = new MenstruationModel();
        mtm.setBeginTime(mtmBass.getEndTime()+86400000l*28);
        mtm.setEndTime(mtmBass.getEndTime()+86400000l*28+86400000l*(mCycle.getNumber()-1));
        mtm.setDate(nowDate);
        mtm.setCon(false);
        if(nextDate > mtm.getBeginTime()){
            if(mtm.getBeginTime() > DateChange.getDate()){
                mtmList.add(mtm);
            }else {
                mtm.setBeginTime(DateChange.getDate());
                mtm.setEndTime(DateChange.getDate() + 86400000l*4);
                mtmList.add(mtm);
            }
            mtmBass = mtm;
        }
        dateView.initData(mtmList);
    }

    /**
     * 获取并预测大姨妈
     * @param nowDate 当月时间
     * @param nextDate 下月时间
     * @return
     */
    private List<MenstruationModel> calculateMt(long nowDate, long nextDate){
        //获取本月大姨妈数据
        List<MenstruationModel> mtmList = mtDao.getMTModelList(nowDate, nextDate);//将数据库中的当月大姨妈数据取出来
        for(int i=0; i<mtmList.size(); i++){
            mtmList.get(i).setCon(true);
        }
        //现在时间小于基础时间，不用计算其他的
        if(nowDate < mtmBass.getDate()){
            return mtmList;
        }
        //如果当月没有大姨妈数据，就根据上一个月的数据预测这个月的姨妈周期
        if(nowDate == mtmBass.getDate()){
            //现在时间跟基础时间相同
            if(!mtmBass.isCon()){
                mtmList.add(mtmBass);
            }
        }else {
            //不同就根据基础时间预测
            MenstruationModel mtm1 = new MenstruationModel();
            mtm1.setBeginTime(mtmBass.getBeginTime()+intervalTime(mtmBass.getDate(), nowDate));
            mtm1.setEndTime(mtmBass.getBeginTime()+intervalTime(mtmBass.getDate(), nowDate)+86400000l*(mCycle.getNumber()-1));
            mtm1.setCon(false);
            mtmList.add(mtm1);
            //判断下一次的大姨妈是否在本月
            MenstruationModel mtm = new MenstruationModel();
            mtm.setBeginTime(mtmBass.getBeginTime()+intervalTime(mtmBass.getDate(), nowDate)+86400000l*28);
            mtm.setEndTime(mtmBass.getBeginTime()+intervalTime(mtmBass.getDate(), nowDate)+86400000l*28+86400000l*(mCycle.getNumber()-1));
            mtm.setCon(false);
            if(nextDate > mtm.getBeginTime()){
                if(mtm.getBeginTime() > DateChange.getDate()){
                    mtmList.add(mtm);
                }else {
                    mtm.setBeginTime(DateChange.getDate());
                    mtm.setEndTime(DateChange.getDate() + 86400000l*4);
                    mtmList.add(mtm);
                }
            }
        }
        //判断上一次的大姨妈是否有在本月
        MenstruationModel mtm1 = new MenstruationModel();
        mtm1.setBeginTime(mtmBass.getBeginTime()+intervalTime(mtmBass.getDate(), nowDate)-86400000l*28);
        mtm1.setEndTime(mtmBass.getBeginTime()+intervalTime(mtmBass.getDate(), nowDate)-86400000l*28+86400000l*(mCycle.getNumber()-1));
        mtm1.setCon(false);
        if(nowDate <= mtm1.getEndTime()){
            mtmList.add(mtm1);
        }
        return mtmList;
    }


    private void setListener() {
        /**
         * 上一月
         */
        findViewById(R.id.iv_click_left_month).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                calendar.setTime(curDate);
                calendar.add(Calendar.MONTH, -1);
                curDate = calendar.getTime();
                long nowDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-1","yyyy-MM-dd");
                long nextDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+2)+"-1","yyyy-MM-dd");
                List<MenstruationModel> mtmList = calculateMt(nowDate, nextDate);
                tvDate.setText(dateView.clickLeftMonth(mtmList));
            }
        });
        /**
         * 下一月
         */
        findViewById(R.id.iv_click_right_month).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                calendar.setTime(curDate);
                calendar.add(Calendar.MONTH, 1);
                curDate = calendar.getTime();
                long nowDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-1","yyyy-MM-dd");
                long nextDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+2)+"-1","yyyy-MM-dd");
                List<MenstruationModel> mtmList = calculateMt(nowDate, nextDate);
                tvDate.setText(dateView.clickRightMonth(mtmList));
            }
        });
        /**
         * 回到当月
         */
        findViewById(R.id.tv_today).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                calendar.setTime(curDate);
                calendar.add(Calendar.MONTH, getNowTime("yyyy")*12 + getNowTime("MM") - (calendar.get(Calendar.MONTH)+1)-calendar.get(Calendar.YEAR)*12);
                curDate = calendar.getTime();
                long nowDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-1","yyyy-MM-dd");
                long nextDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+2)+"-1","yyyy-MM-dd");
                List<MenstruationModel> mtmList = mtDao.getMTModelList(nowDate, nextDate);
                for(int i=0; i<mtmList.size(); i++){
                    mtmList.get(i).setCon(true);
                }
                if(mtmList.size()==0){
                    MenstruationModel mtm = new MenstruationModel();
                    mtm.setBeginTime(list.get(list.size()-1).getBeginTime()+intervalTime(list.get(list.size()-1).getBeginTime(), nowDate));
                    mtm.setEndTime(list.get(list.size()-1).getBeginTime()+intervalTime(list.get(list.size()-1).getBeginTime(), nowDate)+86400000l*(mCycle.getNumber()-1));
                    mtm.setCon(false);
                    if(mtm.getBeginTime() > DateChange.getDate()){
                        mtmList.add(mtm);
                    }else {
                        mtm.setBeginTime(DateChange.getDate());
                        mtm.setEndTime(DateChange.getDate() + 86400000l*4);
                        mtm.setCon(false);
                        mtmList.add(mtm);
                    }
                }
                mtmList.add(mtmBass);
                tvDate.setText(dateView.recurToday(mtmList));
            }
        });
        /**
         * 月经分析
         */
        title.setOnRightClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(mContext, MenstruationAnalyze.class));
            }
        });
        /**
         * 姨妈来了
         */
        llMtCome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                long startTime = mtDao.getStartTimeNumber(nowTime);
                if((startTime-nowTime)/86400000<9 && (startTime-nowTime)/86400000>0){
                    mtDao.updateMTStartTime(nowTime, startTime);
                }else {
                    MenstruationModel mtm = new MenstruationModel();
                    mtm.setDate(DateChange.dateTimeStamp(DateChange.timeStamp2Date(nowTime+"", "yyyy-MM")+"-1", "yyyy-MM-dd"));
                    mtm.setBeginTime(nowTime);
                    mtm.setEndTime(nowTime+86400000l*(mCycle.getNumber()-1));
                    mtm.setCycle(mCycle.getCycle());
                    mtm.setDurationDay(mCycle.getNumber());
                    mtDao.setMTModel(mtm);
                }
                refreshUI();
            }
        });

        /**
         * 姨妈走了
         */
        llMtBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mtDao.updateMTEndTime(nowTime);
                refreshUI();
            }
        });

        /**
         * 流量程度回调
         */
        dvFlow.setOnNumberListener(new DegreeView.OnNumberListener() {

            @Override
            public void onClick(int position) {
                MenstruationMt mt = mtDao.getMTMT(nowTime);
                if(mt != null){
                    mt.setQuantity(position);
                    mtDao.updateMTM(mt);
                }else {
                    mt = new MenstruationMt();
                    mt.setDate(nowTime);
                    mt.setQuantity(position);
                    mt.setPain(0);
                    mtDao.setMTMT(mt);
                }
            }
        });

        /**
         * 痛经程度回调
         */
        dvPain.setOnNumberListener(new DegreeView.OnNumberListener() {

            @Override
            public void onClick(int position) {
                MenstruationMt mt = mtDao.getMTMT(nowTime);
                if(mt != null){
                    mt.setPain(position);
                    mtDao.updateMTM(mt);
                }else {
                    mt = new MenstruationMt();
                    mt.setDate(nowTime);
                    mt.setQuantity(0);
                    mt.setPain(position);
                    mtDao.setMTMT(mt);
                }
            }
        });
    }

    /**
     * 刷新UI
     */
    private void refreshUI(){
        long nowDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-1","yyyy-MM-dd");
        long nextDate = DateChange.dateTimeStamp(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+2)+"-1","yyyy-MM-dd");
        //获取当月数据
        List<MenstruationModel> mtmList = mtDao.getMTModelList(nowDate, nextDate);
        //获取全部数据
        list = mtDao.getMTModelList(0, 0);
        for(int i=0; i<mtmList.size(); i++){
            mtmList.get(i).setCon(true);
        }
        //下一次的月经是否在当月
        MenstruationModel mtm = new MenstruationModel();
        mtm.setBeginTime(mtmBass.getBeginTime()+86400000l*28);
        mtm.setEndTime(mtmBass.getBeginTime()+86400000l*28+86400000l*(mCycle.getNumber()-1));
        mtm.setDate(nowDate);
        mtm.setCon(false);
        if(nextDate > mtm.getBeginTime()){
            if(mtm.getBeginTime() > DateChange.getDate()){
                mtmList.add(mtm);
            }else {
                mtm.setBeginTime(DateChange.getDate());
                mtm.setEndTime(DateChange.getDate() + 86400000l*4);
                mtmList.add(mtm);
            }
        }
        dateView.refreshUI(mtmList);
    }

    /**
     * 计算间隔时间
     * @param startTime
     * @param endTime
     * @return
     */
    private long intervalTime(long startTime, long endTime){
        int i = (int) ((endTime-startTime)/86400000/mCycle.getCycle());
        i = (endTime-startTime)/86400000%mCycle.getCycle()==0 ? i-1 : i;
        return   i*86400000l*mCycle.getCycle();
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
