package com.appinventor.android.earthquakereportapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.appinventor.android.earthquakereportapp.R;
import com.appinventor.android.earthquakereportapp.pojo.Earthquake;
import com.appinventor.android.earthquakereportapp.data.EarthquakeViewModel;

import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final EarthquakeAdapter adapter = new EarthquakeAdapter();
        recyclerView.setAdapter(adapter);

        EarthquakeViewModel earthquakeViewModel = new ViewModelProvider(this).get(EarthquakeViewModel.class);
        earthquakeViewModel.getEarthquakeListObservableData().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(List<Earthquake> earthquakes) {
                adapter.setAllEarthquakes(earthquakes);
            }
        });
    }
}