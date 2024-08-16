package com.example.f1mpdresit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {

    private final ArrayList<String> infoList;

    public InfoAdapter(ArrayList<String> infoList) {
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
        holder.infoTextView.setText(infoList.get(position));
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public static class InfoViewHolder extends RecyclerView.ViewHolder {
        TextView infoTextView;

        public InfoViewHolder(View itemView) {
            super(itemView);
            infoTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
