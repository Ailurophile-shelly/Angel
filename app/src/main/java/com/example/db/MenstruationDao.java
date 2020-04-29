package com.example.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.angel.MenstruationCycle;
import com.example.angel.MenstruationModel;
import com.example.angel.MenstruationMt;

import java.util.ArrayList;
import java.util.List;

/*
 *大姨妈数据类
 */
public class MenstruationDao {
    private Context mContext;
    private MenstruationDBHelper dbHelper;

    public MenstruationDao(Context context){
        super();
        this.mContext = context;
        dbHelper = new MenstruationDBHelper(mContext);
    }

    /*
     * 向数据库添加月经平均周期与平均天数
     */
    public void setMTCycle(MenstruationCycle mc){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        if(db.isOpen()) {
            db.execSQL("INSERT INTO " + MenstruationDBHelper.TB_NAME_MT_CYCLE + " (number, cycle) " +
                            " VALUES (?, ?) ",
                    new Object[]{mc.getNumber(), mc.getCycle()});
            db.close();
        }
    }

    /*
     * 修改月经平均周期与平均天数
     */
    public void upMTCycle(MenstruationCycle  mc){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        if(db.isOpen()) {
            db.execSQL("UPDATE " + MenstruationDBHelper.TB_NAME_MT_CYCLE +
                            " SET number = ?, cycle = ?  ",
                    new Object[]{mc.getNumber(), mc.getCycle()});
            db.close();
        }
    }
    /*
     * 查询月经平均周期与平均天数
     */
    public MenstruationCycle getMTCycle(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        MenstruationCycle  mc = new MenstruationCycle();

        if(db.isOpen()){
            Cursor cursor = db.rawQuery("SELECT * FROM " + MenstruationDBHelper.TB_NAME_MT_CYCLE , null);
            while(cursor.moveToNext()){
                mc.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
                mc.setCycle(cursor.getInt(cursor.getColumnIndex("cycle")));
            }
            cursor.close();
            db.close();
        }
        return mc;
    }

    /*
     * 向数据库添加月经时间与结束时间等数字
     */
    public void setMTModel(MenstruationModel mt){
        SQLiteDatabase db= dbHelper.getWritableDatabase();

        if(db.isOpen()) {
            Cursor cursor = db.rawQuery("SELECT MIN(startTime) FROM " + MenstruationDBHelper.TB_NAME_MT_TIME + " WHERE " +
                    " startTime > ? ", new String[]{mt.getBeginTime()+""});
            while(cursor.moveToNext()){
                if(cursor.getLong(0) != 0){
                    mt.setCycle((int)((cursor.getLong(0)-mt.getBeginTime())/86400000l)+1);
                }
            }
            db.execSQL("INSERT INTO " + MenstruationDBHelper.TB_NAME_MT_TIME + " (date, startTime, endTime, cycle, number) " +
                            " VALUES (?, ?, ?, ?, ?) ",
                    new Object[]{mt.getDate(), mt.getBeginTime(), mt.getEndTime(), mt.getCycle(), mt.getDurationDay()});

            //获取上一次月经数
            Cursor cursor1 = db.rawQuery("SELECT Max(startTime) FROM " + MenstruationDBHelper.TB_NAME_MT_TIME + " WHERE " +
                    " startTime < ? ", new String[]{mt.getBeginTime()+""});
            long startTime = 0;
            while(cursor1.moveToNext()){
                startTime = cursor1.getLong(0);
            }
            if(startTime == 0){
                return;
            }
            db.execSQL("UPDATE " + MenstruationDBHelper.TB_NAME_MT_TIME + " SET cycle = ? "
                            + " WHERE startTime = ? ",
                    new Object[]{(mt.getBeginTime()-startTime)/86400000+1, startTime});

            cursor1.close();
            db.close();
        }

    }
    /*
     * 描述：获取月经开始结束时间等数据
     *
     */
    public List<MenstruationModel> getMTModelList(long time, long nextTime){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<MenstruationModel> mtmList = new ArrayList<MenstruationModel>();

        if(db.isOpen()){
            Cursor cursor;
            if(time == 0){
                cursor = db.rawQuery("SELECT * FROM " + MenstruationDBHelper.TB_NAME_MT_TIME +
                        " ORDER BY startTime ", null);
            }else {
                cursor = db.rawQuery("SELECT * FROM " + MenstruationDBHelper.TB_NAME_MT_TIME + " WHERE " +
                        " date = ? OR (endTime < ? AND endTime >= ?)" +
                        " ORDER BY startTime ", new String[]{time+"", nextTime+"", time+""});
            }
            while(cursor.moveToNext()){
                MenstruationModel mtm = new MenstruationModel();
                mtm.setId(cursor.getInt(cursor.getColumnIndex("id")));
                mtm.setBeginTime(cursor.getLong(cursor.getColumnIndex("startTime")));
                mtm.setEndTime(cursor.getLong(cursor.getColumnIndex("endTime")));
                mtm.setDate(cursor.getLong(cursor.getColumnIndex("date")));
                mtm.setCycle(cursor.getInt(cursor.getColumnIndex("cycle")));
                mtm.setDurationDay(cursor.getInt(cursor.getColumnIndex("number")));
                mtmList.add(mtm);
            }
            cursor.close();
            db.close();
        }
        return mtmList;
    }

