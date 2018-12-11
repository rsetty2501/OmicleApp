package com.example.android.sircleapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final Map<String, Double> distanceClusterName = new HashMap<>();
    ListView listView;
    LinkedList<ClusterUserList> clusterList = new LinkedList<>();
    RouteAdapter routeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Route Cluster");


        listView = findViewById(R.id.list_view_route);
        routeAdapter = new RouteAdapter(this, clusterList);
        listView.setAdapter(routeAdapter);

        for (Map.Entry<String, Double> entry : distanceClusterName.entrySet()) {
            Log.e("main", "Inside map!");
            Log.e("main", "Distance: " + entry.getKey() + " cluster: " + entry.getValue());
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClusterUserList clusterUserList = routeAdapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, SegmentedUsers.class);
                intent.putExtra(SircleAppConstants.CLUSTER_USER_LIST, clusterUserList);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // For the Spinner menu which contains the city names list
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_menu, menu);
        String[] list = new String[]{"Uppsala", "Stockholm", "Gothenburg"};

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_text, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        clusterList.clear();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String cityName = adapterView.getItemAtPosition(i).toString();
                clusterList.clear();
                fetchClusterDataFirebase(cityName);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return true;
    }

    private void fetchClusterDataFirebase(String cityName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // retrieve user data from database
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String currDate = formatter.format(date);

        DatabaseReference Ref = database.getReference(cityName).child(currDate);
        DatabaseReference clusterRef = Ref.child("Clusters"); // Route info
        DatabaseReference distanceRef = Ref.child("Distance");
        getClusterDetails(clusterRef);
        getDistanceCluster(distanceRef);
    }

    private void getDistanceCluster(DatabaseReference distanceRef) {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Double> distClust;
                for (DataSnapshot distSnapshot : dataSnapshot.getChildren()) {
                    distClust = (Map<String, Double>) distSnapshot.getValue();
                    assert distClust != null;
                    for (Map.Entry<String, Double> entry : distClust.entrySet()) {
                        distanceClusterName.put(entry.getKey(), entry.getValue());
                        synchronized (distanceClusterName) {
                            distanceClusterName.notifyAll();
                        }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        distanceRef.addValueEventListener(eventListener);
    }

    private void getClusterDetails(final DatabaseReference clusterRef) {

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    final String route = userSnapshot.getKey();
                    DatabaseReference routeRef = clusterRef.child(route);
                    routeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            LinkedList<DepotUserLocatn> depotUserLocatnLinkedList = new LinkedList<>();
                            for (DataSnapshot routeSnapshot : dataSnapshot.getChildren()) {
                                DepotUserLocatn depotUserLocatn = routeSnapshot.getValue(DepotUserLocatn.class);
                                depotUserLocatnLinkedList.add(depotUserLocatn);
                            }
                            extractDataFirebase(route, depotUserLocatnLinkedList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        // read data
        clusterRef.addValueEventListener(eventListener);
    }

    private void extractDataFirebase(String route, LinkedList<DepotUserLocatn> depotUserLocatnLinkedList) {
        clusterList.add(new ClusterUserList(route, depotUserLocatnLinkedList));
        routeAdapter.notifyDataSetChanged();
    }

}
