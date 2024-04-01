package com.imehemmed.walking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.airbnb.lottie.L;
import com.imehemmed.walking.Room.MyroomDatabase;
import com.imehemmed.walking.Room.RunHistory;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryFragment extends Fragment {

    int leng =0;

    ListView listView ;
    Button exitBtn;
    MyroomDatabase myroomDatabase;
    RunHistory[] runHistories;
    SharedPreferenceManager sharedPreferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history_fragment, container, false);
        myroomDatabase = MyroomDatabase.getINSTANCE(getActivity());
        sharedPreferenceManager  = new SharedPreferenceManager(getActivity());

        listView = v.findViewById(R.id.hsitory_list);
        exitBtn = v.findViewById(R.id.history_exit_btn);
        leng  = sharedPreferenceManager.getInt("id" , 0);

       runHistories = myroomDatabase.historyDao().readHistoryAll();

        exitBtn.setOnClickListener(view -> {
            Navigation.findNavController(view ).navigate(R.id.hsitory_home_gecis);
        });

        RunHistoryAdapter runHistory = new RunHistoryAdapter(getActivity() ,runHistories  ,v);
        listView.setAdapter(runHistory);
        return v;
    }

}