package com.example.android.sircleapp;


import java.io.Serializable;
import java.util.LinkedList;

public class ClusterUserList implements Serializable{

    private String cluster;
    private LinkedList<DepotUserLocatn> userInfoList;

    ClusterUserList(String cluster, LinkedList<DepotUserLocatn> userInfoList){
        this.cluster = cluster;
        this.userInfoList = userInfoList;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public LinkedList<DepotUserLocatn> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(LinkedList<DepotUserLocatn> userInfoList) {
        this.userInfoList = userInfoList;
    }
}
