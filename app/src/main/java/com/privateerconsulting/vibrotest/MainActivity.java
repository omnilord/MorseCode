package com.privateerconsulting.vibrotest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final long OVERHEAD_ESTIMATE = 20L;

    Button btnStart;
    TextView edit, disp;
    Vibrator v;
    CameraManager cm;
    String cam;


    class MorseCodeTask extends AsyncTask<MorseCharacter, MorseCharacter, Void> {

        @Override
        protected Void doInBackground(MorseCharacter... queue) {
            for (MorseCharacter ch : queue) {
                publishProgress(ch);
                if (isCancelled()) { break; }
                try {
                    Thread.sleep(ch.duration + OVERHEAD_ESTIMATE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        protected void onProgressUpdate(MorseCharacter... values) {
            boolean live = false;
            MorseCharacter ch = values[0];
            disp.setText(disp.getText() + "  " + ch.series);

            for (long dit : ch.intervals) {
                if (live) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            cm.setTorchMode(cam, true);
                            if (v.hasVibrator()) {
                                v.vibrate(dit);
                            }
                            cm.setTorchMode(cam, false);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    Thread.sleep(dit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                live = !live;
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        edit = (TextView) findViewById(R.id.editText);
        disp = (TextView) findViewById(R.id.textView);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        cm = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                cam = cm.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

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
