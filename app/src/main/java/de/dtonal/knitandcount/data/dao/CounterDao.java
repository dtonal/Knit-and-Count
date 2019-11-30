package de.dtonal.knitandcount.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import de.dtonal.knitandcount.data.model.Counter;

@Dao
public interface CounterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCounter(Counter counter);

    @Query("SELECT * from counter where project_id = :projectId")
    Counter[] getCounterByProjectId(int projectId);

    @Query("SELECT * from counter where counter_id = :id")
    Counter getCounterById(Integer id);

    @Delete
    void deleteCounters(Counter[] counters);
}
