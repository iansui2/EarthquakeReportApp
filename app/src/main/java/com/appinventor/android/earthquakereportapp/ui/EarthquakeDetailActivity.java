package com.appinventor.android.earthquakereportapp.ui;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.appinventor.android.earthquakereportapp.R;

import java.util.Objects;

public class EarthquakeDetailActivity extends AppCompatActivity {

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_detail_activity);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        toolBar.setTitle(R.string.app_detail);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button);

        TextView textViewMagnitude = findViewById(R.id.magnitude);
        TextView textViewLocationOffset = findViewById(R.id.location_offset);
        TextView textViewPrimaryLocation = findViewById(R.id.primary_location);
        TextView textViewDate = findViewById(R.id.date);
        TextView textViewFelt = findViewById(R.id.number_felt);
        TextView textViewTsunami = findViewById(R.id.tsunami_alert);
        TextView textViewLocation = findViewById(R.id.loc_desc);
        TextView textViewDepth = findViewById(R.id.depth_desc);
        Button buttonViewMore = findViewById(R.id.view_more_button);

        String mFormattedDateAndTime = Objects.requireNonNull(getIntent().getExtras()).getString("date");
        Double mMagnitude = getIntent().getExtras().getDouble("magnitude");
        String mFormattedMagnitude = getIntent().getExtras().getString("formattedMagnitude");
        String mLocationOffset = getIntent().getExtras().getString("offset");
        String mPrimaryLocation = getIntent().getExtras().getString("primary");
        String mFelt = getIntent().getExtras().getString("felt");
        String mTsunamiAlertDescription = getIntent().getExtras().getString("tsunami");
        mUrl = getIntent().getExtras().getString("url");
        String mLocation = getIntent().getExtras().getString("location");
        String mDepth = getIntent().getExtras().getString("depth");

        if(mFelt.equals("null")) {
            mFelt = "0";
        }

        // Get the background of the textViewMagnitude
        GradientDrawable magnitudeSquare = (GradientDrawable) textViewMagnitude.getBackground();
        // Get the magnitude color of the magnitude of the current earthquake property
        int magnitudeColor = getMagnitudeColor(mMagnitude);
        // Set the color of the textViewMagnitude
        magnitudeSquare.setColor(magnitudeColor);

        textViewDate.setText(mFormattedDateAndTime);
        textViewMagnitude.setText(mFormattedMagnitude);
        textViewLocationOffset.setText(mLocationOffset);
        textViewPrimaryLocation.setText(mPrimaryLocation);
        textViewFelt.setText(mFelt);
        textViewTsunami.setText(mTsunamiAlertDescription);
        textViewLocation.setText(mLocation);
        textViewDepth.setText(mDepth);

        buttonViewMore.setOnClickListener(v -> {
            Uri earthquakeUri = Uri.parse(mUrl);
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
            startActivity(websiteIntent);
        });
    }

    private int getMagnitudeColor(Double magnitude) {
        int magnitudeColorResourceId;
        // Convert the double data type to int date type with math.floor
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        // Return the magnitudeColorResourceId
        return ContextCompat.getColor(this, magnitudeColorResourceId);
    }

}