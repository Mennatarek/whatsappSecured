package com.whatsapp.chat.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import org.w3c.dom.Comment;

import java.util.ArrayList;


/**
 * Created by menna on 01/05/15.
 */
public class ContactsListAdapter extends ArrayAdapter<String> {

    Context ctx;
    int resource;
    ArrayList<Contact> contacts;

    public ContactsListAdapter(Context context, int resource,
                               ArrayList<Comment> comments) {
        super(context, resource, comments);
    }
}
