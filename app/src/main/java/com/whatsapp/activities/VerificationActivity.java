package com.whatsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by menna on 02/05/15.
 */
public class VerificationActivity extends Activity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_validation);

    }

    public void validate(View view){
        EditText validationCode = (EditText) findViewById(R.id.validation);
        String verificationCode = validationCode.getText().toString();
        /*TODO Check if the verification text correct*/

        if(true){
            Intent intent;
            intent = new Intent(getApplicationContext(),ContactsActivity.class);
            startActivity(intent);
        }
    }
}
