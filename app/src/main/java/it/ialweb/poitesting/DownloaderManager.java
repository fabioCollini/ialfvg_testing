package it.ialweb.poitesting;

import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DownloaderManager {

    private DownloadListener listener;

    private ParseService parseService;

    private boolean isRunning;
    private AvailableStationsManager availableStationsManager;

    private static DownloaderManager instance;

    private DownloaderManager() {
        availableStationsManager = new AvailableStationsManager();
    }

    public static DownloaderManager getInstance() {
        if (instance == null) {
            synchronized (DownloaderManager.class) {
                if (instance == null) {
                    instance = new DownloaderManager();
                }
            }
        }
        return instance;
    }

    @VisibleForTesting
    public static void setInstance(DownloaderManager instance) {
        DownloaderManager.instance = instance;
    }

    @VisibleForTesting
    public DownloaderManager(ParseService parseService, AvailableStationsManager availableStationsManager) {
        this.parseService = parseService;
        this.availableStationsManager = availableStationsManager;
    }

    private void createParseService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.parse.com")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override public void intercept(RequestFacade request) {
                        request.addHeader("X-Parse-Application-Id", "uFkr2W3mPXkLrtAHU0ufa4cm3WgZ0RbTUInOnnL3");
                        request.addHeader("X-Parse-REST-API-Key", "uraOQm290ETfGMaDuv5AQivchIjosj3xL6bmPTMx");
                    }
                })
                .build();
        if (BuildConfig.DEBUG) {
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        parseService = restAdapter.create(ParseService.class);
    }


    public void attachOrLoadData(DownloadListener newListener) {
        this.listener = newListener;

        if (parseService == null) {
            createParseService();
        }

        isRunning = true;

        parseService.getStations(new Callback<StationList>() {
            @Override public void success(StationList responseList, Response response) {
                ArrayList<Station> list = responseList.getList();
                ArrayList<Station> availableStations = new ArrayList<>();
                for (Station station : list) {
                    if (isNearGreenwich(station) && availableStationsManager.isAvailable(station)) {
                        availableStations.add(station);
                    }
                }
                if (listener != null) {
                    listener.onLoad(availableStations);
                }
                isRunning = false;
            }

            @Override public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.onError(error != null ? error.getResponse().getStatus() : 500);
                }
                isRunning = false;
            }
        });
    }

    private boolean isNearGreenwich(Station station) {
        return Math.abs(station.getLon()) < 0.1;
    }

    public boolean isTaskRunning() {
        return isRunning;
    }

    public void attach(DownloadListener listener) {
        this.listener = listener;
    }

    public void detach() {
        this.listener = null;
    }
}
