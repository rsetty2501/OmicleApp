package com.example.android.sircleapp;

import java.io.Serializable;

public class DepotUserLocatn implements Serializable{

    private DepotUserInfo depotuser;
    private LocationCoord location_coord;

    public DepotUserLocatn(){

    }

    public DepotUserLocatn(DepotUserInfo depotuser, LocationCoord location_coord) {
        this.depotuser = depotuser;
        this.location_coord = location_coord;
    }

    public DepotUserInfo getDepotuser() {
        return depotuser;
    }

    public void setDepotuser(DepotUserInfo depotuser) {
        this.depotuser = depotuser;
    }

    public LocationCoord getLocation_coord() {
        return location_coord;
    }

    public void setLocation_coord(LocationCoord location_coord) {
        this.location_coord = location_coord;
    }
}
