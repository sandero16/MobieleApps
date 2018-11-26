package main.mobieleappsapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv;
    List<String> list;
    SendMessageService mService;
    boolean mBound = false;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SendMessageService.MyBinder binder = (SendMessageService.MyBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public class MessageReceiver extends BroadcastReceiver {

        ArrayList<JSONArray> list;
        public MessageReceiver(ArrayList<JSONArray> list){
            super();

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            //get the message

            //convert it to JSON


            //add it to the list
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logoutButton = findViewById(R.id.logout_button);
        findViewById(R.id.logout_button).setOnClickListener(this);

        lv = (ListView) findViewById(R.id.listview);

        list = new ArrayList<String>();
        // read data in List
        list.add("foo");
        list.add("bar");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, list );

        lv.setAdapter(arrayAdapter);


        //broadcastreceiver



    }


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, SendMessageService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    public void onDestroy() {

        super.onDestroy();



    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.logout_button:
                logout();
                break;
            // ...
        }

    }

    private void logout(){
        //go to the loginscreen
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("logout",1);
        startActivity(intent);


        //finish this activity
        finish();

    }


}
