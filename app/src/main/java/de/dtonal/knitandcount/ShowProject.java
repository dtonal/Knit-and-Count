package de.dtonal.knitandcount;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import de.dtonal.knitandcount.de.dtonal.knitandcount.adapter.CounterAdapter;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.dao.CounterDao;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.de.dtonal.knitandcount.listener.CounterForProjectListener;
import de.dtonal.knitandcount.de.dtonal.knitandcount.listener.CounterInteractionListener;
import de.dtonal.knitandcount.de.dtonal.knitandcount.listener.CounterSavedListener;
import de.dtonal.knitandcount.de.dtonal.knitandcount.listener.ProjectForIdListener;
import de.dtonal.knitandcount.de.dtonal.knitandcount.task.GetCounterTask;
import de.dtonal.knitandcount.de.dtonal.knitandcount.task.GetProjectTask;
import de.dtonal.knitandcount.de.dtonal.knitandcount.task.SaveCounterTask;
import de.dtonal.knitandcount.de.dtonal.knitandcount.utils.FileUtil;

public class ShowProject extends AppCompatActivity implements CounterForProjectListener, ProjectForIdListener, CounterInteractionListener, CounterSavedListener {
    private static final String TAG = "ShowProject";
    private static final int PDF_REQUEST = 1;

    private TextView textViewProjectName;
    private RecyclerView counterRecycler;
    private PDFView pdfView;

    private GetCounterTask getCounterTask;
    private GetProjectTask getProjectTask;
    private SaveCounterTask saveCounterTask;

    private int project_id;

    private CounterAdapter counterAdapter;
    private CounterDao counterDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_project);

        textViewProjectName = findViewById(R.id.project_title);

        initTasks();

        initCounterRecycler();

        loadProject();

        this.pdfView = findViewById(R.id.pdfView);
    }

    private void initCounterRecycler() {
        counterRecycler = findViewById(R.id.counter_recycler);
        counterRecycler.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        counterRecycler.setLayoutManager(layoutManager);
        counterAdapter = new CounterAdapter(new ArrayList<Counter>(), this);
        counterRecycler.setAdapter(counterAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.project_menu, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            copyFileToProjectsFolder(data);
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
             pdfView.fromFile(projectPdf).load();
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
        this.getCounterTask = new GetCounterTask(this, counterDao);
        ProjectDao projectDao = DataBaseService.getOrInitAppDataBase(getApplicationContext()).projectDao();
        this.getProjectTask = new GetProjectTask(this, projectDao);
    }


    @Override
    public void counterForProject(Counter[] counter) {
        counterAdapter.setData(Arrays.asList(counter));
    }

    @Override
    public void projectLoaded(Project project) {
        textViewProjectName.setText(project.getName());
        getCounterTask.execute(project);
        updatePdfView();
    }

    @Override
    public void onUpdatedCounter(Counter counter) {
        this.saveCounterTask = new SaveCounterTask(this, this.counterDao);
        this.saveCounterTask.execute(counter);
    }

    @Override
    public void onCounterSaved(Counter counter) {
        Log.d(TAG, "Counter saved");
    }
}
