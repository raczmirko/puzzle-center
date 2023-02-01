package hu.okrim.puzzlecenter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerActivity extends AppCompatActivity{

    static boolean timerIsRunning = false;
    String currentPuzzle = null;
    int currentPuzzleID = 0;

    ArrayAdapter<CharSequence> spinnerAdapter;
    ArrayAdapter<String> listAdapter;
    Button buttonStartTimer;
    ConstraintLayout screen;
    LinearLayout linearLayout;
    ListView listOfTimes;
    Spinner puzzleTypeSpinner;
    TextView textViewTime;
    TextView textViewPuzzleType;
    Thread timer;
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        buttonStartTimer = findViewById(R.id.buttonStartTimer);
        screen = findViewById(R.id.wholeScreenLayout);
        puzzleTypeSpinner = findViewById(R.id.spinner);
        linearLayout = findViewById(R.id.linearLayoutScreen);
        listOfTimes = findViewById(R.id.listViewTimes);
        textViewTime = findViewById(R.id.textViewTime);
        textViewPuzzleType = findViewById(R.id.textViewPuzzleType);

        listAdapter = new ArrayAdapter<>(this,R.layout.list_layout);
        listOfTimes.setAdapter(listAdapter);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerPuzzleType, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        puzzleTypeSpinner.setAdapter(spinnerAdapter);
        puzzleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!timerIsRunning){
                    System.out.println("touched when timer is not running");
                    currentPuzzle = parent.getItemAtPosition(position).toString();
                    currentPuzzleID = position;
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parent.getChildAt(0)).setTextSize(20);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void startTimerThread(){
        timerIsRunning = true;

        //Creating timer thread
        timer = new Thread(){
            @Override
            public void run() {
                int millis = 0;
                try {
                    while(timerIsRunning){
                        String timeText = createTimeText(millis);
                        Thread.sleep(100);
                        millis += 100;
                        //Checking if Thread is still running because if we stopped the app
                        //whilst loop was running we don't want to increment timer
                        if(timerIsRunning){
                            textViewTime.setText(timeText);
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
            //Color.GREEN was too bright so had to grab another green
            buttonStartTimer.setBackgroundColor(Color.parseColor("#ff669900"));
        }else{
            startTimerThread();
            puzzleTypeSpinner.setEnabled(false);
            buttonStartTimer.setBackgroundColor(Color.RED);
        }
    }

    public String createTimeText(int ms){
        int time = ms;
        //Millis / 60_000 gives number of minutes since 1m = 60s = 60_000ms
        String minutes = String.valueOf(time / 60_000);
        time = time % 60_000;
        //Whatever is left will be the number of seconds if devided by 1000
        String seconds = String.valueOf(time / 1_000);
        time = time % 1000;
        //Rest are the millis, and we only want to display two of them so we devide by 10
        String millis = String.valueOf((time % 1_000) / 10);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(minutes.length() == 1 ? "0" + minutes : minutes);
        stringBuilder.append(":");
        stringBuilder.append(seconds.length() == 1 ? "0" + seconds : seconds);
        stringBuilder.append(":");
        stringBuilder.append(millis.length() == 1 ? "0" + millis : millis);

        return stringBuilder.toString();
    }

    public void endTimer(){
        timerIsRunning = false;
    }

    public void addTimeToList(){
        String messageToInsert = SDF.format(new Date()) + "  |  " + textViewTime.getText().toString() + "  |  " + puzzleTypeSpinner.getSelectedItem().toString();
        listAdapter.insert(messageToInsert,0);
    }

}