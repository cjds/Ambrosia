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


public class SearchActivity extends ListActivity implements SearchView.OnQueryTextListener{

    ArrayList<Item> values = new ArrayList<Item>();
    ArrayList<Item> searchvalues = new ArrayList<Item>();
    ArrayAdapter<Item> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        for(int i=0;i<10;i++)
            values.add(new Item(i, String.valueOf((char)(65+i))+"Item"+i,"this is a pointless descripiton",R.drawable.paper));

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
        Item i=searchvalues.get(position);
        Bundle b =new Bundle();
        b.putAll(i.getBundleVersion());
        Intent intent=new Intent(this,RecipeActivity.class);

        startActivity(intent, b);
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
            Log.v("getSectionForPosition", "called");
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
