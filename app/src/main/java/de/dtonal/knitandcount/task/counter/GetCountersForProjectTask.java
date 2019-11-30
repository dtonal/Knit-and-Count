package de.dtonal.knitandcount.task.counter;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import de.dtonal.knitandcount.data.dao.CounterDao;
import de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.counter.CounterForProjectListener;

public class GetCountersForProjectTask extends AsyncTask<Project, Void, Counter[]> {
    private final WeakReference<CounterForProjectListener> listener;
    private final CounterDao counterDao;

    public GetCountersForProjectTask(CounterForProjectListener counterForProjectListener, CounterDao counterDao) {
        this.listener = new WeakReference<>(counterForProjectListener);
        this.counterDao = counterDao;
    }

    @Override
    protected Counter[] doInBackground(Project... projects) {
        return counterDao.getCounterByProjectId(projects[0].getId());
    }

    @Override
    protected void onPostExecute(Counter[] counters) {
        super.onPostExecute(counters);
        CounterForProjectListener counterForProjectListener = listener.get();
        if (counterForProjectListener != null) {
            counterForProjectListener.counterForProject(counters);
        }
    }
}
