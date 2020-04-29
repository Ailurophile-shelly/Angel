package com.example.angel;
/*
开始天数与结束
 */
public class MenstruationModel {
    private int id;
    private long beginTime;//开始时间
    private long endTime;//结束时间
    private long date;//月经月份
    private int cycle;//周期
    private int durationDay;//月经天数
    private boolean isCon = true;//是否确定完

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getDurationDay() {
        return durationDay;
    }

    public void setDurationDay(int durationDay) {
        this.durationDay = durationDay;
    }

    public boolean isCon() {
        return isCon;
    }

    public void setCon(boolean con) {
        isCon = con;
    }

    //重写输出
    public String toString(){
        return "MenstruationModel [id=" + id + ", beginTime=" + beginTime
                + ", endTime=" + endTime + ", date=" + date + ", cycle="
                + cycle + ", durationDay=" + durationDay + ", isCon=" + isCon
                + "]";
    }
}
