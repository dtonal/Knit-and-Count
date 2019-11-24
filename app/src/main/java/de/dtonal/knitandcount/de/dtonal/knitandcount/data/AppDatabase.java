package de.dtonal.knitandcount.de.dtonal.knitandcount.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import de.dtonal.knitandcount.de.dtonal.knitandcount.data.de.dtonal.knitandcount.data.Converters;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.de.dtonal.knitandcount.data.model.Project;

@Database(entities = {Project.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProjectDao projectDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE project add column creation_date INTEGER");
        }
    };
}
