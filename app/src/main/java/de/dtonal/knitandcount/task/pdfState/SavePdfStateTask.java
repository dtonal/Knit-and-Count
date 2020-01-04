package de.dtonal.knitandcount.task.pdfState;

import android.os.AsyncTask;

import de.dtonal.knitandcount.data.dao.PdfStateDao;
import de.dtonal.knitandcount.data.model.PdfState;

public class SavePdfStateTask extends AsyncTask<PdfState, Void, Void> {

    private final PdfStateDao dao;

    public SavePdfStateTask(PdfStateDao dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(PdfState... pdfStates) {
        dao.insertPdfState(pdfStates[0]);
        return null;
    }
}
