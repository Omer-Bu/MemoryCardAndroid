package com.memorycardgame.views.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.memorycardgame.R;

import java.util.ArrayList;

//this is recycler adapter
public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    private ArrayList<Integer> cdF;

    public CardsAdapter(ArrayList<Integer> cardFront) {

        this.cdF = cardFront;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);

        v.setMinimumWidth(parent.getMeasuredWidth() / 3);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.cdfront.setImageResource(cdF.get(position));
    }

    @Override
    public int getItemCount() {
        return cdF.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{



        private ImageView cdfront;


        public ViewHolder(View itemView) {
            super(itemView);



            cdfront = itemView.findViewById(R.id.cdfront);
        }
    }
}
