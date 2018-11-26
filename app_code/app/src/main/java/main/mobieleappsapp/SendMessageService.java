package main.mobieleappsapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SendMessageService extends Service {

    private IBinder myBinder = new MyBinder();
    private final String hostname ="10.0.2.2";
    private final int port = 6000;
    Socket socket;
    PrintWriter writer;
    BufferedReader reader;
    ReceiverThread receiverThread;


    public class MyBinder extends Binder {
        SendMessageService getService() {
            return SendMessageService.this;
        }
    }

    public class ReceiverThread implements Runnable{
        BufferedReader reader;
        boolean running = true;

        LocalBroadcastManager localBroadcastManager;

        public ReceiverThread(BufferedReader reader){
            this.reader=reader;


        }


        @Override
        public void run() {


            try {
                while(running) {
                    String s = reader.readLine();
                    Intent intent = new Intent("SEND_MESSAGE_TO_ACTIVITY");
                    intent.putExtra("message", s);
                    sendBroadcast(intent);
                }

            } catch (IOException e) {
                running = false;
                e.printStackTrace();
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {

        String name=intent.getStringExtra("name");


        //create socket, receiverThread and outputstream + login
        try {
            socket = new Socket(hostname, port);

            //init input and outputstream
            OutputStream output = socket.getOutputStream();

            writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            //login
            try {
                JSONObject obj = new JSONObject();

                obj.put("type", new Integer(0));
                obj.put("message", "login");
                obj.put("extra", name);

                writer.println(obj.toString());



            //start readerthread
            receiverThread = new ReceiverThread(reader);

            Thread thread = new Thread(receiverThread);
            thread.start();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return myBinder; }



    @Override
    public boolean onUnbind(Intent intent) {

        writer.close();
        try {
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket=null;
        reader=null;
        writer=null;




        return false; }


    protected void sendMessage(String message) {

            //send
            writer.println(message);


    }
}
