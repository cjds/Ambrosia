package me.cjds.ambrosia;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Item {
    int id;
    String title="";
    String description="";
    int image=0; //to be replaced with URL
    int time=0;
    List<String> steps=new ArrayList<String>();

    public Item (int id, String title, String description,int image){
        this.id=id;;
        this.title=title;
        this.description=description;
        this.image=image;

    }

    public Item(Bundle b){
        this.id=b.getInt("id",-1);
        this.title=b.getString("title","");
        this.description=b.getString("description","");
        this.image=b.getInt("image",-1);
        this.steps=b.getStringArrayList("steps");
    }

   Bundle getBundleVersion(){
       Bundle b =new Bundle();
       b.putInt("id",id);
       b.putString("title",title);
       b.putString("description",description);
       b.putInt("image",image);
       //b.putStringArrayList("steps",steps);
       return b;
   }

}