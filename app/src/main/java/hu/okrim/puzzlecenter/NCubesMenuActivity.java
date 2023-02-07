package hu.okrim.puzzlecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class NCubesMenuActivity extends AppCompatActivity {

    Button button2x2;
    Button button3x3;
    Button button4x4;
    Button button5x5;
    Button button6x6;
    Button button7x7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ncubes_menu);

        button2x2 = findViewById(R.id.button2x2);
        button3x3 = findViewById(R.id.button3x3);
        button4x4 = findViewById(R.id.button4x4);
        button5x5 = findViewById(R.id.button5x5);
        button6x6 = findViewById(R.id.button6x6);
        button7x7 = findViewById(R.id.button7x7);
    }

    public void loadNewIntent(android.view.View source){
        Class classToLoad = null;
        Intent intent;

        if(source == button2x2){
            classToLoad = Puzzle2x2x2Activity.class;
        }
        else if(source == button3x3){
            classToLoad = Puzzle3x3x3Activity.class;
        }
        else if(source == button4x4){
            classToLoad = Puzzle4x4x4Activity.class;
        }
        else if(source == button5x5){
            classToLoad = Puzzle5x5x5Activity.class;
        }
        else if(source == button6x6){
            classToLoad = Puzzle6x6x6Activity.class;
        }
        else if(source == button7x7){
            classToLoad = Puzzle7x7x7Activity.class;
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