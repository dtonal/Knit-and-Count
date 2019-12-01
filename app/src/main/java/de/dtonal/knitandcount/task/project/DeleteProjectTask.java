package de.dtonal.knitandcount.task.project;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.project.ProjectDeletedListener;

public class DeleteProjectTask extends AsyncTask<Project, Void, Project[]> {
    private final WeakReference<ProjectDeletedListener> listener;
    private final ProjectDao projectDao;

    public DeleteProjectTask(ProjectDeletedListener listener, ProjectDao projectDao) {
        this.listener = new WeakReference<>(listener);
        this.projectDao = projectDao;
    }

    @Override
    protected Project[] doInBackground(Project... projects) {
        projectDao.deleteProject(projects[0]);
        return projects;
    }

    @Override
    protected void onPostExecute(Project[] projects) {
        super.onPostExecute(projects);
        ProjectDeletedListener deletedListener = listener.get();
        if(deletedListener!=null){
            deletedListener.onProjectDeleted(projects[0]);
        }
        super.onPostExecute(projects);
    }
}
