package com.privateerconsulting.vibrotest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    TextView edit, disp;
    Vibrator v;

    class MorseCodeTask extends AsyncTask<MorseCharacter, MorseCharacter, Void> {

        @Override
        protected Void doInBackground(MorseCharacter... queue) {
            for (MorseCharacter ch : queue) {
                publishProgress(ch);
                if (isCancelled()) { break; }
                try {
                    Thread.sleep(ch.duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(MorseCharacter... values) {
            MorseCharacter ch = values[0];
            disp.setText(ch.series);
            if (v.hasVibrator()) {
                v.vibrate(ch.intervals, -1);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        edit = (TextView) findViewById(R.id.editText);
        disp = (TextView) findViewById(R.id.textView);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence chs = edit.getText();
                ArrayList<MorseCharacter> queue = encode(chs);
                new MorseCodeTask().execute(queue.toArray(new MorseCharacter[queue.size()]));
            }
        });
    }

    protected ArrayList<MorseCharacter> encode(CharSequence chs) {
        ArrayList<MorseCharacter> queue = new ArrayList<>();
        long preInterval = 0L;

        for (int i = 0; i < chs.length(); i++) {
            char ch = Character.toUpperCase(chs.charAt(i));

            disp.setText("");

            try {
                if (MorseCodes.CODES.containsKey(ch)) {
                    queue.add(new MorseCharacter(MorseCodes.CODES.get(ch), preInterval));
                    preInterval = MorseCodes.CHAR;
                } else if (ch == '.') {
                    preInterval = MorseCodes.STOP;
                } else {
                    preInterval = MorseCodes.WORD;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return queue;
    }
}
