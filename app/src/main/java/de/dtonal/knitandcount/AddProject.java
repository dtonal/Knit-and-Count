package de.dtonal.knitandcount;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.dtonal.knitandcount.de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.de.dtonal.knitandcount.listener.ProjectSavedListener;
import de.dtonal.knitandcount.de.dtonal.knitandcount.task.SaveProjectTask;

public class AddProject extends AppCompatActivity implements ProjectSavedListener {
    private static final String TAG = "AddProject";
    EditText editTextProjectName;
    Button buttonSaveProject;
    SaveProjectTask saveProjectTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_project);

        buttonSaveProject = findViewById(R.id.btnAddProject);
        editTextProjectName = findViewById(R.id.project_name);

        saveProjectTask = new SaveProjectTask(this, DataBaseService.getOrInitAppDataBase(getApplicationContext()).projectDao());

        buttonSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Save Project: " + editTextProjectName.getText().toString());
                Project newProject = new Project(editTextProjectName.getText().toString());
                saveProjectTask.execute(newProject);
            }
        });
    }

    private void switchToMainActivity() {
        Intent switchToMainAcitivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchToMainAcitivityIntent);
    }

    @Override
    public void onProjectSaved(Project project) {
        switchToMainActivity();
    }
}
