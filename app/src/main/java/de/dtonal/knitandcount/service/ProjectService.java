package de.dtonal.knitandcount.service;

import de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.project.ProjectDeletedListener;
import de.dtonal.knitandcount.listener.project.ProjectForIdListener;
import de.dtonal.knitandcount.listener.project.ProjectsLoadedListener;
import de.dtonal.knitandcount.task.project.DeleteProjectTask;
import de.dtonal.knitandcount.task.project.GetProjectTask;
import de.dtonal.knitandcount.task.project.GetProjectsTask;

public class ProjectService {
    public static void loadProjectById(int id, ProjectForIdListener listener, ProjectDao dao){
        GetProjectTask getProjectTask = new GetProjectTask(listener, dao);
        getProjectTask.execute(id);
    }

    public static void deleteProject(Project project, ProjectDeletedListener listener, ProjectDao dao){
        DeleteProjectTask deleteProjectTask = new DeleteProjectTask(listener, dao);
        deleteProjectTask.execute(project);
    }

    public static void loadAllProjects(ProjectsLoadedListener listener, ProjectDao dao){
        GetProjectsTask getProjectsTask = new GetProjectsTask(listener, dao);
        getProjectsTask.execute();
    }
}
