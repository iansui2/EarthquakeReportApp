package com.appinventor.android.earthquakereportapp.ui;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appinventor.android.earthquakereportapp.R;
import com.appinventor.android.earthquakereportapp.pojo.Earthquake;
import com.appinventor.android.earthquakereportapp.variables.Constants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.appinventor.android.earthquakereportapp.context.ContextGetter.*;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeHolder> {

    private List<Earthquake> allEarthquakes = new ArrayList<>();

    @Override
    public EarthquakeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.earthquake_item, parent, false);
        return new EarthquakeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeHolder holder, int position) {
        Earthquake currentEarthquakeProperty = allEarthquakes.get(position);

        String primaryLocation;
        String locationOffset;

        Double magnitude = currentEarthquakeProperty.getMag();
        String formattedMagnitude = formatMagnitude(magnitude);

        GradientDrawable magnitudeSquare = (GradientDrawable) holder.textViewMagnitude.getBackground();

        int magnitudeColor = getMagnitudeColor(currentEarthquakeProperty.getMag());
        magnitudeSquare.setColor(magnitudeColor);

        String originalLocation = currentEarthquakeProperty.getPlace();

        if (originalLocation.contains(Constants.LOCATION_SEPARATOR)) {
            String [] parts = originalLocation.split(Constants.LOCATION_SEPARATOR);
            locationOffset = parts[0] + Constants.LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = getAppContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        Date dateObject = new Date(currentEarthquakeProperty.getTime());
        String formattedDate = formatDate(dateObject);
        String formattedTime = formatTime(dateObject);

        holder.textViewMagnitude.setText(formattedMagnitude);
        holder.textViewLocationOffset.setText(locationOffset);
        holder.textViewPrimaryLocation.setText(primaryLocation);
        holder.textViewDate.setText(formattedDate);
        holder.textViewTime.setText(formattedTime);
    }

    @Override
    public int getItemCount() {
        return allEarthquakes.size();
    }

    public void setAllEarthquakes(List<Earthquake> allEarthquakes) {
        this.allEarthquakes = allEarthquakes;
        notifyDataSetChanged();
    }

    static class EarthquakeHolder extends RecyclerView.ViewHolder {
        private TextView textViewMagnitude;
        private TextView textViewLocationOffset;
        private TextView textViewPrimaryLocation;
        private TextView textViewDate;
        private TextView textViewTime;

        public EarthquakeHolder(View itemView) {
            super(itemView);
            textViewMagnitude = itemView.findViewById(R.id.magnitude);
            textViewLocationOffset = itemView.findViewById(R.id.location_offset);
            textViewPrimaryLocation = itemView.findViewById(R.id.primary_location);
            textViewDate = itemView.findViewById(R.id.date);
            textViewTime = itemView.findViewById(R.id.time);
        }
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
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
        return ContextCompat.getColor(getAppContext(), magnitudeColorResourceId);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
