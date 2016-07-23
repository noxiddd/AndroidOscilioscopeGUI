package com.example.jevaughnsdixon.androidgraphingui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button;
    LineChart lineChart,lineChart2;
    ArrayList<Entry> entries=new ArrayList<>();
    boolean show_data_points=false;
    float MaxYValue=40;
    float MinYValue=-40;
    float x=0;
    float y=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lineChart=(LineChart) findViewById(R.id.chart);

        ////////////////////////////////////////////////

        /////////////////////////////////////////////////////
       final LineDataSet dataSet=new LineDataSet(graphing(),"Dataers");
        //dataSet.setCubicIntensity(1f);

        final LineData data=new LineData(dataSet);
        data.setValueTextColor(Color.WHITE);
        lineChart.setBackgroundColor(Color.BLACK);
        /////////////////////////////////////////


        //lineChart.getaxis

        Legend legend=lineChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setForm(Legend.LegendForm.CIRCLE);

        XAxis xAxis=lineChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAvoidFirstLastClipping(true);



        YAxis yAxis=lineChart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setAxisMaxValue(MaxYValue);//max voltage
        yAxis.setAxisMinValue(MinYValue);


        lineChart.setData(data);

        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"DATA POINTS",Toast.LENGTH_LONG).show();
                if(show_data_points==true)
                { dataSet.setDrawCircles(false);}
                else if(show_data_points==false)
                { dataSet.setDrawCircles(true);}

                dataSet.notifyDataSetChanged();

                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            }
        });

    }
/*
    @Override
    protected void onResume()
    {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<=100;i++)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        });
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.single_view_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.double_channel:
                Toast.makeText(MainActivity.this, "DOUBLE CHANNEL", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.separate_double_channel:
                Toast.makeText(MainActivity.this, "SEPARATE", Toast.LENGTH_SHORT).show();
                Intent separate_view = new Intent(MainActivity.this,TwoChannels.class);
                startActivity(separate_view);
                return true;
            case R.id.splash:
                Toast.makeText(MainActivity.this, "SPLASH", Toast.LENGTH_SHORT).show();
                Intent Splash_view = new Intent(MainActivity.this,Splash.class);
                startActivity(Splash_view);
                return true;    
             default:
                 return super.onOptionsItemSelected(menuItem);
        }
    }

    ArrayList<Entry> graphing()
    {
        while(x<50)
        {
            entries.add(new Entry(x,y));
            x=x+0.1f;
            y=(float)Math.sin(x)*20;
        }
        return entries;
    }
}
