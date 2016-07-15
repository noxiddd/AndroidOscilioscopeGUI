package com.example.noxid.androidgraphingui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by noxid on 7/14/16.
 */
public class Splash extends AppCompatActivity {
    ImageView iv;
    Button single,separated,double_;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        iv=(ImageView)findViewById(R.id.image);
        iv.setImageResource(R.drawable.oscioscope_pic);
        single=(Button)findViewById(R.id.single_splash);
        separated=(Button)findViewById(R.id.separated_splash);
        double_=(Button)findViewById(R.id.double_splash);
        tv=(TextView)findViewById(R.id.textView);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Splash.this, "SINGLE", Toast.LENGTH_SHORT).show();
                Intent single_view = new Intent(Splash.this,MainActivity.class);
                startActivity(single_view);
            }
        });

        separated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Splash.this, "SEPARATED", Toast.LENGTH_SHORT).show();
                Intent separate_view = new Intent(Splash.this,TwoChannels.class);
                startActivity(separate_view);
            }
        });

        double_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Splash.this, "Double", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
