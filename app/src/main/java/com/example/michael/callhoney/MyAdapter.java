package com.example.michael.callhoney;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import java.io.File;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Michael on 19/06/2017.
 */

public class MyAdapter extends ArrayAdapter<GestureModel> {

    private ArrayList modelsData;

   public MyAdapter(Context context, ArrayList<GestureModel> models) {
       super(context, 0 , models);
       modelsData = models;
   }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final GestureModel model = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.icon);
        TextView textView = convertView.findViewById(R.id.label);
        imageView.setImageBitmap(model.picture);
        textView.setText(model.name);

        Button button  = convertView.findViewById(R.id.clear_button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                File gesFile = new File(getContext().getFilesDir(), "gestures");
                GestureLibrary lib = GestureLibraries.fromFile(gesFile);
                if (gesFile.exists()) {
                    if (lib.load()) {
                        Set<String> en = lib.getGestureEntries();
                        if (en.contains(model.name)) {
                            ArrayList<Gesture> al = lib.getGestures(model.name);
                            for (int i = 0; i < al.size(); i++) {
                                lib.removeGesture(model.name, al.get(i));
                                lib.save();
                            }
                        }
                    } else {

                    }
                    modelsData.remove(position);
                    MyAdapter.this.notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }
}
