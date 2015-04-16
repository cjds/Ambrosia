package me.cjds.ambrosia;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeActivity extends Activity {

    public Item i;

    void startNewRecipe(){
        WebHandler webHandler = new WebHandler();
        webHandler.setOnTaskFinishedEvent(new WebHandler.OnTaskExecutionFinished(){
            @Override
            public void OnTaskFinishedEvent(ArrayList<String> result){
                Log.d("Set New Item: Json Result",result.get(0));
                String json =result.get(0);
                try {

                    if (json.equals("true\n")){

                        //Make button inactive
                        Button button = (Button)findViewById(R.id.fab);
                        button.setEnabled(false);

                        //Start background service
                        startService(new Intent(RecipeActivity.this, NotificationService.class));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ArrayList<String> urlList = new ArrayList<String>();
        urlList.add("http://rosiechef.herokuapp.com/device");
        ArrayList<String> requestList = new ArrayList<String>();
        requestList.add("post");
        ArrayList<String> paramList = new ArrayList<String>();
        paramList.add("sender");
        paramList.add("device_id");
        paramList.add("recipe_id");
        ArrayList<String> valueList = new ArrayList<String>();
        valueList.add("human");
        valueList.add("2");
        valueList.add(String.valueOf(this.i.id));
        webHandler.execute(requestList,urlList,paramList,valueList);
    }

    void updateViewData(){

        //TextView title=new TextView(this);
        //title=(TextView)findViewById(R.id.title_TextView);
        //title.setText(i.title);

        TextView description=new TextView(this);
        description=(TextView)findViewById(R.id.description_TextView);
        description.setText(this.i.description);

        ImageView image= (ImageView) findViewById(R.id.recipe_ImageView);
        switch(this.i.title){
            case "Fried Rice":
                break;
            case "Beef Stew":
                image.setImageResource(R.drawable.beef);
                break;
            case "Tomato Soup":
                image.setImageResource(R.drawable.tomato);
                break;
            case "Fried Chicken Batter":
                image.setImageResource(R.drawable.chicken);
                break;
            case "Corn Bread Batter":
                break;
        }
        TextView steps=new TextView(this);

        String stepsText="";
        for(int j=1;j<this.i.steps.size()+1;j++){
            stepsText+=(j)+". "+ this.i.steps.get(j-1)+"\n\n";
        }
        steps=(TextView)findViewById(R.id.steps_TextView);
        steps.setText(stepsText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.i = new Item(getIntent().getExtras());

        setContentView(R.layout.activity_recipe);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(i.title);

        //Set Listener
        Button button=new Button(this);
        button=(Button)findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tell the server that the user wants to start THIS recipe
                RecipeActivity.this.startNewRecipe();
            }
        });

        //Update Recipe Details
        this.updateViewData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
