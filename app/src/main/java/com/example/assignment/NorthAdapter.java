package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NorthAdapter extends RecyclerView.Adapter<NorthAdapter.CardViewHolder> {
    private List<North> dataList;
    private Context context;

    public NorthAdapter(Context context, List<North> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void updateData(List<North> newData) {
        dataList = newData;
        notifyDataSetChanged();
    }

    public void setFilteredList(List<North> filteredList) {
        this.dataList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_view, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        North data = dataList.get(position);
        String routeName = data.getName();
        String duration = data.getDuration();
        String departureTime = data.getDepartureTime();
        String arrivalTime = data.getArrivalTime();

        holder.nameTextView.setText(routeName);
        holder.durationTextView.setText("Duration: " + duration);
        holder.departTextView.setText(departureTime);
        holder.arrivalTextView.setText(arrivalTime);
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView durationTextView, departTextView, arrivalTextView;

        public CardViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            durationTextView = itemView.findViewById(R.id.duration);
            departTextView =  itemView.findViewById(R.id.depart_time1);
            arrivalTextView = itemView.findViewById(R.id.arrive_time1);
        }
    }
}