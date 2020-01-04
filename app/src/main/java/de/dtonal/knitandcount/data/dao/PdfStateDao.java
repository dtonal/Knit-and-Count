package de.dtonal.knitandcount.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import de.dtonal.knitandcount.data.model.PdfState;

@Dao
public interface PdfStateDao {
    @Query("SELECT * from pdfstate where project_id = :projectId")
    PdfState[] getPdfStateByProjectId(int projectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPdfState(PdfState pdfState);

}
