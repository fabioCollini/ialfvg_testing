package it.ialweb.poitesting;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;

public class StationList {
    @SerializedName("results")
    private ArrayList<Station> list = new ArrayList<>();

    public StationList() {
    }

    public StationList(Station... stations) {
        list = new ArrayList<>(Arrays.asList(stations));
    }

    public ArrayList<Station> getList() {
        return list;
    }
}
