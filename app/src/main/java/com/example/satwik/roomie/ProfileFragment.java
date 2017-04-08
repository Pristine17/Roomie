package com.example.satwik.roomie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.IOException;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import android.net.Uri;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by satwik on 07-04-2017.
 */
public class ProfileFragment  extends Fragment {

    private CircleImageView profileButton;
    static final int REQUEST_IMAGE_GET = 1;
    private Uri fullPhotoUri;
    final private String email_key="EMAIL";
    final private String id_key="ID";

    private String email;
    private String cartID;
    private Bitmap thumbnail;
    private TextView profileName;
    private TextView groupID;
    DatabaseReference mRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        email=sharedPreferences.getString(email_key,"Oops");
        cartID=sharedPreferences.getString(id_key,"Oopps");
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("/"+cartID+"/members");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.material_design_profile_screen_xml_ui_design,container,false);
        profileButton=(CircleImageView) view.findViewById(R.id.profile_image);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
            }
        });

        profileName=(TextView) view.findViewById(R.id.user_profile_name);
        profileName.setText(email);
        groupID=(TextView) view.findViewById(R.id.user_profile_short_bio);
        groupID.setText(" Cart ID :  "+cartID);
        return view;

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

            profileButton.setImageBitmap(thumbnail);
//            mRef.child("/"+email+"/profilepic").setValue(fullPhotoUri.toString());

            //    Log.e("YOLO", fullPhotoUri.toString());

            // Do work with photo saved at fullPhotoUri

        }
    }
}
