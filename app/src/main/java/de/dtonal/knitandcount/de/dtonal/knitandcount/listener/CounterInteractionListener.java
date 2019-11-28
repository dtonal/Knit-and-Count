package de.dtonal.knitandcount.de.dtonal.knitandcount.listener;

import de.dtonal.knitandcount.de.dtonal.knitandcount.data.model.Counter;

public interface CounterInteractionListener {
    void onUpdatedCounter(Counter counter);
}
