package de.dtonal.knitandcount.task.project;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.project.ProjectForIdListener;

public class GetProjectTask extends AsyncTask<Integer, Void, Project> {
    private final WeakReference<ProjectForIdListener> listener;
    private final ProjectDao projectDao;

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

