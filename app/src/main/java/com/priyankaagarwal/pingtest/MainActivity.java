package com.priyankaagarwal.pingtest;

import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStreamReader;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView _LogScreen;
    private String display = "Ping Logs";
    private Button _BtnStart;
    private TextView _HostName;
    private TextView _PacketSize;
    private TextView _NumOfPackets;
    private List<String> _RespList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _LogScreen = (TextView)findViewById(R.id.TextView_Log);
        _BtnStart = (Button)findViewById(R.id.button);
        _HostName = (EditText)findViewById(R.id.HostText);
        _PacketSize = (EditText)findViewById(R.id.PacketText);
        _NumOfPackets = (EditText)findViewById(R.id.CountText);
        _LogScreen.setText(display);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _RespList.clear();
                display = "";
                display += "Host Name : " + _HostName.getText().toString() + '\n';
                display += "PacketSize : " + _PacketSize.getText().toString() + '\n';
                display += "NumOfPackets : " + _NumOfPackets.getText().toString() + '\n';
                UpdateScreen();

                int Count =  Integer.parseInt(_NumOfPackets.getText().toString());

                for(int i =0 ; i< Count; i++)
                {
                    String s = ExecutePing();
                    _RespList.add(s);
                }

                display += '\n' ;
                display += "XXXXXXXXXX" + '\n';
                display += "XXXXX  FINAL TEST SUMMARY XXXXX" + '\n';

                for(int i =0; i<_RespList.size(); i++)
                {
                    display += "Ping" + Integer.toString(i+1) + " :" + _RespList.get(i) + '\n';
                    UpdateScreen();
                }

                display += "XXXXX  SUMMARY END XXXXX" + '\n';
                display += "XXXXXXXXXX" + '\n';
                UpdateScreen();

            }
        };

        _BtnStart.setOnClickListener(listener);
    }


    private void UpdateScreen()
    {
        _LogScreen.setMovementMethod(new ScrollingMovementMethod());
        _LogScreen.setText(display);
    }

    public String ExecutePing()
    {
        try
        {
            String Host = _HostName.getText().toString();
            String PacketSize = _PacketSize.getText().toString();

            String cmdPing = "ping -c 1 " +Host;
            Runtime r = Runtime.getRuntime();
            java.lang.Process p = r.exec(cmdPing);

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            String Time_string = "fail";

            while((inputLine = in.readLine())!= null)
            {
                display += inputLine + '\n';
                UpdateScreen();

                if(inputLine.contains("time="))
                {
                    Time_string = inputLine.substring(inputLine.indexOf("time=") + 5, inputLine.indexOf("ms")).trim() + "ms";
                }

            }

            return Time_string;
        }

        catch (Exception e)
        {
            Toast.makeText(this, "Error: "+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            return  "fail";
        }


    }

}
