package com.example.goro.quiztest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.goro.quiztest.Note;
import com.example.goro.quiztest.R;

import java.util.ArrayList;

/**
 * Created by Goro on 09.10.2017.
 */

public class BoxAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<Note> objects;

    BoxAdapter(Context context, ArrayList<Note> notes) {
        ctx = context;
        objects = notes;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item_note, parent, false);
        }

        Note p = getProduct(position);

        ((TextView) view.findViewById(R.id.Russian)).setText(p.getRussian());
        ((TextView) view.findViewById(R.id.English)).setText(p.getEnglish());
        return view;
    }

    Note getProduct(int position) {
        return ((Note) getItem(position));
    }

}