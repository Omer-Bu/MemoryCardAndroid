package com.memorycardgame.views.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.memorycardgame.R;

import java.util.Random;

public class BaseFragment extends Fragment {

    public void transitFrag(int fragmentId, Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(fragmentId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void transitFrag(int fragmentId, Fragment fragment, Bundle bundle){
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(fragmentId, fragment);
        fragmentTransaction.commit();
    }

    public void shuffleTheCards(int cards[], int n){
        Random rand = new Random();
        for (int i=0;i<n;i++){
            int r = rand.nextInt(n-i);

            int temp = cards[r];
            cards[r] = cards[i];

            cards[i] = temp;
        }
    }
}
