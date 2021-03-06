package de.dtonal.knitandcount.task.counter;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import de.dtonal.knitandcount.data.dao.CounterDao;
import de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.listener.counter.CounterSavedListener;

public class SaveCounterTask extends AsyncTask<Counter, Void, Counter> {
    private final WeakReference<CounterSavedListener> listenerReference;
    private final CounterDao dao;

    public SaveCounterTask(CounterSavedListener counterSavedListener, CounterDao dao){
        this.listenerReference= new WeakReference<>(counterSavedListener);
        this.dao = dao;
    }

    @Override
    protected Counter doInBackground(Counter... counters) {
        dao.insertCounter(counters[0]);
        return counters[0];
    }

    @Override
    protected void onPostExecute(Counter counter) {
        super.onPostExecute(counter);
        CounterSavedListener listener = listenerReference.get();
        if(listener!=null){
            listener.onCounterSaved(counter);
        }
    }
}
