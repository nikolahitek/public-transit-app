package com.dev.tasevski.gpkumanovo.extra;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.tasevski.gpkumanovo.R;
import com.dev.tasevski.gpkumanovo.model.StopAndTime;

import java.util.List;
import java.util.Locale;

public class BusStopsForLineAdapter extends RecyclerView.Adapter<BusStopsForLineAdapter.MyViewHolder> {
    private List<StopAndTime> stops;
    private int minutes;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView time;
        MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            time = v.findViewById(R.id.time);
        }
    }

    public BusStopsForLineAdapter(List<StopAndTime> stops, int minutes) {
        this.stops = stops;
        this.minutes = minutes;
    }

    @NonNull
    @Override
    public BusStopsForLineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType==3) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_for_line_view, parent, false);
        } else if (viewType==1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_for_line_view_start, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_for_line_end, parent, false);
        }

        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(!stops.get(position).getStop().isRegion())
            holder.name.setText(String.format(Locale.ENGLISH,"Постојка %s",stops.get(position).getStop().getName()));
        if(stops.get(position).getStop().isRegion())
            holder.name.setText(String.format(Locale.ENGLISH,"Регион %s",stops.get(position).getStop().getName()));
        if(stops.get(position).getTimeApart()!=-2) {
            int addedTime = minutes + stops.get(position).getTimeApart();
            holder.time.setText(String.format(Locale.getDefault(), "%02d:%02d", addedTime / 60, addedTime % 60));
        } else {
            holder.time.setText("-    ");
        }

    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 1;
        else if(position == (getItemCount()-1)) return 2;
        return 3;
    }
}
