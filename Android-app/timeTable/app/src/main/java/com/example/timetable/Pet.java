package com.example.timetable;

public class Pet {
    private String name;
    private int type;
    private int imageID;
    public Pet(String name,int type,int imageID){
        this.name=name;
        this.type=type;
        this.imageID=imageID;
    }
    public int getType(){return type;}
    public String getName(){return name;}
    public int getImageID(){return imageID;}
}
