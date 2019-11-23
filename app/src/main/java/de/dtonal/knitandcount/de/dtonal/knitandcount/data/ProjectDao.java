package de.dtonal.knitandcount.de.dtonal.knitandcount.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertProject(Project project);

    @Query("SELECT * FROM project")
    public Project[] getAllProjects();
}
