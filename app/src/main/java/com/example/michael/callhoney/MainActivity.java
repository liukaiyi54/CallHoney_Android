package com.example.michael.callhoney;

import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GestureLibrary gesLib;
    private GestureOverlayView gesOverlay;
    private String gesPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "SD卡不存在!程序无法运行", Toast.LENGTH_LONG).show();
            finish();
        }

        gesPath = new  File(Environment.getExternalStorageDirectory(), "gestures").getAbsolutePath();
        File file = new File(gesPath);

        if (!file.exists()) {
            Toast.makeText(this, "gestures文件不存在!程序无法运行", Toast.LENGTH_LONG).show();
            finish();
        }

        gesLib = GestureLibraries.fromFile(gesPath);
        if (!gesLib.load()) {
            Toast.makeText(this, "gestures文件读取失败!程序无法运行", Toast.LENGTH_LONG).show();
            finish();
        }

        gesOverlay = (GestureOverlayView)findViewById(R.id.myGesture1);
        gesOverlay.addOnGesturePerformedListener(new MyListener(this));

    }

    class MyListener implements GestureOverlayView.OnGesturePerformedListener {
        private Context context;

        public MyListener(Context context) {
            this.context = context;
        }

        @Override
        public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
            ArrayList<Prediction> predictions = gesLib.recognize(gesture);
            if (predictions.size() > 0) {
                Prediction prediction = predictions.get(0);
                if (prediction.score > 1.0) {
                    String phone = prediction.name;
                    if (phone != "") {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this.context, "no match", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this.context, "no match", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickButton(View view) {
        Intent intent = new Intent(this, AddGesturesActivity.class);
        MainActivity.this.startActivity(intent);
    }

}

