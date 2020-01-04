package de.dtonal.knitandcount.task.project;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.project.ProjectSavedListener;

public class UpdateProjectTask extends AsyncTask<Project, Void, Project> {
    private final WeakReference<ProjectSavedListener> listenerReference;
    private final ProjectDao projectDao;

    public UpdateProjectTask(ProjectSavedListener listener, ProjectDao projectDao) {
        this.projectDao = projectDao;
        this.listenerReference = new WeakReference<>(listener);
    }

    @Override
    protected Project doInBackground(Project... projects) {
        projectDao.updateProject(projects[0]);
        return projects[0];
    }

    @Override
    protected void onPostExecute(Project project) {
        super.onPostExecute(project);
        ProjectSavedListener listener = listenerReference.get();
        if (listener != null) {
            listener.onProjectSaved(project);
        }
    }
}
