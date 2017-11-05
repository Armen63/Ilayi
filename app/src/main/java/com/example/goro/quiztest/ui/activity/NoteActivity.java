package com.example.goro.quiztest.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goro.quiztest.R;
import com.example.goro.quiztest.db.DataBaseHelper;
import com.example.goro.quiztest.db.model.Note;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Goro on 04.10.2017.
 */

public class NoteActivity extends Activity implements View.OnClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = NoteActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private TextView t2;
    private Button btnAdd;
    private Button btnBack;
    private EditText etRussian;
    private EditText etEnglish;
    private ArrayList<Note> not;
    private ListView lvMain;
    private Note note;
    private Context context = this;
    private DataBaseHelper dbHelper;
    private ContentValues cv;
    private Cursor cursor;
    private SQLiteDatabase db;
    private BoxAdapter boxAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        findViews();
        init();
        createAndOpenDb();
        setListeners();
    }


    private void createAndOpenDb() {
        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        dbHelper.openDataBase();
        itemForAdding();
    }

    private void findViews() {
        t2 = findViewById(R.id.textView3);
        btnBack = findViewById(R.id.backstart1);
        btnAdd = findViewById(R.id.but2);
        lvMain = findViewById(R.id.lvMain);
        etRussian = findViewById(R.id.edt1);
        etEnglish = findViewById(R.id.edt2);
    }


    private void init() {
        not = new ArrayList<>();
        boxAdapter = new BoxAdapter(this, not);
        dbHelper = new DataBaseHelper(this);
        db = dbHelper.getWritableDatabase();
    }


    // ===========================================================
    // Click Listeners
    // ===========================================================

    private void setListeners() {
        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                return deleteNote(position);
            }

        });
    }

    // ===========================================================
    // CRUD(create, read, update, delete)
    // ===========================================================

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backstart1:
                onBackPressed();
                break;
            case R.id.but2:
                addNote();
                break;
        }
    }

    public void addNote() {
        cv = new ContentValues();
        String russian = etRussian.getText().toString();
        String english = etEnglish.getText().toString();

        cv.put("russian", russian);
        cv.put("english", english);

        db.insert("mywords", null, cv);
        update();
    }

    public void update() {
        not.clear();
        itemForAdding();
    }

    public boolean deleteNote(final int position) {
        AlertDialog.Builder alertDialogBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            alertDialogBuilder = new AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        else
            alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Удалить выбранный элемент?");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "DELETED", Toast.LENGTH_LONG)
                                .show();

                        Note nt = not.get(position);
                        int idn = nt.getId();
                        String where = "_id = " + (idn);
                        db.delete("mywords", where, null);
                        update();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return false;
    }




    public void itemForAdding() {
        cursor = db.rawQuery("Select * from mywords", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                note = new Note(cursor.getString(1), cursor.getString(2));
                note.setId(cursor.getInt(0));
                not.add(note);
            }
        }
        lvMain.setAdapter(boxAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
