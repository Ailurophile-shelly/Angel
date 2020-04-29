package com.example.db;
/*
   大姨妈数据表
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MenstruationDBHelper extends SQLiteOpenHelper {
    public static final String TB_NAME_MT= "menstruation";//姨妈流量
    public static final String TB_NAME_MT_CYCLE= "menstruation_cycle";//循环周期
    public static final String TB_NAME_MT_TIME= "menstruation_time";//天数
    public MenstruationDBHelper(Context context){
        super(context, "xmjk.db", null, 1);//数据库
    }
    public MenstruationDBHelper( Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "xmjk.db", factory, 1);//数据库
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        大姨妈平均周期与天数
         */
        String sql = "CREATE TABLE IF NOT EXISTS " +
                TB_NAME_MT_CYCLE+ " ( " +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " number  INTEGER, " + //月经天数
                " cycle INTEGER " + //月经周期
                " )";
        db.execSQL(sql);

        /*
        大姨妈开始与结束时间
        月份，开始时间，结束时间，周期，天数
         */
        String sql1 = "create table if not exists "+TB_NAME_MT_TIME +"(id integer primary key autoincrement,date integer,startTime integer,endTime integer,cycle integer,number integer)";
        db.execSQL(sql1);

        /*
        大姨妈流量痛经表
        日期，月经流量程度，痛经程度
         */
        String sql2 = "create table if not exists "+TB_NAME_MT +"(id integer primary key autoincrement,date integer,quantity integer,pain integer)";
        db.execSQL(sql2);
    }


    //更新
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TB_NAME_MT );
        onCreate(db);
    }
}
