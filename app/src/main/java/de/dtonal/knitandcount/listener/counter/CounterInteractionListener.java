package de.dtonal.knitandcount.listener.counter;

import de.dtonal.knitandcount.data.model.Counter;

public interface CounterInteractionListener {
    void onUpdatedCounter(Counter counter);

    void onEditCounter(Counter counter);
}
