package com.i_mehemmed.walking;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class history_fragment extends Fragment {

    SharedPreferences sharedPreferencesleng , sharedPreferenceskonum , go_map;
    SharedPreferences.Editor go_maps_editor;
    int leng =0;
    ArrayList<String> date_time_text = new ArrayList<>();
    ListView listView ;
    Button exit_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history_fragment, container, false);



        listView = v.findViewById(R.id.hsitory_list);
        exit_btn = v.findViewById(R.id.history_exit_btn);


        exit_btn.setOnClickListener(view -> {
           // startActivity(new Intent(this , home.class));
        });

        sharedPreferencesleng = getActivity().getSharedPreferences("shard_history_leng"  , MODE_PRIVATE);
        leng = sharedPreferencesleng.getInt("history_leng" , 0);
        for(int i = 0; i<leng ; i++){
            sharedPreferenceskonum = getActivity().getSharedPreferences(String.valueOf("shared_history_konum"+i)  , MODE_PRIVATE);
            date_time_text.add(sharedPreferenceskonum.getString("history_date" , ""));
        }

        history_Adapter history_adapter = new history_Adapter(getActivity() , date_time_text);
        listView.setAdapter(history_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                go_map = getActivity().getSharedPreferences("go_konum" , MODE_PRIVATE);
                go_maps_editor = go_map.edit();
                go_maps_editor.putBoolean("true"  , true);
                go_maps_editor.putInt("position" , i);
                go_maps_editor.commit();

                Navigation.findNavController(view ).navigate(R.id.hsitory_home_gecis);

            }
        });


        return v;
    }
}