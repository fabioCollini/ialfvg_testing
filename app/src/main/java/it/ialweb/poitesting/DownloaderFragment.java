package it.ialweb.poitesting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class DownloaderFragment extends Fragment {

    private DownloaderManager downloaderManager = DownloaderManager.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public static DownloaderFragment getOrCreateFragment(FragmentManager fragmentManager, String tag) {
        DownloaderFragment fragment = (DownloaderFragment) fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = new DownloaderFragment();
            fragmentManager.beginTransaction().add(fragment, tag).commit();
        }
        return fragment;
    }

    public void attachOrLoadData(DownloadListener newListener) {
        downloaderManager.attachOrLoadData(newListener);
    }

    public boolean isTaskRunning() {
        return downloaderManager.isTaskRunning();
    }

    public void attach(DownloadListener listener) {
        downloaderManager.attach(listener);
    }

    public void detach() {
        downloaderManager.detach();
    }
}
