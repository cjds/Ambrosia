package me.cjds.ambrosia;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.widget.SearchView.OnCloseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchActivity extends ListActivity implements SearchView.OnQueryTextListener{

    ArrayList<Item> values = new ArrayList<Item>();
    ArrayList<Item> searchvalues = new ArrayList<Item>();
    ArrayAdapter<Item> adapter;



    //Testing WebHandler.java
    public void getAllRecipes(){
        WebHandler webHandler = new WebHandler();

        webHandler.setOnTaskFinishedEvent(new WebHandler.OnTaskExecutionFinished(){
            @Override
            public void OnTaskFinishedEvent(ArrayList<String> result){
                Log.d("Json Result",result.get(0));
                String json =result.get(0);
                try {
                    JSONArray resultArray = new JSONArray(json);

                    values.clear();
                    for (int i=0; i<resultArray.length();i++){
                        JSONObject dict = resultArray.getJSONObject(i);
                        int drawable=R.drawable.paper;
                        switch(dict.getString("name")){
                            case "Fried Rice":
                                break;
                            case "Beef Stew":
                                drawable=R.drawable.beefround;
                                break;
                            case "Tomato Soup":
                                drawable=R.drawable.soup;
                                break;
                            case "Fried Chicken Batter":
                                drawable=R.drawable.chickenround;
                                break;
                            case "Corn Bread Batter":
                                break;
                        }

                        Item item=new Item(dict.getInt("id"), dict.getString("name"),dict.getString("description"),drawable);
                        JSONArray steps=dict.getJSONArray("steps");

                        for (int j=0; j<steps.length();j++){

                          item.steps.add(steps.getJSONObject(j).getString("step_title"));
                        }
                        values.add(item);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                //Update
                Log.d("Update List","");
                adapter.notifyDataSetChanged();
            }
        });

        ArrayList<String> urlList = new ArrayList<String>();
        urlList.add("http://rosiechef.herokuapp.com/recipe");
        ArrayList<String> requestList = new ArrayList<String>();
        requestList.add("get");
        ArrayList<String> paramList = new ArrayList<String>();
//        urlList.add("");
        ArrayList<String> valueList = new ArrayList<String>();
//        urlList.add("");
        webHandler.execute(requestList,urlList,paramList,valueList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.getAllRecipes();

        Intent intent = getIntent();
        searchvalues=values;
        adapter = new MyAZAdapter<Item>(this,R.layout.listitem,values);
        setListAdapter(adapter);

    }

    public void doMySearch(String query){
        searchvalues = new ArrayList<Item>();

        for(Item value : values){
            if(value.title.toLowerCase().contains(query.toLowerCase())){
                searchvalues.add(value);
            }
            else if(value.description.toLowerCase().contains(query.toLowerCase())){
                searchvalues.add(value);
            }
        }
        adapter = new MyAZAdapter<Item>(this,R.layout.listitem,searchvalues);
        setListAdapter(adapter);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Item i= searchvalues.get(position);
        Bundle b = new Bundle();
//        b.putAll(i.getBundleVersion());
        b.putInt("id",i.id);
        b.putString("title",i.title);
        b.putString("description",i.description);
        b.putString("image","");
        b.putStringArrayList("steps",(ArrayList<String>)i.steps);
        Intent intent=new Intent(this,RecipeActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        doMySearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        doMySearch(newText);
        return true;
    }


    class MyAZAdapter<T> extends ArrayAdapter<T> implements SectionIndexer {
        ArrayList<Item> myElements;
        HashMap<String, Integer> azIndexer;
        String[] sections;
        Context context;
        int layoutResourceId;

        public MyAZAdapter(Context context,int layoutID, List<T> objects) {
            super(context, layoutID, objects);
            myElements = (ArrayList<Item>) objects;
            azIndexer = new HashMap<String, Integer>(); //stores the positions for the start of each letter
            this.layoutResourceId=layoutID;
            this.context=context;
            int size = objects.size();
            for (int i = size - 1; i >= 0; i--) {
                String element = ((String)((Item)objects.get(i)).title);
                //We store the first letter of the word, and its index.
                azIndexer.put(element.substring(0, 1), i);
            }

            Set<String> keys = azIndexer.keySet(); // set of letters

            Iterator<String> it = keys.iterator();
            ArrayList<String> keyList = new ArrayList<String>();

            while (it.hasNext()) {
                String key = it.next();
                keyList.add(key);
            }
            Collections.sort(keyList);//sort the keylist
            sections = new String[keyList.size()]; // simple conversion to array
            keyList.toArray(sections);
        }




        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ItemHolder holder = null;

            if(row == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new ItemHolder();
                holder.icon = (ImageView)row.findViewById(R.id.ItemIcon);
                holder.title = (TextView)row.findViewById(R.id.ItemTitleText);
                holder.description = (TextView)row.findViewById(R.id.ItemDescriptionText);

                row.setTag(holder);
            }
            else
            {
                holder = (ItemHolder)row.getTag();
            }

            Item recipe = myElements.get(position);
            holder.title.setText(recipe.title);
            holder.description.setText(recipe.description);
            holder.icon.setImageResource(recipe.image);

            return row;
        }

        public int getPositionForSection(int section) {
            String letter = sections[section];
            return azIndexer.get(letter);
        }

        public int getSectionForPosition(int position) {
            return 0;
        }

        public Object[] getSections() {
            return sections; // to string will be called to display the letter
        }

        public class ItemHolder {
            ImageView icon ;
            TextView title;
            TextView description;

        }

    }
}
