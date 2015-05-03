package com.whatsapp.activities;

import android.app.Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;


/**
 * Created by hesham on 02/05/15.
 */

public class BgThread extends Application
{
    private ActivityTracker currentActivity = null;
    private Hashtable<String, String> inbox;
    private Hashtable<String, Integer> notifications;

    public void onCreate() {
        super.onCreate();

        inbox = new Hashtable<String, String>();
        notifications = new Hashtable<String, Integer>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadDataFromServer();
            }});
        thread.start();
    }

    public synchronized void setUpdatable(ActivityTracker updatable) {
        this.currentActivity = updatable;
    }

    private synchronized void updateCurrentActivity() {
        if (currentActivity != null)
            currentActivity.updateActivityFromBgThread();
    }

    public void addToInbox(String phoneNbr, String message){
        String messages = inbox.get(phoneNbr);
        inbox.put(phoneNbr, messages + "\n" +  message);
        if(notifications.contains(phoneNbr)) {
            int notific = notifications.get(phoneNbr) + 1;
            notifications.put(phoneNbr, notific);
        }else{
            notifications.put(phoneNbr, 1);
        }
    }

    public String retrieveInbox(String phoneNbr){

        notifications.put(phoneNbr, 0);

        return inbox.get(phoneNbr);

    }

    private void loadDataFromServer() {
        try {

            final ServerSocket serverSocket = new ServerSocket(5420);

            while(true){

               Socket socket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandler.start();

            }


        }catch(IOException e){
            e.printStackTrace();
        }

    }

    class ClientHandler extends Thread{
        Socket socket;
        DataInputStream dis;
        DataOutputStream dos;
        BgThread thread;

        public ClientHandler(Socket socket, BgThread thread){
            this.socket = socket;
            this.thread = thread;
            try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public void run(){

            while(true){

                try{
                    String response = dis.readUTF();

                    String [] filtered = response.split(";");

                    String phoneNbr = filtered[0];
                    String message = filtered[1];

                    addToInbox(phoneNbr, message);

                    updateCurrentActivity();

                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }

    }

}