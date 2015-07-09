package com.andr0day.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class MyActivity extends Activity {

    TextView textView;

    volatile boolean b = true;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    textView.setText("AsyncTask1");
                    break;
                case 2:
                    textView.setText("AsyncTask2");
                    break;
            }
        }

    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView) findViewById(R.id.txt);
        new AsyncTask<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... params) {
                int i = 0;
                while (b && i < 10) {
                    try {
                        Thread.sleep(1000);
                        handler.sendEmptyMessage(1);
                        i++;
                    } catch (Exception e) {

                    }
                }
                return null;
            }
        }.execute();
    }

    public void onResume() {
        super.onResume();
        new AsyncTask<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... params) {
                b = false;
                handler.sendEmptyMessage(2);
                handler.removeMessages(1);
                return null;
            }
        }.execute();
    }
}
