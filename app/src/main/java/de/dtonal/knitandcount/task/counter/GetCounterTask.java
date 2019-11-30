package de.dtonal.knitandcount.task.counter;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import de.dtonal.knitandcount.data.dao.CounterDao;
import de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.listener.counter.CounterLoadedListener;

public class GetCounterTask extends AsyncTask<Integer, Void, Counter> {
    private final WeakReference<CounterLoadedListener> listener;
    private final CounterDao counterDao;

    public GetCounterTask(CounterLoadedListener counterForProjectListener, CounterDao counterDao) {
        this.listener = new WeakReference<>(counterForProjectListener);
        this.counterDao = counterDao;
    }

    @Override
    protected Counter doInBackground(Integer... ids) {
        return counterDao.getCounterById(ids[0]);
    }

    @Override
    protected void onPostExecute(Counter counter) {
        super.onPostExecute(counter);
        CounterLoadedListener counterLoadedListener= listener.get();
        if (counterLoadedListener != null) {
            counterLoadedListener.onCounterLoaded(counter);
        }super.onPostExecute(counter);
    }
}
