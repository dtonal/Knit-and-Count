package de.dtonal.knitandcount;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.dtonal.knitandcount.de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.dao.CounterDao;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.de.dtonal.knitandcount.listener.CounterForProjectListener;
import de.dtonal.knitandcount.de.dtonal.knitandcount.listener.ProjectForIdListener;
import de.dtonal.knitandcount.de.dtonal.knitandcount.task.GetCounterTask;
import de.dtonal.knitandcount.de.dtonal.knitandcount.task.GetProjectTask;

public class ShowProject extends AppCompatActivity implements CounterForProjectListener, ProjectForIdListener {
    private static final String TAG = "ShowProject";
    private TextView textViewProjectName;
    private Project project;
    private GetCounterTask getCounterTask;
    private GetProjectTask getProjectTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_project);

        textViewProjectName = findViewById(R.id.project_title);

        initTasks();

        loadProject();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNewCounter() {
        Log.d(TAG, "CreateNewCounter");
    }

    private void loadProject() {
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        int project_id = extras.getInt("project_id");
        getProjectTask.execute(project_id);
    }

    private void initTasks() {
        CounterDao counterDao = DataBaseService.getOrInitAppDataBase(getApplicationContext()).counterDao();
        this.getCounterTask = new GetCounterTask(this, counterDao);
        ProjectDao projectDao = DataBaseService.getOrInitAppDataBase(getApplicationContext()).projectDao();
        this.getProjectTask = new GetProjectTask(this, projectDao);
    }


    @Override
    public void counterForProject(Counter[] counter) {
        Log.d(TAG, "Found counters: " + counter.length);

    }

    @Override
    public void projectLoaded(Project project) {
        textViewProjectName.setText(project.getName());
        getCounterTask.execute(project);
    }


}
