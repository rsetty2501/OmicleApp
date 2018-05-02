package com.example.android.sircleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    TextView textView;
    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference depotRef;
    String city = "Uppsala";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        database = FirebaseDatabase.getInstance();


        // retrieve user data from database
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String currDate = formatter.format(date);
        String userPath = city + "/userInfo";
        userRef = database.getReference(userPath).child(currDate);
        getUserDetails();

        // retrieve depot data from database
        String depotPath = city + "/depotInfo";
        depotRef = database.getReference(depotPath);
        getDepotDetails();


    }

    private void getUserDetails(){
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userValue;

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    userValue = userSnapshot.getValue(User.class);
                    assert userValue != null;
                    textView.append(userValue.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        // read data
        userRef.addListenerForSingleValueEvent(eventListener);
    }

    private void getDepotDetails(){
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Depot depotValue;
                depotValue = dataSnapshot.getValue(Depot.class);
                assert depotValue != null;
                textView.append(depotValue.getAddress());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        // read data
        depotRef.addListenerForSingleValueEvent(eventListener);
    }

}
