package main.mobieleappsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logoutButton = findViewById(R.id.logout_button);
        findViewById(R.id.logout_button).setOnClickListener(this);

        lv = (ListView) findViewById(R.id.listview);

        list = new ArrayList<String>();
        // add stuff to list
        list.add("foo");
        list.add("bar");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, list );

        lv.setAdapter(arrayAdapter);

        // voor sockets te testen

        Intent serviceIntent = new Intent(this,SendMessageService.class);
        startService(serviceIntent);


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
