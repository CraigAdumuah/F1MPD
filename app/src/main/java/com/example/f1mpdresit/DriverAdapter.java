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

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {

    private List<Driver> driverList;

    public DriverAdapter(List<Driver> driverList) {
        this.driverList = driverList;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_item, parent, false);
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver driver = driverList.get(position);
        holder.positionText.setText("Position: " + driver.getPosition());
        holder.nameText.setText("Name: " + driver.getGivenName() + " " + driver.getFamilyName());
        holder.pointsText.setText("Points: " + driver.getPoints());
        holder.winsText.setText("Wins: " + driver.getWins());
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    static class DriverViewHolder extends RecyclerView.ViewHolder {
        TextView positionText, nameText, pointsText, winsText;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            positionText = itemView.findViewById(R.id.positionText);
            nameText = itemView.findViewById(R.id.nameText);
            pointsText = itemView.findViewById(R.id.pointsText);
            winsText = itemView.findViewById(R.id.winsText);
        }
    }
}
