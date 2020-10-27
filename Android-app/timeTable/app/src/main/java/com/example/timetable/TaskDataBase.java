package com.example.timetable;

import android.content.ContentValues;
import android.content.Context;
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
    private MyDataBaseHelper dbHelper; //helper
    private Context context; //context
    private SQLiteDatabase taskTables; //database
    private static final String DATA_BASE_NAME = "TaskDataBase.bd"; //name
    private static TaskDataBase taskDataBase; //class
    private ArrayList<Task> taskArrayList; //return list


    /**construct database
     * @param context
    **/
    private TaskDataBase(Context context){
        this.context = context;
        dbHelper = new MyDataBaseHelper(context,DATA_BASE_NAME,null,1);
        taskTables = dbHelper.getWritableDatabase();
        taskArrayList = new ArrayList<>();
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
        Long id = taskAdd.getTaskId();
        String taskName = taskAdd.getTaskName();
        String taskTime = taskAdd.getTaskTime();
        int hour = Integer.parseInt(taskTime.substring(0,2));
        int min = Integer.parseInt(taskTime.substring(3));
        int imageId = taskAdd.getImageId();
        boolean done = taskAdd.isDone();
        boolean repeat = taskAdd.isRepeat();


        //contentValues input
        contentValues.put("id",id);
        contentValues.put("task_name",taskName);
        contentValues.put("hour",hour);
        contentValues.put("min",min);
        contentValues.put("image_id",imageId);
        contentValues.put("done",done);
        contentValues.put("repeat",repeat);

        taskTables.insert(MyDataBaseHelper.TABLE_NAME,null,contentValues);
        updateArrayList("add",taskAdd);

        return taskArrayList;
    }

    /**
     * find task in database
     * @param task the task wanting to find
     * @return index in database
     */
    private Long retrieve(Task task){
        Long taskId = 0L;

        return taskId;
    }

    /**
     * modify task in database
     * @param taskUp the task needed to be update
     * @return new sorted Arraylist which includes all task
     */
    public ArrayList update(Task taskUp){
        //find in ArrayList
        Task taskOld;
        int i = 0;
        while(!taskArrayList.get(i).getTaskId().equals(taskUp.getTaskId())){
            i++;
        }
        taskOld = taskArrayList.get(i);

        //compare difference
        ContentValues contentValues = new ContentValues();
        if(!taskOld.getTaskName().equals(taskUp.getTaskName())){
            contentValues.put("task_name",taskUp.getTaskName());
        }
        if(!taskOld.getTaskTime().equals(taskUp.getTaskTime())){
            String taskTime = taskUp.getTaskTime();
            int hour = Integer.parseInt(taskTime.substring(0,2));
            int min = Integer.parseInt(taskTime.substring(3));
            contentValues.put("hour",hour);
            contentValues.put("min",min);
        }
        if(!(taskOld.isDone()==taskUp.isDone())){
            contentValues.put("done",taskUp.isDone());
        }
        if(!(taskOld.isRepeat()==taskUp.isRepeat())){
            contentValues.put("repeat",taskUp.isRepeat());
        }
        if(!(taskOld.getImageId()==taskUp.getImageId())){
            contentValues.put("image_id",taskUp.getImageId());
        }

        //upgrade
        taskTables.update(MyDataBaseHelper.TABLE_NAME,contentValues,
                "id = ?",new String[]{String.valueOf(taskUp.getTaskId())});
        updateArrayList("up",taskUp);
        return taskArrayList;
    }

    /**
     *delete task in database
     * @param taskDel the task needed to be deleted
     * @return new sorted Arraylist which includes all task
     */
    public ArrayList delete(Task taskDel){
        taskTables.delete(MyDataBaseHelper.TABLE_NAME,
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
        Long taskId = task.getTaskId();
        if(action.equals("add")){
            taskArrayList.add(task);
        }else if(action.equals("del")){
            taskArrayList.remove(task);
        }else if(action.equals("up")){
            int i = 0;
            while(!taskArrayList.get(i).getTaskId().equals(task.getTaskId())){
                i++;
            }
            taskArrayList.remove(taskArrayList.get(i));
            taskArrayList.add(task);
        }
        sortArrayList();
    }

    /**
     * sort ArrayList
     */
    private void sortArrayList(){
        Task task = taskArrayList.get(taskArrayList.size()-1);
        int taskHour = Integer.parseInt(task.getTaskTime().substring(0,2));
        int taskMin = Integer.parseInt(task.getTaskTime().substring(3));
        int i = 0;
        Task lastTask = taskArrayList.get(i);
        int lastHour = Integer.parseInt(lastTask.getTaskTime().substring(0,2));
        int lastMin = Integer.parseInt(lastTask.getTaskTime().substring(3));

        while(!lastTask.equals(task)){
            if(taskHour<lastHour){
                break;
            }else if(taskHour==lastHour){
                if(taskMin<lastMin){
                    break;
                }
            }else{
                i++;
                lastTask = taskArrayList.get(i);
                lastHour = Integer.parseInt(lastTask.getTaskTime().substring(0,2));
                lastMin = Integer.parseInt(lastTask.getTaskTime().substring(3));
            }
        }
        taskArrayList.add(i,task);
    }

    public ArrayList getTaskList(){
        return taskArrayList;
    }
}
