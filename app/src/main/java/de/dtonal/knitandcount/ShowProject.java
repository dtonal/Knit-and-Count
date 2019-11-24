package de.dtonal.knitandcount;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.dtonal.knitandcount.de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.model.Project;

public class ShowProject extends AppCompatActivity {
    private static final String TAG = "ShowProject";
    private TextView textViewProjectName;
    private Project project;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_project);

        textViewProjectName = findViewById(R.id.project_title);


        Bundle extras = getIntent().getExtras();
        assert extras != null;
        int project_id = extras.getInt("project_id");

        GetProjectTask getProjectTask = new GetProjectTask();
        getProjectTask.execute(project_id);
    }

    private void setProject(Project project){

        textViewProjectName.setText(project.getName());

    }

    private class GetProjectTask extends AsyncTask<Integer, Void, Project> {

        @Override
        protected Project doInBackground(Integer... params) {
            return DataBaseService.getOrInitAppDataBase(getApplicationContext()).projectDao().getById(params[0]);
        }

        @Override
        protected void onPostExecute(Project project) {
            super.onPostExecute(project);
            setProject(project);
        }
    }
}
