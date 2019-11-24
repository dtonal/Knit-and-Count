package de.dtonal.knitandcount.de.dtonal.knitandcount.listener;

import de.dtonal.knitandcount.de.dtonal.knitandcount.data.model.Project;

public interface ProjectsLoadedListener {
    public void onProjectsLoaded(Project[] projects);
}
