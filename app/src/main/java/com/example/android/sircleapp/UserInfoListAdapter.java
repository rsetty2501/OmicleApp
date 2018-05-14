package com.example.android.sircleapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;


public class UserInfoListAdapter extends ArrayAdapter<DepotUserLocatn> {

    private Context mContext;
    private LinkedList<DepotUserLocatn> userList = new LinkedList<>();


    UserInfoListAdapter(@NonNull Context context, LinkedList<DepotUserLocatn> objects) {
        super(context, 0, objects);
        mContext = context;
        userList = objects;
    }

    // The listView is the caller in this case to the getView() method
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.userinfo_list_items, parent, false);

        DepotUserLocatn currentUserInfo = userList.get(position);

        // Set the position in the circle
        TextView positionView = listItem.findViewById(R.id.position_loc);
        positionView.setText(String.valueOf(position));

        // Set the color of the circle as red
        final GradientDrawable gradientDrawable = (GradientDrawable) positionView.getBackground();

        // User name
        TextView userName = listItem.findViewById(R.id.textview_name);
        userName.setText(currentUserInfo.getDepotuser().getName());

        // User address
        TextView userAdd = listItem.findViewById(R.id.textView_address);
        userAdd.setText(currentUserInfo.getDepotuser().getAddress());

        Button button = listItem.findViewById(R.id.buttonStatus);
        if(position == 0){
            button.setText("Start");
            button.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            gradientDrawable.setColor(mContext.getResources().getColor(R.color.green));
//            button.setBackgroundColor(Color.GREEN);
//            gradientDrawable.setColor(Color.GREEN);
        }
        else{
            button.setText("Not serviced");
            button.setBackgroundColor(mContext.getResources().getColor(R.color.red));
            gradientDrawable.setColor(mContext.getResources().getColor(R.color.red));
//            button.setBackgroundColor(Color.RED);
//            gradientDrawable.setColor(Color.RED);

        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button1 = (Button) view;
                if(button1.getText().toString().equals("Not serviced")){
                    button1.setText("Serviced");
                    button1.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                    gradientDrawable.setColor(mContext.getResources().getColor(R.color.green));
//                    button1.setBackgroundColor(Color.GREEN);
//                    gradientDrawable.setColor(Color.GREEN);
                }
                else if(button1.getText().toString().equals("Start")){
                    button1.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                    gradientDrawable.setColor(mContext.getResources().getColor(R.color.green));
//                    button1.setBackgroundColor(Color.GREEN);
//                    gradientDrawable.setColor(Color.GREEN);
                }
                else{
                    button1.setText("Not Serviced");
                    button1.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                    gradientDrawable.setColor(mContext.getResources().getColor(R.color.red));
//                    button1.setBackgroundColor(Color.RED);
//                    gradientDrawable.setColor(Color.RED);
                }

            }
        });
        return listItem;
    }
}
