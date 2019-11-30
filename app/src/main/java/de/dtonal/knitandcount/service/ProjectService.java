package de.dtonal.knitandcount.service;

import de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.listener.project.ProjectForIdListener;
import de.dtonal.knitandcount.task.project.GetProjectTask;

public class ProjectService {
    public static void loadProjectById(int id, ProjectForIdListener listener, ProjectDao dao){
        GetProjectTask getProjectTask = new GetProjectTask(listener, dao);
        getProjectTask.execute(id);
    }
}
