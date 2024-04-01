package com.imehemmed.walking;

import android.annotation.SuppressLint;
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

import com.imehemmed.walking.Room.MyroomDatabase;
import com.imehemmed.walking.Room.RunHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RunHistoryAdapter extends ArrayAdapter {

    Context context;
    RunHistory[] runHistories;
    View view;
    MyroomDatabase myroomDatabase;

    public RunHistoryAdapter(Context context, RunHistory[] runHistories, View v) {
        super(context, R.layout.history_design, R.id.historydesign_time_text, runHistories);
        this.runHistories = runHistories;
        this.context = context;
        this.view = v;
        myroomDatabase = MyroomDatabase.getINSTANCE(context);
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.history_design, null);

        int length = myroomDatabase.historyDetailDao().readHistorySizebyParentId(runHistories[position].getId());
        RunHistory runHistory = runHistories[position];
        TextView timeText = v.findViewById(R.id.historydesign_time_text);
        TextView distanceText = v.findViewById(R.id.historydesign_pointCount);
        CardView cardView = v.findViewById(R.id.designcardview);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
        distanceText.setText("Distance: " + length + " Points");
        timeText.setText(simpleDateFormat.format(runHistory.getStartTime()));
        cardView.setTag(position);

        cardView.setOnClickListener(view1 -> {
            HistoryFragmentDirections.HistoryMapsGecis directions = HistoryFragmentDirections.historyMapsGecis();
            directions.setRunOrMap(true);
            directions.setParentId(runHistory.getId());
            Navigation.findNavController(view).navigate(directions);
        });

        return v;
    }
}
