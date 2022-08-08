package com.i_mehemmed.walking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class history_Adapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> date_time_text = new ArrayList<>();
    public history_Adapter(Context context , ArrayList<String> date_time_text){
        super(context , R.layout.history_design ,  R.id.historydesign_time_text,date_time_text);
        this.context = context;
        this.date_time_text = date_time_text;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View v = LayoutInflater.from(context).inflate(R.layout.history_design , null);
        TextView historydesign_time_text = v.findViewById(R.id.historydesign_time_text);
        historydesign_time_text.setText(date_time_text.get(position));


       return  v;
    }
}
