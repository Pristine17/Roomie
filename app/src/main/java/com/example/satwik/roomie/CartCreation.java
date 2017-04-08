package com.example.satwik.roomie;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import static android.app.PendingIntent.getActivity;

public class CartCreation extends AppCompatActivity {

    final private String id_key="ID";
    static String id;
    DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_creation);
    }

    public void createOwn(View view)
    {
        Random random=new Random();
        int id_num = random.nextInt(10000);
        id = id_num + "";
        Log.e("THIS IS ID",id);

        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(id_key, id);
        editor.commit();

        Intent intent = new Intent(CartCreation.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void joinExisting(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create");
        builder.setMessage("Enter your id: ");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                id = input.getText().toString();
               mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       for(DataSnapshot s:dataSnapshot.getChildren())
                       {
                           if(id.compareTo(s.getKey().toString())==0)
                           {
                               Log.e("Pls", "How is this not null");

                               SharedPreferences sharedPreferences = CartCreation.this.getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);
                               SharedPreferences.Editor editor = sharedPreferences.edit();
                               editor.putString(id_key, id);
                               editor.commit();
                               Intent intent = new Intent(CartCreation.this, MainActivity.class);
                               startActivity(intent);
                               finish();
                           }
                           else
                           {
                               Toast.makeText(getApplicationContext(),"Invalid Cart ID",Toast.LENGTH_LONG);
                           }
                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });



            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
