package me.cjds.ambrosia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LoginActivity extends Activity implements View.OnClickListener{


    SharedPreferences sharedPreferences ;
    Button sign_up_button;
    TextView username;
    TextView password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(sharedPreferences.getString("username", "")==""){
            Intent intent=new Intent(this,SearchActivity.class);
            startActivity(intent );
        }

        sign_up_button=(Button)findViewById(R.id.signupbutton);
        sign_up_button.setOnClickListener(this);
         username=(TextView)findViewById(R.id.signupname);
         password=(TextView)findViewById(R.id.signuppassword);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public void onClick(View v) {
        String username=this.username.getText().toString();
        String password=this.password.getText().toString();

        //HTTP Access
        int user_id=1;

        Intent intent=new Intent(this,SearchActivity.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id",user_id);
        editor.putString("username",username);
        editor.putString("password",password);
        startActivity(intent);
    }
}
