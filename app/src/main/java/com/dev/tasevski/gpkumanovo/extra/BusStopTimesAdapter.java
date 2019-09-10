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

public class BusStopTimesAdapter extends RecyclerView.Adapter<BusStopTimesAdapter.MyViewHolder> {
    private List<String> stopNames;
    private List<Integer> idList;
    private List<String> closestTimes;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView number;
        TextView time;
        MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            number = v.findViewById(R.id.number);
            time = v.findViewById(R.id.time);
        }
    }

    public BusStopTimesAdapter(List<String> stopNames, List<Integer> idList, List<String> closestTimes) {
        this.stopNames = stopNames;
        this.closestTimes = closestTimes;
        this.idList = idList;
    }

    @NonNull
    @Override
    public BusStopTimesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_times_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(String.format(Locale.ENGLISH,"Автобус %s",stopNames.get(position)));
        holder.number.setText(String.format(Locale.ENGLISH,"%03d",idList.get(position)));
        holder.time.setText(closestTimes.get(position));
    }

    @Override
    public int getItemCount() {
        return stopNames.size();
    }
}
