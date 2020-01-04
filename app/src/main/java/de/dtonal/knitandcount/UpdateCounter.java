package de.dtonal.knitandcount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import de.dtonal.knitandcount.data.DataBaseService;
import de.dtonal.knitandcount.data.dao.CounterDao;
import de.dtonal.knitandcount.data.model.Counter;
import de.dtonal.knitandcount.listener.counter.CounterDeletedListener;
import de.dtonal.knitandcount.listener.counter.CounterLoadedListener;
import de.dtonal.knitandcount.listener.counter.CounterSavedListener;
import de.dtonal.knitandcount.service.CounterService;
import de.dtonal.knitandcount.task.counter.SaveCounterTask;

public class UpdateCounter extends AppCompatActivity implements CounterSavedListener, CounterLoadedListener, CounterDeletedListener {
    private EditText counterName;
    private EditText startValue;
    private EditText resetValue;
    private Button btnSaveCounter;
    private Button btnDeleteCounter;
    private Counter counter;
    private CounterDao counterDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_counter);

        int counterId = getIntent().getExtras().getInt("counter_id");

        counterName = findViewById(R.id.counterName);
        startValue = findViewById(R.id.startValue);
        resetValue = findViewById(R.id.resetValue);
        btnSaveCounter = findViewById(R.id.btnSaveCounter);
        btnDeleteCounter = findViewById(R.id.btnDeleteCounter);
        btnDeleteCounter.setVisibility(View.VISIBLE);

        startValue.setHint("Current value");

        SaveCounterTask saveCounterTask = new SaveCounterTask(this, DataBaseService.getOrInitAppDataBase(getApplicationContext()).counterDao());

        btnSaveCounter.setEnabled(false);
        btnDeleteCounter.setEnabled(false);

        this.counterDao = DataBaseService.getOrInitAppDataBase(getApplicationContext()).counterDao();
        CounterService.loadCounterById(counterId, this, counterDao);
    }


    @Override
    public void onCounterSaved(Counter counter) {
        this.finish();
    }

    private void switchToProjectActivity() {
        Intent showProjectIntent = new Intent(this, ShowProject.class);
        showProjectIntent.putExtra("project_id", this.counter.getProjectId());
        startActivity(showProjectIntent);
    }

    @Override
    public void onCounterLoaded(final Counter counter) {
        this.counter = counter;
        btnSaveCounter.setEnabled(true);
        btnDeleteCounter.setEnabled(true);
        startValue.setText(String.valueOf(counter.getValue()));
        int resetValueAsInt = counter.getResetValue();
        String resetValueAsString = resetValueAsInt > 0 ? String.valueOf(resetValueAsInt) : "";
        resetValue.setText(resetValueAsString);
        counterName.setText(counter.getName());

        btnSaveCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter.setName(counterName.getText().toString());
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
                CounterService.increment(counter);
                CounterService.decrement(counter);
                CounterService.saveCounter(counter, UpdateCounter.this, counterDao);
            }
        });

        btnDeleteCounter.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                CounterService.deleteCounter(counter, UpdateCounter.this, counterDao);
            }
        });


    }

    @Override
    public void onCounterDeleted(Counter[] counter) {
        switchToProjectActivity();
    }
}
