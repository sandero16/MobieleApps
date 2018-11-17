package main.mobieleappsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logoutButton = findViewById(R.id.logout_button);
        findViewById(R.id.logout_button).setOnClickListener(this);

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
