package com.example.jevaughnsdixon.androidgraphingui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    LineChart lineChart,lineChart2;
    ArrayList<Entry> entries=new ArrayList<>();
    boolean show_data_points=false;
    float MaxYValue=40;
    float MinYValue=-40;
    float x=0;
    float y=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        lineChart=(LineChart) findViewById(R.id.chart);
        lineChart2=(LineChart)findViewById(R.id.chart2);
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
