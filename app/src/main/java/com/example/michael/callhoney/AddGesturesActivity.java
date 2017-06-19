package com.example.michael.callhoney;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import android.content.Intent;

public class AddGesturesActivity extends AppCompatActivity {
    private Gesture ges;
    private GestureLibrary lib;
    private GestureOverlayView overlay;
    private Button button01, button02, button03;
    private EditText et;
    private String gesPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_gestures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("添加手势");

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "SD卡不存在！程序无法运行", Toast.LENGTH_SHORT).show();
            finish();
        }

        et = (EditText) this.findViewById(R.id.editText);
        overlay = (GestureOverlayView) this.findViewById(R.id.myGesture1);
        button01 = (Button)this.findViewById(R.id.button1);
        button01.setEnabled(false);
        button02 = (Button)this.findViewById(R.id.button2);
        button03 = (Button)this.findViewById(R.id.button3);

        gesPath = new File(Environment.getExternalStorageDirectory(), "gestures").getAbsolutePath();

        et.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (ges != null && et.getText().length() != 0) {
                    button01.setEnabled(true);
                } else {
                    button01.setEnabled(false);
                }
                return false;
            }
        });

        overlay.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
            @Override
            public void onGestureStarted(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
                button01.setEnabled(false);
                ges = null;
            }

            @Override
            public void onGesture(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

            }

            @Override
            public void onGestureEnded(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
                ges = overlay.getGesture();
                if (ges != null && et.getText().length() != 0) {
                    button01.setEnabled(true);
                }
            }

            @Override
            public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {

            }
        });

        button01.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gesName = et.getText().toString();
                try {
                    File file = new File(gesPath);
                    lib = GestureLibraries.fromFile(gesPath);

                    if (!file.exists()) {
                        lib.addGesture(gesName, ges);
                        if (lib.save()) {
                            et.setText("");
                            button01.setEnabled(false);
                            overlay.clear(true);

                            Toast.makeText(AddGesturesActivity.this, "保存成功:" + gesPath, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddGesturesActivity.this, "保存失败：" + gesPath, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (!lib.load()) {
                            Toast.makeText(AddGesturesActivity.this, "读取失败：" + gesPath, Toast.LENGTH_LONG).show();
                        } else {
                            Set<String> en = lib.getGestureEntries();
                            if (en.contains(gesName)) {
                                ArrayList<Gesture> al = lib.getGestures(gesName);
                                for (int i = 0; i < al.size(); i++) {
                                    lib.removeGesture(gesName, al.get(i));
                                }
                            }
                            lib.addGesture(gesName, ges);
                            if (lib.save()) {
                                et.setText("");
                                button01.setEnabled(false);
                                overlay.clear(true);
                                Toast.makeText(AddGesturesActivity.this, "保存成功：" + gesPath, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddGesturesActivity.this, "保存失败：" + gesPath, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button02.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                et.setText("");
                button01.setEnabled(false);
                overlay.clear(true);
            }
        });

        button03.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddGesturesActivity.this, GesturesActivity.class);
                startActivity(intent);
            }
        });
    }
}
