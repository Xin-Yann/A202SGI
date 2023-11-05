package com.example.assignment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import com.example.assignment.NorthAdapter.CardViewHolder;

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
        String Duration = data.getDuration();
        holder.nameTextView.setText(routeName);
        holder.durationTextView.setText("Duration: " + Duration + " mins");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView durationTextView;

        public CardViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            durationTextView = itemView.findViewById(R.id.duration);
        }
    }
}
