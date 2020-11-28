package com.appinventor.android.earthquakereportapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appinventor.android.earthquakereportapp.R;
import com.appinventor.android.earthquakereportapp.pojo.EarthquakeTrivia;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeTriviaAdapter extends RecyclerView.Adapter<EarthquakeTriviaAdapter.EarthquakeTriviaHolder> {

    private List<EarthquakeTrivia> allEarthquakeTrivia = new ArrayList<>();

    @NonNull
    @Override
    public EarthquakeTriviaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a View and inflate the layout of earthquake_item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.earthquake_trivia_item, parent, false);
        return new EarthquakeTriviaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeTriviaHolder holder, int position) {
        // Get the current earthquake property
        EarthquakeTrivia currentEarthquakeTriviaProperty = allEarthquakeTrivia.get(position);

        String title = currentEarthquakeTriviaProperty.getTitle();
        String extract = currentEarthquakeTriviaProperty.getExtract();

        holder.titleTextView.setText(title);
        holder.extractTextView.setText(extract);
    }

    @Override
    public int getItemCount() {
        return allEarthquakeTrivia.size();
    }

    public void setAllEarthquakeTrivia(List<EarthquakeTrivia> allEarthquakeTrivia) {
        this.allEarthquakeTrivia = allEarthquakeTrivia;
        // Notify the RecyclerView that the data set has changed.
        notifyDataSetChanged();
    }

    public static class EarthquakeTriviaHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView extractTextView;

        public EarthquakeTriviaHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            extractTextView = itemView.findViewById(R.id.extract);
        }
    }

    public void clear() {
        int size = allEarthquakeTrivia.size();
        // Clear the list
        allEarthquakeTrivia.clear();
        // Notify the RecyclerView of the item range removed
        notifyItemRangeRemoved(0, size);
    }
}
