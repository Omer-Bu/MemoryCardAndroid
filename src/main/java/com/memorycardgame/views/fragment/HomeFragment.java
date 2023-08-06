package com.memorycardgame.views.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.memorycardgame.R;
import com.memorycardgame.views.activity.LanguageActivity;

import java.util.Calendar;


//this is the home fragment
public class HomeFragment extends BaseFragment {

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //attach xml file with java class
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //initialize views on which we can set the listeners and navigate on other screens
        TextView easyTextView = rootView.findViewById(R.id.easyTv);
        TextView hardTextView = rootView.findViewById(R.id.hardTv);
        TextView scoreboardTv = rootView.findViewById(R.id.scoreboardTv);
        TextView langTv = rootView.findViewById(R.id.langTv);
        ImageView ivCall = rootView.findViewById(R.id.iv_call);
        ImageView ivMsg = rootView.findViewById(R.id.iv_message);
        CardView addEventToCalendar = rootView.findViewById(R.id.add_event);
        CardView addAlarmToCalendar = rootView.findViewById(R.id.add_alarm);
        //open calendar
        addAlarmToCalendar.setOnClickListener(v -> {
            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            startActivity(i);
//            Intent intent = new Intent(
//                    ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
//                    Uri.parse("tel:" + ""));
//            intent.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
//            startActivity(intent);
        });

        //adding events to calendar
        addEventToCalendar.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("allDay", false);
            intent.putExtra("rrule", "FREQ=YEARLY");
            intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
            intent.putExtra("title", "An Event from memory card game app");
            startActivity(intent);
        });

        //for call action dial
        ivCall.setOnClickListener(v -> {
            Uri number = Uri.parse("tel:+9724088399");
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);

        });

        //for action message
        ivMsg.setOnClickListener(v -> {
            Uri uri = Uri.parse("smsto:+9724088399");
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra("sms_body", "");
            startActivity(it);
        });
        //attach the listener
        hardTextView.setOnClickListener(v -> transitFrag(R.id.fragment, new HardLevelFragment()));
        //attach the listeners
        easyTextView.setOnClickListener(v -> transitFrag(R.id.fragment, new EasyLevelFragment()));
        //attach the listeners
        langTv.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LanguageActivity.class);
            startActivity(intent);
        });
        //attach the listeners
        scoreboardTv.setOnClickListener(v -> transitFrag(R.id.fragment, new ScoreBoardFragment()));

        return rootView;
    }


}
