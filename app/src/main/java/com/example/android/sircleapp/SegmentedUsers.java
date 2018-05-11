package com.example.android.sircleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.LinkedList;

public class SegmentedUsers extends AppCompatActivity {

    ListView listView;
    ClusterUserList clusterUserList;
    String clusterName;
    LinkedList<DepotUserLocatn> depotUserLocatns = new LinkedList<>();
    UserInfoListAdapter userInfoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        listView = findViewById(R.id.list_view_user);

        setClusterUserList();
        setTitle(clusterName);

        // Setup FAB to open Route map
        FloatingActionButton mapFab = findViewById(R.id.mapFloat);
        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    private void setClusterUserList(){
        clusterUserList = (ClusterUserList) getIntent().getSerializableExtra(SircleAppConstants.CLUSTER_USER_LIST);
        clusterName = clusterUserList.getCluster();
        depotUserLocatns = clusterUserList.getUserInfoList();
        userInfoListAdapter = new UserInfoListAdapter(getApplicationContext(), depotUserLocatns);
        listView.setAdapter(userInfoListAdapter);
    }


    // This method will be used to return back to the main activity when the return button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
