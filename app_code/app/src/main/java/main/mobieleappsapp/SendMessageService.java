package main.mobieleappsapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SendMessageService extends IntentService {

    private final String hostname ="10.0.2.2";
    private final int port = 6000;

    // init
    public SendMessageService(){
        super("SendMessageService");







    }
    @Override
    protected void onHandleIntent(Intent intent) {

        //tests

        try {
            Socket socket = new Socket(hostname, port);

            //send
            OutputStream output = socket.getOutputStream();

            PrintWriter writer = new PrintWriter(output, true);
            writer.println("This is a message sent to the server 2");

            // read
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            Log.w("info ----", reader.readLine());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
