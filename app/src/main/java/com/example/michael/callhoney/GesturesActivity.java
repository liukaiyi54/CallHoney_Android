package com.example.michael.callhoney;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class GesturesActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList gestures;
    private File gesFile;
    private GestureLibrary lib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestures);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mListView = (ListView)this.findViewById(R.id.myListView);

        loadGestures();

        MyAdapter adapter = new MyAdapter(this, gestures);

        mListView.setAdapter(adapter);
    }

    private void loadGestures() {
        gestures  = new ArrayList<>();
        gesFile = new File(Environment.getExternalStorageDirectory(), "gestures").getAbsoluteFile();
        try {
            lib = GestureLibraries.fromFile(gesFile);

            if (gesFile.exists()) {
                if (!lib.load()) {

                } else {
                    Object[] en = lib.getGestureEntries().toArray();
                    for (int i = 0;i < en.length; i++) {
                        ArrayList<Gesture> al = lib.getGestures(en[i].toString());
                        for (int j = 0; j < al.size(); j++) {
                            String name = en[i].toString();
                            Gesture gs = al.get(j);
                            Bitmap pic = gs.toBitmap(64, 64, 12, Color.GREEN);
                            GestureModel model = new GestureModel(name, pic);
                            gestures.add(model);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
