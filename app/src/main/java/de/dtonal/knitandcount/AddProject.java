package de.dtonal.knitandcount;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.project.ProjectSavedListener;
import de.dtonal.knitandcount.task.project.SaveProjectTask;

public class AddProject extends AppCompatActivity implements ProjectSavedListener {
    private static final String TAG = "AddProject";
    private EditText editTextProjectName;
    private EditText editTextNeedleSize;
    private EditText editTextYardage;
    private EditText editTextSize;
    private EditText editTextGaugeWet;
    private EditText editTextGaugeDry;
    private EditText editTextNotes;
    private SaveProjectTask saveProjectTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_project);

        Button buttonSaveProject = findViewById(R.id.btnAddProject);
        editTextProjectName = findViewById(R.id.project_name);
        editTextNotes = findViewById(R.id.notes);
        editTextGaugeDry = findViewById(R.id.gauge_dry);
        editTextGaugeWet = findViewById(R.id.gauge_wet);
        editTextSize = findViewById(R.id.size);
        editTextYardage = findViewById(R.id.yardage);
        editTextNeedleSize = findViewById(R.id.needlesize);

        saveProjectTask = new SaveProjectTask(this, DataBaseService.getOrInitAppDataBase(getApplicationContext()).projectDao());

        buttonSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Save Project: " + editTextProjectName.getText().toString());
                Project newProject = new Project(editTextProjectName.getText().toString());
                newProject.setNeedleSize(editTextNeedleSize.getText().toString());
                newProject.setYardage(editTextYardage.getText().toString());
                newProject.setSize(editTextSize.getText().toString());
                newProject.setGauge_wet(editTextGaugeWet.getText().toString());
                newProject.setNotes(editTextNotes.getText().toString());
                newProject.setGauge_dry(editTextGaugeDry.getText().toString());
                saveProjectTask.execute(newProject);
            }
        });
    }

    private void switchToMainActivity() {
        Intent switchToMainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchToMainActivityIntent);
    }

    @Override
    public void onProjectSaved(Project project) {
        switchToMainActivity();
    }
}
