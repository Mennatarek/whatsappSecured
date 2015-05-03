package com.whatsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.view.View.OnClickListener;

public class RegisterActivity extends Activity {

    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private ImageView img;

    private static final String MOBILE_PATTERN = "^((\\+|00)(\\d{1,3})[\\s-]?)?(\\d{11})$";

    private Pattern pattern;
    private Matcher matcher;

    private String ip;
    private String serverIP;
    private int serverPort;
    private String reply;

    public boolean mobileValidator(String mobile){
        pattern = Pattern.compile(MOBILE_PATTERN);
        matcher = pattern.matcher(mobile);
        return matcher.matches();

    }
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_register);
        img = (ImageView) findViewById(R.id.avatar);
        ip = getIpAddress();
        serverIP="10.0.2.2";
        serverPort=5420;

    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String phoneNbr;
        Socket socket = null;
        DataInputStream dis;
        DataOutputStream dos;
        String message;

        MyClientTask(String address, int port, String phoneNbr){
            dstAddress = address;
            dstPort = port;
            this.phoneNbr = phoneNbr;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                socket = new Socket(dstAddress, dstPort);
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());

                dos.writeUTF("logIn;" + phoneNbr + ";" + ip + ";key");

                reply = dis.readUTF();

                dis.close();
                dos.close();
                socket.close();


                if(reply.equals("registered")){
                    Intent intent;
                    intent = new Intent(getApplicationContext(),VerificationActivity.class);
                    startActivity(intent);
                }else if (reply.equals("loggedIn")){
                    Intent intent;
                    intent = new Intent(getApplicationContext(),ContactsActivity.class);
                    startActivity(intent);
                }

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    public void chooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                img.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void register(View v){
        EditText mobile = (EditText) findViewById(R.id.mobile);
            if(mobileValidator(mobile.getText().toString())) {

                /* TODO If user exist go to chat else create user and go to verification Activity*/
                MyClientTask myClientTask = new MyClientTask(
                        serverIP,
                        serverPort, mobile.getText().toString());
                myClientTask.execute();

            }else{
                mobile.setError("Invalid Mobile Number");
            }
    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress();
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

}
