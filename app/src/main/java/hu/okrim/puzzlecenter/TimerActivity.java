package hu.okrim.puzzlecenter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import java.util.HashMap;

public class TimerActivity extends AppCompatActivity{

    static boolean timerIsRunning = false;
    static int currentMillis = 0;
    String currentPuzzle = null;
    int currentPuzzleID = 0;

    static HashMap<Integer, String> n2Records = new HashMap<>();
    static HashMap<Integer, String> n3Records = new HashMap<>();
    static HashMap<Integer, String> n4Records = new HashMap<>();
    static HashMap<Integer, String> n5Records = new HashMap<>();
    static HashMap<Integer, String> n6Records = new HashMap<>();
    static HashMap<Integer, String> n7Records = new HashMap<>();
    static HashMap<Integer, String> pyraminxRecords = new HashMap<>();
    static HashMap<Integer, String> megaminxRecords = new HashMap<>();
    static HashMap<Integer, String> squareOneRecords = new HashMap<>();
    static HashMap<Integer, String> skewbRecords = new HashMap<>();
    static HashMap<Integer, String> clockRecords = new HashMap<>();

    static HashMap[] recordMaps = {
            n2Records, n3Records, n4Records,
            n4Records, n5Records, n6Records,
            n7Records, pyraminxRecords, megaminxRecords,
            squareOneRecords, skewbRecords, clockRecords
    };

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                        Thread.sleep(100);
                        //Checking if Thread is still running because if we stopped the app
                        //whilst loop was running we don't want to increment timer
                        if(timerIsRunning){
                            currentMillis += 100;
                            String timeText = createTimeText(currentMillis);
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
        addTimeToCorrespondingMap(currentMillis);
    }

    public void addTimeToList(){
        String messageToInsert = SDF.format(new Date()) + "  |  " + textViewTime.getText().toString() + "  |  " + puzzleTypeSpinner.getSelectedItem().toString();
        listAdapter.insert(messageToInsert,0);
    }

    public void addTimeToCorrespondingMap(int millis){
        if(puzzleTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("3x3x3")){
            checkIfTimeIsRecord("3x3x3", millis);
        }
    }

