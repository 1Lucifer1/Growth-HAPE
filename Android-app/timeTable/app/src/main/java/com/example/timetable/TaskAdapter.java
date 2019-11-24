package com.example.timetable;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    private int resourceID;
    public TaskAdapter(Context context, int textViewResourceID, List<Task> objects){
        super(context,textViewResourceID,objects);
        resourceID=textViewResourceID;
    }
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        Task task=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        ImageView taskDoneImage=(ImageView)view.findViewById(R.id.绿钩);
        TextView taskImfomation=(TextView)view.findViewById(R.id.任务时间_名称);
        taskDoneImage.setImageResource(task.getImageId());
        taskImfomation.setText("  "+task.getTaskTime()+"    "+task.getTaskName());
        return view;
    }
}
