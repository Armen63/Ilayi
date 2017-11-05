package com.example.goro.quiztest.ui.activity;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goro.quiztest.R;
import com.example.goro.quiztest.db.DataBaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    // ===========================================================
    // Constants
    // ===========================================================
    private static final String LOG_TAG = TestActivity.class.getSimpleName();


    private Context context = this;
    private DataBaseHelper myDbHelper;
    private View layout;
    private TextView text;
    private Button btnOne;
    private Button btnTwo;
    private Button btnThree;
    private Button btnFour;
    private Button btnBack;
    private Button btnStartAgain;
    private TextView tvRussian;
    private TextView tvEnglish;
    private RelativeLayout img;
    private Toast toast;
    private Window window;
    private LayoutInflater inflater;
    private int i;
    private int buttonInt;
    private Random random;
    private List<String> answers;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Cursor info;
    private ContentValues cv;
    private SQLiteDatabase mydb1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        findViews();
        init();
        setListeners();
        quiz();
    }

    @Override
    protected void onPause() {
        super.onPause();
        i--;
        db = myDbHelper.getWritableDatabase();
        cv = new ContentValues();
        cv.put("counttest", i); //These Fields should be your String values of actual column names
        db.update("info", cv, "_id=" + 1, null);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    private void findViews() {
        text = (TextView) findViewById(R.id.textView2);
        btnBack = (Button) findViewById(R.id.btn_back_to_menu);
        btnStartAgain = (Button) findViewById(R.id.ic_learn_activity_first_position);
        btnOne = (Button) findViewById(R.id.button2);
        btnTwo = (Button) findViewById(R.id.button1);
        btnThree = (Button) findViewById(R.id.button3);
        btnFour = (Button) findViewById(R.id.button4);
        tvRussian = (TextView) findViewById(R.id.textView);
        tvEnglish = (TextView) findViewById(R.id.textView1);
        img = (RelativeLayout) findViewById(R.id.relativeLayout);

    }

    private void init() {
        initWindow();
        initDb();
        answers = new ArrayList<>();
        random = new Random();
        inflater = getLayoutInflater();
    }

    private void setListeners() {
        btnStartAgain.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
    }

    private void initDb() {
        myDbHelper = new DataBaseHelper(this);
        mydb1 = myDbHelper.getReadableDatabase();
        info = mydb1.rawQuery("SELECT * FROM info WHERE _id=" + 1 + "", null);
        info.moveToFirst();
        i = info.getInt(info.getColumnIndex("counttest"));


        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        myDbHelper.openDataBase();

        db = myDbHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM top500 WHERE _id=" + i + "", null);

    }

    private void initWindow() {
        window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.start));
        text.setText("" + i + "/500");
    }

    public void quiz() {

        text.setText("" + i + "/500");

        cursor = db.rawQuery("SELECT * FROM top500 WHERE _id=" + i + "", null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            answers.add(cursor.getString(cursor.getColumnIndex("russian")));
            tvRussian.setText(cursor.getString(cursor.getColumnIndex("russian")));
            tvEnglish.setText(cursor.getString(cursor.getColumnIndex("english")));
            int id = getResources().getIdentifier((cursor.getString(cursor.getColumnIndex("img"))), "drawable", this.getPackageName());
            img.setBackground(getResources().getDrawable(id));
        }

        for (int i = 0; i < 3; ++i) {
            cursor = db.rawQuery("SELECT * FROM top500 WHERE _id=" + randomIntGenerator() + "", null);
            cursor.moveToFirst();
            if (cursor != null && cursor.getCount() != 0) {
                answers.add(cursor.getString(this.cursor.getColumnIndex("russian")));
            }
        }

        buttonInt = random.nextInt(4);
        btnOne.setText(answers.get(buttonInt));
        answers.remove(buttonInt);

        buttonInt = random.nextInt(3);
        btnTwo.setText(answers.get(buttonInt));
        answers.remove(buttonInt);

        buttonInt = random.nextInt(2);
        btnThree.setText(answers.get(buttonInt));
        answers.remove(buttonInt);

        btnFour.setText(answers.get(0));
        i++;
    }

    public int randomIntGenerator() {
        return random.nextInt(499) + 1;
    }

    public void startAgain() {
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
                        i = 1;
                        quiz();
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

    public void right() {
        layout = inflater.inflate(R.layout.right, (ViewGroup) findViewById(R.id.toast_layout_root));

        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        // toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 800);

    }

    public void wrong() {
        layout = inflater.inflate(R.layout.wrong, (ViewGroup) findViewById(R.id.toast_layout_root1));

        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);
        toast.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 600);

    }



    // ===========================================================
    // Click Listeners
    // ===========================================================
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_learn_activity_first_position:
                startAgain();
                break;
            case R.id.btn_back_to_menu:
                onBackPressed();
                break;
            case R.id.button1:
                buttonChecker(btnTwo);
                break;
            case R.id.button2:
                buttonChecker(btnOne);
                break;
            case R.id.button3:
                buttonChecker(btnThree);
                break;
            case R.id.button4:
                buttonChecker(btnFour);
                break;
        }
    }

    private void buttonChecker(Button btnPressed) {
        if (btnPressed.getText().equals(tvRussian.getText())) {
            right();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    quiz();
                }
            }, 1000);
        } else
            wrong();
    }
}

