package com.appinventor.android.earthquakereportapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appinventor.android.earthquakereportapp.R;
import com.appinventor.android.earthquakereportapp.data.EarthquakeRepository;
import com.appinventor.android.earthquakereportapp.data.EarthquakeViewModel;
import com.appinventor.android.earthquakereportapp.network.NetworkUtil;
import com.appinventor.android.earthquakereportapp.pojo.Earthquake;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.appinventor.android.earthquakereportapp.network.NetworkUtil.*;

public class EarthquakeActivity extends AppCompatActivity {

    private EarthquakeRepository earthquakeRepository;
    private Call<ResponseBody> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        final TextView emptyStateTextView = findViewById(R.id.empty_view);
        final Button tryAgainButton = findViewById(R.id.try_again_button);
        final ProgressBar progressBar = findViewById(R.id.loading_indicator);

        // Make the button invisible
        tryAgainButton.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // Set the layout manager of RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set the RecyclerView size to fixed
        recyclerView.setHasFixedSize(true);

        final EarthquakeAdapter adapter = new EarthquakeAdapter();
        // Set the adapter of the RecyclerView to the EarthquakeAdapter
        recyclerView.setAdapter(adapter);

        // Create an EarthquakeViewModel Object
        final EarthquakeViewModel earthquakeViewModel = new ViewModelProvider(this).get(EarthquakeViewModel.class);

        // Get the method from the EarthquakeViewModel and call the method observe
        earthquakeViewModel.getEarthquakeListObservableData().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(final List<Earthquake> earthquakes) {
                // Make the progress bar invisible
                progressBar.setVisibility(View.INVISIBLE);
                // Set the LiveData for the RecyclerView
                setupList(earthquakes);

                tryAgainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Set the LiveData for the RecyclerView again if there is no internet connection
                        tryAgain(earthquakes);
                    }
                });
            }

            private void setupList(List<Earthquake> earthquakes) {
                if (networkAvailable(getApplicationContext())) {
                    // Set empty state text to display "No earthquakes found."
                    emptyStateTextView.setText(R.string.no_earthquakes);

                    // Clear the adapter
                    adapter.clear();

                    if (earthquakes != null && !earthquakes.isEmpty()) {
                        // Add the data to the RecyclerView
                        adapter.setAllEarthquakes(earthquakes);
                        // Set empty state text to display nothing
                        emptyStateTextView.setText("");
                    }
                } else {
                    // Set empty state text to display "No earthquakes found."
                    emptyStateTextView.setText(R.string.no_internet_connection);
                    // Make the progress bar invisible
                    tryAgainButton.setVisibility(View.VISIBLE);
                }
            }

            private void tryAgain(List<Earthquake> earthquakes) {
                // Make the progress bar visible
                progressBar.setVisibility(View.VISIBLE);
                // Clear the adapter
                emptyStateTextView.setText("");
                // Make the button invisible
                tryAgainButton.setVisibility(View.INVISIBLE);

                if (networkAvailable(getApplicationContext())) {
                    // Make the progress bar invisible
                    progressBar.setVisibility(View.INVISIBLE);

                    // Clear the adapter
                    adapter.clear();

                        // Add the data to the RecyclerView
                        adapter.setAllEarthquakes(earthquakes);
                        // Set empty state text to display nothing
                        emptyStateTextView.setText("");
                } else {
                    // Make the progress bar invisible
                    progressBar.setVisibility(View.INVISIBLE);
                    // Set empty state text to display "No earthquakes found."
                    emptyStateTextView.setText(R.string.no_internet_connection);
                    // Make the progress bar invisible
                    tryAgainButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}