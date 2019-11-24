package de.dtonal.knitandcount.de.dtonal.knitandcount.task;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import de.dtonal.knitandcount.de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.de.dtonal.knitandcount.listener.ProjectForIdListener;

public class GetProjectTask extends AsyncTask<Integer, Void, Project> {
    private WeakReference<ProjectForIdListener> listener;
    private ProjectDao projectDao;

    public GetProjectTask(ProjectForIdListener projectForIdListener, ProjectDao projectDao) {
        this.projectDao = projectDao;
        this.listener = new WeakReference<>(projectForIdListener);
    }


    @Override
    protected Project doInBackground(Integer... integers) {
        return projectDao.getById(integers[0]);
    }

    @Override
    protected void onPostExecute(Project project) {
        super.onPostExecute(project);
        ProjectForIdListener projectForIdListener = listener.get();
        if(projectForIdListener!=null){
            projectForIdListener.projectLoaded(project);
        }
    }
}

