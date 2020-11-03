package com.example.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;


/*  *String taskName;
 *String taskTime;//00:00
 *int imageId;//int
 *boolean done;
 *boolean repeat;


 *public static final String CREATE_TASK = "create table Thing("
 *+"id integer primary key autoincrement,"
 *+"task_name text,"
 *+"hour int,"
 *+"min int,"
 *+"image_id int,"
 *+"done boolean,"
 *+"repeat boolean)";
 */

public class TaskDataBase {
    private final int VERSION = 3;
    private DataBaseHelper dbHelper; //helper
    private Context context; //context
    private SQLiteDatabase taskTables; //database
    private static final String DATA_BASE_NAME = "TaskDataBase.bd"; //name
    private static TaskDataBase taskDataBase; //class
    private ArrayList<Task> taskArrayList; //return list
    private int level = -1; //return level
    private ArrayList<Task> historyTaskArrayList;

    /**construct database
     * @param context
     **/
    private TaskDataBase(Context context){
        this.context = context;
        dbHelper = new DataBaseHelper(context,DATA_BASE_NAME,null,VERSION);
        taskTables = dbHelper.getWritableDatabase();
        LoadData loadData = new LoadData();
        loadData.run();
    }

    /**
     * provide level
     * @return current level
     */
    public int getLevel(){
        Cursor cursor =taskTables.query(DataBaseHelper.LEVEL_TABLE_NAME,null,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                level = cursor.getInt(cursor.getColumnIndex("level"));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return level;
    }

    /**
     * set variable level
     * @param level
     */
    public void setLevel(int level) {
        if(getLevel()==-1){
            levelCreate(level);
        }else{
            ContentValues contentValues = new ContentValues();
            contentValues.put("level",level);
            taskTables.update(DataBaseHelper.LEVEL_TABLE_NAME, contentValues,
                    "level = ?", new String[]{String.valueOf(level)});
        }
        this.level = level;
    }

    /**
     * create level
     */
    private void levelCreate(int level){
        ContentValues contentValues =new ContentValues();
        contentValues.put("level",level);
        taskTables.insert(DataBaseHelper.LEVEL_TABLE_NAME,null,contentValues);
    }

    /**return database manager
     * @param context
     * @return taskDataBase
     * **/
    public static TaskDataBase getTaskDataBase(Context context){
        if(taskDataBase==null){
            synchronized (TaskDataBase.class){
                if(taskDataBase==null){
                    taskDataBase = new TaskDataBase(context);
                }
            }
        }
        return taskDataBase;
    }

    /**
     * add task in datadbase
     * @param taskAdd the task which need to be added
     * @return new sorted Arraylist which includes all task
     */

    public ArrayList create(Task taskAdd){
        ContentValues contentValues = new ContentValues();
        String taskName = taskAdd.getTaskName();
        String taskTime = taskAdd.getTaskTime();
        int hour;
        int min;
        try{
            hour = Integer.parseInt(taskTime.substring(0,taskTime.indexOf(":")));
            min = Integer.parseInt(taskTime.substring(taskTime.indexOf(":") + 1));
        }catch(Exception e){
            hour = 0;
            min = 0;
        }
        int imageId = taskAdd.getImageId();
        boolean done = taskAdd.isDone();
        boolean repeat = taskAdd.isRepeat();

        //contentValues input
        contentValues.put("task_name",taskName);
        contentValues.put("hour",hour);
        contentValues.put("min",min);
        contentValues.put("image_id",imageId);
        contentValues.put("done",done);
        contentValues.put("repeat",repeat);

        taskTables.insert(DataBaseHelper.TABLE_NAME,null,contentValues);
        int id = findNewAddTaskId(taskAdd);
        taskAdd.setTaskId(id);
        updateArrayList("add",taskAdd);

        return taskArrayList;
    }

    /**
     * find all elements in table
     * @return ArrayList loaded from database
     */
    public ArrayList retrieve(){
        ArrayList<Task> arrayList= new ArrayList<>();
        Cursor cursor = taskTables.query(DataBaseHelper.TABLE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Task task = cursorFind(cursor);
                arrayList.add(task);
                arrayList = sortArrayList(arrayList);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    /**
     * use cursor find thing
     * @param cursor
     * @return
     */
    private Task cursorFind(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String taskName = cursor.getString(cursor.getColumnIndex("task_name"));
        int hour = cursor.getInt(cursor.getColumnIndex("hour"));
        int min = cursor.getInt(cursor.getColumnIndex("min"));
        String hourstr;
        if(hour<10){
            hourstr = "0" + hour;
        }else{
            hourstr = "" + hour;
        }
        String minstr;
        if(min < 10){
            minstr = "0" + min;
        }else{
            minstr = "" + min;
        }
        int imageId = cursor.getInt(cursor.getColumnIndex("image_id"));
        int done = cursor.getInt(cursor.getColumnIndex("done"));
        int repeat = cursor.getInt(cursor.getColumnIndex("repeat"));
        boolean Bdone = (done==1);
        boolean Brepeat = (repeat==1);
        return new Task(id,taskName,hourstr+":" + minstr,imageId,Bdone,Brepeat);
    }

    /**
     * find taskId in database
     * @param task the task wanting to find
     * @return index in database
     */
    private int findNewAddTaskId(Task task){
        int taskId = 0;
        Cursor cursor = taskTables.query(DataBaseHelper.TABLE_NAME,new String[]{"id"},null,null,null,null,null);
        if(cursor.moveToLast()){
            taskId = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        return taskId;
    }

    /**
     * find task by id
     * @param id task which you want to find
     * @return found task
     */
    private Task findTaskById(int id){
        Task res = new Task("","");
        Cursor cursor = taskTables.query(DataBaseHelper.TABLE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                int taskId = cursor.getInt(cursor.getColumnIndex("id"));
                if(taskId==id){
                    res = cursorFind(cursor);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return res;
    }


    /**
     * modify task in database
     *
     * @return new sorted Arraylist which includes all task
     */
    public ArrayList update(Task taskUp){

        //modify difference
        ContentValues contentValues = new ContentValues();
        contentValues.put("task_name",taskUp.getTaskName());
        String taskTime = taskUp.getTaskTime();
        int hour;
        int min;
        try{
            hour = Integer.parseInt(taskTime.substring(0,taskTime.indexOf(":")));
            min = Integer.parseInt(taskTime.substring(taskTime.indexOf(":") + 1));
        }catch(Exception e){
            hour = 0;
            min = 0;
        }
        contentValues.put("hour",hour);
        contentValues.put("min",min);
        contentValues.put("image_id",taskUp.getImageId());
        contentValues.put("done",taskUp.isDone());
        contentValues.put("repeat",taskUp.isRepeat());

        //upgrade
        if(contentValues.size()==0){
            return taskArrayList;
        }
        taskTables.update(DataBaseHelper.TABLE_NAME,contentValues,
                "id=?",new String[]{String.valueOf(taskUp.getTaskId())});
        updateArrayList("up",taskUp);

        return taskArrayList;
    }

    /**
     *delete task in database
     * @param taskDel the task needed to be deleted
     * @return new sorted Arraylist which includes all task
     */
    public ArrayList delete(Task taskDel){
        taskTables.delete(DataBaseHelper.TABLE_NAME,
                "id = ?",new String[]{String.valueOf(taskDel.getTaskId())});
        updateArrayList("del",taskDel);
        return taskArrayList;
    }


    /**
     * upgrade ArrayList
     * @param action which method use this method
     * @param task the task should be update
     */
    private void updateArrayList(String action,Task task){
        int taskId = task.getTaskId();
        if(action.equals("add")){
            taskArrayList.add(task);
        }else if(action.equals("del")){
            taskArrayList.remove(task);
        }else if(action.equals("up")){
            taskArrayList.remove(taskArrayList.indexOf(task));
            taskArrayList.add(task);
        }
        sortArrayList(taskArrayList);
    }


    /**
     * sort ArrayList
     * @param arrayList ArrayList need to be edit
     */
    private ArrayList sortArrayList(ArrayList<Task> arrayList){
        if(arrayList.size()==0){
            return arrayList;
        }
        Task task = arrayList.get(arrayList.size()-1);
        int taskHour = Integer.parseInt(task.getTaskTime().substring(0,2));
        int taskMin = Integer.parseInt(task.getTaskTime().substring(3));
        int i = 0;
        Task lastTask = arrayList.get(i);
        int lastHour = Integer.parseInt(lastTask.getTaskTime().substring(0,2));
        int lastMin = Integer.parseInt(lastTask.getTaskTime().substring(3));

        for(i = 0;i < arrayList.size();i++){
            if(taskHour<lastHour){
                break;
            }else if(taskHour==lastHour){
                if(taskMin<lastMin){
                    break;
                }else if(taskMin==lastMin){
                    break;
                }
            }
            lastTask = arrayList.get(i);
            lastHour = Integer.parseInt(lastTask.getTaskTime().substring(0,2));
            lastMin = Integer.parseInt(lastTask.getTaskTime().substring(3));
        }

        if(i > 0){
            arrayList.add(i-1,task);
        }else{
            arrayList.add(0,task);
        }

        arrayList.remove(arrayList.size()-1);
        return arrayList;
    }

    public ArrayList getTaskList(){
        return taskArrayList;
    }

    /**
     * get id list in database
     * @param date the number between today and that day
     * @return String tuple
     */
    private String[] getIdList(int date){
        String res = "";
        Cursor cursor = taskTables.query(DataBaseHelper.HISTORY_TABLE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                int tem = cursor.getInt(cursor.getColumnIndex("date"));
                if(date==tem){
                    res = cursor.getString(cursor.getColumnIndex("ids_str"));
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return res.split(";");
    }


    /**get day which they want
     * @param date the number between today and that day
     * @return
     */
    public ArrayList getHistoryTable(int date){
        String[] ids = getIdList(date);
        for(int i = 0;i < ids.length;i++){
            Task task = findTaskById(Integer.parseInt(ids[i]));
            historyTaskArrayList.add(task);
            sortArrayList(historyTaskArrayList);
        }
        return historyTaskArrayList;
    }

    /**
     * set data in database
     * @param date thing need to input
     */
    public void setHistoryTable(int date){
        ContentValues contentValues = new ContentValues();
        ArrayList<Task> arrayList = retrieve();
        String str = "";
        for (int i = 0;i < arrayList.size();i++){
            if(arrayList.get(i).isRepeat()){
                str = str + arrayList.get(i).getTaskId() + ";";
            }
        }
        str = str.substring(1,str.length()-1);
        contentValues.put("date",date);
        contentValues.put("ids_str",str);
        taskTables.insert(DataBaseHelper.HISTORY_TABLE_NAME,null, contentValues);
    }


    /**
     * Thread class
     */
    class LoadData implements Runnable{
        @Override
        public void run() {
            taskArrayList = retrieve();
            getLevel();
        }
    }
}
