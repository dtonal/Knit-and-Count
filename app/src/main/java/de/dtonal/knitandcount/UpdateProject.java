package de.dtonal.knitandcount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.data.dao.ProjectDao;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.project.ProjectForIdListener;
import de.dtonal.knitandcount.listener.project.ProjectSavedListener;
import de.dtonal.knitandcount.service.ProjectService;
import de.dtonal.knitandcount.task.project.UpdateProjectTask;

public class UpdateProject extends AppCompatActivity implements ProjectSavedListener, ProjectForIdListener {
    private EditText editTextProjectName;
    private EditText editTextNeedleSize;
    private EditText editTextYardage;
    private EditText editTextSize;
    private EditText editTextGaugeWet;
    private EditText editTextGaugeDry;
    private EditText editTextNotes;
    private UpdateProjectTask updateProjectTask;
    private Project project;

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

        int project_id = Objects.requireNonNull(getIntent().getExtras()).getInt("project_id");
        ProjectDao projectDao = DataBaseService.getOrInitAppDataBase(getApplicationContext()).projectDao();
        ProjectService.loadProjectById(project_id, this, projectDao);

        updateProjectTask = new UpdateProjectTask(this, projectDao);

        buttonSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                project.setNeedleSize(editTextNeedleSize.getText().toString());
                project.setYardage(editTextYardage.getText().toString());
                project.setSize(editTextSize.getText().toString());
                project.setGauge_wet(editTextGaugeWet.getText().toString());
                project.setGauge_dry(editTextGaugeDry.getText().toString());
                project.setNotes(editTextNotes.getText().toString());
                project.setName(editTextProjectName.getText().toString());
                updateProjectTask.execute(project);
            }
        });
    }

    private void switchToMainActivity() {
        Intent intent = new Intent(this, ShowProject.class);
        intent.putExtra("project_id", project.getId());
        startActivity(intent);
    }

    @Override
    public void onProjectSaved(Project project) {
        switchToMainActivity();
    }

    @Override
    public void projectLoaded(Project project) {
        this.project = project;
        editTextGaugeDry.setText(project.getGauge_dry());
        editTextGaugeWet.setText(project.getGauge_wet());
        editTextNeedleSize.setText(project.getNeedleSize());
        editTextNotes.setText(project.getNotes());
        editTextProjectName.setText(project.getName());
        editTextSize.setText(project.getSize());
        editTextYardage.setText(project.getYardage());
    }
}
