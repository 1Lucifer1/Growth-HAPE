package com.example.timetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Start extends Activity {
    ImageView startImage;
    int[] pictures;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        startImage=(ImageView)findViewById(R.id.启动背景);
        start();
    }
    void start(){
        int pic=(int)(Math.random()*32);
        initpictures();
        startImage.setImageResource(pictures[pic]);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(Start.this,MainView.class);
                startActivity(intent);
                finish();
            }
        },3*1000);
    }
    void initpictures(){
        pictures=new int[32];
        pictures[0]=R.mipmap.a0;
        pictures[1]=R.mipmap.a1;
        pictures[2]=R.mipmap.a2;
        pictures[3]=R.mipmap.a3;
        pictures[4]=R.mipmap.a4;
        pictures[5]=R.mipmap.a5;
        pictures[6]=R.mipmap.a6;
        pictures[7]=R.mipmap.a7;
        pictures[8]=R.mipmap.a8;
        pictures[9]=R.mipmap.a9;
        pictures[10]=R.mipmap.a10;
        pictures[11]=R.mipmap.a11;
        pictures[12]=R.mipmap.a12;
        pictures[13]=R.mipmap.a13;
        pictures[14]=R.mipmap.a14;
        pictures[15]=R.mipmap.a15;
        pictures[16]=R.mipmap.a16;
        pictures[17]=R.mipmap.a17;
        pictures[18]=R.mipmap.a18;
        pictures[19]=R.mipmap.a19;
        pictures[20]=R.mipmap.a20;
        pictures[21]=R.mipmap.a21;
        pictures[22]=R.mipmap.a22;
        pictures[23]=R.mipmap.a23;
        pictures[24]=R.mipmap.a24;
        pictures[25]=R.mipmap.a25;
        pictures[26]=R.mipmap.a26;
        pictures[27]=R.mipmap.a27;
        pictures[28]=R.mipmap.a28;
        pictures[29]=R.mipmap.a29;
        pictures[30]=R.mipmap.a30;
        pictures[31]=R.mipmap.a31;
    }
}
