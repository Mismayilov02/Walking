package com.mehemmed_i.walking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.mehemmed_i.walking.Room.MyroomDatabase;

import java.util.ArrayList;

public class history_fragment extends Fragment {

    int leng =0;

    ListView listView ;
    Button exit_btn;
    MyroomDatabase myroomDatabase;
    ArrayList<String>date = new ArrayList<>();
    SharedPreferenceManager sharedPreferenceManager;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history_fragment, container, false);
        myroomDatabase = MyroomDatabase.getINSTANCE(getActivity());
        sharedPreferenceManager  = new SharedPreferenceManager(getActivity());

        history_fragmentDirections.HistoryMapsGecis directions = history_fragmentDirections.historyMapsGecis();

        listView = v.findViewById(R.id.hsitory_list);
        exit_btn = v.findViewById(R.id.history_exit_btn);
        leng  = sharedPreferenceManager.getInt("id" , 0);

        for(int i = leng-1 ; 0<=i; i--){
            date.add(sharedPreferenceManager.getString("date"+i , "15"));
        }

        exit_btn.setOnClickListener(view -> {
            Navigation.findNavController(view ).navigate(R.id.hsitory_home_gecis);
        });

//        History[]history = myroomDatabase.history_dao().read_history_all();
        history_Adapter history_adapter = new history_Adapter(getActivity() ,date  ,v);
        listView.setAdapter(history_adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                directions.setPosition(i);
//                directions.setRunOrMap(true);
//                Navigation.findNavController(view ).navigate(directions);
//
//            }
//        });


        return v;
    }

}