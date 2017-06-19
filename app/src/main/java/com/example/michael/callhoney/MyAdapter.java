package com.example.michael.callhoney;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Michael on 19/06/2017.
 */

public class MyAdapter extends ArrayAdapter<GestureModel> {

   public MyAdapter(Context context, ArrayList<GestureModel> models) {
       super(context, 0 , models);
   }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GestureModel model = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.icon);
        TextView textView = convertView.findViewById(R.id.label);
        imageView.setImageBitmap(model.picture);
        textView.setText(model.name);

        return convertView;
    }
}
