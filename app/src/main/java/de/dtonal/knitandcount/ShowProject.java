package de.dtonal.knitandcount;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class ShowProject extends AppCompatActivity implements CounterForProjectListener, ProjectForIdListener, CounterInteractionListener, CounterSavedListener {
    private static final String TAG = "ShowProject";
    private TextView textViewProjectName;
    private GetCounterTask getCounterTask;
    private GetProjectTask getProjectTask;
    private SaveCounterTask saveCounterTask;
    private int project_id;
    private RecyclerView counterRecycler;
    private LinearLayoutManager layoutManager;
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
    }

    private void initCounterRecycler() {
        counterRecycler = findViewById(R.id.counter_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        counterRecycler.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNewCounter() {
        Log.d(TAG, "CreateNewCounter");
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
        Log.d(TAG, "Found counters: " + counter.length);
        counterAdapter.setData(Arrays.asList(counter));

    }

    @Override
    public void projectLoaded(Project project) {
        textViewProjectName.setText(project.getName());
        getCounterTask.execute(project);
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
