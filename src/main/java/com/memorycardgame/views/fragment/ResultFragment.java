package com.memorycardgame.views.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.memorycardgame.R;

import org.jetbrains.annotations.NotNull;


public class ResultFragment extends Fragment {


    SharedPreferences.Editor editor;

    int topScoreeasy;
    int topscorehard;
    SharedPreferences pref;
    TextView newtopscore;
    TextView tv1,tv2;
    static TextView totaltime;

    public ResultFragment() {
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_end, container, false);


        pref = getActivity().getSharedPreferences("HighScore",0);
        editor= pref.edit();


        newtopscore = rootView.findViewById(R.id.top);
        tv1 = rootView.findViewById(R.id.tv1);
        tv2 = rootView.findViewById(R.id.tv2);
        totaltime = rootView.findViewById(R.id.totaltime);


        topScoreeasy = pref.getInt("easy_high",50);
        topscorehard = pref.getInt("hard_high",50);

        Bundle b=getArguments();


        if (b.getString("Data").equals("win")){


            if (Integer.valueOf(b.get("level").toString()) == 0){
                if (Integer.valueOf(b.get("Time").toString()) < topScoreeasy){


                    editor.putInt("easy_high",Integer.valueOf(b.get("Time").toString())).apply();

                    getString(R.string.time);
                    newtopscore.setText(getString(R.string.new_high_score));
                }
            }
            else if (Integer.valueOf(b.get("level").toString()) == 1){

                if (Integer.valueOf(b.get("Time").toString()) < topscorehard){

                    editor.putInt("hard_high",Integer.valueOf(b.get("Time").toString())).apply();

                    newtopscore.setText(getString(R.string.new_high_score));
                }
            }


            tv1.setText(getString(R.string.won));
            tv2.setText(getString(R.string.well_done));

            totaltime.setText(getString(R.string.time)+" :"+b.get("Time").toString());
        }
        else{

            tv1.setText(getString(R.string.lost));
            tv2.setText(getString(R.string.nice_try));

            totaltime.setText(getString(R.string.time)+" :"+b.get("Time").toString());
        }

        return rootView;
    }

}
