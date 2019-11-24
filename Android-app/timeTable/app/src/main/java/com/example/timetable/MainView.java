package com.example.timetable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.renderscript.ScriptC;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainView extends Activity {
    private Calendar ca = Calendar.getInstance();
    private int  mYear = ca.get(Calendar.YEAR);
    private int  mMonth = ca.get(Calendar.MONTH);  //月是从0月开始
    private int  mDay = ca.get(Calendar.DAY_OF_MONTH);
    private String returnData1;//任务名
    private String returnData2;//任务日期
    private String returnData3;//开始时间
    //
    long firstClickTime=0;//用来实现双击
    Task task_choosen;//防误触
    private int missionEditedPosition;
    private boolean mission_edited=false;
    private List<Task> taskList=new ArrayList<>();
    private int click_soundEffect;
    private int expG;
    private int expP=0;
    private int[] successSoundEffect=new int[4];
    private TaskAdapter adapter;
    private ListView listView;
    private ImageView petView;
    private int girlCheck = 0;
    private ImageView girlView;
    private SoundPool soundPool;
    private Dialog dialog;
    static boolean statusChanged=false;
    static String editedTaskTime;
    static String editedTaskName;
    static boolean editedTaskStatus=false;
    private long petClick=0;
    public static int petType=3;
    TaskDataBase dataBase;
    private int[]girlImage=new int[6];
    private int[][]petImage=new int[6][5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        //
        dataBase=TaskDataBase.getTaskDataBase(this);
        taskList=dataBase.getTaskList();
//        expG=dataBase.getLevel();
//        dataBase.getTaskList();

        //下面设置顶部状态栏透明
        if(Build.VERSION.SDK_INT >= 21) {//版本5.0以上支持
            View decorView =getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //
        Toast.makeText(getApplicationContext(),"welcome!",Toast.LENGTH_SHORT).show();
        setContentView(R.layout.main_view);
        petView=(ImageView)findViewById(R.id.桌宠);
        petView.setImageResource(R.mipmap.pet31);
//        initTasks();
        initPetImage();
        girlView=(ImageView)findViewById(R.id.girl);
        girlView.setImageResource(R.mipmap.girl1);
        initGirlImage();
        soundPool=new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
        initSuccessSound();
        click_soundEffect=soundPool.load(MainView.this,R.raw.m_click,0);
        //任务列表加载
        getTaskList();
//        adapter = new TaskAdapter(MainView.this, R.layout.task_item, taskList);
//        listView=(ListView)findViewById(R.id.任务列表);
//        listView.setAdapter(adapter);
        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //播放按键音
            @SuppressLint({"ResourceType", "InflateParams"})
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundPool.play(click_soundEffect,1,1,0,0, (float) 1.5);
                Task task=taskList.get(position);
                if (firstClickTime>0&&task.equals(task_choosen)){
                    long secondClickTime=SystemClock.uptimeMillis();
                    long delta=secondClickTime-firstClickTime;
                    if (delta<1200) {
                        //双击事件
                        if (task.getState()) {//啥都不干
                        } else {
                            soundPool.play(successSoundEffect[(int)(Math.random()*4)],1,1,1,0,1);
                            expG++;
                            expP++;
                            expP=expP%10;
//                            dataBase.setLevel(expG);
                            task.setDone(true);
                            task.setImageId(R.mipmap.icon_yes_sec);
                            taskList=dataBase.update(task);
//                           dataBase.update(task);
                            //弹窗显示
                            dialog = new Dialog(MainView.this, R.style.MyDialog);
                            LayoutInflater inflater = (LayoutInflater) MainView.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            dialog.addContentView(inflater.inflate(R.layout.success,null), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            dialog.show();
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            }, 500);
                            petCheck();
                            girlCheck();
                            adapter=new TaskAdapter(MainView.this, R.layout.task_item, taskList);
                            listView.setAdapter(adapter);
                        }
                    }
                    firstClickTime=0;//归零准备下一次

                }else {
                    firstClickTime=SystemClock.uptimeMillis();
                    task_choosen=task;
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainView.this);
                builder.setTitle("编辑");
                builder.setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Task task=taskList.get(position);
                        //
                       editedTaskTime=task.getTaskTime();
                       editedTaskName=task.getTaskName();
                       editedTaskStatus=true;
//                        TextView t = (TextView)findViewById(R.id.default_time);
//                        t.setText(task.getTaskTime());
                        //
                        Intent intent=new Intent(MainView.this,Activity_Dialog.class);
                        startActivityForResult(intent,1);
                        missionEditedPosition=position;
                        mission_edited=true;
                    }
                });
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        taskList.remove(position);
                        taskList=dataBase.delete(taskList.get(position));
