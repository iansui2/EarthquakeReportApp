package com.appinventor.android.earthquakereportapp.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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
import com.appinventor.android.earthquakereportapp.network.AppExecutors;
import com.appinventor.android.earthquakereportapp.network.ConnectivityUtil;
import com.appinventor.android.earthquakereportapp.pojo.EarthquakeRoom;
import com.appinventor.android.earthquakereportapp.viewmodels.EarthquakeViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

public class EarthquakeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;

    private LinearLayoutManager linearLayoutManager;

    private static long START_TIME_IN_MILLIS = 5000;
    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    private boolean isLoading = false;

    private ConnectivityUtil networkConnectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        networkConnectivity = new ConnectivityUtil(AppExecutors.getInstance(), this);

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

        progressBar = findViewById(R.id.loading_indicator);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.primary_red));

        MaterialAlertDialogBuilder alertCheckConnectionDialog = new MaterialAlertDialogBuilder(this);
        alertCheckConnectionDialog.setTitle(R.string.no_internet_connection)
                .setMessage(R.string.swipe_refresh)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.cancel())
                .create();

        MaterialAlertDialogBuilder alertCheckConnectionFirstTimeDialog = new MaterialAlertDialogBuilder(this);
        alertCheckConnectionFirstTimeDialog.setTitle(R.string.no_internet_connection)
                .setMessage(R.string.click_ok)
                .setCancelable(false)
                .create();

        MaterialAlertDialogBuilder alertSavedDataDialog = new MaterialAlertDialogBuilder(this);
        alertSavedDataDialog.setTitle(R.string.no_internet_connection)
                .setMessage(R.string.saved_data)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.cancel())
                .create();

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
        earthquakeViewModel.getEarthquakeListObservableData().observe(this, new Observer<List<EarthquakeRoom>>() {
            @Override
            public void onChanged(final List<EarthquakeRoom> earthquake) {
                // Set the LiveData for the RecyclerView
                setupList(earthquake);
                swipeRefreshLayout.setOnRefreshListener(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    refresh();
                });
            }

            public void setupList(List<EarthquakeRoom> earthquakes) {
                // Get the current time in milliseconds and add it to the time left in milliseconds
                // and assign it to a variable
                mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
                // Create a new countdown timer and start it
                mCountDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
                    public void onTick(long millisUntilFinished) {
                        // Assign the parameter millisUntilFinished to the variable mTimeLeftInMillis
                        mTimeLeftInMillis = millisUntilFinished;
                    }

                    public void onFinish() {
                        networkConnectivity.checkInternetConnection((isConnected -> {
                            // Make the progress bar invisible
                            showProgressBar(false);
                            // Assign the value false to the variable mTimerRunning
                            mTimerRunning = false;
                            swipeRefreshLayout.setRefreshing(false);

                            if (isConnected) {
                                if (earthquakes != null && !earthquakes.isEmpty()) {
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                                    // Clear the adapter
                                    adapter.clear();
                                    // Add the data to the RecyclerView
                                    adapter.setAllEarthquakes(earthquakes);
                                }
                            } else {
                                if (earthquakes != null && !earthquakes.isEmpty()) {
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                                    // Clear the adapter
                                    adapter.clear();
                                    // Add the data to the RecyclerView
                                    adapter.setAllEarthquakes(earthquakes);


                                    networkConnectivity.checkInternetConnection(isConnected1 -> {
                                        if (!isConnected1) {
                                            alertSavedDataDialog.show();
                                        }
                                    });

                                } else {
                                    alertCheckConnectionFirstTimeDialog.setPositiveButton(R.string.ok, (dialog, which) -> {
                                        earthquakeViewModel.retryCall();

                                        dialog.cancel();
                                        showProgressBar(true);

                                        networkConnectivity.checkInternetConnection(isConnected2 -> {
                                            if (!isConnected2) {
                                                alertCheckConnectionFirstTimeDialog.show();
                                                swipeRefreshLayout.setRefreshing(false);
                                            }
                                        });
                                    });
                                    alertCheckConnectionFirstTimeDialog.show();
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }));
                    }
                }.start();
                // Assign the value true to the variable mTimerRunning
                mTimerRunning = true;
            }

            public void refresh() {
                earthquakeViewModel.retryCall();

                networkConnectivity.checkInternetConnection(isConnected3 -> {
                    if (!isConnected3) {
                        alertCheckConnectionDialog.show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        earthquakeViewModel.callEarthquakeObservableInRepository();
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

    private void showProgressBar(boolean visibility) {
        progressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }
}