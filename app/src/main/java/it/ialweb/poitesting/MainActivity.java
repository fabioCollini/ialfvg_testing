package it.ialweb.poitesting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DownloaderFragment fragment;

    private View errorLayout;

    private View loadingLayout;

    private View body;

    private View noData;

    private TextView totalCount;

    private DownloadListener listener = new DownloadListener() {

        @Override
        public void onLoad(ArrayList<Station> result) {
            populateView(result);
        }

        @Override
        public void onError(int statusCode) {
            errorLayout.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.GONE);
        }
    };

    private void populateView(ArrayList<Station> result) {
        loadingLayout.setVisibility(View.GONE);
        if (result.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
        } else {
            body.setVisibility(View.VISIBLE);
            totalCount.setText(Integer.toString(result.size()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorLayout = findViewById(R.id.error_layout);
        loadingLayout = findViewById(R.id.loading_layout);
        body = findViewById(R.id.body);
        noData = findViewById(R.id.no_data);
        totalCount = (TextView) findViewById(R.id.total_count);

        fragment = DownloaderFragment.getOrCreateFragment(getSupportFragmentManager(), "stationLoader");

        if (fragment.isTaskRunning()) {
            fragment.attach(listener);
        } else {
            ArrayList<Station> savedList = null;
            if (savedInstanceState != null) {
                savedList = savedInstanceState.getParcelableArrayList("stations");
            }

            if (savedList != null) {
                populateView(savedList);
            } else {
                loadData();
            }
        }
    }

    private void loadData() {
        loadingLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        body.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        fragment.attachOrLoadData(listener);
    }
}
