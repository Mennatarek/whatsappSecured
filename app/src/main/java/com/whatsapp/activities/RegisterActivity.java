package com.whatsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends Activity {




    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private ImageView img;

    private static final String MOBILE_PATTERN = "^((\\+|00)(\\d{1,3})[\\s-]?)?(\\d{11})$";

    private Pattern pattern;
    private Matcher matcher;


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
                Intent intent;
                intent = new Intent(getApplicationContext(),VerificationActivity.class);
                startActivity(intent);
            }else{
                mobile.setError("Invalid Mobile Number");
            }
        }
    }
