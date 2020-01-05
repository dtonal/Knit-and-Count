package de.dtonal.knitandcount;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.dtonal.knitandcount.adapter.CounterAdapter;
import de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.data.dao.CounterDao;
import de.dtonal.knitandcount.data.dao.PdfStateDao;
import de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.data.model.PdfState;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.counter.CounterForProjectListener;
import de.dtonal.knitandcount.listener.counter.CounterInteractionListener;
import de.dtonal.knitandcount.listener.counter.CounterSavedListener;
import de.dtonal.knitandcount.listener.pdfState.PdfStateLoadedListener;
import de.dtonal.knitandcount.listener.project.ProjectDeletedListener;
import de.dtonal.knitandcount.listener.project.ProjectForIdListener;
import de.dtonal.knitandcount.service.ProjectService;
import de.dtonal.knitandcount.task.counter.GetCountersForProjectTask;
import de.dtonal.knitandcount.task.counter.SaveCounterTask;
import de.dtonal.knitandcount.task.pdfState.GetPdfStateTask;
import de.dtonal.knitandcount.task.pdfState.SavePdfStateTask;
import de.dtonal.knitandcount.task.project.GetProjectTask;
import de.dtonal.knitandcount.utils.FileUtil;

public class ShowProject extends AppCompatActivity implements CounterForProjectListener, ProjectForIdListener, CounterInteractionListener, CounterSavedListener, ProjectDeletedListener, OnRenderListener, PdfStateLoadedListener {
    private static final String TAG = "ShowProject";
    private static final int PDF_REQUEST = 1;

    private TextView textViewProjectName;
    private PDFView pdfView;

    private GetCountersForProjectTask getCounterTask;
    private GetProjectTask getProjectTask;

    private int project_id;

    private CounterAdapter counterAdapter;
    private CounterDao counterDao;
    private GridLayoutManager layoutManager;
    private Project project;
    private ProjectDao projectDao;
    private PdfState pdfState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_project);

        textViewProjectName = findViewById(R.id.project_title);

        this.pdfView = findViewById(R.id.pdfView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initTasks();

        initCounterRecycler();

        loadProject();
    }

    private void initCounterRecycler() {
        RecyclerView counterRecycler = findViewById(R.id.counter_recycler);
        counterRecycler.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager.setSpanCount(1);
        }

        counterRecycler.setLayoutManager(layoutManager);
        counterAdapter = new CounterAdapter(new ArrayList<Counter>(), this);
        counterRecycler.setAdapter(counterAdapter);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager.setSpanCount(4);
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager.setSpanCount(2);
        }

        super.onConfigurationChanged(newConfig);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.project_menu, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.createCounterMenuItem:
                createNewCounter();
                return true;
            case R.id.addChangePdfMenuItem:
                choosePdf();
                return true;
            case R.id.deleteProject:
                ProjectService.deleteProject(this.project, this, projectDao);
            case R.id.removePdfMenuItem:
                removePdf();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void choosePdf() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, PDF_REQUEST);
    }

    private void removePdf() {
        File projectsDir = FileUtil.getOrCreateProjectFolder(getApplicationContext(), project_id);
        File copy = new File(projectsDir + "/project.pdf");
        if (copy.exists()) {
            copy.delete();
        }
        if (this.pdfState != null) {
            this.pdfState.update(1f, 0f, 0f);
        }
        updatePdfView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            copyFileToProjectsFolder(data);
            if (this.pdfState != null) {
                this.pdfState.update(1f, 0f, 0f);
            }
            updatePdfView();
        }
    }

    private void copyFileToProjectsFolder(@NotNull Intent data) {
        File projectsDir = FileUtil.getOrCreateProjectFolder(getApplicationContext(), project_id);
        File copy = new File(projectsDir+"/project.pdf");
        try {
            FileUtil.copyChosenDataContentToFile(getContentResolver(), data.getData(), copy);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePdfView() {
        File projectsDir = FileUtil.getOrCreateProjectFolder(getApplicationContext(), project_id);
        File projectPdf = new File(projectsDir+"/project.pdf");
        if(projectPdf.exists()){
            pdfView.setVisibility(View.VISIBLE);
            pdfView.fromFile(projectPdf).onRender(this).load();
        } else {
            pdfView.setVisibility(View.INVISIBLE);
        }
    }


    private void createNewCounter() {
        Intent switchToCreateNewCounterIntent = new Intent(this, AddCounter.class);
        switchToCreateNewCounterIntent.putExtra("project_id", project_id);
        startActivity(switchToCreateNewCounterIntent);
    }

    private void loadProject() {
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        project_id = extras.getInt("project_id");
        getProjectTask.execute(project_id);
    }

    private void initTasks() {
        counterDao = DataBaseService.getOrInitAppDataBase(getApplicationContext()).counterDao();
        this.getCounterTask = new GetCountersForProjectTask(this, counterDao);
        projectDao = DataBaseService.getOrInitAppDataBase(getApplicationContext()).projectDao();
        this.getProjectTask = new GetProjectTask(this, projectDao);
    }


    @Override
    public void counterForProject(Counter[] counter) {
        counterAdapter.setData(Arrays.asList(counter));
    }

    @Override
    public void projectLoaded(final Project project) {
        textViewProjectName.setText(project.getName());
        getCounterTask.execute(project);
        updatePdfView();

        textViewProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowProject.this, UpdateProject.class);
                intent.putExtra("project_id", project.getId());
                startActivity(intent);
            }
        });

        this.project = project;
    }

    @Override
    public void onUpdatedCounter(Counter counter) {
        SaveCounterTask saveCounterTask = new SaveCounterTask(this, this.counterDao);
        saveCounterTask.execute(counter);
    }

    @Override
    public void onEditCounter(Counter counter) {
        Intent intent = new Intent(this, UpdateCounter.class);
        intent.putExtra("counter_id", counter.getId());
        startActivity(intent);
    }

    @Override
    public void onCounterSaved(Counter counter) {
        Log.d(TAG, "Counter saved");
    }

    @Override
    public void onProjectDeleted(Project project) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onInitiallyRendered(int nbPages) {
        PdfStateDao pdfStateDao = DataBaseService.getOrInitAppDataBase(getApplicationContext()).pdfStateDao();
        GetPdfStateTask getPdfStateTask = new GetPdfStateTask(this, pdfStateDao);
        getPdfStateTask.execute(this.project_id);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.pdfState != null) {
            this.pdfState.update(pdfView.getZoom(), pdfView.getCurrentXOffset(), pdfView.getCurrentYOffset());
            SavePdfStateTask savePdfStateTask = new SavePdfStateTask(DataBaseService.getOrInitAppDataBase(getApplicationContext()).pdfStateDao());
            savePdfStateTask.execute(this.pdfState);
        }
    }

    @Override
    public void onPdfStateLoaded(PdfState pdfState) {
        this.pdfState = pdfState;
        if (this.pdfState == null) {
            this.pdfState = new PdfState(this.project_id);
        }
        pdfView.zoomTo(this.pdfState.getZoom());
        pdfView.moveTo(this.pdfState.getOffsetX(), this.pdfState.getOffsetY());
        pdfView.loadPages();
    }
}
