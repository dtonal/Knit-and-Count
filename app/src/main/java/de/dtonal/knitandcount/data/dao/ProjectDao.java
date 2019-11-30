package de.dtonal.knitandcount.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import de.dtonal.knitandcount.data.model.Project;

@Dao
public interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProject(Project project);

    @Query("SELECT * FROM project order by creation_date")
    Project[] getAllProjects();

    @Query("SELECT * FROM project where id = :id")
    Project getById(int id);
}
