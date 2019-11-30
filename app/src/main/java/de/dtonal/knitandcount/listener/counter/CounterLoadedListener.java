package de.dtonal.knitandcount.listener.counter;

import de.dtonal.knitandcount.data.model.Counter;

public interface CounterLoadedListener {
    void onCounterLoaded(Counter counter);
}
