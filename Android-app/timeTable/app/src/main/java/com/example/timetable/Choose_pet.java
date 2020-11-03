package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class Choose_pet extends Activity {
    private List<Pet>pets=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //下面设置顶部状态栏透明
        if(Build.VERSION.SDK_INT >= 21) {//版本5.0以上支持
            View decorView =getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //
        setContentView(R.layout.activity_choose_pet);
        initPets();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view_choosePet);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        PetAdapter adapter=new PetAdapter(pets);
        recyclerView.setAdapter(adapter);
    }
    public void initPets(){
        Pet straberry=new Pet("草莓",0,R.mipmap.pet_cm00);
        Pet watermelon=new Pet("西瓜",1,R.mipmap.pet10);
        Pet tomato=new Pet("番茄",2,R.mipmap.pet20);
        Pet yumi=new Pet("玉米",3,R.mipmap.pet30);
        Pet baihe=new Pet("百合",4,R.mipmap.pet40);
        Pet yujinxiang=new Pet("郁金香",5,R.mipmap.pet50);
        pets.add(straberry);
        pets.add(watermelon);
        pets.add(tomato);
        pets.add(yumi);
        pets.add(baihe);
        pets.add(yujinxiang);
    }

}
