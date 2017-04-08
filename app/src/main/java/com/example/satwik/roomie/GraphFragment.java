package com.example.satwik.roomie;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by satwik on 08-04-2017.
 */
public class GraphFragment extends Fragment {
    BarChart barChart;
    String[] name = {"Satwik","Daniel"};
    String owes[] = {"340","200"};


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.graph_frag,container,false);
        barChart = (BarChart) view.findViewById(R.id.bar_graph);
        ArrayList<String> names = new ArrayList<>();
        ArrayList<BarEntry> barEntries =new ArrayList<>();
int count =0;
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        for(String i:StatsFragment.recievable.keySet())
        {
            names.add(i);

            int num=Integer.parseInt(sharedPreferences.getString(i,"ppp"));
            Log.e("LUCKY",""+num);
            barEntries.add(new BarEntry(num,count));
            count++;
        }


        BarDataSet barDataSet  = new BarDataSet(barEntries,"Money Owed");

        BarData data = new BarData(names,barDataSet);
        barChart.setData(data);

        barChart.setTouchEnabled(true);
        barChart.setDrawBarShadow(true);
        barChart.setDrawValueAboveBar(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);

        return view;
    }
}
