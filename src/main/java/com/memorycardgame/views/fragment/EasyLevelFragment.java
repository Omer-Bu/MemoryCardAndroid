package com.memorycardgame.views.fragment;

import static com.memorycardgame.utils.Utils.memoryCardImagesArray;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.memorycardgame.views.adapter.CardsAdapter;
import com.memorycardgame.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;


public class EasyLevelFragment extends BaseFragment {

    EasyFlipView easyFlipView;
    RecyclerView memoryCardRecyclerView;
    TextView topScoreTv, counter;
    public long tomeLeft;
    public boolean pause, canceled;
    Bundle bundle;
    int count;

    int position;
    int topScore;
    public long time_esy = 50000;
    static long best_time = 50;

    int interval = 1000;
    public ArrayList<Integer> cardsArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_easy_level, container, false);

        canceled = false;
        pause = false;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HighScore", 0);
        memoryCardRecyclerView = rootView.findViewById(R.id.easylevelview);
        topScoreTv = rootView.findViewById(R.id.topscoreeasy);
        counter = rootView.findViewById(R.id.counterr);
        cardsArrayList = new ArrayList<>();
        RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
        memoryCardRecyclerView.setLayoutManager(lm);


        bundle = new Bundle();
        bundle.putInt("level", 0);


        topScore = sharedPreferences.getInt("easy_high", (int) (EasyLevelFragment.best_time));

        topScoreTv.append(topScore + "");
        //shuffle cards
        shuffleTheCards(memoryCardImagesArray, 12);
        shuffleTheCards(memoryCardImagesArray, 12);
        for (int cd : memoryCardImagesArray) {
            cardsArrayList.add(cd);
        }

        //add cards
        memoryCardRecyclerView.setAdapter(new CardsAdapter(cardsArrayList));
        callWhenTimerTick();
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        //On touch flip the card
        memoryCardRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {
                whenInterceptTouchEvents(rv, e);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        // if user click back button
        rootView.setOnKeyListener(this::ifUserClicksBackBtn);
        return rootView;
    }

    //if user clicks the back button we call this method
    private Boolean ifUserClicksBackBtn(View v, int keyCode, KeyEvent event){


        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            pause = true;

            AlertDialog.Builder pause = new AlertDialog.Builder(getContext());
            pause.setTitle("Exit");
            pause.setMessage("Do you want to go Main menu");
            pause.setCancelable(false);
            pause.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EasyLevelFragment.this.pause = false;
                    new CountDownTimer(tomeLeft, interval) {
                        int time;
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (EasyLevelFragment.this.pause || canceled) {
                                cancel();
                            } else {
                                counter.setText("Time : " + millisUntilFinished / interval);
                                tomeLeft = millisUntilFinished;

                                if (count == 12) {

                                    bundle.putString("Data", "win");

                                    time = (int) ((time_esy - millisUntilFinished) / interval);

                                    bundle.putInt("Time", time);
                                    cancel();

                                    this.onFinish();
                                }
                            }
                        }

                        @Override
                        public void onFinish() {
                            if (count < 12) {
                                bundle.putString("Data", "lost");
                                bundle.putInt("Time", (int) (time_esy / interval));
                            }
                            transitFrag(R.id.fragment, new ResultFragment(), bundle);
                        }
                    }.start();
                }
            });

            pause.setNegativeButton("Quit", (dialog, which) -> {
                canceled = true;
                getFragmentManager().popBackStack();
            });
            pause.show();
            return true;
        }
        return false;
    }


    //when user flip the card this code run and match if they both same or not
    private void whenInterceptTouchEvents(RecyclerView rv, MotionEvent e) {
        final View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null) {

            final int position = rv.getChildAdapterPosition(child);

            if (easyFlipView == null) {


                easyFlipView = (EasyFlipView) child;

                EasyLevelFragment.this.position = position;
            } else {
                if (EasyLevelFragment.this.position == position) {
                    easyFlipView = null;
                } else {
                    if (cardsArrayList.get(EasyLevelFragment.this.position).equals(cardsArrayList.get(position))) {

                        ((EasyFlipView) child).setOnFlipListener((easyFlipView, newCurrentSide) -> {
                            for (int i = 0; i < memoryCardRecyclerView.getChildCount(); i++) {

                                EasyFlipView child1 = (EasyFlipView) memoryCardRecyclerView.getChildAt(i);
                                child1.setEnabled(false);
                            }
                            new Handler().postDelayed(() -> {
                                EasyLevelFragment.this.easyFlipView.setVisibility(View.GONE);
                                child.setVisibility(View.GONE);
                                child.setEnabled(false);
                                EasyLevelFragment.this.easyFlipView.setEnabled(false);
                                EasyLevelFragment.this.easyFlipView = null;
                                count += 2;

                                for (int i = 0; i < memoryCardRecyclerView.getChildCount(); i++) {

                                    EasyFlipView child1 = (EasyFlipView) memoryCardRecyclerView.getChildAt(i);
                                    child1.setEnabled(true);
                                }
                                ((EasyFlipView) child).setOnFlipListener(null);
                            }, 200);//timer
                        });
                    } else {
                        ((EasyFlipView) child).setOnFlipListener((easyFlipView, newCurrentSide) -> {
                            for (int i = 0; i < memoryCardRecyclerView.getChildCount(); i++) {
                                EasyFlipView child1 = (EasyFlipView) memoryCardRecyclerView.getChildAt(i);
                                child1.setEnabled(false);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    EasyLevelFragment.this.easyFlipView.flipTheView();
                                    ((EasyFlipView) child).flipTheView();
                                    EasyLevelFragment.this.easyFlipView = null;
                                    ((EasyFlipView) child).setOnFlipListener(null);

                                    for (int i = 0; i < memoryCardRecyclerView.getChildCount(); i++) {
                                        EasyFlipView child1 = (EasyFlipView) memoryCardRecyclerView.getChildAt(i);
                                        child1.setEnabled(true);
                                    }
                                }
                            }, 100);
                        });
                    }
                }

            }
        }
    }

    //this calls when timer ticks
    private void callWhenTimerTick() {
        //Stopping the time on top if game is paused or cancelled
        new CountDownTimer(time_esy, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (canceled || pause) {


                    cancel();
                } else {

                    counter.setText("Time : " + millisUntilFinished / interval);

                    tomeLeft = millisUntilFinished;

                    if (count == 12) {


                        bundle.putString("Data", "win");

                        long time = (time_esy - millisUntilFinished) / interval;
                        if (time < best_time) {
                            best_time = time;
                        }

                        bundle.putInt("Time", (int) time);

                        cancel();

                        this.onFinish();

                    }

                }
            }

            @Override
            public void onFinish() {
                if (count < 12) {
                    bundle.putString("Data", "lost");
                    bundle.putInt("Time", (int) (time_esy / interval));
                }
                transitFrag(R.id.fragment, new ResultFragment(), bundle);

            }
        }.start();

    }


}
