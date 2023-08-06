package com.memorycardgame.views.fragment;

import static com.memorycardgame.utils.Utils.memoryCardHardImagesArray;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.memorycardgame.views.adapter.CardsAdapter;
import com.memorycardgame.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;


//this is the screen of hard level
public class HardLevelFragment extends BaseFragment {


    //some local variables we made in a global of the class
    TextView topScoreTv, counter;
    private RecyclerView memoryCardRecyclerView;
    EasyFlipView easyFlipView;
    Bundle bundle;
    long tomeLeft;
    static long best_time=40;
    boolean pause;
    boolean cancel;
    int count;
    ArrayList<Integer> cardsArrayList;

    int top_scrore;
    int position;
    public long time_hd = 40000;
    int interval = 1000;


    // on create view method of fragment lifecycle it calls automatically when app starts
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_hard_level, container, false);
        cancel = false;
        pause = false;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HighScore",0);
        memoryCardRecyclerView = v.findViewById(R.id.hardlevelview);
        topScoreTv = v.findViewById(R.id.topscore);
        counter = v.findViewById(R.id.counter);
        RecyclerView.LayoutManager lm= new GridLayoutManager(getContext(),4, LinearLayoutManager.VERTICAL,false);
        memoryCardRecyclerView.setLayoutManager(lm);
        cardsArrayList = new ArrayList<>();
        bundle =new Bundle();
        bundle.putInt("level",1);
        top_scrore = sharedPreferences.getInt("hard_high", (int)(HardLevelFragment.best_time));
        topScoreTv.append(top_scrore +"");

        //card shuffle here
        shuffleTheCards(memoryCardHardImagesArray,16);
        shuffleTheCards(memoryCardHardImagesArray,16);
        for (int cd : memoryCardHardImagesArray){
            cardsArrayList.add(cd);
        }

        //add cards
        memoryCardRecyclerView.setAdapter(new CardsAdapter(cardsArrayList));
        pause = false;
        cancel = false;
        callWhenTimerTick();
        v.setFocusableInTouchMode(true);
        v.requestFocus();


        //On touch flip the card
        memoryCardRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {
                whenInterceptTouchEvents(rv, e);
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });


        // if user click back button
        v.setOnKeyListener(this::ifUserClicksBackBtn);

        return v;
    }

    //if user clicks the back button we call this method
    private Boolean ifUserClicksBackBtn(View v, int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
            pause = true;
            AlertDialog.Builder pause = new AlertDialog.Builder(getContext());
            pause.setTitle("Exit");
            pause.setMessage("Do you want to go Main menu");
            pause.setCancelable(false);
            pause.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HardLevelFragment.this.pause = false;


                    new CountDownTimer(tomeLeft,interval){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (HardLevelFragment.this.pause || cancel){
                                cancel();
                            }
                            else {
                                ((TextView) v.findViewById(R.id.counter)).setText("Time : " + millisUntilFinished / interval);
                                tomeLeft = millisUntilFinished;
                                if (count == 16) {

                                    bundle.putString("Data", "win");

                                    long time = (time_hd - millisUntilFinished)/ interval;
                                    if(time<best_time)
                                    {
                                        best_time=time;
                                    }
                                    bundle.putInt("Time", (int)time);

                                    cancel();

                                    this.onFinish();

                                }
                            }
                        }

                        @Override
                        public void onFinish() {
                            callWhenTimerFinish();
                        }
                    }.start();
                }
            });
            pause.setNegativeButton("Quit", (dialog, which) -> {
                cancel =true;
                getFragmentManager().popBackStack();
            });
            pause.show();
            return true;
        }
        return false;
    }

    //when we touch on recycler view items this call
    //when user flip the card this code run and match if they both same or not
    private void whenInterceptTouchEvents(RecyclerView rv, MotionEvent e){
        final View child = rv.findChildViewUnder(e.getX(),e.getY());
        if (child != null){

            final int position = rv.getChildAdapterPosition(child);

            if (easyFlipView == null){

                easyFlipView = (EasyFlipView) child;

                HardLevelFragment.this.position = position;
            }

            else{
                if (HardLevelFragment.this.position == position){
                    easyFlipView =null;
                }
                else{
                    if (cardsArrayList.get(HardLevelFragment.this.position).equals(cardsArrayList.get(position))){

                        ((EasyFlipView) child).setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
                            @Override
                            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                                for (int i = 0; i < memoryCardRecyclerView.getChildCount(); i++) {

                                    EasyFlipView child1 = (EasyFlipView) memoryCardRecyclerView.getChildAt(i);
                                    child1.setEnabled(false);

                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        HardLevelFragment.this.easyFlipView.setVisibility(View.GONE);
                                        child.setVisibility(View.GONE);

                                        child.setEnabled(false);

                                        HardLevelFragment.this.easyFlipView.setEnabled(false);
                                        HardLevelFragment.this.easyFlipView =null;
                                        count+=2;

                                        for (int i = 0; i < memoryCardRecyclerView.getChildCount(); i++) {
                                            EasyFlipView child1 = (EasyFlipView) memoryCardRecyclerView.getChildAt(i);

                                            child1.setEnabled(true);
                                        }
                                        ((EasyFlipView) child).setOnFlipListener(null);
                                    }
                                },200);
                            }
                        });
                    }
                    else {
                        ((EasyFlipView) child).setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
                            @Override
                            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                                for (int i = 0; i < memoryCardRecyclerView.getChildCount(); i++) {

                                    EasyFlipView child1 = (EasyFlipView) memoryCardRecyclerView.getChildAt(i);

                                    child1.setEnabled(false);
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        HardLevelFragment.this.easyFlipView.flipTheView();
                                        ((EasyFlipView) child).flipTheView();

                                        HardLevelFragment.this.easyFlipView = null;

                                        ((EasyFlipView) child).setOnFlipListener(null);


                                        for (int i = 0; i < memoryCardRecyclerView.getChildCount(); i++) {
                                            EasyFlipView child1 = (EasyFlipView) memoryCardRecyclerView.getChildAt(i);
                                            child1.setEnabled(true);
                                        }
                                    }
                                }, 100);
                            }
                        });
                    }
                }

            }
        }
    }

    //this calls when timer ticks
    private void callWhenTimerTick(){
        //Stopping the time on top if game is paused or cancelled
        new CountDownTimer(time_hd,interval){
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                if (pause || cancel){
                    cancel();
                }
                else {
                    tomeLeft = millisUntilFinished;
                    counter.setText("Time : " + millisUntilFinished / interval);
                    if (count == 16) {
                        bundle.putString("Data", "win");
                        long time = (time_hd - millisUntilFinished)/ interval;
                        if(time<best_time)
                        {
                            best_time=time;
                        }

                        bundle.putInt("Time", (int) time);
                        cancel();
                        this.onFinish();
                    }
                }
            }

            @Override
            public void onFinish() {
                if (count < 16) {
                    bundle.putString("Data", "lost");
                    bundle.putInt("Time", (int) (time_hd /interval));
                }
                transitFrag(R.id.fragment, new ResultFragment(), bundle);
            }
        }.start();
    }

    //when timers finish this calls
    private void callWhenTimerFinish(){
        if (count < 16) {
            bundle.putString("Data", "lost");
            bundle.putInt("Time", (int) (time_hd /interval));
        }
        transitFrag(R.id.fragment, new ResultFragment(), bundle);
    }





}
