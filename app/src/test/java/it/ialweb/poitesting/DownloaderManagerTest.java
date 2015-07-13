package it.ialweb.poitesting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import retrofit.Callback;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DownloaderManagerTest {

    @Mock AvailableStationsManager availableStationsManager;

    @Mock ParseService parseService;

    @InjectMocks DownloaderManager downloaderManager;

    @Mock DownloadListener downloadListener;

    @Captor ArgumentCaptor<ArrayList<Station>> captor;

    @Test
    public void testAttachOrLoadDataEmptyList() {
        defineMock(parseService, new StationList());
        when(availableStationsManager.isAvailable(any(Station.class))).thenReturn(true);

        downloaderManager.attachOrLoadData(downloadListener);

        verify(downloadListener).onLoad(captor.capture());
        Assert.assertEquals(0, captor.getValue().size());
    }

    @Test
    public void testAttachOrLoadData() {
        defineMock(parseService, new StationList(new Station(12, 35, "aaaa")));
        when(availableStationsManager.isAvailable(any(Station.class))).thenReturn(true);

        downloaderManager.attachOrLoadData(new DownloadListener() {
            @Override public void onLoad(ArrayList<Station> result) {
                Assert.assertEquals(0, result.size());
            }

            @Override public void onError(int statusCode) {
                Assert.fail();
            }
        });
    }

    @Test
    public void testAttachOrLoadDataNoEmptyList() {
        defineMock(parseService, new StationList(new Station(0, 0, "aaaa")));

        when(availableStationsManager.isAvailable(any(Station.class))).thenReturn(true);

        downloaderManager.attachOrLoadData(new DownloadListener() {
            @Override public void onLoad(ArrayList<Station> result) {
                Assert.assertEquals(1, result.size());
            }

            @Override public void onError(int statusCode) {
                Assert.fail();
            }
        });
    }

    private void defineMock(ParseService parseService, final StationList result) {
        doAnswer(new Answer<Object>() {
            @Override public Object answer(InvocationOnMock invocation) throws Throwable {
                Callback<StationList> callback = (Callback<StationList>) invocation.getArguments()[0];
                callback.success(result, null);
                return null;
            }
        }).when(parseService).getStations(any(Callback.class));
    }
}