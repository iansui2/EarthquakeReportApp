package com.appinventor.android.earthquakereportapp.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appinventor.android.earthquakereportapp.R;
import com.appinventor.android.earthquakereportapp.data.EarthquakeRepository;
import com.appinventor.android.earthquakereportapp.data.EarthquakeViewModel;
import com.appinventor.android.earthquakereportapp.pojo.Earthquake;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

import static com.appinventor.android.earthquakereportapp.network.ConnectivityUtil.*;

public class EarthquakeActivity extends AppCompatActivity {

    private ProgressBar mainProgressBar;
    private DrawerLayout drawerLayout;

    private LinearLayoutManager linearLayoutManager;

    private static long START_TIME_IN_MILLIS = 5000;
    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    private boolean isLoading = false;
    private boolean isRefreshing = false;

    private EarthquakeRepository earthquakeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.home:
                        item.setChecked(true);
                        Intent homeIntent = new Intent(EarthquakeActivity.this, EarthquakeActivity.class);
                        startActivity(homeIntent);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.trivia:
                        item.setChecked(true);
                        Intent destinationIntent = new Intent(EarthquakeActivity.this, EarthquakeTriviaActivity.class);
                        startActivity(destinationIntent);
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });

        final ImageView iconImageView = findViewById(R.id.no_internet_image);
        final TextView emptyStateTextView = findViewById(R.id.empty_view_summary);
        final TextView emptyStateTextViewDescription = findViewById(R.id.empty_view_description);
        final Button tryAgainButton = findViewById(R.id.try_again_button);
        mainProgressBar = findViewById(R.id.main_loading_indicator);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setVisibility(View.INVISIBLE);

        // Make the image view invisible
        iconImageView.setVisibility(View.INVISIBLE);

        // Make the button invisible
        tryAgainButton.setVisibility(View.INVISIBLE);

        linearLayoutManager = new LinearLayoutManager(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // Set the layout manager of RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int itemCountDivided = totalItemCount / 20;

                if(!isLoading) {
                    if((visibleItemCount + firstVisibleItemPosition) >= itemCountDivided) {
                        isLoading = true;
                    }
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
                // Call the tryAgain function when the button is clicked
                tryAgainButton.setOnClickListener(v -> tryAgain(earthquakes));
                swipeRefreshLayout.setOnRefreshListener(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    refresh(earthquakes);
                    isRefreshing = true;
                });
            }

            public void setupList(List<Earthquake> earthquakes) {
                // Get the current time in milliseconds and add it to the time left in milliseconds
                // and assign it to a variable
                mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
                // Create a new countdown timer and start it
                mCountDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
                    public void onTick(long millisUntilFinished) {
                        // Assign the parameter millisUntilFinished to the variable mTimeLeftInMillis
                        mTimeLeftInMillis = millisUntilFinished;
                        // Make the progress bar visible
                        if(!isRefreshing) {
                            showMainProgressBar(true);
                        } else {
                            showMainProgressBar(false);
                        }
                        // Make the image view invisible
                        iconImageView.setVisibility(View.INVISIBLE);
                        // Set the text of the text view to nothing
                        emptyStateTextView.setText("");
                        // Set the text of the text view to nothing
                        emptyStateTextViewDescription.setText("");
                        // Make the button invisible
                        tryAgainButton.setVisibility(View.INVISIBLE);
                    }

                    public void onFinish() {
                        // Assign the value false to the variable mTimerRunning
                        mTimerRunning = false;
                            if (networkAvailable()) {
                                // Make the progress bar invisible
                                showMainProgressBar(false);
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                                // Clear the adapter
                                adapter.clear();

                                if (earthquakes != null && !earthquakes.isEmpty()) {
                                    // Add the data to the RecyclerView
                                    adapter.setAllEarthquakes(earthquakes);
                                    // Set empty state text to display nothing
                                    emptyStateTextView.setText("");
                                } else {
                                    Log.e("EarthquakeActivity", "Adapter is null and empty");
                                }

                            } else {
                                // Make the progress bar invisible
                                showMainProgressBar(false);
                                swipeRefreshLayout.setRefreshing(false);
                                swipeRefreshLayout.setVisibility(View.INVISIBLE);
                                // Make the image view visible
                                iconImageView.setVisibility(View.VISIBLE);
                                // Set the text of the text view to "No Internet Connection"
                                emptyStateTextView.setText(R.string.no_internet_connection);
                                // Set the text of the text view to "Please Check your Internet Connection"
                                emptyStateTextViewDescription.setText(R.string.check_internet_connection);
                                // Make the button visible
                                tryAgainButton.setVisibility(View.VISIBLE);
                            }
                    }
                }.start();
                // Assign the value true to the variable mTimerRunning
                mTimerRunning = true;
            }

            public void tryAgain(List<Earthquake> earthquakes) {
                earthquakeViewModel.retryCall();
                // Make the image view invisible
                iconImageView.setVisibility(View.INVISIBLE);
                // Set the text of the text view to nothing
                emptyStateTextView.setText("");
                // Set the text of the text view to nothing
                emptyStateTextViewDescription.setText("");
                // Make the button invisible
                tryAgainButton.setVisibility(View.INVISIBLE);
                // Make the progress bar visible
                showMainProgressBar(true);
            }

            public void refresh(List<Earthquake> earthquakes) {
                earthquakeViewModel.retryCall();
                // Make the image view invisible
                iconImageView.setVisibility(View.INVISIBLE);
                // Set the text of the text view to nothing
                emptyStateTextView.setText("");
                // Set the text of the text view to nothing
                emptyStateTextViewDescription.setText("");
                // Make the button invisible
                tryAgainButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void showMainProgressBar(boolean visibility) {
        mainProgressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }
}