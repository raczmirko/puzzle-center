package hu.okrim.puzzlecenter;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerActivity extends AppCompatActivity {

    static boolean timerIsRunning = false;

    ArrayAdapter<String> listAdapter;
    Button buttonStartTimer;
    ConstraintLayout screen;
    LinearLayout linearLayout;
    ListView listOfTimes;
    TextView textViewTime;
    Thread timer;
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        buttonStartTimer = findViewById(R.id.buttonStartTimer);
        screen = findViewById(R.id.wholeScreenLayout);
        linearLayout = findViewById(R.id.linearLayoutScreen);
        listOfTimes = findViewById(R.id.listViewTimes);
        listAdapter = new ArrayAdapter<>(this,R.layout.list_layout);
        listOfTimes.setAdapter(listAdapter);
        textViewTime = findViewById(R.id.textViewTime);

        //Adding onTouchListeners to the components so timer can be stopped
        //by pressing the screen anywhere
        listOfTimes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(timerIsRunning){
                    endTimer();
                    addTimeToList();
                }
                return true;
            }
        });
        screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(timerIsRunning){
                    endTimer();
                    addTimeToList();
                }
                return true;
            }
        });
    }

    public void startTimerThread(android.view.View source){
        timerIsRunning = true;
        //Disable button so it cannot be clicked anymore nor focused (not to mess up listener)
        buttonStartTimer.setEnabled(false);
        buttonStartTimer.setClickable(false);
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
        buttonStartTimer.setEnabled(true);
        buttonStartTimer.setClickable(true);
    }

    public void addTimeToList(){
        String messageToInsert = SDF.format(new Date()) + " --- " + textViewTime.getText().toString();
        listAdapter.insert(messageToInsert,0);
    }
}