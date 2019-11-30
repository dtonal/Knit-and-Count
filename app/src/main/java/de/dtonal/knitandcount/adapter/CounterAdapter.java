package de.dtonal.knitandcount.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.dtonal.knitandcount.R;
import de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.listener.counter.CounterInteractionListener;
import de.dtonal.knitandcount.service.CounterService;

public class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.ViewHolder> {
    private final List<Counter> counters;
    private final CounterInteractionListener counterInteractionListener;

    public CounterAdapter(List<Counter> counters, CounterInteractionListener counterInteractionListener) {
        this.counters = counters;
        this.counterInteractionListener = counterInteractionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CounterAdapter.ViewHolder holder, int position) {
        holder.counterValue.setText(String.valueOf(counters.get(position).getValue()));
        holder.counterName.setText(counters.get(position).getName());
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return counters.size();
    }

    public void setData(List<Counter> counters){
        this.counters.clear();
        this.counters.addAll(counters);
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView counterName;
        final TextView counterValue;
        final Button buttonIncrement;
        final Button buttonDecrement;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            counterName = itemView.findViewById(R.id.counter_name);
            counterValue = itemView.findViewById(R.id.counter_value);
            buttonIncrement = itemView.findViewById(R.id.counter_increment);
            buttonDecrement = itemView.findViewById(R.id.counter_decrement);
        }

        void bind(final int position) {
            this.buttonIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Counter counter = counters.get(position);
                    CounterService.increment(counter);
                    counterInteractionListener.onUpdatedCounter(counter);
                    counterValue.setText(String.valueOf(counter.getValue()));
                }
            });
            this.buttonDecrement.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Counter counter = counters.get(position);
                    CounterService.decrement(counter);
                    counterInteractionListener.onUpdatedCounter(counter);
                    counterValue.setText(String.valueOf(counter.getValue()));
                }
            });
            this.counterName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Counter counter = counters.get(position);
                    counterInteractionListener.onEditCounter(counter);
                }
            });
        }
    }
}
