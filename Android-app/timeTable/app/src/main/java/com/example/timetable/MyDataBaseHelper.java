package com.example.timetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseHelper extends SQLiteOpenHelper {
    /*
    String taskName;
    String taskTime;//00:00
    int imageId;//int
    boolean done;
    boolean repeat;
     */

    public static final String CREATE_TASK = "create table TaskTable("
            +"id long,"
            +"task_name text,"
            +"hour int,"
            +"min int,"
            +"image_id int,"
            +"done boolean,"
            +"repeat boolean)";

    public static final String TABLE_NAME = "TaskTable";

    //带全部参数的构造函数，此构造函数必不可少,name为数据库名称
    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
