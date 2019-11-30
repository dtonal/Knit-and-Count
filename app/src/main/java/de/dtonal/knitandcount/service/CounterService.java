package de.dtonal.knitandcount.service;

import de.dtonal.knitandcount.data.dao.CounterDao;
import de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.listener.counter.CounterDeletedListener;
import de.dtonal.knitandcount.listener.counter.CounterLoadedListener;
import de.dtonal.knitandcount.listener.counter.CounterSavedListener;
import de.dtonal.knitandcount.task.counter.DeleteCounterTask;
import de.dtonal.knitandcount.task.counter.GetCounterTask;
import de.dtonal.knitandcount.task.counter.SaveCounterTask;

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

    public static void loadCounterById(int counterId, CounterLoadedListener listener, CounterDao counterDao){
        GetCounterTask task = new GetCounterTask(listener, counterDao);
        task.execute(counterId);
    }

    public static void saveCounter(Counter counter, CounterSavedListener listener, CounterDao counterDao){
        SaveCounterTask task = new SaveCounterTask(listener, counterDao);
        task.execute(counter);
    }

    public static void deleteCounter(Counter counter, CounterDeletedListener listener, CounterDao counterDao){
        DeleteCounterTask task = new DeleteCounterTask(listener, counterDao);
        task.execute(counter);
    }
}
