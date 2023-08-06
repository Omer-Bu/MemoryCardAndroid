package com.memorycardgame.views.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.memorycardgame.R;


public class ScoreBoardFragment extends Fragment {


    TextView easytv,hardtv;

    public ScoreBoardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_scoreboard, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HighScore",0);

        easytv =v.findViewById(R.id.escore);
        hardtv =v.findViewById(R.id.hscore);



        easytv.append(""+sharedPreferences.getInt("easy_high", (int) (EasyLevelFragment.best_time)));

        hardtv.append(""+sharedPreferences.getInt("hard_high", (int) (HardLevelFragment.best_time)));

        return v;
    }

}
