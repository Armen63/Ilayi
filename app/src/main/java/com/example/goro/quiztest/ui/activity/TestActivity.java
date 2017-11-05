package com.example.goro.quiztest.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
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


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private DataBaseHelper myDbHelper;
    private View layout;
    private TextView text;
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button back;
    private Button update;
    private TextView tx1;
    private TextView tx2;
    private RelativeLayout img;
    private Random random;
    private Toast toast;
    private LayoutInflater inflater;
    private int i;
    private List<String> answers;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ContentValues cv;
    private Window window;
    private SQLiteDatabase mydb1;
    private Cursor info;


    @Override
    protected void onPause() {
        super.onPause();
        int f = 1;
        i--;
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("counttest", i); //These Fields should be your String values of actual column names

        db.update("info", cv, "_id=" + f, null);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        findViews();
        init();
        setListeners();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        quiz();

    }

    private void init() {
        initWindow();
        initDb();
        cursor = db.rawQuery("SELECT * FROM top500 WHERE _id=" + i + "", null);
        inflater = getLayoutInflater();
    }

    private void setListeners() {
        update.setOnClickListener(this);
        back.setOnClickListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
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

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        db = myDbHelper.getWritableDatabase();
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
        List<String> answers = new ArrayList<String>();


        Random random = new Random();

        SQLiteDatabase db = myDbHelper.getWritableDatabase();


        Cursor c = db.rawQuery("SELECT * FROM top500 WHERE _id=" + i + "", null);
        c.moveToFirst();
        if (c != null && c.getCount() != 0) {
            //   b2.setText(c.getString(c.getColumnIndex("russian")));
            answers.add(c.getString(c.getColumnIndex("russian")));
            tx1.setText(c.getString(c.getColumnIndex("russian")));
            tx2.setText(c.getString(c.getColumnIndex("english")));
            int id = getResources().getIdentifier((c.getString(c.getColumnIndex("img"))), "drawable", this.getPackageName());
            img.setBackground(getResources().getDrawable(id));

        }


        int rand1 = random.nextInt(499) + 1;


        Cursor c1 = db.rawQuery("SELECT * FROM top500 WHERE _id=" + rand1 + "", null);
        c1.moveToFirst();
        if (c1 != null && c1.getCount() != 0) {
            answers.add(c1.getString(c1.getColumnIndex("russian")));
        }

        int rand2 = random.nextInt(499) + 1;
        Cursor c2 = db.rawQuery("SELECT * FROM top500 WHERE _id=" + rand2 + "", null);
        c2.moveToFirst();
        if (c2 != null && c2.getCount() != 0) {
            answers.add(c2.getString(c1.getColumnIndex("russian")));
        }


        int rand3 = random.nextInt(499) + 1;
        Cursor c3 = db.rawQuery("SELECT * FROM top500 WHERE _id=" + rand3 + "", null);
        c3.moveToFirst();
        if (c3 != null && c3.getCount() != 0) {
            answers.add(c3.getString(c1.getColumnIndex("russian")));
        }


        Random tii = new Random();
        int kk = tii.nextInt(4);
        b1.setText(answers.get(kk));
        answers.remove(kk);

        Random tiv = new Random();
        int k = tiv.nextInt(3);
        b2.setText(answers.get(k));
        answers.remove(k);

        Random ti = new Random();
        int p = ti.nextInt(2);
        b3.setText(answers.get(p));
        answers.remove(p);

        b4.setText(answers.get(0));
        i++;
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
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
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
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                toast.cancel();
            }
        }, 600);

    }

    private void findViews() {
        text = (TextView) findViewById(R.id.textView2);
        back = (Button) findViewById(R.id.btn_back_to_menu);
        update = (Button) findViewById(R.id.ic_learn_activity_first_position);
        b1 = (Button) findViewById(R.id.button2);
        b2 = (Button) findViewById(R.id.button1);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        tx1 = (TextView) findViewById(R.id.textView);
        tx2 = (TextView) findViewById(R.id.textView1);
        img = (RelativeLayout) findViewById(R.id.relativeLayout);

    }

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
                buttonChecker(b2);
                break;
            case R.id.button2:
                buttonChecker(b1);
                break;
            case R.id.button3:
                buttonChecker(b3);
                break;
            case R.id.button4:
                buttonChecker(b4);
                break;
        }
    }

    private void buttonChecker(Button btnPressed) {
        if (btnPressed.getText().equals(tx1.getText())) {
            right();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    quiz();
                }
            }, 1000);
        } else
            wrong();
    }
}

