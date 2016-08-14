package com.felhr.serialportexample;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
     LineChart lineChart;
    ArrayList<String> xVals=new ArrayList<String>();
    ArrayList<Entry> entries=new ArrayList<>();
    Button button;
    String data_str="";
    int safe_to_split=1;//1 for no and 2 for yes
    float x=0;
    float y=0;
    boolean do_it=false;

    int count=0;
    LineDataSet dataSet;
    LineData data;
    float[] fArr=new float[10000];
    String[] sArr=new String[10000];
    int current_index=0;
    boolean time_start=false;
    long time_started=0;
    long curent_time=0;
    /*
     * Notifications from UsbService will be received here.
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private UsbService usbService;
    private TextView display;
    private EditText editText;
    private MyHandler mHandler;
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart=(LineChart) findViewById(R.id.chart);
        button=(Button)findViewById(R.id.button);
        entries.add(new Entry(x,y));
        dataSet=new LineDataSet(entries,"Channel 1");




        data=new LineData(dataSet);//was final LineData data=new LineData(dataSet);
        data.setValueTextColor(Color.WHITE);
        lineChart.setBackgroundColor(Color.BLACK);

        Legend legend=lineChart.getLegend();
        legend.setTextColor(Color.WHITE);//sets the graph axis numebers to white
        legend.setForm(Legend.LegendForm.CIRCLE);

        XAxis xAxis=lineChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);//sets the graph number to white color
        xAxis.setAvoidFirstLastClipping(true);

        //xAxis.set


        YAxis yAxis=lineChart.getAxisLeft();//set the maximum and minimum voltage for the scope at 40 and -40
        yAxis.setTextColor(Color.WHITE);
        yAxis.setAxisMaxValue(40);//max voltage
        yAxis.setAxisMinValue(-40);

        dataSet.setDrawCircles(false);//removes lar
        lineChart.setVisibleXRangeMaximum(2f);
        lineChart.setData(data);




       /* editText = (EditText) findViewById(R.id.editText1);
        Button sendButton = (Button) findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")) {
                    String data = editText.getText().toString();
                    if (usbService != null) { // if UsbService was correctly binded, Send data
                        usbService.write(data.getBytes());
                    }
                }
            }
        });*/
        display = (TextView) findViewById(R.id.textView1);
        mHandler = new MyHandler(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // UsbService.ACTION_USB_DETACHEDusb
               if(usbService.SERVICE_CONNECTED==true) {
                   usbService.close_port();
                   Toast.makeText(MainActivity.this, "Port Closed", Toast.LENGTH_SHORT).show();
                   button.setText("Connect");
                   usbService.SERVICE_CONNECTED=false;
                   //dataSet.notifyDataSetChanged();
                   lineChart.notifyDataSetChanged();
                   lineChart.invalidate();
                   //lineChart.setVisibleXRange(6,30);
                   //lineChart.moveViewToX(data.getXMax());
               }
               else if(usbService.SERVICE_CONNECTED==false)
               {
                   usbService.open_port();
                   button.setText("Disconnect");
                   usbService.SERVICE_CONNECTED=true;
                   startService(UsbService.class, usbConnection, null);
               }
            }
        });
        //graphing().

    }

    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {///////use regex match to make sure string is numeric
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data_msg = (String) msg.obj;
                    float[] y_value=new float[6];
                    y_value[0]=0;
                    y_value[1]=0;

                    Log.i("Data_before",data_msg);
                    //data_msg=mActivity.get().check_string(data_msg);
                    try {

                        y_value = mActivity.get().fix_string(data_msg);
                        if(mActivity.get().do_it==false)
                        {   break;}
                        mActivity.get().updateChart(y_value[1]);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    Log.i("Data_After",data_msg);


                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

   /* ArrayList<Entry> graphing()//plots the sine graph that goes into both upper and lower graphs
    {
        while(x<50)
        {
            entries.add(new Entry(x,y));
            x=x+0.1f;
            y=(float)Math.sin(x)*20;
        }
        return entries;
    }*/

    float[] stringBuffer(String str,int safe)
    {    Log.i("Reach","stringBuffer method0   "+str+" safe "+safe);
          float[] gf={0,0,0};
        if(safe==1)
        {
            data_str=str;
            Log.i("Reach","stringBuffer method1  "+data_str);
            return gf;
        }
        else if(safe==2){
            Log.i("Reach","stringBuffer method2 "+str);
        try{
            Log.i("data_str",data_str+" str: +"+str);
            data_str=data_str.concat(str);
            Log.i("data_str_concat",data_str);
        float[] y_val=new float[100];//will this give an error????

            String[] split=data_str.split("\\s+");//should split up by whitespace

        for(int i=0;i<=split.length-1;i++)
        {

            //split[i]=' '+split[i];
            Log.i("I_AM2","HERE"+"++"+split[i]+" i:"+i);
            y_val[i] = Float.parseFloat(split[i]);//string length may not match
            Log.i("I_AM","HERE");
            Log.i("data_str2",split[i]+"++++"+y_val[0]);
        }
            data_str="";
            //y_val[0]=12f;
         return y_val;
        }
       catch (NullPointerException e)//in case string not parsable
       {
           e.printStackTrace();
           Log.e("Exeption",str);
           data_str="";
       }
            data_str="";
        }
        return null;
    }

    private String[] concat_split(String str)
    {
        data_str.concat(str);
        sArr=data_str.split("\\s+");
        return sArr;
    }

    private float[] stringToFloat(String[] strArr)
    {  try{

            fArr[current_index]=Float.parseFloat(strArr[current_index]);
             Log.i("arr",""+fArr[current_index]);
            current_index++;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return fArr;
    }

     private String check_string(String data_msg)
    {boolean check1=false;
        boolean check2=false;
        if(data_msg.endsWith(" ")==false)//character at end of string
        {
            safe_to_split=1;//concatenates the string
           data_str=data_str.concat(data_msg);
            Log.i("End_with",data_msg+"+++"+data_str);
            check1=true;
            //return data_str;
        }
        if(data_msg.startsWith(" ")==false)
        {
            for(int i=0;i<=data_msg.length()-1;i++)
            {
                if(data_msg.charAt(i)!=' ')
                {
                    data_msg=data_msg.substring(i);
                }
                else{
                    break;
                }
            }
            check2=true;
        }
        //if (check1==true && check2==true)
        //{ safe_to_split=2;}//variable safe in other words in stringBUffer method}
         if(check1==false && check2==false)
        {   safe_to_split=2;}


        return data_msg;
    }

    void updateChart(float y)
    {
        data.addEntry(new Entry(x/1000,y),0);
        Log.i("x_incre y_val_before",""+x);
        if(time_start==false)
        {
            time_started=System.currentTimeMillis();//System.nanoTime();
            curent_time=System.currentTimeMillis();//System.nanoTime();
            x=(float)(curent_time-time_started);
            time_start=true;
        }
        else
        {
            curent_time=System.currentTimeMillis();//System.nanoTime();
            x=(float)(curent_time-time_started);
        }


        Log.i("x_incre y_val",""+x);

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    public float[] fix_string(String str)
    {
        float[] y_val=new float[100];//will this give an error????
        y_val[1]=0;
        y_val[0]=0;
        y_val[2]=0;
        if(count==0) {
            data_str = str;
            count=1;
            do_it=false;
        }
        else if(count==1)
        {  count = 2;
           data_str=data_str.concat(str);
            do_it=false;
        }
        else if(count==2)
        {count=0;
            try {
                data_str = data_str.concat(str);

                String[] split = data_str.split("\\s+");//should split up by whitespace
                for (int i = 0; i <= split.length - 1; i++) {

                    //split[i]=' '+split[i];
                    Log.i("fixi", "HERE" + "++" + split[i] + " i:" + i);
                    y_val[i] = Float.parseFloat(split[i]);//string length may not match
                    Log.i("fixi", "HERE");
                    Log.i("data_str2", split[i] + "++++" + y_val[0]);
                }
                Log.i("data_str_after-",data_str);
                Log.i("data_str_after0", split[0] + "++++" + y_val[0]);
                Log.i("data_str_after1", split[1] + "++++" + y_val[1]);
                Log.i("data_str_after2", split[2] + "++++" + y_val[2]);
                do_it=true;
            }
            catch(NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
        return y_val;
    }

}