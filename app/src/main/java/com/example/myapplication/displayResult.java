package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.health;
import com.example.myapplication.model.rate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.bumptech.glide.Glide;

public class displayResult extends AppCompatActivity {

    public TextView Tid, Tprice, Trating;
    public Button logout, rateButton;
    public ImageView v2;
    public RatingBar ratingstar;
    DatabaseReference reference, rateReferene;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);

        auth = FirebaseAuth.getInstance();

        Tid = (TextView) findViewById(R.id.textViewID);
        Tprice = (TextView) findViewById(R.id.textPrice);
        v2 = (ImageView) findViewById(R.id.imageView2);
        logout = (Button) findViewById(R.id.btn_logout);
        rateButton = (Button) findViewById(R.id.btn_rating);
        Trating = (TextView)findViewById(R.id.textViewRating);
        ratingstar = (RatingBar)findViewById(R.id.ratingbar);


        Tid.setText(getIntent().getStringExtra("id"));
        String ID = Tid.getText().toString();
        reference = FirebaseDatabase.getInstance().getReference("health").child(ID);
        rateReferene = FirebaseDatabase.getInstance().getReference("rate").child(ID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final health health = dataSnapshot.getValue(health.class);
                Tprice.setText(health.getPrice());
                String url = health.getPicture();
                Glide.with(getApplicationContext()).load(url).into(v2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rateReferene.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final rate rate = dataSnapshot.getValue(rate.class);

                //display rating from firebase
                Trating.setText("The rating is " + rate.getAverage());

                //get counter
                String counter = rate.getCounter();
                int intCounter = Integer.parseInt(counter);

                //get totalrate
                String totalrate = rate.getTotalrate();
                 double floatRate = Double.parseDouble(totalrate);



                rateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String rating = "rating is : " + ratingstar.getRating();
                        Toast.makeText(displayResult.this,rating,Toast.LENGTH_LONG).show();

                        double ratingStar = ratingstar.getRating();
                        double CurrentRate = floatRate + ratingStar;
                        String StringCurrentRate = Double.toString(CurrentRate);

                        int CurrentCounter = intCounter + 1;
                        String StringCounter = Integer.toString(CurrentCounter);

                        double average = CurrentRate / CurrentCounter;
                        String StringAverage = Double.toString(average);

                        rateReferene.child("counter").setValue(StringCounter);
                        rateReferene.child("totalrate").setValue(StringCurrentRate);
                        rateReferene.child("average").setValue(StringAverage);

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(displayResult.this, login.class));
            }
        });

        /**rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String rating = "rating is : " + ratingstar.getRating();
                //Toast.makeText(displayResult.this,rating,Toast.LENGTH_LONG).show();
                //rateReferene.child("counter").setValue(StringCounter);
            }
        });
         **/
    }
}

