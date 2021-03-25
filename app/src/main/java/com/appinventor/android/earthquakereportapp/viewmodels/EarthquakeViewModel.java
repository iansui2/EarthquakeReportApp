package com.appinventor.android.earthquakereportapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appinventor.android.earthquakereportapp.data.EarthquakeRepository;
import com.appinventor.android.earthquakereportapp.pojo.EarthquakeRoom;

import java.util.List;

public class EarthquakeViewModel extends AndroidViewModel {

    private final LiveData<List<EarthquakeRoom>> earthquakeObservableData;
    EarthquakeRepository earthquakeRepository;

    public EarthquakeViewModel(@NonNull Application application) {
        super(application);
        earthquakeRepository = new EarthquakeRepository(getApplication());
        earthquakeObservableData = earthquakeRepository.getAllEarthquake();
    }

    public void callEarthquakeObservableInRepository() {
        earthquakeRepository.insertEarthquake();
    }

    public LiveData<List<EarthquakeRoom>> getEarthquakeListObservableData() {
        return earthquakeObservableData;
    }

    public void retryCall() {
        earthquakeRepository.getRetryCallback();
    }
}
