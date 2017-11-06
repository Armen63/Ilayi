package com.example.goro.quiztest;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.imeng);
        TextView tv1 = (TextView) findViewById(R.id.top500);

        Typeface face = Typeface.createFromAsset(getAssets(), "font/snap itc.ttf");
        Typeface face1 = Typeface.createFromAsset(getAssets(), "font/jakobctt-bold.ttf");

        tv.setTypeface(face);
        tv1.setTypeface(face1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MainActivity.this, MenuActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, 200);
    }

}
