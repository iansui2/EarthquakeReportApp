package com.appinventor.android.earthquakereportapp.ui;

import android.content.Intent;
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
import com.appinventor.android.earthquakereportapp.pojo.EarthquakeRoom;
import com.appinventor.android.earthquakereportapp.variables.Constants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.appinventor.android.earthquakereportapp.util.ContextGetter.getAppContext;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeHolder> {

    private List<EarthquakeRoom> allEarthquakes = new ArrayList<>();

    @NonNull
    @Override
    public EarthquakeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a View and inflate the layout of earthquake_item
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.earthquake_item, parent, false);
        return new EarthquakeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeHolder holder, int position) {
        // Get the current earthquake property
        EarthquakeRoom currentEarthquakeProperty = allEarthquakes.get(position);

        String primaryLocation;
        String locationOffset;

        // Get the magnitude of the current earthquake property
        Double magnitude = currentEarthquakeProperty.getMag();
        // Format the value of magnitude
        String formattedMagnitude = formatMagnitude(magnitude);

        // Get the background of the textViewMagnitude
        GradientDrawable magnitudeSquare = (GradientDrawable) holder.textViewMagnitude.getBackground();

        // Get the magnitude color of the magnitude of the current earthquake property
        int magnitudeColor = getMagnitudeColor(currentEarthquakeProperty.getMag());
        // Set the color of the textViewMagnitude
        magnitudeSquare.setColor(magnitudeColor);

        // Get the place of the current earthquake property
        String originalLocation = currentEarthquakeProperty.getPlace();

        if (originalLocation.contains(Constants.LOCATION_SEPARATOR)) {
            // Split the parts of the array from the location separator
            String [] parts = originalLocation.split(Constants.LOCATION_SEPARATOR);
            // Combine the 0th index of the array to the location separator
            locationOffset = parts[0] + Constants.LOCATION_SEPARATOR;
            // Get the 1st index of the array
            primaryLocation = parts[1];
        } else {
            // Get the string near the
            locationOffset = getAppContext().getString(R.string.near_the);
            // Get the string originalLocation
            primaryLocation = originalLocation;
        }

        // Get the date and time of the current earthquake property
        long time = currentEarthquakeProperty.getTime();
        // Create a date object
        Date dateObject = new Date(time);
        // Format the date
        String formattedDate = formatDate(dateObject);
        // Format the time
        String formattedTime = formatTime(dateObject);
        // Format both date and time
        String formattedDateAndTime = formatDateAndTime(dateObject);

        // Get the number of people who felt the current earthquake
        String felt = currentEarthquakeProperty.getFelt();

        // Get the tsunami alert of the current earthquake property
        int tsunamiAlert = currentEarthquakeProperty.getTsunami();
        String tsunamiAlertDescription = checkTsunamiAlert(tsunamiAlert);

        // Get the url of the current earthquake property
        String url = currentEarthquakeProperty.getUrl();

        // Get the longitude of the current earthquake property
        Double longitude = currentEarthquakeProperty.getLongitude();
        String formattedLongitude = formatLongitudeAndLatitude(longitude);

        // Get the latitude of the current earthquake property
        Double latitude = currentEarthquakeProperty.getLatitude();
        String formattedLatitude = formatLongitudeAndLatitude(latitude);

        String location = formattedLongitude + ", " + formattedLatitude;

        // Get the depth of the current earthquake property
        Double depth = currentEarthquakeProperty.getDepth();

        String formattedDepth = depth + " m";

        holder.textViewMagnitude.setText(formattedMagnitude);
        holder.textViewLocationOffset.setText(locationOffset);
        holder.textViewPrimaryLocation.setText(primaryLocation);
        holder.textViewDate.setText(formattedDate);
        holder.textViewTime.setText(formattedTime);

        holder.itemView.setOnClickListener(v -> {
            Intent earthquakeDetailActivityIntent = new Intent(getAppContext(), EarthquakeDetailActivity.class);
            earthquakeDetailActivityIntent.putExtra("date", formattedDateAndTime);
            earthquakeDetailActivityIntent.putExtra("magnitude", magnitude);
            earthquakeDetailActivityIntent.putExtra("formattedMagnitude", formattedMagnitude);
            earthquakeDetailActivityIntent.putExtra("offset", locationOffset);
            earthquakeDetailActivityIntent.putExtra("primary", primaryLocation);
            earthquakeDetailActivityIntent.putExtra("felt", felt);
            earthquakeDetailActivityIntent.putExtra("tsunami", tsunamiAlertDescription);
            earthquakeDetailActivityIntent.putExtra("url", url);
            earthquakeDetailActivityIntent.putExtra("location", location);
            earthquakeDetailActivityIntent.putExtra("depth", formattedDepth);
            earthquakeDetailActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getAppContext().startActivity(earthquakeDetailActivityIntent);
        });
    }

    @Override
    public int getItemCount() {
        return allEarthquakes.size();
    }

    public void setAllEarthquakes(List<EarthquakeRoom> allEarthquakes) {
        this.allEarthquakes = allEarthquakes;
        // Notify the RecyclerView that the data set has changed.
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
            case 10:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude0Below;
                break;
        }
        // Return the magnitudeColorResourceId
        return ContextCompat.getColor(getAppContext(), magnitudeColorResourceId);
    }

    private String formatMagnitude(double magnitude) {
        // Format the magnitude to one decimal place
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private String formatLongitudeAndLatitude(double longitudeAndLatitude) {
        // Format the magnitude to one decimal place
        DecimalFormat magnitudeFormat = new DecimalFormat("0.000");
        return magnitudeFormat.format(longitudeAndLatitude);
    }

    private String formatDate(Date dateObject) {
        // Format the date to month, day and year
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        // Format the time to hours and minutes
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatDateAndTime(Date dateObject) {
        // Format the time to hours and minutes
        SimpleDateFormat timeFormat = new SimpleDateFormat("LLL dd, yyyy h:mm a");
        return timeFormat.format(dateObject);
    }

    private String checkTsunamiAlert(int tsunamiAlert) {
        String tsunamiAlertDescription;
        if(tsunamiAlert == 1) {
            tsunamiAlertDescription = "Yes";
        } else {
            tsunamiAlertDescription = "No";
        }
        return tsunamiAlertDescription;
    }


    public void clear() {
        int size = allEarthquakes.size();
        // Clear the list
        allEarthquakes.clear();
        // Notify the RecyclerView of the item range removed
        notifyItemRangeRemoved(0, size);
    }
}
