package de.dtonal.knitandcount.de.dtonal.knitandcount.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Project.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProjectDao projectDao();
}
