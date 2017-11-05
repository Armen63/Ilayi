package com.example.goro.quiztest.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.goro.quiztest.db.DataBaseHelper;
import com.example.goro.quiztest.R;

import java.io.IOException;

/**
 * Created by Goro on 04.10.2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class LearnActivity extends Activity implements View.OnClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = LearnActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private int position;
    private TextView tvTrophies;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   ;
    private TextView tvEnglish;
    private TextView tvRussian;
    private Button btnStartAgain;
    private Button btnBack;
    private ImageButton btnImgNextWord;
    private ImageButton btnImgPreviousWord;
    private RelativeLayout rlScreen;
    private Context context = this;
    private DataBaseHelper myDbHelper;
    private SQLiteDatabase myDb1;
    private Cursor info;
    private Cursor cursor;
    private SQLiteDatabase db;
    private ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        findViews();
        initAndSet();
        createAndOpenDb();
        setListeners();
        nextScreen();
    }

    private void setListeners() {
        btnBack.setOnClickListener(this);
        btnImgNextWord.setOnClickListener(this);
        btnImgPreviousWord.setOnClickListener(this);
        btnStartAgain.setOnClickListener(this);
    }

    private void createAndOpenDb() {

        try {
            myDbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myDbHelper.openDataBase();

        myDb1 = myDbHelper.getReadableDatabase();
        info = myDb1.rawQuery("SELECT * FROM info WHERE _id=" + 1 + "", null);
        info.moveToFirst();
        position = info.getInt(info.getColumnIndex("countlearn"));
        db = myDbHelper.getWritableDatabase();

    }

    private void initAndSet() {
        myDbHelper = new DataBaseHelper(this);
        tvTrophies.setText("" + position + "/500");
    }

    private void findViews() {
        tvTrophies = findViewById(R.id.tv_learn_activity_trophies);
        tvEnglish = findViewById(R.id.eng);
        tvRussian = findViewById(R.id.rus);
        btnImgNextWord = findViewById(R.id.btn_learn_activity_next);
        btnImgPreviousWord = findViewById(R.id.btn_learn_activity_previous);
        rlScreen = findViewById(R.id.learn1);
        btnStartAgain = findViewById(R.id.ic_learn_activity_first_position);
        btnBack = findViewById(R.id.btn_learn_activity_back);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cv = new ContentValues();
        cv.put("countlearn", position);
        db.update("info", cv, "_id=" + 1, null);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void nextWord() {
        position++;
        nextScreen();
    }

    private void previous() {
        position--;
        nextScreen();
    }

    private void update() {
        position = 1;
        nextScreen();
    }

    private void startAgain() {
        AlertDialog.Builder alertDialogBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            alertDialogBuilder = new AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        else
            alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Вы хотите начать сначала?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
    }

    private void nextScreen() {
        tvTrophies.setText("" + position + "/500");
        cursor = db.rawQuery("SELECT * FROM top500 WHERE _id=" + position + "", null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            tvEnglish.setText(cursor.getString(cursor.getColumnIndex("english")));
            tvRussian.setText(cursor.getString(cursor.getColumnIndex("russian")));
            int id = getResources().getIdentifier((cursor.getString(cursor.getColumnIndex("img"))), "drawable", this.getPackageName());
            rlScreen.setBackground(getResources().getDrawable(id));
        }
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_learn_activity_back:
                onBackPressed();
                break;
            case R.id.btn_learn_activity_next:
                if (position < 500)
                    nextWord();
                break;
            case R.id.btn_learn_activity_previous:
                if (position > 1)
                    previous();
                break;
            case R.id.ic_learn_activity_first_position:
                startAgain();
                break;
        }
    }
}
