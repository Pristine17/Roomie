package com.example.satwik.roomie;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Daniel on 27-03-2017.
 */
public class Cart extends Fragment implements View.OnClickListener {

    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<String> ids=new ArrayList<>();
    final private String email_key="EMAIL";
    static String email;
    View rootview;
    DataAdapter adapter;
    RecyclerView recyclerView;
    private View dialogView;
    private AlertDialog.Builder alertDialog;
    private boolean add = false;
    private EditText add_item;
    private EditText price;
    private EditText quantity;
    private ImageButton addImage;
    static public Bitmap thumbnail;
    Uri fullPhotoUri;
    private DatabaseReference mDatabase;
    private DatabaseReference itemRef=FirebaseDatabase.getInstance().getReference().child("/0000/cart/items/");


    static final int REQUEST_IMAGE_GET = 1;


    private int edit_position;
    private Paint p = new Paint();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        email=sharedPreferences.getString(email_key,"Oops");
      //  Log.e("YOLO BOIS",email);


       // mDatabase.child("/0000/cart/items").push().setValue(new Item("sdfsdfs","sdfsdfs","25","2",));
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.cart, container, false);

        initViews();
        initDialog();
        ValueEventListener itemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                ids.clear();



                for(DataSnapshot dsp:dataSnapshot.getChildren())
                {
                    Log.e("PLS",dsp.getKey());
                    adapter.addItem(dsp.getValue(Item.class));
                    ids.add(dsp.getKey());
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        itemRef.addValueEventListener(itemListener);
        return rootview;
    }


    private void initViews() {
        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        if (fab == null) {
            Log.e("yolo", "dammit");
        }
        fab.setOnClickListener(this);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DataAdapter(items);
        recyclerView.setAdapter(adapter);


//        items.add(new Item("fgdfg","Apple","25","2"));
//        items.add(new Item("dfgdfgd","Orange","50","1"));
//        items.add(new Item("dfgdfgdfgd","Grapes","40","3"));
//        items.add(new Item("dfgdgdfgdf","Watermelon","567","50"));
//        items.add(new Item("dfgdfgd","Musk Melon","33","2"));

        adapter.notifyDataSetChanged();

        initSwipe();

//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
//
//                @Override public boolean onSingleTapUp(MotionEvent e) {
//                    return true;
//                }
//
//            });
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//
//                View child = rv.findChildViewUnder(e.getX(), e.getY());
//                if(child != null && gestureDetector.onTouchEvent(e)) {
//                    int position = rv.getChildAdapterPosition(child);
//                    Toast.makeText(getActivity(), (String)items.get(position), Toast.LENGTH_SHORT).show();
//                }
//
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    DeleteItem(position);
                } else {
                    //removeView();
                    //adapter.removeItem(position);
                    TaskCompleted(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_black_48dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_black_48dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initDialog() {
        alertDialog = new AlertDialog.Builder(getActivity());
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_layout, null);
        alertDialog.setView(dialogView);
        add_item = (EditText) dialogView.findViewById(R.id.add_item);
        price = (EditText) dialogView.findViewById(R.id.price);
        quantity = (EditText) dialogView.findViewById(R.id.quantity);
        addImage = (ImageButton) dialogView.findViewById(R.id.addImage);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add) {
                    Item newItem;
                    if(fullPhotoUri!=null)
                    {
                        newItem=new Item(fullPhotoUri.toString(),add_item.getText().toString(),price.getText().toString(),quantity.getText().toString(),email);
                    }
                    else
                    {
                        newItem=new Item("",add_item.getText().toString(),price.getText().toString(),quantity.getText().toString(),email);
                    }

                    add = false;
                    itemRef.push().setValue(newItem);

                    dialog.dismiss();
                } else {
                    // items.set(edit_position,add_item.getText().toString());
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });

    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            // thumbnail = data.getParcelable("data");
            fullPhotoUri = data.getData();
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fullPhotoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        //    Log.e("YOLO", fullPhotoUri.toString());

            // Do work with photo saved at fullPhotoUri

        }
    }

    private void removeView() {
        if (dialogView.getParent() != null) {
            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
        }
    }

    public void TaskCompleted(int position) {
     //   Log.e("HELP","IM HERE!");

        FirebaseDatabase.getInstance().getReference().child("/0000/members/"+email+"/archive/"+ids.get(position)).setValue(adapter.getItem(position));
        itemRef.child(""+ids.get(position)).removeValue();
    }

    public void DeleteItem(int position) {
        itemRef.child(""+ids.get(position)).removeValue();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                removeView();
                add = true;
                alertDialog.setTitle("Add Item");
                add_item.setText("");
                alertDialog.show();
                break;
        }

    }
}
