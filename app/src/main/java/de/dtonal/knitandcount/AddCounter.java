package de.dtonal.knitandcount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.listener.counter.CounterSavedListener;
import de.dtonal.knitandcount.task.counter.SaveCounterTask;

public class AddCounter extends AppCompatActivity implements CounterSavedListener {
    private EditText counterName;
    private EditText startValue;
    private EditText resetValue;
    private SaveCounterTask saveCounterTask;
    private int projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_counter);

        projectId = Objects.requireNonNull(getIntent().getExtras()).getInt("project_id");

        counterName = findViewById(R.id.counterName);
        startValue = findViewById(R.id.startValue);
        resetValue = findViewById(R.id.resetValue);
        Button btnSaveCounter = findViewById(R.id.btnSaveCounter);

        saveCounterTask = new SaveCounterTask(this, DataBaseService.getOrInitAppDataBase(getApplicationContext()).counterDao());

        btnSaveCounter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Counter counter = new Counter(counterName.getText().toString(), projectId);
                try {
                    counter.setResetValue(Integer.valueOf(resetValue.getText().toString()));
                } catch (NumberFormatException e) {
                    // its ok
                }
                try {
                    counter.setValue(Integer.valueOf(startValue.getText().toString()));
                } catch (NumberFormatException e) {
                    //its ok
                }
                saveCounterTask.execute(counter);
            }
        });
    }

    @Override
    public void onCounterSaved(Counter counter) {
        Intent showProjectIntent = new Intent(this, ShowProject.class);
        showProjectIntent.putExtra("project_id", projectId);
        startActivity(showProjectIntent);
    }
}
