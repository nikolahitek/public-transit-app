package com.dev.tasevski.gpkumanovo.extra;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.tasevski.gpkumanovo.R;
import com.dev.tasevski.gpkumanovo.model.BusLine;

import java.util.List;
import java.util.Locale;

public class LinesAdapter extends RecyclerView.Adapter<LinesAdapter.MyViewHolder> {
    private List<BusLine> lines;
    private final OnItemClickListener<BusLine> listener;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView number;
        MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            number = v.findViewById(R.id.number);
        }

        void bind(final BusLine item, final OnItemClickListener<BusLine> listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public LinesAdapter(List<BusLine> lines, OnItemClickListener<BusLine> listener) {
        this.lines = lines;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LinesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lines_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(String.format(Locale.ENGLISH,"Линија %s",lines.get(position).getName()));
        holder.number.setText(String.format(Locale.ENGLISH,"%d",lines.get(position).getId()));
        holder.bind(lines.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return lines.size();
    }
}
