package com.mehemmed_i.walking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;

import java.util.ArrayList;

public class history_Adapter extends ArrayAdapter<String> {

    Context context;
   ArrayList<String>date = new ArrayList<>();
View view;
    public history_Adapter(Context context , ArrayList<String> date , View v ){
        super(context , R.layout.history_design ,  R.id.historydesign_time_text,date);
        this.date = date;
        this.context = context;
        this.view = v;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View v = LayoutInflater.from(context).inflate(R.layout.history_design , null);

        TextView historydesign_time_text = v.findViewById(R.id.historydesign_time_text);
        CardView cardView = v.findViewById(R.id.designcardview);
        historydesign_time_text.setText(date.get(position));
        cardView.setTag(position);

        cardView.setOnClickListener(view1 -> {
            history_fragmentDirections.HistoryMapsGecis directions = history_fragmentDirections.historyMapsGecis();
            directions.setRunOrMap(true);
            directions.setId(position);
            Navigation.findNavController(view ).navigate(directions);
        });

       return  v;
    }
}
