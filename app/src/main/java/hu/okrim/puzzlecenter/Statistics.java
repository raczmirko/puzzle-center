package hu.okrim.puzzlecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Statistics extends AppCompatActivity {

    TextView tv_totalSolves;
    TextView tv_favouritePuzzle;
    TextView tv_totalSolvesForFavouritePuzzle;
    TextView tv_firstSolveDate;
    TextView tv_firstSolvePuzzle;
    DatabaseController databaseController = new DatabaseController(Statistics.this);
    ImageView iv_FavouritePuzzle;
    ImageView iv_FirstSolve;

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

        iv_FavouritePuzzle = findViewById(R.id.iv_favouritePuzzle);
        iv_FirstSolve = findViewById(R.id.iv_firstSolve);

        loadGeneralStatistics();
    }

    public void loadGeneralStatistics(){
        //Refresh statistics onCreate
        //1st card
            //This one doesn't need error handling since if there are no records counter returns 0
            tv_totalSolves.setText(databaseController.getTotalSolves());
        //2nd card
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
        //3rd card
            List<DataEntryModel> firstSolveDetails = databaseController.getFirstSolve();
            if(!firstSolveDetails.isEmpty()){
                String firstSolvePuzzle = firstSolveDetails.get(0).getPuzzle();
                String firstSolveDate = firstSolveDetails.get(0).getDateString();
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
}