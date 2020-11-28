package com.appinventor.android.earthquakereportapp.ui;

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

import com.appinventor.android.earthquakereportapp.R;
import com.appinventor.android.earthquakereportapp.data.EarthquakeTriviaRepository;
import com.appinventor.android.earthquakereportapp.data.EarthquakeTriviaViewModel;
import com.appinventor.android.earthquakereportapp.data.EarthquakeViewModel;
import com.appinventor.android.earthquakereportapp.pojo.EarthquakeTrivia;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

import static com.appinventor.android.earthquakereportapp.network.ConnectivityUtil.networkAvailable;

public class EarthquakeTriviaActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private LinearLayoutManager linearLayoutManager;

    private EarthquakeTriviaRepository earthquakeTriviaRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_trivia_activity);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        toolBar.setTitle(R.string.app_trivia);

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
                        Intent homeIntent = new Intent(EarthquakeTriviaActivity.this, EarthquakeActivity.class);
                        startActivity(homeIntent);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.trivia:
                        item.setChecked(true);
                        Intent destinationIntent = new Intent(EarthquakeTriviaActivity.this, EarthquakeTriviaActivity.class);
                        startActivity(destinationIntent);
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // Set the layout manager of RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);
        // Set the RecyclerView size to fixed
        recyclerView.setHasFixedSize(true);

        final EarthquakeTriviaAdapter adapter = new EarthquakeTriviaAdapter();

        // Set the adapter of the RecyclerView to the EarthquakeAdapter
        recyclerView.setAdapter(adapter);

        // Create an EarthquakeViewModel Object
        final EarthquakeTriviaViewModel earthquakeTriviaViewModel = new ViewModelProvider(this).get(EarthquakeTriviaViewModel.class);

        // Get the method from the EarthquakeViewModel and call the method observe
        earthquakeTriviaViewModel.getEarthquakeTriviaObservableData().observe(this, new Observer<List<EarthquakeTrivia>>() {
            @Override
            public void onChanged(List<EarthquakeTrivia> earthquakeTrivia) {
                adapter.setAllEarthquakeTrivia(earthquakeTrivia);
            }
        });

        earthquakeTriviaViewModel.callEarthquakeTriviaObservableInRepository();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}