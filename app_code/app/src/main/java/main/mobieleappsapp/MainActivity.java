package main.mobieleappsapp;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv;
    List<String> list;
    SendMessageService mService;
    boolean mBound = false;
    private ArrayAdapter<String> arrayAdapter;
    private String loc="GPS_DISABLED";
    private static final String DATABASE_NAME = "message_db";
    private MessageDatabase messageDatabase;
    private MessageReceiver br;
    public static String token;


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


        List<String> list;

        public MessageReceiver(List<String> list) {
            super();

            this.list = list;

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            //get the message
            String data = intent.getStringExtra("message");




            if(data!=null) {
                //add it to the list
                list.add(data);
                arrayAdapter.notifyDataSetChanged();

                //add it to the database
                new AddMessageToDbTask().execute(data);


            }

        }
    }

    private class AddMessageToDbTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            try {
                JSONObject obj = new JSONObject(strings[0]);
                messageDatabase.databaseAccess().insert(new DatabaseMessage(obj));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class DeleteAllDb extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            messageDatabase.databaseAccess().deleteAll();
            return null;
        }
    }

    private class GetAllDb extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            List<DatabaseMessage> all = messageDatabase.databaseAccess().getAll();

            //Log.w("---------------","all: " + all.toString());


            for(DatabaseMessage dm: all){

                list.add(dm.toJSON().toString());
                arrayAdapter.notifyDataSetChanged();
            }

            //Log.w("---------------","list: " + list.toString());

            return null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logoutButton = findViewById(R.id.logout_button);
        findViewById(R.id.logout_button).setOnClickListener(this);

        Button postButton = findViewById(R.id.post_button);
        findViewById(R.id.post_button).setOnClickListener(this);

        lv = (ListView) findViewById(R.id.listview);


        list = new ArrayList<String>();
        //list.add("test");


        // read data in List

        arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, list);

        lv.setAdapter(arrayAdapter);

        //read token

        token = getIntent().getStringExtra("token");

        //GPS
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                loc=Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + " " + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            finish();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, locationListener);


        //content provider (room database)

        messageDatabase = Room.databaseBuilder(getApplicationContext(),
                MessageDatabase.class, DATABASE_NAME)
                .build();
        
        //get all items from database

        AsyncTask<Void, Void, Void> execute = new GetAllDb().execute();


        //broadcastreceiver

        br = new MessageReceiver(list);
        IntentFilter filter = new IntentFilter("SEND_MESSAGE_TO_ACTIVITY");
        this.registerReceiver(br, filter);


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
        this.unregisterReceiver(br);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.post_button:
                // get text

                EditText text = (EditText)findViewById(R.id.text_field);
                String value = text.getText().toString();
                text.getText().clear();

                //create message

                JSONObject message=new JSONObject();
                try {
                    message.put("type",new Integer(1));
                    message.put("message", value);
                    message.put("extra", loc);

                //send message

                    mService.sendMessage(message.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.logout_button:

                logout();
                break;
            // ...
        }

    }

    private void logout(){
        //send logout message to server
        //create message

        JSONObject message=new JSONObject();
        try {
            message.put("type",new Integer(0));
            message.put("message", "");
            message.put("extra", "logout");

            //send message

            mService.sendMessage(message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //go to the loginscreen
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("logout",1);
        startActivity(intent);


        //finish this activity
        finish();

    }


}
