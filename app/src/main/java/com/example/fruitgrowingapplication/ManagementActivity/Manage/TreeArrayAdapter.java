package com.example.fruitgrowingapplication.ManagementActivity.Manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fruitgrowingapplication.R;

import java.util.List;

public class TreeArrayAdapter extends ArrayAdapter<String> {

    List<Integer> images;
    String[] texts;

    public TreeArrayAdapter(@NonNull Context context, @NonNull String[] texts, List<Integer> images) {
        super(context, 0, texts);
        this.images = images;
        this.texts = texts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.dropdown_image_view);
        imageView.setImageResource(images.get(position));

        TextView textView = convertView.findViewById(R.id.dropdown_text_view);
        textView.setText(texts[position]);
        return convertView;
    }
}
