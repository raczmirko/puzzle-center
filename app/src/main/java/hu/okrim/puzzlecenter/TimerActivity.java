package hu.okrim.puzzlecenter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TimerActivity extends AppCompatActivity{

    static boolean timerIsRunning = false;
    static int currentMillis = 0;
    String currentPuzzle = null;
    int currentPuzzleID = 0;

    ArrayAdapter<CharSequence> spinnerAdapter;
    ArrayAdapter<String> listAdapter;
    Button buttonStartTimer;
    Button buttonCancelTimer;
    ConstraintLayout screen;
    LinearLayout linearLayout;
    ListView listOfTimes;
    Spinner puzzleTypeSpinner;
    TextView textViewTime;
    TextView textViewPuzzleType;
    Thread timer;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat listTimeFormat = new SimpleDateFormat("HH:mm:ss");
    DatabaseController databaseController = new DatabaseController(TimerActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        initUI();
    }

    void initUI(){
        buttonStartTimer = findViewById(R.id.buttonStartTimer);
        buttonCancelTimer = findViewById(R.id.buttonCancelTimer);
        screen = findViewById(R.id.wholeScreenLayout);
        puzzleTypeSpinner = findViewById(R.id.spinner);
        linearLayout = findViewById(R.id.linearLayoutScreen);
        listOfTimes = findViewById(R.id.listViewTimes);
        textViewTime = findViewById(R.id.textViewTime);
        textViewPuzzleType = findViewById(R.id.textViewPuzzleType);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // In portrait mode only
            listAdapter = new ArrayAdapter<>(this,R.layout.custom_list_layout);
            listOfTimes.setAdapter(listAdapter);
        }
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerPuzzleType, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        puzzleTypeSpinner.setAdapter(spinnerAdapter);
        puzzleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!timerIsRunning){
                    currentPuzzle = parent.getItemAtPosition(position).toString();
                    currentPuzzleID = position;
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parent.getChildAt(0)).setTextSize(30);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_timer);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLastSelectedPuzzleToSpinner();
    }

    private void loadLastSelectedPuzzleToSpinner() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        //Loading currentPuzzle from SharedPreferences, or if not found then 2x2x2 will be default
        currentPuzzle = sharedPreferences.getString("puzzleSelected", "2x2x2");
        //Setting the spinner to the correct puzzle
        //spinnerAdapter.setDropDownViewResource(spinnerAdapter.getPosition(currentPuzzle));
        puzzleTypeSpinner.setSelection(spinnerAdapter.getPosition(currentPuzzle));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timerIsRunning){
            timerIsRunning = false;
        }
        //Saving what puzzle was selected on the spinner.
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("puzzleSelected", currentPuzzle);
        editor.apply();
    }

    public void startTimerThread(){
        timerIsRunning = true;
        currentMillis = 0;

        //Creating timer thread
        timer = new Thread(){
            @Override
            public void run() {
                try {
                    while(timerIsRunning){
                        Thread.sleep(10);
                        //Checking if Thread is still running because if we stopped the app
                        //whilst loop was running we don't want to increment timer
                        currentMillis += 10;
                        if(timerIsRunning){
                            String timeText = TimeFormatController.createTimeText(currentMillis);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Updating timer text on UI thread
                                    if(timerIsRunning) {
                                        textViewTime.setText(timeText);
                                    }
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        if(!timer.isAlive()){
            timer.start();
        }
    }

    public void solveTimerPressed(android.view.View source){
        if(timerIsRunning){
            endTimer();
            addTimeToList();
            puzzleTypeSpinner.setEnabled(true);
            buttonStartTimer.setBackgroundColor(Color.parseColor("#ff669900"));
            buttonStartTimer.setText(R.string.start_timer);
            buttonCancelTimer.setVisibility(View.GONE);
        }else{
            startTimerThread();
            puzzleTypeSpinner.setEnabled(false);
            buttonStartTimer.setBackgroundColor(Color.RED);
            buttonStartTimer.setText(R.string.stop_timer);
            buttonCancelTimer.setVisibility(View.VISIBLE);
        }
    }

    public void cancelTimerPressed(android.view.View source){
        //Only does something if timer is running
        if(timerIsRunning){
            timerIsRunning = false;
            puzzleTypeSpinner.setEnabled(true);
            buttonStartTimer.setBackgroundColor(Color.parseColor("#ff669900"));
            buttonStartTimer.setText(R.string.start_timer);
            //Make the cancel timer button disappear when timer is inactive
            buttonCancelTimer.setVisibility(View.GONE);
            textViewTime.setText(R.string.zero_time);
            Toast.makeText(getApplicationContext(),"Timer cancelled.", Toast.LENGTH_SHORT).show();
        }
    }

    public void endTimer(){
        timerIsRunning = false;
        String date = SDF.format(new Date());
        String puzzle = puzzleTypeSpinner.getSelectedItem().toString().toLowerCase();
        databaseController.addRecord(new DataEntryModel(date, puzzle, currentMillis));
    }

    public void addTimeToList(){
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            String messageToInsert = listTimeFormat.format(new Date()) + "  |  " + textViewTime.getText().toString() + "  |  " + puzzleTypeSpinner.getSelectedItem().toString();
            listAdapter.insert(messageToInsert, 0);
        }
    }
}