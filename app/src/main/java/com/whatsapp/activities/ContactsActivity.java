package com.whatsapp.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class ContactsActivity extends ActivityTracker {
    String phoneNumber;
    ListView lv;
    Activity x = this;
    ArrayList<String> contacts= new ArrayList<String>();
    private static final String TAG = ContactsActivity.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        lv=(ListView) findViewById(R.id.list);
        getNumber(this.getContentResolver());

    }

    public void updateActivity(){
        // fetch any updates in the inbox
        // 3ayez a3raf el contacts nbrs

    }




    public void getNumber(ContentResolver cr)
    {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(name+":"+phoneNumber);
        }
        phones.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,contacts);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String item = contacts.get(position);
                String [] nameNumber = item.split(":");
                //clickedContact is the mobile number of the clicked contact
                String clickedContact=nameNumber[1];


                Intent i = new Intent(x,ChatActivity.class);
                i.putExtra("contact",clickedContact);
                startActivity(i);
            }

        });
    }
}