    /*
     * 描述：获取月经开始结束时间等数据
     */
    public MenstruationModel getMTModel(long startTime, long endTime){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        MenstruationModel mtm = new MenstruationModel();

        if(db.isOpen()){
            Cursor cursor = db.rawQuery("SELECT * FROM " + MenstruationDBHelper.TB_NAME_MT_TIME + " WHERE " +
                    " startTime <= ? AND endTime >= ?", new String[]{startTime+"", endTime+""});
            while(cursor.moveToNext()){
                mtm.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                mtm.setBeginTime(cursor.getLong(cursor.getColumnIndex("startTime")));
                mtm.setEndTime(cursor.getLong(cursor.getColumnIndex("endTime")));
                mtm.setDate(cursor.getLong(cursor.getColumnIndex("date")));
                mtm.setCycle(cursor.getInt(cursor.getColumnIndex("cycle")));
                mtm.setDurationDay(cursor.getInt(cursor.getColumnIndex("number")));
            }
            cursor.close();
            db.close();
        }
        return mtm;
    }

    /*
     * 计算结束相差天数
     *
     */
    public int getEndTimeNumber(long nowTime){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long time = 0;
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("SELECT MAX(endTime) FROM " + MenstruationDBHelper.TB_NAME_MT_TIME + " WHERE " +
                    " endTime <= ?", new String[]{nowTime+""});
            while(cursor.moveToNext()){
                time = cursor.getLong(0);
            }
            cursor.close();
            db.close();
        }
        if(time == 0){
            return 10;
        }
        return (int) ((nowTime-time)/86400000);
    }

    /*
     * 计算相差天数
     *
     */
    public long getStartTimeNumber(long nowTime){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long time = 0;
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("SELECT MIN(startTime) FROM " + MenstruationDBHelper.TB_NAME_MT_TIME + " WHERE " +
                    " startTime >= ?", new String[]{nowTime+""});
            while(cursor.moveToNext()){
                time = cursor.getLong(0);
            }
            cursor.close();
            db.close();
        }
        return time;
    }

    /*
     * 修改结束时间
     *
     */
    public void updateMTEndTime(long newTime){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        if(db.isOpen()) {
            Cursor cursor = db.rawQuery("SELECT MAX(startTime) FROM " + MenstruationDBHelper.TB_NAME_MT_TIME + " WHERE " +
                    " startTime <= ? ", new String[]{newTime+""});
            long time = 0;
            if(cursor.moveToNext()){
                time = cursor.getLong(0);
            }
            if(time == 0){
                return;
            }
            db.execSQL("UPDATE " + MenstruationDBHelper.TB_NAME_MT_TIME + " SET endTime = ?, number = ? "
                            + " WHERE startTime = ? ",
                    new Object[]{newTime, (newTime-time)/86400000l+1, time});
            cursor.close();
            db.close();
        }
    }

    /*
     * 修改开始时间
     */
    public void updateMTStartTime(long newTime, long oldTime){
        SQLiteDatabase db= dbHelper.getWritableDatabase();

        if(db.isOpen()) {
            //修改当月信息
            Cursor cursor2 = db.rawQuery("SELECT endTime FROM " + MenstruationDBHelper.TB_NAME_MT_TIME + " WHERE " +
                    " startTime = ?", new String[]{oldTime+""});
            long endTime = 0;
            while(cursor2.moveToNext()){
                endTime = cursor2.getLong(0);
            }
            db.execSQL("UPDATE " + MenstruationDBHelper.TB_NAME_MT_TIME + " SET startTime = ?, number = ? "
                            + " WHERE startTime == ? ",
                    new Object[]{newTime, (endTime-newTime)/86400000l+1, oldTime});
            //修改当月周期
            Cursor cursor1 = db.rawQuery("SELECT MIN(startTime) FROM " + MenstruationDBHelper.TB_NAME_MT_TIME + " WHERE " +
                    " startTime > ?", new String[]{oldTime+""});
            long nextTime = 0;
            while(cursor1.moveToNext()){
                nextTime = cursor1.getLong(0);
            }
            if(nextTime != 0){
                db.execSQL("UPDATE " + MenstruationDBHelper.TB_NAME_MT_TIME + " SET cycle = ? "
                                + " WHERE startTime == ? ",
                        new Object[]{(nextTime-newTime)/86400000l+1, newTime});
            }

            //修改上月信息
            Cursor cursor = db.rawQuery("SELECT MAX(startTime) FROM " + MenstruationDBHelper.TB_NAME_MT_TIME + " WHERE " +
                    " startTime < ?", new String[]{newTime+""});
            long time = 0;
            while(cursor.moveToNext()){
                time = cursor.getLong(0);
            }
            if(time != 0){
                db.execSQL("UPDATE " + MenstruationDBHelper.TB_NAME_MT_TIME + " SET cycle = ? "
                                + " WHERE startTime == ? ",
                        new Object[]{(newTime-time)/86400000l+1, time});
            }
            cursor.close();
            db.close();
        }
    }

    //=======================流量与痛经表===========================

    /*
     * 向数据库添加月经流量与痛经等数据
     */
    public void setMTMT(MenstruationMt mt){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        if(db.isOpen()) {
            db.execSQL("INSERT INTO " + MenstruationDBHelper.TB_NAME_MT + " (date, quantity, pain) " +
                            " VALUES (?, ?, ?) ",
                    new Object[]{mt.getDate(), mt.getQuantity(), mt.getPain()});
            db.close();
        }
    }

    /*
     * 描述：获取月经流量与痛经等数字
     */
    public MenstruationMt getMTMT(long time){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        MenstruationMt mtm = null;

        if(db.isOpen()){
            Cursor cursor = db.rawQuery("SELECT * FROM " + MenstruationDBHelper.TB_NAME_MT + " WHERE " +
                    " date == ?", new String[]{time+""});
            while(cursor.moveToNext()){
                mtm = new MenstruationMt();
                mtm.setDate(cursor.getLong(cursor.getColumnIndex("date")));
                mtm.setPain(cursor.getInt(cursor.getColumnIndex("pain")));
                mtm.setQuantity(cursor.getInt(cursor.getColumnIndex("quantity")));
            }
            cursor.close();
            db.close();
        }
        return mtm;
    }


    /*
     * 修改疼痛流量表
     */
    public void updateMTM(MenstruationMt mt){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        if(db.isOpen()) {
            db.execSQL("UPDATE " + MenstruationDBHelper.TB_NAME_MT + " SET quantity = ?, pain = ? "
                            + " WHERE date == ? ",
                    new Object[]{mt.getQuantity(), mt.getPain(), mt.getDate()});
            db.close();
        }
    }
}
