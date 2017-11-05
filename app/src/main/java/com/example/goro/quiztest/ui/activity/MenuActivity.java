package com.example.goro.quiztest.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.goro.quiztest.R;
import com.example.goro.quiztest.db.DataBaseHelper;

import java.io.IOException;

/**
 * Created by Goro on 03.10.2017.
 */

public class MenuActivity extends Activity implements View.OnClickListener {
    private int position;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text;
    private RelativeLayout btnNotes;
    private RelativeLayout btnLearn;
    private RelativeLayout btnTest;
    private Typeface face;
    private DataBaseHelper myDbHelper;
    private Cursor info;
    private SQLiteDatabase mydb1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        findViews();
        init();
        createAndOpenDb();
        setter();
        setListeners();
    }

    private void createAndOpenDb() {
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        myDbHelper.openDataBase();
        info.moveToFirst();
        position = info.getInt(info.getColumnIndex("countlearn"));
    }

    private void setter() {
        text1.setTypeface(face);
        text2.setTypeface(face);
        text3.setTypeface(face);
        text.setTypeface(face);
        text.setText("" + (position - 1) + "");
    }

    private void setListeners() {
        btnNotes.setOnClickListener(this);
        btnLearn.setOnClickListener(this);
        btnTest.setOnClickListener(this);
    }

    private void init() {
        face = Typeface.createFromAsset(getAssets(), "font/Appetite.ttf");
        myDbHelper = new DataBaseHelper(this);
        mydb1 = myDbHelper.getReadableDatabase();
        info = mydb1.rawQuery("SELECT * FROM info WHERE _id=" + 1 + "", null);
    }

    private void findViews() {
        text1 = findViewById(R.id.tv_menu_activity_notes);
        text2 = findViewById(R.id.tv_menu_activity_learn);
        text3 = findViewById(R.id.tv_menu_activity_test);
        text = findViewById(R.id.count);
        btnNotes = findViewById(R.id.btn_menu_activity_notes);
        btnLearn = findViewById(R.id.btn_menu_activity_learn);
        btnTest = findViewById(R.id.btn_menu_activity_test);

    }


    @Override
    protected void onResume() {
        super.onResume();
        position = info.getInt(info.getColumnIndex("counttest"));
        text.setTypeface(face);
        text.setText("" + (position - 1) + "");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_menu_activity_learn:
                startActivity(new Intent(MenuActivity.this, LearnActivity.class));
                break;
            case R.id.btn_menu_activity_notes:
                startActivity(new Intent(MenuActivity.this, NoteActivity.class));
                break;
            case R.id.btn_menu_activity_test:
                startActivity(new Intent(MenuActivity.this, TestActivity.class));
                break;
        }
    }
}