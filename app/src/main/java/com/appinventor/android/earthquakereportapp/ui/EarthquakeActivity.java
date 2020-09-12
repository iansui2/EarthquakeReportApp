package com.appinventor.android.earthquakereportapp.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appinventor.android.earthquakereportapp.R;
import com.appinventor.android.earthquakereportapp.data.EarthquakeViewModel;
import com.appinventor.android.earthquakereportapp.pojo.Earthquake;

import java.sql.Time;
import java.util.List;
import java.util.Objects;

import static com.appinventor.android.earthquakereportapp.network.ConnectivityUtil.*;

public class EarthquakeActivity extends AppCompatActivity {

    public ProgressBar progressBar;

    private static final long START_TIME_IN_MILLIS = 5000;
    public CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        final ImageView iconImageView = findViewById(R.id.no_internet_image);
        final TextView emptyStateTextView = findViewById(R.id.empty_view_summary);
        final TextView emptyStateTextViewDescription = findViewById(R.id.empty_view_description);
        final Button tryAgainButton = findViewById(R.id.try_again_button);
        progressBar = findViewById(R.id.loading_indicator);

        iconImageView.setVisibility(View.INVISIBLE);

        // Make the progress bar invisible
        tryAgainButton.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // Set the layout manager of RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set the RecyclerView size to fixed
        recyclerView.setHasFixedSize(true);
        // Create a DividerItemDecoration for the RecyclerView
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                // Get the child adapter position from the parent
                int position = parent.getChildAdapterPosition(view);
                // Hide the divider for the last child
                if (position == Objects.requireNonNull(parent.getAdapter()).getItemCount() - 1) {
                    outRect.setEmpty();
                } else {
                    super.getItemOffsets(outRect, view, parent, state);
                }
            }
        });

        final EarthquakeAdapter adapter = new EarthquakeAdapter();
        // Set the adapter of the RecyclerView to the EarthquakeAdapter
        recyclerView.setAdapter(adapter);

        // Create an EarthquakeViewModel Object
        final EarthquakeViewModel earthquakeViewModel = new ViewModelProvider(this).get(EarthquakeViewModel.class);

        // Get the method from the EarthquakeViewModel and call the method observe
        earthquakeViewModel.getEarthquakeListObservableData().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(final List<Earthquake> earthquakes) {
                // Set the LiveData for the RecyclerView
                setupList(earthquakes);
                tryAgainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tryAgain(earthquakes);
                    }
                });
            }

            public void setupList(List<Earthquake> earthquakes) {
                mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
                mCountDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
                    public void onTick(long millisUntilFinished) {
                        mTimeLeftInMillis = millisUntilFinished;
                        // Make the progress bar visible
                        showProgressBar(true);
                        iconImageView.setVisibility(View.INVISIBLE);
                        // Clear the adapter
                        emptyStateTextView.setText("");
                        emptyStateTextViewDescription.setText("");
                        // Make the button invisible
                        tryAgainButton.setVisibility(View.INVISIBLE);
                    }

                    public void onFinish() {
                        mTimerRunning = false;
                        if (networkAvailable(getApplicationContext())) {
                            // Make the progress bar invisible
                            showProgressBar(false);
                            // Clear the adapter
                            adapter.clear();

                            if (earthquakes != null && !earthquakes.isEmpty()) {
                                // Add the data to the RecyclerView
                                adapter.setAllEarthquakes(earthquakes);
                                // Set empty state text to display nothing
                                emptyStateTextView.setText("");
                            }
                        } else {
                            // Make the progress bar invisible
                            showProgressBar(false);
                            iconImageView.setVisibility(View.VISIBLE);
                            // Set empty state text to display "No earthquakes found."
                            emptyStateTextView.setText(R.string.no_internet_connection);
                            emptyStateTextViewDescription.setText(R.string.check_internet_connection);
                            // Make the button visible
                            tryAgainButton.setVisibility(View.VISIBLE);
                        }
                    }
                }.start();
                mTimerRunning = true;
            }

            public void tryAgain(List<Earthquake> earthquakes) {
                mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
                mCountDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
                    public void onTick(long millisUntilFinished) {
                        mTimeLeftInMillis = millisUntilFinished;
                        // Make the progress bar visible
                        showProgressBar(true);
                        iconImageView.setVisibility(View.INVISIBLE);
                        // Clear the adapter
                        emptyStateTextView.setText("");
                        emptyStateTextViewDescription.setText("");
                        // Make the button invisible
                        tryAgainButton.setVisibility(View.INVISIBLE);
                    }

                    public void onFinish() {
                        mTimerRunning = false;
                        earthquakeViewModel.retryCall();
                        showProgressBar(false);
                        // Clear the adapter
                        emptyStateTextView.setText("");
                        // Make the button invisible
                        tryAgainButton.setVisibility(View.INVISIBLE);

                        if (networkAvailable(getApplicationContext())) {
                            // Clear the adapter
                            adapter.clear();
                            if (earthquakes != null && !earthquakes.isEmpty()) {
                                // Add the data to the RecyclerView
                                adapter.setAllEarthquakes(earthquakes);
                                // Set empty state text to display nothing
                                emptyStateTextView.setText("");
                            }

                        } else {
                            // Make the progress bar invisible
                            showProgressBar(false);
                            iconImageView.setVisibility(View.VISIBLE);
                            // Set empty state text to display "No earthquakes found."
                            emptyStateTextView.setText(R.string.no_internet_connection);
                            emptyStateTextViewDescription.setText(R.string.check_internet_connection);
                            // Make the button visible
                            tryAgainButton.setVisibility(View.VISIBLE);
                        }
                    }
                }.start();
                mTimerRunning = true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeft", mTimeLeftInMillis);
        outState.putBoolean("timerRunning", mTimerRunning);
        outState.putLong("endTime", mEndTime);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mTimeLeftInMillis = savedInstanceState.getLong("millisLeft");
        mTimerRunning = savedInstanceState.getBoolean("timerRunning");

        if (mTimerRunning) {
            mEndTime = savedInstanceState.getLong("endTime");
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
        } else {
            mCountDownTimer.onFinish();
            mCountDownTimer.cancel();
        }
    }

    public void showProgressBar(boolean visibility) {
        progressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

}