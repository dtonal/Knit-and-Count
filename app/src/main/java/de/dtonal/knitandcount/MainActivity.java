package de.dtonal.knitandcount;

import android.content.Intent;
import android.os.AsyncTask;
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

import de.dtonal.knitandcount.de.dtonal.knitandcount.data.AppDatabase;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.de.dtonal.knitandcount.listener.OnProjectClickListener;

public class MainActivity extends AppCompatActivity implements OnProjectClickListener {

    AppDatabase db = null;
    private static final String TAG = "MainActivity";
    private LinearLayoutManager layoutManager;
    private FloatingActionButton fab;
    private RecyclerView projectsRecycler;
    private ProjectAdapter projectsAdapter;

    private ArrayList<Project> projects = new ArrayList<>();



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
        GetProjectsTask getProjectsTask = new GetProjectsTask();
        getProjectsTask.execute();
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

    private class GetProjectsTask extends AsyncTask<Void, Void, Project[]>{

        @Override
        protected Project[] doInBackground(Void... params) {
            return db.projectDao().getAllProjects();
        }

        @Override
        protected void onPostExecute(Project[] projects) {
            super.onPostExecute(projects);
            updateProjects(projects);
        }
    }

    private void updateProjects(Project[] allProjects) {
        this.projectsAdapter.setData(Arrays.asList(allProjects));
    }
}
