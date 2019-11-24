package com.example.timetable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {
    SoundPool soundPool;
    private boolean choosen=false;
    int click_soundEffect;
    int successSound;
    private List<Pet> pets;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView petImage;
        View petView;
        TextView petName;
        public ViewHolder(View view){
            super(view);
            petView=view;
            petImage=(ImageView)view.findViewById(R.id.桌宠图片);
            petName=(TextView)view.findViewById(R.id.桌宠名称);
        }
    }
    public PetAdapter(List<Pet> petList){
        pets=petList;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        soundPool=new SoundPool(10,AudioManager.STREAM_SYSTEM,5);
        click_soundEffect=soundPool.load(parent.getContext(),R.raw.m_click,0);
        successSound=soundPool.load(parent.getContext(),R.raw.m_success_one,1);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.petImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(click_soundEffect,1,1,0,0, (float) 1.5);
                int position=holder.getAdapterPosition();
                final Pet pet=pets.get(position);
                AlertDialog.Builder dialog=new AlertDialog.Builder(parent.getContext());
                dialog.setTitle(pet.getName());
                dialog.setMessage("\n您是想领取它的幼芽回去吗？");
                dialog.setNegativeButton("不", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setPositiveButton("嗯", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        soundPool.play(successSound,1,1,1,0,(float)2.5);
                        MainView.petType=pet.getType();
                        MainView.statusChanged=true;
                        AlertDialog.Builder dialog2=new AlertDialog.Builder(parent.getContext());
                        dialog2.setTitle("幼芽交给你啦");
                        dialog2.setMessage("\n按时完成任务让它茁壮成长吧~！");
                        dialog2.setPositiveButton("好~", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                soundPool.play(successSound,1,1,0,0, (float) 2.5);
                            }
                        });
                        dialog2.show();
                    }
                });
                dialog.show();

            }

        });

        return holder;
    }
    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Pet pet=pets.get(position);
        holder.petName.setText(pet.getName());
        holder.petImage.setImageResource(pet.getImageID());
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }
}
