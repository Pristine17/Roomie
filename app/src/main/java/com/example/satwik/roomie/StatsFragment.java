package com.example.satwik.roomie;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    static String email;
    static String cart;
    final private String email_key="EMAIL";
    final private String graph_key="GRAPH";
    final private String id_key="ID";
   StatsAdapter adapter;
    public static HashMap<String,HashMap<String,Integer>> recievable= new HashMap<String, HashMap<String, Integer>>();
    private HashMap<String,HashMap<String,Integer>> payable=new HashMap<String, HashMap<String, Integer>>();
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Stats> list = new ArrayList<Stats>();
    private DatabaseReference archiveRef;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        email=sharedPreferences.getString(email_key,"Oops");
        cart=sharedPreferences.getString(id_key,"0000");
        archiveRef= FirebaseDatabase.getInstance().getReference().child("/"+cart+"/members");

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


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adapter.clear();

                for(DataSnapshot dsp:dataSnapshot.getChildren())
                {
                   // Log.e("Hkkkui","kkk");
                    int total=0;
                    HashMap<String,Integer> temp=new HashMap<String,Integer>();
                    for(DataSnapshot in:dsp.child("/archive").getChildren())
                    {

                        Log.e("Hkkkui","kkk");

                        if(temp.get(in.child("addedBy").getValue().toString())==null)
                        {

                            temp.put(in.child("addedBy").getValue().toString(),(Integer.parseInt(in.child("itemPrice").getValue().toString()))*(Integer.parseInt(in.child("itemQuant").getValue().toString())));
                        }
                        else
                        {
                            temp.put(in.child("addedBy").getValue().toString(),(temp.get(in.child("addedBy").getValue().toString()))+((Integer.parseInt(in.child("itemPrice").getValue().toString()))*(Integer.parseInt(in.child("itemQuant").getValue().toString()))));
                        }

                    }
                    recievable.put(dsp.getKey(),temp);

                }

                for(String i:recievable.keySet())
                {
                    for(String j:recievable.get(i).keySet())
                    {
                        Log.e("IM HERE","REPALLT");
                        Stats item;
                        if(i.compareTo(j)==0)
                        {
                            continue;
                        }
                        if(i.compareTo(email)==0)
                        {


                            adapter.addStat(new Stats(j,i,recievable.get(i).get(j).toString()));
                        }
                        else
                        {
                            if(j.compareTo(email)==0)
                            {
                                adapter.addStat(new Stats(j,i,recievable.get(i).get(j).toString()));
                            }
                        }
                    }
                }

                HashMap<String,Integer> pls=new HashMap<String,Integer>();
              //  recievable.keySet().toArray();

                for(String i:recievable.keySet())
                {
                    for(String j:recievable.get(i).keySet())
                    {




                        if(i==j)
                        {
                            continue;
                        }
                        else
                        {
                            if(pls.get(j)==null)
                            {
                                pls.put(j,recievable.get(i).get(j));
                            }
                            else {
                                pls.put(j, pls.get(j) + recievable.get(i).get(j));
                            }
                        }

                    }
                }

                SharedPreferences sharedPreferences=getActivity().getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                for(String i:recievable.keySet())
                {


                    editor.putString(i,pls.get(i).toString());
                }

                editor.commit();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        archiveRef.addValueEventListener(postListener);


        return view;

    }
}
