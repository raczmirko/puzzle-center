package hu.okrim.puzzlecenter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimerActivity extends AppCompatActivity{

    static boolean timerIsRunning = false;
    static boolean inspectionIsRunning = false;
    static int totalMillis = 0;
    String currentPuzzle = null;
    int currentPuzzleID = 0;
    int orientation;
    CountDownTimer inspectionTimer;

    ArrayAdapter<CharSequence> spinnerAdapter;
    ArrayAdapter<String> listAdapter;
    Button buttonStartTimer;
    Button buttonCancelTimer;
    ConstraintLayout screen;
    LinearLayout linearLayout;
    ListView listOfTimes;
    Spinner puzzleTypeSpinner;
    TextView textViewTime;
    TextView tv_pb;
    TextView tv_ao5;
    TextView tv_ao12;

    Thread timer;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        tv_pb = findViewById(R.id.tv_t_pb);
        tv_ao5 = findViewById(R.id.tv_t_ao5);
        tv_ao12 = findViewById(R.id.tv_t_ao12);
        orientation = getResources().getConfiguration().orientation;

        //int orientation = getResources().getConfiguration().orientation;
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
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        loadStats(currentPuzzle);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonStartTimer.setOnClickListener(this::solveTimerPressed);

        buttonStartTimer.setOnLongClickListener(v -> {
            // TODO Auto-generated method stub
            if(!timerIsRunning && !inspectionIsRunning){
                inspectionIsRunning = true;
                startInspectionTimer(v);
            }
            return true;
        });

        loadLastSelectedPuzzleToSpinner();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_timer);
        orientation = getResources().getConfiguration().orientation;
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

    private void loadStats(String puzzle){
        List<String> statisticsList = databaseController.getStatistics(puzzle);
        //Setting defaults so that if selected puzzle doesn't have stats
        //the previous puzzles stats don't remain
        tv_pb.setText(R.string.dash);
        tv_ao5.setText(R.string.dash);
        tv_ao12.setText(R.string.dash);
        if(!statisticsList.isEmpty()){
            //Setting pb
            if(!(statisticsList.get(0) == null)){
                String pbText = TimeFormatController.createTimeText(Integer.parseInt(statisticsList.get(0)));
                tv_pb.setText(pbText);
            }
            //Setting ao5
            if(!(statisticsList.get(2) == null)){
                String ao5String = statisticsList.get(2);
                int ao5Int = Integer.parseInt(ao5String);
                String ao5StringFormatted = TimeFormatController.createTimeText(ao5Int);
                tv_ao5.setText(ao5StringFormatted);
            }
            //Setting ao12
            if(!(statisticsList.get(3) == null)){
                String ao12String = statisticsList.get(3);
                int ao12Int = Integer.parseInt(ao12String);
                String ao12StringFormatted = TimeFormatController.createTimeText(ao12Int);
                tv_ao12.setText(ao12StringFormatted);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timerIsRunning){
            timerIsRunning = false;
        }
        saveSelectedPuzzle();
    }

    private void saveSelectedPuzzle() {
        //Saving what puzzle was selected on the spinner.
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("puzzleSelected", currentPuzzle);
        editor.apply();
    }

    public void startTimerThread(){
        timerIsRunning = true;
        totalMillis = 0;
        long startTime = System.currentTimeMillis();
        //Creating timer thread
        timer = new Thread(){
            @Override
            public void run() {
                while(timerIsRunning){
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Another if to only adjust time on screen if during the 10ms sleep time
                    //the timer was not stopped. Otherwise stop incrementing the time.
                    if(timerIsRunning){
                        //Casting to int as the maximum time the app can track is 5_999_000 millis
                        //Which is 99 minutes 59 seconds
                        //Since the time format is 00:00.00 (it's not really useful to track more...)
                        //An int can store values up to 2 billion so maximum 6 million fits
                        totalMillis = (int) (System.currentTimeMillis() - startTime);
                        runOnUiThread(() -> {
                            // Updating timer text on UI thread
                            textViewTime.setText(TimeFormatController.createTimeText(totalMillis));
                        });
                    }
                }
            }
        };
        if(!timer.isAlive()){
            timer.start();
        }
    }

    public void startInspectionTimer(View source){
        buttonStartTimer.setBackgroundColor(Color.YELLOW);
        buttonStartTimer.setTextColor(Color.BLACK);
        buttonStartTimer.setText(R.string.inspection);
        inspectionTimer = new CountDownTimer(15000, 1000) {
            int remainingTime = 15;
            @Override
            public void onTick(long l) {
                textViewTime.setText(String.valueOf(remainingTime));
                remainingTime -= 1;
            }

            @Override
            public void onFinish() {
                solveTimerPressed(source);
            }
        }.start();
    }

    public void solveTimerPressed(View source){
        inspectionIsRunning = false;
        //Can only cancel inspection timer if it is running
        if(inspectionTimer != null){
            inspectionTimer.cancel();
        }
        if(timerIsRunning){
            endTimer();
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                addTimeToList();
                loadStats(currentPuzzle);
            }
            puzzleTypeSpinner.setEnabled(true);
            buttonStartTimer.setBackgroundColor(Color.parseColor("#ff669900"));
            buttonStartTimer.setText(R.string.start_timer);
            buttonStartTimer.setTextColor(Color.WHITE);
            buttonCancelTimer.setVisibility(View.GONE);
        }else{
            startTimerThread();
            puzzleTypeSpinner.setEnabled(false);
            buttonStartTimer.setBackgroundColor(Color.RED);
            buttonStartTimer.setText(R.string.stop_timer);
            buttonStartTimer.setTextColor(Color.WHITE);
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
        databaseController.addRecord(new DataEntryModel(date, puzzle, totalMillis));
    }

    public void addTimeToList(){
        //int orientation = getResources().getConfiguration().orientation;
        String messageToInsert = listTimeFormat.format(new Date()) + "  |  " + TimeFormatController.createTimeText(totalMillis) + "  |  " + puzzleTypeSpinner.getSelectedItem().toString();
        listAdapter.insert(messageToInsert, 0);
    }
}