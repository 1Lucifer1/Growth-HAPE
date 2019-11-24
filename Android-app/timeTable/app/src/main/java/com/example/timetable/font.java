package com.example.timetable;

import android.app.Application;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public class font extends Application {
    private static font app;
    //调字体

    @Override
    public void onCreate(){
        super.onCreate();
        app = this;

        initTypeface();
    }

    private void initTypeface(){
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/fontone.ttf");

        try{
            Field field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null,typeface);
        }catch(NoSuchFieldException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
    }
}
