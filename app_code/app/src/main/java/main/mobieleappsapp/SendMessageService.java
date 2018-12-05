package main.mobieleappsapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.ArrayList;

public class SendMessageService extends Service {

    private IBinder myBinder = new MyBinder();
    private final String hostname ="10.0.2.2";
    private final int port = 6000;
    Socket socket;
    PrintWriter writer;
    BufferedReader reader;
    ReceiverThread receiverThread;
    private String name;
    private AsyncTask<Void,Void,Void> loginTask;


    public class MyBinder extends Binder {
        SendMessageService getService() {
            return SendMessageService.this;
        }
    }

    public class ReceiverThread implements Runnable{
        BufferedReader reader;
        boolean running = true;


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

            } catch (Exception e) {
                running = false;
                e.printStackTrace();
            }
        }
    }

    //TODO put all communication in Asynctasks!

    private class LoginTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                socket = new Socket(hostname, port);

                //init input and outputstream
                OutputStream output = socket.getOutputStream();

                writer = new PrintWriter(output, true);

                InputStream input = socket.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));

                //login

                JSONObject obj = new JSONObject();

                obj.put("type", new Integer(0));
                obj.put("message", MainActivity.token);
                obj.put("extra", name);

                writer.println(obj.toString());



                //start readerthread
                receiverThread = new ReceiverThread(reader);

                Thread thread = new Thread(receiverThread);
                thread.start();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    private  class LogoutTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

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


            return null;
        }
    }

    private class SendMessageTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {

            writer.println(strings[0]);

            return null;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {

        //String name=intent.getStringExtra("name");
        name = "user3";

        loginTask = new LoginTask().execute();

        return myBinder; }



    @Override
    public boolean onUnbind(Intent intent) {

        AsyncTask<Void, Void, Void> logoutTask = new LogoutTask().execute();

        return false; }


    public void sendMessage(String message) {

            //send
        AsyncTask<String, Void, Void> sendMessageTask = new SendMessageTask().execute(message);


    }
}
