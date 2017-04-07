package com.example.satwik.roomie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by satwik on 07-04-2017.
 */
public class StatsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private HashMap<String,HashMap<String,Integer>> recievable= new HashMap<String, HashMap<String, Integer>>();
    private HashMap<String,HashMap<String,Integer>> payable=new HashMap<String, HashMap<String, Integer>>();
    RecyclerView.LayoutManager layoutManager;
    String[] user1= {"Satwik","daniel","Ringe"};
    String[] user2 = {"Sid","Anisha","Raj"};
    String[] owes= {"Rs.200","Rs.400","Rs.77"};
    ArrayList<Stats> list = new ArrayList<Stats>();
    private DatabaseReference archiveRef= FirebaseDatabase.getInstance().getReference().child("/0000/members");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dsp:dataSnapshot.getChildren())
                {
                    Log.e("Hui","bby");
                    int total=0;
                    HashMap<String,Integer> temp=new HashMap<String,Integer>();
                    for(DataSnapshot in:dsp.child("/archive").getChildren())
                    {


                        temp.put(in.child("addedBy").getValue().toString(),total+(Integer.parseInt(in.child("itemPrice").getValue().toString()))*(Integer.parseInt(in.child("itemQuant").getValue().toString())));
                    }
                    recievable.put(dsp.getKey(),temp);

                }
                Log.e("Recievabby pristine17",recievable.get("pristine17").get("pristine17").toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        archiveRef.addValueEventListener(postListener);



        int  count= 0;
        for(String i : owes)
        {
            Stats s = new Stats(user1[count],user2[count],i);
             count++;
        list.add(s);
    }



}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.recycler_stas,container,false);
        recyclerView =(RecyclerView) view.findViewById(R.id.card_recycler_view2);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new StatsAdapter(list);
        recyclerView.setAdapter(adapter);




        return view;

    }
}
