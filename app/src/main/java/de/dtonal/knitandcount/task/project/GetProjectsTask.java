package de.dtonal.knitandcount.task.project;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.project.ProjectsLoadedListener;

public class GetProjectsTask extends AsyncTask<Void, Void, Project[]> {
    private WeakReference<ProjectsLoadedListener> listenerReference;
    private ProjectDao projectDao;

    public GetProjectsTask(ProjectsLoadedListener projectsLoadedListener, ProjectDao projectDao) {
        this.projectDao = projectDao;
        this.listenerReference = new WeakReference<>(projectsLoadedListener);
    }


    @Override
    protected Project[] doInBackground(Void... voids) {
        return projectDao.getAllProjects();
    }

    @Override
    protected void onPostExecute(Project[] projects) {
        super.onPostExecute(projects);
        ProjectsLoadedListener listener = listenerReference.get();
        if(listener!=null){
            listener.onProjectsLoaded(projects);
        }
    }
}
