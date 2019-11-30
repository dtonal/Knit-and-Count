package de.dtonal.knitandcount;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import de.dtonal.knitandcount.adapter.ProjectAdapter;
import de.dtonal.knitandcount.data.AppDatabase;
import de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.project.OnProjectClickListener;
import de.dtonal.knitandcount.listener.project.ProjectsLoadedListener;
import de.dtonal.knitandcount.task.project.GetProjectsTask;

public class MainActivity extends AppCompatActivity implements OnProjectClickListener, ProjectsLoadedListener {

    AppDatabase db = null;
    private static final String TAG = "MainActivity";
    private LinearLayoutManager layoutManager;
    private FloatingActionButton fab;
    private RecyclerView projectsRecycler;
    private ProjectAdapter projectsAdapter;

    private ArrayList<Project> projects = new ArrayList<>();
    private GetProjectsTask getProjectsTasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.db = DataBaseService.getOrInitAppDataBase(getApplicationContext());

        initButton();

        initProjectsRecyclerView();

        initTasks();

        updateProjects();
    }

    private void initTasks() {
        this.getProjectsTasks = new GetProjectsTask(this, this.db.projectDao());
    }

    private void updateProjects() {
        getProjectsTasks.execute();
    }

    private void initProjectsRecyclerView() {
        projectsRecycler = findViewById(R.id.projects_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        projectsRecycler.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        projectsRecycler.setLayoutManager(layoutManager);

        projectsAdapter = new ProjectAdapter(projects, this);
        projectsRecycler.setAdapter(projectsAdapter);

    }

    private void initButton() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "floating action triggerd");
                startActivity(new Intent(MainActivity.this, AddProject.class));
            }
        });
    }

    @Override
    public void onProjectClicked(Project project) {
        Log.d(TAG, "Project was clicked: " + project.getName());
        Intent showProjectIntent = new Intent(this, ShowProject.class);
        showProjectIntent.putExtra("project_id", project.getId());
        startActivity(showProjectIntent);
    }

    @Override
    public void onProjectsLoaded(Project[] projects) {
        this.projectsAdapter.setData(Arrays.asList(projects));
    }

}
