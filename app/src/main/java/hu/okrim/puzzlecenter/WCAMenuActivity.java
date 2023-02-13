package hu.okrim.puzzlecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class WCAMenuActivity extends AppCompatActivity {

    Button buttonSkewb;
    Button buttonSquareOne;
    Button buttonPyraminx;
    Button buttonMegaminx;
    Button buttonClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wcamenu);

        buttonSkewb = findViewById(R.id.buttonSkewb);
        buttonSquareOne = findViewById(R.id.buttonSquareOne);
        buttonPyraminx = findViewById(R.id.buttonPyraminx);
        buttonMegaminx = findViewById(R.id.buttonMegaminx);
        buttonClock = findViewById(R.id.buttonClock);
    }

    public void loadNewIntent(android.view.View source){
        Class classToLoad = null;
        Intent intent;

        if(source == buttonSkewb){
            classToLoad = PuzzleSkewbActivity.class;
        }
        else if(source == buttonSquareOne){
            classToLoad = PuzzleSquareOneActivity.class;
        }
        else if(source == buttonPyraminx){
            classToLoad = PuzzlePyraminxActivity.class;
        }
        else if(source == buttonMegaminx){
            classToLoad = PuzzleMegaminxActivity.class;
        }
        else if(source == buttonClock){
            classToLoad = PuzzleMagicClockActivity.class;
        }

        //Defining which class to load in the new intent
        intent = new Intent(this, classToLoad);
        intent.putExtra("onlyAlgs", getIntent().getExtras().getBoolean("onlyAlgs"));
        try{
            startActivity(intent);
        }catch(NullPointerException NPE){
            Log.d("ActivityError", NPE.getMessage());
        }
    }
}