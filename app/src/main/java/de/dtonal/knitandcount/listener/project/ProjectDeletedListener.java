package de.dtonal.knitandcount.listener.project;

import de.dtonal.knitandcount.data.model.Project;

public interface ProjectDeletedListener {
    void onProjectDeleted(Project project);
}
