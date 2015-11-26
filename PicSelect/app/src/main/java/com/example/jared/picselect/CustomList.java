package com.example.jared.picselect;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.Console;


public class CustomList extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] names;
    private SharedPreferences prefs;

    public CustomList(Activity context, String[] names, SharedPreferences prefs) {
        super(context, R.layout.list_item, names);
        this.context = context;
        this.names = names;
        this.prefs = prefs;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        if (prefs.getBoolean(names[position], true))
        {
            txtTitle.setText(names[position]);
        } else {
            txtTitle.setText(names[position] + " has been viewed");
            TableLayout table = (TableLayout) rowView.findViewById(R.id.table);
            table.setBackgroundColor(Color.parseColor("#cccccc"));
            table.setVisibility(View.GONE);
        }



        return rowView;


    }
}
