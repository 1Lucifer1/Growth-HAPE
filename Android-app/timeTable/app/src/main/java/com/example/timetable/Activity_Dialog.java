package com.example.timetable;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Activity_Dialog extends Activity {
    private Calendar ca = Calendar.getInstance();
    private EditText editText1;
    private RadioGroup rg;
    private RadioButton rb_learn, rb_other;
    private boolean rb_l = false;
    private boolean rb_o = false;
    private String time;
    Intent intent = new Intent();
    TextView timeShown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task2);
        //编辑显示时间
        editText1 = (EditText) findViewById(R.id.text_name);
        timeShown=(TextView) findViewById(R.id.default_time);
        if (MainView.editedTaskStatus){
            MainView.editedTaskStatus=false;
            timeShown.setText(MainView.editedTaskTime);
            editText1.setText(MainView.editedTaskName);
        }
        //


        rg = (RadioGroup) findViewById(R.id.rg_sex);
        rb_learn = (RadioButton) findViewById(R.id.rb_learn);
        rb_other = (RadioButton) findViewById(R.id.rb_other);
        rg.setOnCheckedChangeListener(new MyRadioButtonListener());

        Button btnDone = (Button) findViewById(R.id.done_button);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText1.getText().toString().equals("")){
                    finish();
                }else if ((rb_o==false&&rb_l==false)&&time==null){
                    String task_name = editText1.getText().toString();
                    intent.putExtra("task_class","今天");
                    setResult(RESULT_OK, intent);
                    intent.putExtra("task_name",task_name);
                    setResult(RESULT_OK, intent);
                    intent.putExtra("time","          ");
                    setResult(RESULT_OK, intent);
                    finish();  //ca.get(Calendar.HOUR)+":"+ca.get(Calendar.MINUTE)
                }else if ((rb_o!=false||rb_l!=false)&&time==null){
                    String task_name = editText1.getText().toString();
                    String task_class;
                    if (rb_o) task_class = rb_other.getText().toString();
                    else task_class = rb_learn.getText().toString();
                    intent.putExtra("task_class",task_class);
                    setResult(RESULT_OK, intent);
                    intent.putExtra("task_name",task_name);
                    setResult(RESULT_OK, intent);
                    intent.putExtra("time","          ");
                    setResult(RESULT_OK, intent);
                    finish();  //ca.get(Calendar.HOUR)+":"+ca.get(Calendar.MINUTE)
                } else if((rb_o==false&&rb_l==false)&&time!=null){
                    String task_name = editText1.getText().toString();
                    intent.putExtra("task_class","今天");
                    setResult(RESULT_OK, intent);
                    intent.putExtra("task_name",task_name);
                    setResult(RESULT_OK, intent);
                    intent.putExtra("time",time);
                    setResult(RESULT_OK, intent);
                    finish();
                }else if ((rb_o!=false||rb_l!=false)&&time!=null){
                    String task_name = editText1.getText().toString();
                    String task_class;
                    if (rb_o) task_class = rb_other.getText().toString();
                    else task_class = rb_learn.getText().toString();
                    //Intent intent = new Intent();
                    intent.putExtra("task_class", task_class);
                    setResult(RESULT_OK, intent);
                    intent.putExtra("task_name", task_name);
                    setResult(RESULT_OK, intent);
                    intent.putExtra("time",time);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });


        Button btnTime = (Button) findViewById(R.id.time_button);
        final TimePickerDialog[] dialog = new TimePickerDialog[1];
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(Activity_Dialog.this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        if (minute < 10) {
                            time = String.valueOf(hour) + ":" + "0" + String.valueOf(minute);
                        } else {
                            time = String.valueOf(hour) + ":" + String.valueOf(minute);
                        }
                        timeShown.setText(time);
                    }
                }, 0, 0, true).show();
            }
        });
    }




    public void onBackPressed(){
        if (editText1.getText().toString().equals("")||(rb_o==false&&rb_l==false)||time==null){
            finish();
        }else{
            String task_name = editText1.getText().toString();

            String task_class;
            if (rb_o) task_class = rb_other.getText().toString();
            else task_class = rb_learn.getText().toString();

            intent.putExtra("task_class",task_class);
            setResult(RESULT_OK,intent);
            intent.putExtra("task_name",task_name);
            setResult(RESULT_OK,intent);
            intent.putExtra("time",time);
            setResult(RESULT_OK,intent);
            finish();
        }
    }


    class MyRadioButtonListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 选中状态改变时被触发
            switch (checkedId) {
                case R.id.rb_other:
                    rb_o = true;
                    break;
                case R.id.rb_learn:
                    rb_l = true;
                    break;
                default:
            }
        }
    }
}
