package com.example.michael.callhoney;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
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

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

        gesOverlay = (GestureOverlayView) findViewById(R.id.myGesture1);
        gesOverlay.addOnGesturePerformedListener(new MyListener(this));

    }

    class MyListener implements GestureOverlayView.OnGesturePerformedListener {
        private Context context;

        public MyListener(Context context) {
            this.context = context;
        }

        @Override
        public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {

            gesPath = new File(getFilesDir(), "gestures").getPath();
            gesLib = GestureLibraries.fromFile(gesPath);

            gesLib.load();

            ArrayList<Prediction> predictions = gesLib.recognize(gesture);
            if (predictions.size() > 0) {
                Prediction prediction = predictions.get(0);
                if (prediction.score > 1.0) {
                    String phone = prediction.name;
                    if (phone != "") {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));

                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                            return;
                        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mybutton:
                startActivity(new Intent(this, AddGesturesActivity.class));
                break;
            case R.id.mybuton2:
                startActivity(new Intent(this, GesturesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

