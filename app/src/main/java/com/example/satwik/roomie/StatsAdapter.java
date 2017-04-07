package com.example.satwik.roomie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by satwik on 07-04-2017.
 */

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {
    ArrayList<Stats>  stat = new ArrayList<>();

    public StatsAdapter(ArrayList<Stats> stat)
    {
        this.stat = stat;
    }
    @Override
    public StatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_frag,parent,false);
        StatsViewHolder statsViewHolder = new StatsViewHolder(view);
        return statsViewHolder;
    }

    @Override
    public void onBindViewHolder(StatsViewHolder holder, int position) {

        Stats obj = stat.get(position);
        holder.user1.setText(obj.getUser1());
        holder.user2.setText(obj.getUser2());
        holder.owes.setText(obj.getOwes());
    }

    @Override
    public int getItemCount() {
        return stat.size();
    }

    public static class StatsViewHolder extends RecyclerView.ViewHolder
    {

        TextView user1,user2,owes;
        public StatsViewHolder(View itemView) {
            super(itemView);

            user1 = (TextView)itemView.findViewById(R.id.textView7);
            user2 = (TextView)itemView.findViewById(R.id.textView8);
            owes = (TextView)itemView.findViewById(R.id.textView6);



        }
    }
}
