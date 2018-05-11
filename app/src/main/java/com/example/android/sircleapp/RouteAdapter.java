package com.example.android.sircleapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.LinkedList;

public class RouteAdapter extends ArrayAdapter<ClusterUserList> {

    private Context mContext;
    private LinkedList<ClusterUserList> clusterUserLists;

    RouteAdapter(@NonNull Context context, LinkedList<ClusterUserList> objects) {
        super(context, 0, objects);
        mContext = context;
        clusterUserLists = objects;
    }

    // The listView is the caller in this case to the getView() method
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){

        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.cluster_list_items,parent,false);

        ClusterUserList currentUser = clusterUserLists.get(position);
        // cluster
        TextView cluster = listItem.findViewById(R.id.cluster);
        cluster.setText(currentUser.getCluster());

        return listItem;
    }
}