    public void checkIfTimeIsRecord(String category, int millis){
        HashMap<Integer, String> mapToCheck = new HashMap<>();
        int current1st = 0;
        int current2nd = 0;
        int current3rd = 0;
        String recordString = SDF.format(new Date()) + "-" + millis;
        switch(category){
            case "3x3x3":
                mapToCheck = n3Records;
                break;
        }
        //Getting records so that we can compare them to new results
        //1st place doesn't exist
        if(mapToCheck.get(1) == null){
            //So current time will be new record in any case
            mapToCheck.put(1, recordString);
            current1st = millis;
            Log.d("newRecord", "New record logged, no previous record found: " + millis);
            System.out.println("New record logged, no previous record was found.");
        }
        //1st place exists, but 2nd place doesn't yet exist
        //2nd record doesn't exist yet
        else if (mapToCheck.get(2) == null) {
            //Since in this branch 1st place must exist we get it from Map
            //Catching NullPointerException of .split()
            try{
                current1st = Integer.parseInt(mapToCheck.get(1).split("-")[1]);
            }catch(NullPointerException NPE){
                System.out.println(NPE.getMessage());
            }
            //If current time is less than 1st then current -> 1st and 1st -> 2nd
            if(millis < current1st){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //Previous 1st moves to 2nd place
                    mapToCheck.put(2, mapToCheck.get(1));
                    //If 2nd was empty 3rd will surely be empty so no need to check
                    //Adding new 1st place after 2nd and 3rd values are moved correctly
                    mapToCheck.replace(1, recordString);
                }
                Log.d("newRecord", "New 1st place logged: " + millis + ". Previous record is now 2nd best time.");
                System.out.println("New 1st place logged: " + millis + ". Previous record is now 2nd best time.");
            }
            //If current time is more than 1st then current -> 2nd
            // (since in this branch there was no 2nd there is no need to shift 2nd -> 3rd)
            else if(millis > current1st){
                mapToCheck.put(2, recordString);
                Log.d("newRecord", "New 2nd place logged, no previous 2nd place found: " + millis);
                System.out.println("New 2nd place logged, no previous 2nd place found: " + millis);
            }
        }
        //1st and 2nd places exist but 3rd doesn't yet exist
        //3rd record doesn't exist yet
        else if (mapToCheck.get(3) == null) {
            //Since in this branch 1st and 2nd places must exist we get them from Map
            //Catching NullPointerException of .split()
            try{
                current1st = Integer.parseInt(mapToCheck.get(1).split("-")[1]);
            }catch(NullPointerException NPE){
                System.out.println(NPE.getMessage());
            }
            try{
                current2nd = Integer.parseInt(mapToCheck.get(2).split("-")[1]);
            }catch(NullPointerException NPE){
                System.out.println(NPE.getMessage());
            }
            //If current time is less than previous 1st
            if(millis < current1st){
                //2nd -> 3rd
                mapToCheck.put(3,mapToCheck.get(2));
                //replace requires API24
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //1st -> 2nd
                    mapToCheck.replace(2, mapToCheck.get(1));
                    //new 1st
                    mapToCheck.replace(1,recordString);
                }
                Log.d("newRecord", "New 1st place logged: " + millis + ". Previous record is now 2nd best time.");
                System.out.println("New 1st place logged: " + millis + ". Previous record is now 2nd best time.");
            }
            //If current time is more than 1st but less than 2nd
            else if(millis > current1st && millis < current2nd){
                //Since in this branch there was no 3rd we just add 2nd to 3rd place
                mapToCheck.put(3, mapToCheck.get(2));
                //replace requires API24
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //And replace 2nd with new value
                    mapToCheck.replace(2, recordString);
                }
                Log.d("newRecord", "New 2nd place logged, no previous 2nd place found: " + millis  + ". Previous 2nd is now 3rd best time.");
                System.out.println("New 2nd place logged, no previous 2nd place found: " + millis  + ". Previous 2nd is now 3rd best time.");
            }
            //If current time is more than 1st and 2nd and there is no 3rd place currently
            else{
                mapToCheck.put(3, recordString);
            }
            Log.d("newRecord", "New 3rd place logged, no previous 3rd place found: " + millis);
            System.out.println("New 3rd place logged, no previous 3rd place found: " + millis);
        }
        //1sr 2nd and 3rd places all exist already
        else{
            try{
                current1st = Integer.parseInt(mapToCheck.get(1).split("-")[1]);
            }catch(NullPointerException NPE){
                System.out.println(NPE.getMessage());
            }
            try{
                current2nd = Integer.parseInt(mapToCheck.get(2).split("-")[1]);
            }catch(NullPointerException NPE){
                System.out.println(NPE.getMessage());
            }
            try{
                current3rd = Integer.parseInt(mapToCheck.get(3).split("-")[1]);
            }catch(NullPointerException NPE){
                System.out.println(NPE.getMessage());
            }
            //Case 1: new time is better than previous best time (shifting 1st to 2nd & 2nd to 3rd)
            //1st place exists but is worse then new time
            if(millis < current1st) {
                //Function requires API24 at least
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //3rd gets discarded and overwrittend by 2nd
                    mapToCheck.replace(3, mapToCheck.get(2));
                    //2nd gets replaced by 1st
                    mapToCheck.replace(2, mapToCheck.get(1));
                    //New 1st place is saved
                    mapToCheck.replace(1, recordString);
                }
                Log.d("newRecord", "New 1st place logged: " + millis + ". Shifting 2nd and 3rd places.");
                System.out.println("New 1st place logged: " + millis + ". Shifting 2nd and 3rd places.");
            }
            else if (millis > current1st && millis < current2nd) {
                //Function requires API24 at least
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //Previous 2nd moves to 3rd place, discarding previous 3rd
                    mapToCheck.replace(3, mapToCheck.get(2));
                    //Adding new 2nd place after 2nd value is moved to 3rd place
                    mapToCheck.replace(2, recordString);
                }
                Log.d("newRecord", "New 2nd place logged: " + millis + ". Shifting 2nd to 3rd place.");
                System.out.println("New 2nd place logged: " + millis + ". Shifting 2nd to 3rd place.");
            }
            else if(millis > current2nd && millis < current3rd){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //3rd value gets replaced by new record
                    mapToCheck.replace(3, recordString);
                }
                Log.d("newRecord", "New 3rd place logged: " + millis + ". Discarding old 3rd place.");
                System.out.println("New 3rd place logged: " + millis + ". Discarding old 3rd place.");
            }
        }
        //Printing the current leaderboard
        System.out.println("1st time: " + mapToCheck.get(1));
        System.out.println("2nd time: " + mapToCheck.get(2));
        System.out.println("3rd time: " + mapToCheck.get(3));
    }

    void saveToSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
//        SharedPreferences.Editor myEditor = sharedPreferences.edit();

//        myEditor.putString("mood", spinner.getSelectedItem().toString());
//        myEditor.commit();
//
//        System.out.println("Saved " + spinner.getSelectedItem().toString());
    }

    void loadFromSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        String mood = sharedPreferences.getString("mood", "");
//        for(int i = 0; i < spinner.getCount(); i++){
//            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(mood)){
//                spinner.setSelection(i);
//            }
//        }
//        System.out.println("Loaded " + mood);
    }
}