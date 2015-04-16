package me.cjds.ambrosia;

import android.app.ActionBar;
import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class RecipeActivity extends Activity {


    void updateViewData(Item i){

        //TextView title=new TextView(this);
        //title=(TextView)findViewById(R.id.title_TextView);
        //title.setText(i.title);

        TextView description=new TextView(this);
        description=(TextView)findViewById(R.id.description_TextView);
        description.setText(i.description);


        ImageView image= (ImageView) findViewById(R.id.recipe_ImageView);
        switch(i.title){

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
        for(int j=1;j<i.steps.size()+1;j++){
            stepsText+=(j)+". "+ i.steps.get(j-1)+"\n\n";
        }

        steps=(TextView)findViewById(R.id.steps_TextView);

        steps.setText(stepsText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Item i = new Item(getIntent().getExtras());

        setContentView(R.layout.activity_recipe);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(i.title);
        this.updateViewData(i);
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
