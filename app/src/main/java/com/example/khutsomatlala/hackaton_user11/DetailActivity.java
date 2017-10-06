package com.example.khutsomatlala.hackaton_user11;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DetailActivity extends Activity {


    //Like
    private Boolean mProcessLike = false;
    private DatabaseReference mDBLike;
    private FirebaseAuth mAuth;
    TextView tvLikes;
    //ImageButton mLikeButton;
    FloatingActionButton mLikeButton;
    int counter = 0;

    long totalLikes = 0;


//        try {


    String call;
    String lat;
    String lon;
    String PlaceName;
    String infor;
    String address;
    String hours;
    String pic;

    String place_uid;

    //rating
    RatingBar ratingRatingBar;
    TextView ratingDisplayTextView;


    ImageView middlePic;
    TextView txtInformation, txtAddress, txtCell, txtHours;

    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    //messaging list
    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference, mCommentsDatabaseReference, mLikeDatabaseReference;
EditText message;

    List<FriendlyMessage> mComments;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        message =(EditText)findViewById(R.id.messageEditText);

        message.clearFocus();

        message.setFocusableInTouchMode(true);

        mComments = new ArrayList<>();
        Intent i = getIntent();

        lat = i.getStringExtra("lat");
        lon = i.getStringExtra("lon");
        call = i.getStringExtra("call");
        PlaceName = i.getStringExtra("name");
        infor = i.getStringExtra("infor");
        address = i.getStringExtra("address");
        hours = i.getStringExtra("hours");
        pic = i.getStringExtra("pic");


        txtInformation = findViewById(R.id.txtInformation);

        txtHours = findViewById(R.id.txtHours);

        middlePic = findViewById(R.id.middlePic);

        Glide.with(this)
                .load(pic)
                .override(300, 200)
                //  .centerCrop()
                .into(middlePic);


        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(PlaceName);


        txtInformation.setText("Description - " + infor);

        txtHours.setText("operating hours - " + hours);


        //rating bar

        ratingRatingBar = findViewById(R.id.rating_rating_bar);
        ratingDisplayTextView = findViewById(R.id.rating_display_text_View);
     /*   ratingRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if( ratingRatingBar.getRating() == 1){
                    rateMessage = "Hated it";

                }
                else if(  (int)v == 2)
                {
                    rateMessage = "Disliked it";
                }
                else if( (int)v == 3)
                {
                    rateMessage = "It's OK";
                }
                else if( (int)v == 4)
                {
                    rateMessage = "Liked it";
                }
                else {
                    rateMessage = "Loved it";
                }

                ratingDisplayTextView.setText( "" + rateMessage);
            }

        });*/


        // ------------------- chating things
        mUsername = ANONYMOUS;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        // mAuthStateListener = FirebaseAuth.getInstance();

        mCommentsDatabaseReference = mFirebaseDatabase.getReference().child("comments");


        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("places");

        // Initialize references to views

        mMessageListView = (ListView) findViewById(R.id.messageListView);

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);


        //   again check if the user is already logged in or not
        if (mFirebaseAuth.getCurrentUser() == null) {

//            User not logged in
            finish();
            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
        }

        //  fetch and  display the user details

        final FirebaseUser user = mFirebaseAuth.getCurrentUser();

        if (user != null) {

            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();


            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.

            String uid = user.getUid();

//            Toast.makeText(this, "User UID - >" + uid , Toast.LENGTH_SHORT).show();
            //     Toast.makeText(this, "name - " + name, Toast.LENGTH_SHORT).show();

            mUsername = name;
        }


        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: Sending data to the DB
                FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername);

                String key = mCommentsDatabaseReference.push().getKey();

                //  mCommentsDatabaseReference.child(place_uid).child(key).setValue(friendlyMessage);
                mCommentsDatabaseReference.child(PlaceName).child(key).setValue(friendlyMessage);


                // Clear input box
                mMessageEditText.setText("");
            }
        });

        // mCommentsDatabaseReference.addValueEventListener(new ValueEventListener()
        mCommentsDatabaseReference.child(PlaceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                progressDialog.dismiss();

                //Fectching information from database

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    final FriendlyMessage friendlyMessage = snapshot.getValue(FriendlyMessage.class);
                    mComments.add(friendlyMessage);

                }

                //Init adapter
                mMessageAdapter = new MessageAdapter(DetailActivity.this, R.layout.image_item, mComments);

                //
                mMessageListView.setAdapter(mMessageAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Like
        mDBLike = FirebaseDatabase.getInstance().getReference();
        mDBLike.keepSynced(true);


        tvLikes = (TextView) findViewById(R.id.numLikes);
        mLikeButton = (FloatingActionButton) findViewById(R.id.btnLike);
        mAuth = FirebaseAuth.getInstance();

        mLikeButton.setImageResource(R.drawable.thumbs);

        mLikeButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {

                                               ImageUpload likes = new ImageUpload();

                                               if (mProcessLike == false) {

                                                   //likes.setLike("True");


                                                   mDBLike.child("Likes").child(PlaceName).child(mAuth.getCurrentUser().getDisplayName()).setValue(likes);
                                                   mProcessLike = true;

                                                   mLikeButton.setImageResource(R.drawable.orange);

                                               } else {

//                    if (counter != 0) {
//                        counter = counter - 1;
//                    }
                                                   // likes.setNum(counter);
                                                   //likes.setLike("False");
//                                                   counter = 0;

                                                   mDBLike.child("Likes").child(PlaceName).child(mAuth.getCurrentUser().getDisplayName()).removeValue();
                                                   mProcessLike = false;

                                                   mLikeButton.setImageResource(R.drawable.thumbs);


                                                   //  Intent intent = new Intent(getContext(), max_pic.class);

                                                   //  String pic = listImage.get(position).getUrI();

                                                   //    intent.putExtra("pic",pic);

                                                   //    context.startActivity(intent);

                                               }

                                               //comment
//                                               // Get a reference to our likes
//                                               final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                               DatabaseReference ref = database.getReference("Likes").child(PlaceName);
//
//                                               // Attach a listener to read the data at our likes reference
//                                               ref.addValueEventListener(new ValueEventListener() {
//                                                   @Override
//                                                   public void onDataChange(DataSnapshot dataSnapshot) {
//                                                       ImageUpload likes = dataSnapshot.getValue(ImageUpload.class);
//                                                       if (likes != null) {
//
//                                                           totalLikes = dataSnapshot.getChildrenCount();
//                                                           Toast.makeText(DetailActivity.this, totalLikes + "", Toast.LENGTH_SHORT).show();
//                                                           tvLikes.setText(totalLikes + "");
//
//                                                       } else {
//                                                           Toast.makeText(DetailActivity.this, " Number of  likes" + totalLikes, Toast.LENGTH_SHORT).show();
//
//                                                           tvLikes.setText(totalLikes + "");
//                                                       }
//
//                                                   }
//
//                                                   @Override
//                                                   public void onCancelled(DatabaseError databaseError) {
//                                                       System.out.println("The read failed: " + databaseError.getCode());
//                                                   }
//                                               });


                                               totalLikes = 0;
                                           }
                                       }

        );


        callLikes();
    }


    public void Call(View view) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + call));
        startActivity(intent);
    }

    public void direction(View view) {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        intent.putExtra("name", PlaceName);
        startActivity(intent);
    }

    public void GoToBook(View view) {

        Intent i = new Intent(getApplicationContext(), book_new.class);
        i.putExtra("pic", pic);
        startActivity(i);
    }

public void callLikes(){

    // Get a reference to our likes
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Likes").child(PlaceName);

    // Attach a listener to read the data at our likes reference
    ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ImageUpload likes = dataSnapshot.getValue(ImageUpload.class);
            if (likes != null) {

                totalLikes = dataSnapshot.getChildrenCount();
//                Toast.makeText(DetailActivity.this, totalLikes + "", Toast.LENGTH_SHORT).show();
                tvLikes.setText(totalLikes + "");

            } else {
//                Toast.makeText(DetailActivity.this, " Number of  likes" + totalLikes, Toast.LENGTH_SHORT).show();

                tvLikes.setText(totalLikes + "");
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("The read failed: " + databaseError.getCode());
        }
    });


}

}

