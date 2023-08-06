package com.memorycardgame.views.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity {

    //attach fragment on the activity
    public void transitFrag(int fragmentId, Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentId, fragment);
        transaction.commit();
    }
}
