package de.dtonal.knitandcount.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import de.dtonal.knitandcount.data.model.Project;

@Dao
public interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProject(Project project);

    @Query("SELECT * FROM project order by creation_date desc")
    Project[] getAllProjects();

    @Query("SELECT * FROM project where id = :id")
    Project getById(int id);

    @Delete
    void deleteProject(Project project);

    @Update
    void updateProject(Project project);
}
