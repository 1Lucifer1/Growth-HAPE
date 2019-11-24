package com.example.timetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    /*
    String taskName;
    String taskTime;//00:00
    int imageId;//int
    boolean done;
    boolean repeat;
     */

    public static final String CREATE_TASK = "create table TaskTable("
            +"id integer primary key autoincrement ,"
            +"task_name text,"
            +"hour int,"
            +"min int,"
            +"image_id int,"
            +"done boolean,"
            +"repeat boolean)";

    public static final String CREATE_LEVEL = "create table level_table("
            + "level int)";

    public static final String CREATE_HISTORY = "create table history_table("
            + "date int,"
            + "ids_str text)";

    public static final String HISTORY_TABLE_NAME = "history_table";
    public static final String LEVEL_TABLE_NAME = "level_table";
    public static final String TABLE_NAME = "TaskTable";

    //带全部参数的构造函数，此构造函数必不可少,name为数据库名称
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK);
        db.execSQL(CREATE_LEVEL);
        db.execSQL(CREATE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion){
            case 1:
            case 2:
                db.execSQL(CREATE_LEVEL);
            case 3:
                db.execSQL(CREATE_HISTORY);
                break;
        }
    }
}
