package com.example.noxid.androidgraphingui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noxid on 7/14/16.
 */
public class Splash extends AppCompatActivity {

    ImageView iv;
    Button single,separated,double_,connect_arduino;
    TextView tv;
    long begin_time=0;
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
        connect_arduino=(Button)findViewById(R.id.connect);
        tv=(TextView)findViewById(R.id.textView);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);

        connect_arduino.setBackgroundColor(Color.LTGRAY);
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

        connect_arduino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               UsbManager usbManager=(UsbManager)getSystemService(Context.USB_SERVICE);
                UsbDevice device=null;
                UsbDeviceConnection connection=null;
                HashMap<String,UsbDevice> usbDevices=usbManager.getDeviceList();
                if(!usbDevices.isEmpty())
                {
                    boolean keep=true;
                    for(Map.Entry<String,UsbDevice> entry:usbDevices.entrySet())
                    {
                        device=entry.getValue();
                        int deviceVID=device.getVendorId();
                        int devicePID=device.getProductId();
                        if(deviceVID != 0x1d6b || (devicePID!=0x0001 || devicePID !=0x0002 || devicePID!=0x0003))
                        {
                            connection=usbManager.openDevice(device);
                            keep=false;
                        }
                        else
                        {
                            Toast.makeText(Splash.this, "No Connection", Toast.LENGTH_SHORT).show();
                            connection=null;
                            device=null;

                        }
                        if(!keep)
                        {
                            break;
                        }
                    }


                    UsbSerialDevice serialPort=UsbSerialDevice.createUsbSerialDevice(device,connection);
                    if(serialPort!=null)
                    {
                        if(serialPort.open())
                        {
                            Toast.makeText(Splash.this, "Seial Port Open", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Splash.this, "Port Could not be opened", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else
                {
                    Toast.makeText(Splash.this, "No UsbDevice Attached", Toast.LENGTH_SHORT).show();

                }





            }
        });
/*
        //find drivers from attached devices
        UsbManager usbManager=(UsbManager)getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> usbSerialDriverList= UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if(usbSerialDriverList.isEmpty())
        {
            Toast.makeText(Splash.this, "No Driver", Toast.LENGTH_SHORT).show();

            return;
        }

        //open connecttion to first available driver
        UsbSerialDriver usbSerialDriver=usbSerialDriverList.get(0);
        UsbDeviceConnection usbDeviceConnection=usbManager.openDevice(usbSerialDriver.getDevice());
        if(usbDeviceConnection==null)
        {
            //probably call
            //UsbManager.
            Toast.makeText(Splash.this, "No USB devices attached", Toast.LENGTH_SHORT).show();
            return;
        }

       UsbSerialPort usbSerialPort= usbSerialDriver.getPorts().get(0);
        try {
            usbSerialPort.open(usbDeviceConnection);
            usbSerialPort.setParameters(9600,UsbSerialPort.DATABITS_8,UsbSerialPort.STOPBITS_1,UsbSerialPort.PARITY_NONE);
            int usbOut,usbIn;
            String tOut="S/n";
            byte[] bytesOut=tOut.getBytes();
            byte[] bytesIn=new byte[25];
            usbOut= usbSerialPort.write(bytesOut,1000);
            for (int i=0;i<4;i++) {
                usbIn = usbSerialPort.read(bytesIn, 1000);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }



   /* new AsyncTask<Void,Void,List<UsbSerialPort>>()
    {
        @Override
        protected List<UsbSerialPort> doInBackground(Void... params)
        {

        }
    }*/
/*
    @Override
    protected void onResume()
    {
        super.onResume();

    }*/
    private UsbSerialInterface.UsbReadCallback mCallback =new UsbSerialInterface.UsbReadCallback()
   {
      @Override
       public void onReceivedData(byte[] arg0)
      {
          try {
              long start_time=System.nanoTime();
              begin_time=start_time;

              String str=new String(arg0,"UTF-8");
              Log.i("UsbArduino",str);

          } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
          }

      }
   };

}
