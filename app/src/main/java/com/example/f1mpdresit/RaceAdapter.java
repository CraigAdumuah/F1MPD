package com.example.f1mpdresit;
// Name                 Craig Adumuah_________________
// Student ID           S2026435_________________
// Programme of Study   Computing_________________
//
//
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RaceAdapter extends RecyclerView.Adapter<RaceAdapter.RaceViewHolder> {

    private List<Race> raceList;

    public RaceAdapter(List<Race> raceList) {
        this.raceList = raceList;
    }

    @NonNull
    @Override
    public RaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.race_item, parent, false);
        return new RaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RaceViewHolder holder, int position) {
        Race race = raceList.get(position);
        holder.roundText.setText("Round: " + race.getRound());
        holder.raceNameText.setText("Race: " + race.getRaceName());
        holder.dateText.setText("Date: " + race.getDate());
        holder.timeText.setText("Time: " + race.getTime());

        if (race.isPast()) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return raceList.size();
    }

    static class RaceViewHolder extends RecyclerView.ViewHolder {
        TextView roundText, raceNameText, dateText, timeText;

        public RaceViewHolder(@NonNull View itemView) {
            super(itemView);
            roundText = itemView.findViewById(R.id.roundText);
            raceNameText = itemView.findViewById(R.id.raceNameText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
