package com.example.khutsomatlala.hackaton_user11;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String FB_DATABASE_PATH = "places";
    private DatabaseReference mDatabaseRef;
    private List<ImageUpload> imgList;
    private ListView lv;
    private imageListAdapter adapter;
    private ProgressDialog progressDialog;
    public static Boolean stauts = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.ListViewImage);
        // Showprogress dialog during list image loading
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Co working places  loading...");
        progressDialog.show();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                //Fectching information from database

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    final ImageUpload imageUpload = snapshot.getValue(ImageUpload.class);
                    imgList.add(imageUpload);

                }

                //Init adapter
                adapter = new imageListAdapter(MainActivity.this, R.layout.image_item, imgList);

                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


