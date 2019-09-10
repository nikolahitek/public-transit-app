package com.dev.tasevski.gpkumanovo.extra;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.tasevski.gpkumanovo.R;
import com.dev.tasevski.gpkumanovo.model.BusStop;

import java.util.List;
import java.util.Locale;

public class BusStopsAdapter extends RecyclerView.Adapter<BusStopsAdapter.MyViewHolder> {
    private List<BusStop> stops;
    private final OnItemClickListener<BusStop> listener;
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView number;
        TextView distance;
        MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            number = v.findViewById(R.id.number);
            distance = v.findViewById(R.id.distance);
        }

        void bind(final BusStop item, final OnItemClickListener<BusStop> listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public BusStopsAdapter(List<BusStop> stops, OnItemClickListener<BusStop> listener) {
        this.stops = stops;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusStopsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stops_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(!stops.get(position).isRegion())
            holder.name.setText(String.format(Locale.ENGLISH,"Постојка %s",stops.get(position).getName()));
        else
            holder.name.setText(String.format(Locale.ENGLISH,"Регион %s",stops.get(position).getName()));
        holder.number.setText(String.format(Locale.ENGLISH,"%03d",stops.get(position).getId()));
        if (stops.get(position).getDistanceFromUser()==-1) {
            holder.distance.setText("На ? m");
        } else {
            holder.distance.setText(String.format(Locale.ENGLISH, "На %d m", (long) (stops.get(position).getDistanceFromUser() * 1000)));
        }
        holder.bind(stops.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }
}
