package de.dtonal.knitandcount;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import de.dtonal.knitandcount.de.dtonal.knitandcount.data.AppDatabase;
import de.dtonal.knitandcount.de.dtonal.knitandcount.data.Project;

public class MainActivity extends AppCompatActivity {

    AppDatabase db = null;
    private static final String TAG = "MainActivity";
    private LinearLayoutManager layoutManager;

    private AppDatabase getOrInitAppDataBase(){
        if(db == null){
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name").build();
        }
        return db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView projectsRecycler = (RecyclerView) findViewById(R.id.projects_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        projectsRecycler.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        projectsRecycler.setLayoutManager(layoutManager);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Project toAdd = new Project();
                toAdd.name = "haha";

                getOrInitAppDataBase().projectDao().insertProject(toAdd);

                Log.v(TAG, "Lets fetz");

                Project[] projects = getOrInitAppDataBase().projectDao().getAllProjects();

                Log.v(TAG, "Projetcs found: " + projects.length);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
