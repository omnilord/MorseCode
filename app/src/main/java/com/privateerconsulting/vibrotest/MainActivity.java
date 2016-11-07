package com.privateerconsulting.vibrotest;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    TextView edit;
    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        edit = (TextView) findViewById(R.id.editText);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v.hasVibrator()) {
                    CharSequence chs = edit.getText();
                    long[] pat = MorseCodes.encode(chs);
                    String o = "";
                    if (pat.length > 0) {
                        v.vibrate(pat, -1);
                    }
                }
            }
        });
    }
}
