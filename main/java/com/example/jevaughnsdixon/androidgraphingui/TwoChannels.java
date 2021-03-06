package com.example.noxid.androidgraphingui;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by Jevaughn S. Dixon on 7/12/2016.
 */
public class TwoChannels extends AppCompatActivity {
    Button pause1,pause2,save1,save2;
    LineChart lineChart,lineChart2;
    ArrayList<Entry> entries=new ArrayList<>();
    boolean show_data_points=false;
    float MaxYValue=40;
    float MinYValue=-40;
    float x=0;
    float y=0;
    int graph1count=0;
    int graph2count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        lineChart=(LineChart) findViewById(R.id.chart);
        lineChart2=(LineChart)findViewById(R.id.chart2);
        pause1=(Button)findViewById(R.id.button_pause);
        pause2=(Button)findViewById(R.id.button_pause2);
        save1=(Button)findViewById(R.id.button_save);
        save2=(Button)findViewById(R.id.button_save2);

        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(graph1count==0)
                {lineChart.saveToGallery("UpperGraph",60);}
                else
                {
                    graph1count++;
                    lineChart.saveToGallery("UpperGraph"+graph1count,60);
                }
                Toast.makeText(TwoChannels.this, "Upper Graph Saved To Gallery", Toast.LENGTH_LONG).show();
            }
        });

        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(graph2count==0)
                {lineChart2.saveToGallery("LowerGraph",50);}
                else
                {
                    graph2count++;
                    lineChart2.saveToGallery("LowerGraph"+graph2count,50);
                }
                Toast.makeText(TwoChannels.this, "Lower Graph Saved To Gallery", Toast.LENGTH_LONG).show();

            }
        });


        LineDataSet dataSet=new LineDataSet(graphing(),"Dataaa");

        final LineData data=new LineData(dataSet);
        data.setValueTextColor(Color.WHITE);
        lineChart.setBackgroundColor(Color.BLACK);
        lineChart2.setBackgroundColor(Color.BLACK);
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
        lineChart2.setData(data);

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
                
           case R.id.splash:
                Toast.makeText(TwoChannels.this, "SPLASH", Toast.LENGTH_SHORT).show();
                Intent Splash_view = new Intent(TwoChannels.this,Splash.class);
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
