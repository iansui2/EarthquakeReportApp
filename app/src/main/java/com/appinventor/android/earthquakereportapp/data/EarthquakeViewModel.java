package com.appinventor.android.earthquakereportapp.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appinventor.android.earthquakereportapp.pojo.Earthquake;

import java.util.List;

public class EarthquakeViewModel extends AndroidViewModel {

    private LiveData<List<Earthquake>> earthquakeObservableData;
    EarthquakeRepository earthquakeRepository;

    public EarthquakeViewModel(Application application) {
        super(application);
        earthquakeRepository = new EarthquakeRepository();
        earthquakeObservableData = earthquakeRepository.callWebService();
    }

    public LiveData<List<Earthquake>> getEarthquakeListObservableData() {
        return earthquakeObservableData;
    }
}