//                        dataBase.delete(taskList.get(position));
                        getTaskList();
                    }
                });
                builder.show();
                return false;
            }
        } );

        girlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expG<=2){
                    girlView.setImageResource(girlImage[girlCheck]);
                    Toast.makeText(MainView.this,"不要碰我！QAQ",Toast.LENGTH_SHORT).show();
                }else if(expG<=4){
                    girlView.setImageResource(girlImage[girlCheck-1]);
                    Toast.makeText(MainView.this,"快学习啦！OAO",Toast.LENGTH_SHORT).show();
                }else if (expG<=6){
                    girlView.setImageResource(girlImage[girlCheck-1]);
                    Toast.makeText(MainView.this,"你想要对我说什么=v=",Toast.LENGTH_SHORT).show();
                }else if (expG<=8){
                    girlView.setImageResource(girlImage[girlCheck-1]);
                    Toast.makeText(MainView.this,"作业写完了吗？欧尼酱",Toast.LENGTH_SHORT).show();
                }else{
                    girlView.setImageResource(girlImage[girlCheck]);
                    Toast.makeText(MainView.this,"一起来玩吧~~",Toast.LENGTH_SHORT).show();
                }


            }
        });
        //桌宠说的话
        final String[] petSays=new String[10];
        petSays[0]="快去学习！";
        petSays[1]="今天学了多少东西呀？";
        petSays[2]="勤学如春起之苗，不见其增,日有所长";
        petSays[3]="再点我一次试试看呐";
        petSays[4]="再点我一下可以选择你想要种植的作物哦";
        petSays[5]="再点我一下可以选择你想要种植的作物哦";
        petSays[6]="快去学习！";
        petSays[7]="学习累了话，出去走走休息休息吧！";
        petSays[8]="作业写完了吗？";
        petSays[9]="任务完成了记得点两次打勾哦";
        petView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(click_soundEffect,1,1,0,0, (float) 1.5);
                petClick++;
                if (petClick%2==0){
                        Intent intent=new Intent(MainView.this,Choose_pet.class);
                        startActivity(intent);
                    }
                else {
                    Toast.makeText(MainView.this,petSays[(int)(Math.random()*10)],Toast.LENGTH_LONG).show();
                }
            }
        });

        //
        Button btnAdd = (Button) findViewById(R.id.add_button);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.add_button:
                        Intent intent = new Intent(MainView.this, Activity_Dialog.class);
                        startActivityForResult(intent,1);
                        break;
                    default:
                }
            }
        });

        Button btnDataPicker = (Button) findViewById(R.id.date_button);
        btnDataPicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.date_button:

                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // TODO Auto-generated method stub
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                            }
                        };

                        new DatePickerDialog(MainView.this, onDateSetListener, mYear,
                                mMonth, mDay).show();

                        Log.d("MainActivity","日期："+ String.valueOf(mYear) +"/"+ String.valueOf(mMonth+1)+"/"+ String.valueOf(mDay));
                        break;
                    default:
                }
            }
        });
        //
        // 下面设置顶部日期
        Calendar c = Calendar.getInstance();
        TextView textView = (TextView) findViewById(R.id.顶部日期);
        textView.setText((c.get(Calendar.MONTH)+1+"月"+c.get(Calendar.DATE)+"日"));
        //
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case 1:
                if (resultCode==RESULT_OK){
//                    String returnData1;//任务名
//                    String returnData2;//任务日期
//                    String returnData3;//开始时间
                    returnData1 = data.getStringExtra("task_name");
                    returnData2 = data.getStringExtra("task_class");
                    returnData3 = data.getStringExtra("time");
                    if (returnData3.length()==4)returnData3="0"+returnData3;
                    Task task=new Task(returnData1,returnData3);
                    if (mission_edited){
                        //如果是编辑，那么直接修改
                        taskList.get(missionEditedPosition).setTaskTime(returnData3);
                        taskList.get(missionEditedPosition).setTaskName(returnData1);
                        taskList=dataBase.update(taskList.get(missionEditedPosition));
//                        dataBase.update(taskList.get(missionEditedPosition));
                        //归零
                        mission_edited=false;

                    }else {
                        //如果不是edited，就直接增加到后面
                        addTasks(task);
                    }
                    statusChanged=true;
//                    Log.d("MainActivity","任务名称： "+returnData1);  //return data
//                    Log.d("MainActivity","任务日期： "+returnData2);
//                    Log.d("MainActivity","开始时间： "+returnData3);
                }
                break;
            default:
                break;
        }
    }
    private void addTasks(Task task){
//        taskList.add(task);
        taskList=dataBase.create(task);
//        taskList=dataBase.create(task);
    }

    public void initSuccessSound(){
        successSoundEffect[0]=soundPool.load(this,R.raw.m_success_one,1);
        successSoundEffect[1]=soundPool.load(this,R.raw.m_success_two,1);
        successSoundEffect[2]=soundPool.load(this,R.raw.m_success_three,1);
        successSoundEffect[3]=soundPool.load(this,R.raw.m_success_four,1);
    }
    void getTaskList(){
        adapter = new TaskAdapter(MainView.this, R.layout.task_item, taskList);
        listView=(ListView)findViewById(R.id.任务列表);
        listView.setAdapter(adapter);}

    public void initPetImage(){
        //0草莓  1西瓜  2番茄  3玉米   4百合 5郁金香  6铃兰
        petImage[0][0]=R.mipmap.pet_cm01;
        petImage[0][1]=R.mipmap.pet_cm02;
        petImage[0][2]=R.mipmap.pet_cm03;

        petImage[1][0]=R.mipmap.pet11;
        petImage[1][1]=R.mipmap.pet12;
        petImage[1][2]=R.mipmap.pet13;
        petImage[1][3]=R.mipmap.pet14;

        petImage[2][0]=R.mipmap.pet21;
        petImage[2][1]=R.mipmap.pet22;
        petImage[2][2]=R.mipmap.pet23;
        petImage[2][3]=R.mipmap.pet24;

        petImage[3][0]=R.mipmap.pet31;
        petImage[3][1]=R.mipmap.pet32;
        petImage[3][2]=R.mipmap.pet33;
        petImage[3][3]=R.mipmap.pet34;

        petImage[4][0]=R.mipmap.pet41;
        petImage[4][1]=R.mipmap.pet42;
        petImage[4][2]=R.mipmap.pet43;
        petImage[4][3]=R.mipmap.pet44;
        petImage[4][4]=R.mipmap.pet45;

        petImage[5][0]=R.mipmap.pet51;
        petImage[5][1]=R.mipmap.pet52;
        petImage[5][2]=R.mipmap.pet53;
        petImage[5][3]=R.mipmap.pet54;
        petImage[5][4]=R.mipmap.pet55;
    }
    public void initGirlImage(){
        girlImage[0]=R.mipmap.girl1;
        girlImage[1]=R.mipmap.girl2;
        girlImage[2]=R.mipmap.girl3;
        girlImage[3]=R.mipmap.girl5;
        girlImage[4]=R.mipmap.girl5;
        girlImage[5]=R.mipmap.girl7;
    }
    public void girlCheck(){
        if(expG<=0){
            girlView.setImageResource(girlImage[girlCheck]);
        }else if(expG<=3){
            girlCheck =1;
            girlView.setImageResource(girlImage[girlCheck]);
        }else if (expG<=6){
            girlCheck =2;
            girlView.setImageResource(girlImage[girlCheck]);
        }else if (expG<=8){
            girlCheck =3;
            girlView.setImageResource(girlImage[girlCheck]);
        }else{
            girlCheck =5;
            girlView.setImageResource(girlImage[girlCheck]);
        }
    }
    public void petCheck(){
        switch (petType){
            case 0:
                if (expP<=2){
                    petView.setImageResource(petImage[petType][0]);
                }else if (expP<=4){
                    petView.setImageResource(petImage[petType][1]);
                }else if (expP<=8){
                    petView.setImageResource(petImage[petType][2]);
                }
                break;
            case 1:
            case 2:
            case 3:
                if (expP<=2){
                    petView.setImageResource(petImage[petType][0]);
                }else if (expP<= 4) {
                    petView.setImageResource(petImage[petType][1]);
                }else if (expP<=6){
                    petView.setImageResource(petImage[petType][2]);
                }else if (expP<=8){
                    petView.setImageResource(petImage[petType][3]);
                }
                break;
            case 4:
            case 5:
                if (expP<=2){
                    petView.setImageResource(petImage[petType][0]);
                }else if (expP<=3){
                    petView.setImageResource(petImage[petType][1]);
                }else if (expP<=6){
                    petView.setImageResource(petImage[petType][2]);
                }else if (expP<=7){
                    petView.setImageResource(petImage[petType][3]);
                }else if (expP<=8){
                    petView.setImageResource(petImage[petType][4]);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (statusChanged){
            expP=0;
            petCheck();
            getTaskList();
            editedTaskStatus=false;
            statusChanged=false;
        }
    }


}