package com.example.jared.picselect;

import android.support.v4.app.*;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MyListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    protected CustomList adapter;
    private String[] names;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        Resources res = getResources();
        names = res.getStringArray(R.array.names);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.names, android.R.layout.simple_list_item_1);

        adapter = new CustomList(getActivity(), names, ((MainActivity)getActivity()).prefs);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        boolean avliable = ((MainActivity)getActivity()).prefs.getBoolean(names[position], true);
        if (avliable) {
            ((MainActivity)getActivity()).showImage(position);
            adapter.notifyDataSetChanged();
        } else {

        }

    }
}
