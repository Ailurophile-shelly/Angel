package com.example.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.angel.R;
import com.example.util.DateChange;

import java.util.Calendar;
import java.util.Date;

/*
 * 日期选择PopupWindow控件,弹窗
 *    1、创建PopupWindow对象实例；
 *    2、设置背景、注册事件监听器和添加动画；
 *    3、显示PopupWindow
 *
 */
public class CompositePopupWindow extends PopupWindow {
    private PopupWindow popComposite;
    private View popView;
    private DatePicker datePicker;
    private String title = "Cancel";
    private TextView tvTitle;
    private int y, m, d;
    public CompositePopupWindow(Context context){
        //用于Pop的View
        popView = LayoutInflater.from(context).inflate(R.layout.view_select_combo_pop_1, null);
        initView();
        setListener();
    }

    @SuppressWarnings("deprecation")
    private void initView(){
        //创建对象，背景
        popComposite = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置动画
        popComposite.setAnimationStyle(R.style.anim_popup_dir);
       //设置背景
        popComposite.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        popComposite.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        popComposite.setFocusable(true);

        datePicker = (DatePicker) popView.findViewById(R.id.date_picker);
        tvTitle = (TextView) popView.findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }

    private void setListener(){
        //文字
        tvTitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(tvTitle.getText().toString().equals("Cancel")){
                    //消失隐藏
                    dismiss();
                }
            }
        });
        //图标
        popView.findViewById(R.id.iv_confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popComposite.isShowing()){
                    selectComposite.onSelectComposite(y, m, d);
                    popComposite.dismiss();
                }
            }
        });
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        y = year;
        m = monthOfYear+1;
        d = dayOfMonth;
        datePicker.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener(){

            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                y = year;
                m = monthOfYear+1;
                d = dayOfMonth;
            }
        });
    }

    //设置最大日期
    public void setMaxDate(){
        Date d = new Date();
        //系统当前时间
        datePicker.setMaxDate(d.getTime());
    }

    //设置最小日期
    public void setMinDate(){
        datePicker.setMinDate(DateChange.getDate());
    }

    public void dismiss(){
        if(popComposite != null){
            popComposite.dismiss();
        }
    }

    public void show(View parent){
        if(!popComposite.isShowing()){
            popComposite.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        }
    }

    public boolean isShow(){
        if(popComposite != null){
            return popComposite.isShowing();
        }
        return false;
    }

    //内部类
    private SelectComposite selectComposite;

    public void setSelectComposite(SelectComposite selectComposite){
        this.selectComposite = selectComposite;
    }

    public interface SelectComposite{
        public void onSelectComposite(int year, int monthOfYear, int dayOfMonth);
    }
}
