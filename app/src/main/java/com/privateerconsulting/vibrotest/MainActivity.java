package com.privateerconsulting.vibrotest;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnFlash;
    TextView edit, disp;
    Vibrator v;
    CameraManager cm;
    String cam = null;
    boolean flashOn = false;


    class vHandler extends Handler {
        public vHandler(Looper L) {
            super(L);
        }

        public void handleMessage(Message msg) {
            final MorseCharacter ch = (MorseCharacter) msg.obj;
            if (v.hasVibrator()) {
                runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          v.vibrate(ch.intervals, -1);
                      }
                });
            }
        }
    }


    class fHandler extends Handler {
        public fHandler(Looper L) {
            super(L);
        }

        public void handleMessage(Message msg) {
            final MorseCharacter ch = (MorseCharacter) msg.obj;

            if (cam != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    runOnUiThread(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void run() {
                            try {
                                long[] series = ch.intervals;
                                for (long interval : series) {
                                    cm.setTorchMode(cam, flashOn);
                                    flashOn = !flashOn;
                                    Thread.sleep(interval);
                                }
                                flashOn = false;
                                cm.setTorchMode(cam, false);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            }
        }
    }


    protected class MorseCodeTaskHandler<T extends Handler>  {

        HandlerThread thread;
        Looper looper;
        T handle;

        public MorseCodeTaskHandler(String name, Class<T> cls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            thread = new HandlerThread(name);
            thread.start();
            looper = thread.getLooper();
            handle = cls.getDeclaredConstructor(Looper.class).newInstance(looper);
        }

        void msg(MorseCharacter ch) {
            Message msg = handle.obtainMessage();
            msg.obj = ch;
            handle.sendMessage(msg);
        }
    }


    class MorseCodeTask extends AsyncTask<MorseCharacter, MorseCharacter, Void> {
        @Override
        protected Void doInBackground(MorseCharacter... queue) {
            try {
                MorseCodeTaskHandler<vHandler> vThread = new MorseCodeTaskHandler<>("Vibrating", vHandler.class);
                MorseCodeTaskHandler<fHandler> fThread = new MorseCodeTaskHandler<>("Flashing", fHandler.class);

                for (MorseCharacter ch : queue) {
                    publishProgress(ch);

                    if (v.hasVibrator()) {
                        vThread.msg(ch);
                    }

                    if (cam != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        fThread.msg(ch);
                    }

                    if (isCancelled()) {
                        break;
                    }

                    try {
                        Thread.sleep(ch.duration + 10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(MorseCharacter... queue) {
            MorseCharacter ch = queue[0];
            disp.setText(ch.series);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnStart = (Button) findViewById(R.id.btnStart);
        btnFlash = (Button) findViewById(R.id.btnFlash);
        edit = (TextView) findViewById(R.id.editText);
        disp = (TextView) findViewById(R.id.textView);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        cm = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cam = cm.getCameraIdList()[0];
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
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
