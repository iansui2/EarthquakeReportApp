package com.appinventor.android.earthquakereportapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appinventor.android.earthquakereportapp.data.EarthquakeTriviaRepository;
import com.appinventor.android.earthquakereportapp.pojo.EarthquakeTrivia;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class EarthquakeTriviaViewModel extends AndroidViewModel {

    private final EarthquakeTriviaRepository earthquakeTriviaRepository;
    private final LiveData<List<EarthquakeTrivia>> earthquakeTriviaObservableData;

    private final CompositeDisposable disposables;

    public EarthquakeTriviaViewModel(@NonNull Application application) {
        super(application);
        disposables = new CompositeDisposable();
        earthquakeTriviaRepository = new EarthquakeTriviaRepository(getApplication());
        earthquakeTriviaObservableData = earthquakeTriviaRepository.getAllTrivia();
    }

    public void callEarthquakeTriviaObservableInRepository() {
        disposables.add(earthquakeTriviaRepository.insertTrivia().subscribe());
    }

    public LiveData<List<EarthquakeTrivia>> getEarthquakeTriviaObservableData() {
        return earthquakeTriviaObservableData;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
