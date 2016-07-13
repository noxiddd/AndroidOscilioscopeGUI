package com.example.jevaughnsdixon.androidgraphingui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Jevaughn S. Dixon on 7/12/2016.
 */
public class TwoChannels extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.separate_view_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.double_channel:
                Toast.makeText(TwoChannels.this, "DOUBLE CHANNEL", Toast.LENGTH_LONG).show();

                return true;
            case R.id.single_channel:
                Toast.makeText(TwoChannels.this, "SINGLE", Toast.LENGTH_LONG).show();
                Intent separate_view = new Intent(TwoChannels.this,MainActivity.class);
                startActivity(separate_view);
                return true;
            default:

                return super.onOptionsItemSelected(menuItem);
        }
    }
}
