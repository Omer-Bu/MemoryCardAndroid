package com.memorycardgame.views.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.memorycardgame.R;

//First screen when your app runs
public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //these are some animations which we implement on first screen and there are some delays in seconds
        YoYo.with(Techniques.RollIn)
                .duration(1000)
                .repeat(0)
                .playOn(view.findViewById(R.id.top_layout));

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> YoYo.with(Techniques.Tada)
                .duration(2000)
                .repeat(2)
                .playOn(view.findViewById(R.id.top_layout)), 1100);
        handler.postDelayed(() -> {
            view.findViewById(R.id.bottom_layout).setVisibility(View.VISIBLE);
            YoYo.with(Techniques.BounceInUp)
                    .duration(1000)
                    .repeat(0)
                    .playOn(view.findViewById(R.id.bottom_layout));
        }, 1100);


        handler.postDelayed(() -> {
            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            final HomeFragment r= new HomeFragment();
            fragmentTransaction.replace(R.id.fragment, r);
            fragmentTransaction.commit();
        }, 5000);

    }
}