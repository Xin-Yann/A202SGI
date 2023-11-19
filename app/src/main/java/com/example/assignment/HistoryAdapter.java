package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<History> historyArrayList;

    public HistoryAdapter(Context context, ArrayList<History> historyArrayList) {
        this.context = context;
        this.historyArrayList = historyArrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.booking_history_a,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {

        History history = historyArrayList.get(position);

        holder.destination_name.setText(history.destination_name);
        holder.origin_name.setText(history.origin_name);
        holder.seat_type.setText(history.seat_type);
        holder.seat_no.setText(history.seat_no);
        holder.train_date.setText(history.train_date);
        holder.seat_coach.setText(history.seat_coach);

    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView destination_name, origin_name, seat_type, seat_no, train_date, seat_coach;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            destination_name = itemView.findViewById(R.id.destinationName);
            origin_name = itemView.findViewById(R.id.originName);
            seat_type = itemView.findViewById(R.id.ticketType);
            seat_no = itemView.findViewById(R.id.seatNo);
            train_date = itemView.findViewById(R.id.date);
            seat_coach = itemView.findViewById(R.id.coach);
        }
    }

}
