package com.whatsapp.chat.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


/**
 * Created by menna on 01/05/15.
 */
public class ContactsListAdapter extends ArrayAdapter<String> {

    Context ctx;
    int resource;
    ArrayList<String> contacts;

    public ContactsListAdapter(Context context, int resource,
                               ArrayList<String> contacts) {
        super(context, resource, contacts);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        String contact = getItem(position);
        //open chat with this person

       return null;
    }
}
