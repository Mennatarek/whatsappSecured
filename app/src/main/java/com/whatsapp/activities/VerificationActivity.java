package com.whatsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.SecureRandom;

/**
 * Created by menna on 02/05/15.
 */
public class VerificationActivity extends Activity {
    String sms;
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        sms = "";
        setContentView(R.layout.activity_validation);
        try {
            SecureRandom sr =  SecureRandom.getInstance("SHA1PRNG");
            byte[] bytes = new byte[5];
            sr.nextBytes(bytes);
            sms=String.valueOf(bytes[0])+String.valueOf(bytes[1])+String.valueOf(bytes[2])+String.valueOf(bytes[3])+String.valueOf(bytes[4]);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(this.getIntent().getStringExtra("number"), null, sms, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void validate(View view){
        EditText validationCode = (EditText) findViewById(R.id.validation);
        String verificationCode = validationCode.getText().toString();
//-4676-43-104-74
        if(verificationCode==sms){
            Intent intent;
            intent = new Intent(getApplicationContext(),ContactsActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), " wrong code",
                    Toast.LENGTH_LONG).show();
        }
    }
}
