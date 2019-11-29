package de.dtonal.knitandcount.de.dtonal.knitandcount.service;

import de.dtonal.knitandcount.de.dtonal.knitandcount.data.model.Counter;

public class CounterService {
    public static void increment(Counter counter){
        int currentValue = counter.getValue();
        currentValue ++;
        if(counter.getResetValue() > -1 && currentValue>=counter.getResetValue()){
            currentValue = 0;
        }
        counter.setValue(currentValue);
    }

    public static void decrement(Counter counter){
        int currentValue = counter.getValue();
        currentValue--;
        if(currentValue<0){
            currentValue = 0;
        }
        counter.setValue(currentValue);
    }
}
