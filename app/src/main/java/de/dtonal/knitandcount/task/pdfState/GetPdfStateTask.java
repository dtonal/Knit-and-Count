package de.dtonal.knitandcount.task.pdfState;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import de.dtonal.knitandcount.data.dao.PdfStateDao;
import de.dtonal.knitandcount.data.model.PdfState;
import de.dtonal.knitandcount.listener.pdfState.PdfStateLoadedListener;

public class GetPdfStateTask extends AsyncTask<Integer, Void, PdfState> {
    private final WeakReference<PdfStateLoadedListener> listener;
    private final PdfStateDao pdfStateDto;

    public GetPdfStateTask(PdfStateLoadedListener listener, PdfStateDao pdfStateDto) {
        this.listener = new WeakReference<>(listener);
        this.pdfStateDto = pdfStateDto;
    }

    @Override
    protected PdfState doInBackground(Integer... ids) {
        PdfState[] pdfStateByProjectId = pdfStateDto.getPdfStateByProjectId(ids[0]);
        return pdfStateByProjectId.length > 0 ? pdfStateByProjectId[0] : null;
    }

    @Override
    protected void onPostExecute(PdfState pdfState) {
        PdfStateLoadedListener loadedListener = listener.get();
        if (loadedListener != null) {
            loadedListener.onPdfStateLoaded(pdfState);
        }
        super.onPostExecute(pdfState);
    }
}
