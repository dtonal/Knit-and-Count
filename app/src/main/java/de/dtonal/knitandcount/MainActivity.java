package de.dtonal.knitandcount;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import de.dtonal.knitandcount.adapter.ProjectAdapter;
import de.dtonal.knitandcount.data.AppDatabase;
import de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.project.OnProjectClickListener;
import de.dtonal.knitandcount.listener.project.ProjectsLoadedListener;
import de.dtonal.knitandcount.service.ProjectService;

public class MainActivity extends AppCompatActivity implements OnProjectClickListener, ProjectsLoadedListener {
    private AppDatabase db = null;
    private ProjectAdapter projectsAdapter;

    private final ArrayList<Project> projects = new ArrayList<>();

    @Override
    protected void onRestart() {
        updateProjects();
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.db = DataBaseService.getOrInitAppDataBase(getApplicationContext());

        initButton();

        initProjectsRecyclerView();

        updateProjects();
    }

    private void updateProjects() {
        ProjectService.loadAllProjects(this, this.db.projectDao());
    }

    private void initProjectsRecyclerView() {
        RecyclerView projectsRecycler = findViewById(R.id.projects_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        projectsRecycler.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        projectsRecycler.setLayoutManager(layoutManager);

        projectsAdapter = new ProjectAdapter(projects, this);
        projectsRecycler.setAdapter(projectsAdapter);

    }

    private void initButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddProject.class));
            }
        });
    }

    @Override
    public void onProjectClicked(Project project) {
        Intent showProjectIntent = new Intent(this, ShowProject.class);
        showProjectIntent.putExtra("project_id", project.getId());
        startActivity(showProjectIntent);
    }

    @Override
    public void onProjectsLoaded(Project[] projects) {
        this.projectsAdapter.setData(Arrays.asList(projects));
    }

}
