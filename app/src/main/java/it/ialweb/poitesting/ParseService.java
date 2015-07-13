package it.ialweb.poitesting;

import retrofit.Callback;
import retrofit.http.GET;

public interface ParseService {
    @GET("/1/classes/stations/") void getStations(Callback<StationList> callback);
}
