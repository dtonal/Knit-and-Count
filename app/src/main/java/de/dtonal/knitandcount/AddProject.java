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

public class AddProject extends AppCompatActivity {
    private static final String TAG = "AddProject";
    EditText editTextProjectName;
    Button buttonSaveProject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_project);

        buttonSaveProject = findViewById(R.id.btnAddProject);
        editTextProjectName = findViewById(R.id.project_name);

        buttonSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO add Project
                Log.d(TAG, "Save Project: " + editTextProjectName.getText().toString());
                Project newProject = new Project(editTextProjectName.getText().toString());
                SaveProjectTask saveProjectTask = new SaveProjectTask(newProject);
                saveProjectTask.execute();
            }
        });
    }

    private void switchToMainActivity() {
        Intent switchToMainAcitivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchToMainAcitivityIntent);
    }

    private class SaveProjectTask extends AsyncTask<Void, Void, Void> {

        private Project projectToAdd;

        SaveProjectTask(Project projectToAdd){
            this.projectToAdd = projectToAdd;
        }

        @Override
        protected Void doInBackground(Void... params) {
            DataBaseService.getOrInitAppDataBase(getApplicationContext()).projectDao().insertProject(projectToAdd);
            return null;
        }

        @Override
        protected void onPostExecute(Void nothing) {
            super.onPostExecute(nothing);
            switchToMainActivity();
        }
    }
}
