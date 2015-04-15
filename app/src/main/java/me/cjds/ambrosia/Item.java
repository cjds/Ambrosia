package me.cjds.ambrosia;

import android.os.Bundle;

public class Item {
    int id;
    String title="";
    String description="";
    int image=0; //to be replaced with URL

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
    }

   Bundle getBundleVersion(){
       Bundle b =new Bundle();
       b.putInt("id",id);
       b.putString("title",title);
       b.putString("description",description);
       b.putInt("image",image);
       return b;
   }

}