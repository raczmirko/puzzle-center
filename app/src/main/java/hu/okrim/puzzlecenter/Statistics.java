package hu.okrim.puzzlecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class Statistics extends AppCompatActivity {

    String currentPuzzle = null;

    TextView tv_totalSolves;
    TextView tv_favouritePuzzle;
    TextView tv_totalSolvesForFavouritePuzzle;
    TextView tv_firstSolveDate;
    TextView tv_firstSolvePuzzle;
    TextView tv_distinctDaysOfUsage;
    TextView tv_pb;
    TextView tv_totalSolveOfSelected;
    TextView tv_ao5;
    TextView tv_ao12;
    TextView tv_ao5diff;

    DatabaseController databaseController = new DatabaseController(Statistics.this);
    ImageView iv_FavouritePuzzle;
    ImageView iv_FirstSolve;

    Spinner puzzleTypeSpinner;
    ArrayAdapter<CharSequence> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //TextViews
        tv_totalSolves = findViewById(R.id.tv_totalSolves);
        tv_favouritePuzzle = findViewById(R.id.tv_favouritePuzzle);
        tv_totalSolvesForFavouritePuzzle = findViewById(R.id.tv_totalSolvesForFavouritePuzzle);
        tv_firstSolveDate = findViewById(R.id.tv_firstSolveDate);
        tv_firstSolvePuzzle = findViewById(R.id.tv_firstSolvePuzzle);
        tv_distinctDaysOfUsage = findViewById(R.id.tv_distinctDaysOfAppUsageValue);
        tv_pb = findViewById(R.id.tv_pbValue);
        tv_totalSolveOfSelected = findViewById(R.id.tv_totalSolvesForSelectedValue);
        tv_ao5 = findViewById(R.id.tv_ao5Value);
        tv_ao12 = findViewById(R.id.tv_ao12Value);
        tv_ao5diff = findViewById(R.id.tv_ao5DiffValue);


        iv_FavouritePuzzle = findViewById(R.id.iv_favouritePuzzle);
        iv_FirstSolve = findViewById(R.id.iv_firstSolve);

        puzzleTypeSpinner = findViewById(R.id.spinner);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerPuzzleType, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        puzzleTypeSpinner.setAdapter(spinnerAdapter);
        puzzleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPuzzle = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(30);
                loadSpecificStatistics(currentPuzzle);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        loadLastSelectedPuzzleToSpinner();
        loadGeneralStatistics();
    }

    public void loadGeneralStatistics(){
        //Refresh statistics onCreate
        //1st card (total solves)
            //This one doesn't need error handling since if there are no records counter returns 0
            tv_totalSolves.setText(databaseController.getTotalSolves());
        //2nd card (most trained puzzle)
            //getFavouritePuzzle() returns a list with 2 columns:
            List<String> favouritePuzzleDetails = databaseController.getFavouritePuzzle();
            //1st is puzzle and 2nd is counter (how many times it was played)
            //But we can only get those values if the list is not empty (on first startup it's empty)
            if(!favouritePuzzleDetails.isEmpty()){
                String favouritePuzzle = databaseController.getFavouritePuzzle().get(0);
                tv_favouritePuzzle.setText(favouritePuzzle);
                String totalSolvesForFavouritePuzzleString = "(" + databaseController.getFavouritePuzzle().get(1) + " total solves)";
                tv_totalSolvesForFavouritePuzzle.setText(totalSolvesForFavouritePuzzleString);
                setIconOfPuzzle(favouritePuzzle, iv_FavouritePuzzle);
            }
            //Else we have to set default values
            else{
                tv_favouritePuzzle.setText(R.string.none_yet);
                tv_totalSolvesForFavouritePuzzle.setText(R.string.total_solves_default);
                iv_FavouritePuzzle.setImageResource(R.drawable.questionmark);
            }
        //3rd card (first solve)
            List<DataEntryModel> firstSolveDetails = databaseController.getFirstSolve();
            if(!firstSolveDetails.isEmpty()){
                String firstSolvePuzzle = firstSolveDetails.get(0).getPuzzle();
                String firstSolveDate = firstSolveDetails.get(0).getDateString().substring(0,10);
                firstSolveDate = firstSolveDate.concat(" ~ " + databaseController.getDaysSinceFirstSolve() + " days ago");
                tv_firstSolvePuzzle.setText(firstSolvePuzzle);
                tv_firstSolveDate.setText(firstSolveDate);
                setIconOfPuzzle(firstSolvePuzzle, iv_FirstSolve);
            }
            //Defaults
            else{
                tv_firstSolvePuzzle.setText(R.string.none_yet);
                tv_firstSolveDate.setText(R.string.dash);
                iv_FirstSolve.setImageResource(R.drawable.questionmark);
                }
        //4th card (number of distinct days the app was opened on)
            //No need for error handling as COUNT gives 0 if there are no records
            int distinctDaysValue = Integer.parseInt(databaseController.getDistinctDaysOfUse());
            String distinctDaysText;
            if(distinctDaysValue == 1) {
                distinctDaysText = "on " + databaseController.getDistinctDaysOfUse() + " day.";
            }
            else{
                distinctDaysText = "on " + databaseController.getDistinctDaysOfUse() + " different days.";
            }
            tv_distinctDaysOfUsage.setText(distinctDaysText);

    }

    public void loadSpecificStatistics(String puzzle){
        //Setting defaults
        tv_pb.setText(R.string.dash);
        tv_totalSolveOfSelected.setText(R.string.dash);
        tv_ao5.setText(R.string.dash);
        tv_ao12.setText(R.string.dash);
        tv_ao5diff.setText(R.string.dash);
        List<String> statisticsList = databaseController.getStatistics(puzzle);
        if(!statisticsList.isEmpty()){
            //Setting pb
            if(!(statisticsList.get(0) == null)){
                String pbText = TimeFormatController.createTimeText(Integer.parseInt(statisticsList.get(0)));
                tv_pb.setText(pbText);
            }
            //Setting total solves for selected puzzle
            if(!(statisticsList.get(1) == null)){
                tv_totalSolveOfSelected.setText(statisticsList.get(1));
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
            //Setting ao5 diff
            if(!(statisticsList.get(4) == null)){
                String diffString = statisticsList.get(4);
                int diffInt = Integer.parseInt(diffString);
                String diffStringFormatted = TimeFormatController.createTimeText(diffInt);
                tv_ao5diff.setText(diffStringFormatted);
            }
        }
    }

    public void setIconOfPuzzle(String puzzle, ImageView icon){
        switch(puzzle){
            case "2x2x2":
                icon.setImageResource(R.drawable._x2);
                break;
            case "3x3x3":
                icon.setImageResource(R.drawable._x3);
                break;
            case "4x4x4":
                icon.setImageResource(R.drawable._x4);
                break;
            case "5x5x5":
                icon.setImageResource(R.drawable._x5);
                break;
            case "6x6x6":
                icon.setImageResource(R.drawable._x6);
                break;
            case "7x7x7":
                icon.setImageResource(R.drawable._x7);
                break;
            case "pyraminx":
                icon.setImageResource(R.drawable.pyraminx);
                break;
            case "megaminx":
                icon.setImageResource(R.drawable.megaminx);
                break;
            case "square-1":
                icon.setImageResource(R.drawable.square1);
                break;
            case "skewb":
                icon.setImageResource(R.drawable.skewb);
                break;
            case "magic clock":
                icon.setImageResource(R.drawable.clock);
                break;
            case "void cube":
                icon.setImageResource(R.drawable.resource_void);
                break;
            case "super ivy cube":
                icon.setImageResource(R.drawable.superivy);
                break;
            case "pentacle cube":
                icon.setImageResource(R.drawable.pentacle);
                break;
            case "2x2 pyraminx":
                icon.setImageResource(R.drawable._x2pyraminx);
                break;
            case "2x2x4":
                icon.setImageResource(R.drawable._x2x4);
                break;
            case "master pyraminx":
                icon.setImageResource(R.drawable.masterpyraminx);
                break;
        }
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
        saveSelectedPuzzle();
    }

    private void saveSelectedPuzzle() {
        //Saving what puzzle was selected on the spinner.
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("puzzleSelected", currentPuzzle);
        editor.apply();
    }
}