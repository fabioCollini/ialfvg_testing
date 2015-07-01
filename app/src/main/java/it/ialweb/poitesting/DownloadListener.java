package it.ialweb.poitesting;

import java.util.ArrayList;

public interface DownloadListener {
    void onLoad(ArrayList<Station> result);

    void onError(int statusCode);
}
