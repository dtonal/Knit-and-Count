package de.dtonal.knitandcount.task.counter;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import de.dtonal.knitandcount.data.dao.CounterDao;
import de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.listener.counter.CounterDeletedListener;

public class DeleteCounterTask extends AsyncTask<Counter, Void, Counter[]> {
    private WeakReference<CounterDeletedListener> listener;
    private CounterDao counterDao;

    public DeleteCounterTask(CounterDeletedListener listener, CounterDao counterDao) {
        this.listener = new WeakReference<>(listener);
        this.counterDao = counterDao;
    }

    @Override
    protected Counter[] doInBackground(Counter... counters) {
        counterDao.deleteCounters(counters);
        return counters;
    }

    @Override
    protected void onPostExecute(Counter[] counters) {
        super.onPostExecute(counters);
        CounterDeletedListener deletedListener = listener.get();
        if(deletedListener!=null){
            deletedListener.onCounterDeleted(counters);
        }
        super.onPostExecute(counters);
    }
}
