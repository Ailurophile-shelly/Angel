package com.example.angel;

public class DateCardModel {
    public int date;
    public boolean isToday;
    public int type;//显示状态：1为月经期，2为预测期，3为安全期，4为易孕期, 0为其他
    public int isStart;//1表示开始，2表示结束，0表示其他
    public boolean istoMonth;//是否为当月的日期
    public boolean isClick;//点击哪个日期
    public DateCardModel(int date,boolean isToday,int type,int isStart,boolean istoMonth,boolean isClick){
        this.date = date;
        this.isToday = isToday;
        this.type = type;
        this.isStart = isStart;
        this.istoMonth = istoMonth;
        this.isClick = isClick;
    }

}
