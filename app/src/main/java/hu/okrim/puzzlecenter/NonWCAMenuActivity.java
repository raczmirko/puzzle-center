package hu.okrim.puzzlecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class NonWCAMenuActivity extends AppCompatActivity {

    Button buttonVoid;
    Button buttonSuperIvy;
    Button buttonPentacle;
    Button button2x2Pyraminx;
    Button button2x2x4;
    Button buttonMasterPyraminx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_wcamenu);

        buttonVoid = findViewById(R.id.buttonVoid);
        buttonSuperIvy = findViewById(R.id.buttonSuperIvy);
        buttonPentacle = findViewById(R.id.buttonPentacle);
        button2x2Pyraminx = findViewById(R.id.button2x2Pyraminx);
        button2x2x4 = findViewById(R.id.button2x2x4);
        buttonMasterPyraminx = findViewById(R.id.buttonMasterPyraminx);
    }

    public void loadNewIntent(android.view.View source){
        Class classToLoad = null;
        Intent intent;

        if(source == buttonVoid){
            classToLoad = PuzzleVoidActivity.class;
        }
        else if(source == buttonSuperIvy){
            classToLoad = PuzzleSuperIvyActivity.class;
        }
        else if(source == buttonPentacle){
            classToLoad = PuzzlePentacleActivity.class;
        }
        else if(source == button2x2Pyraminx){
            classToLoad = Puzzle2x2PyraminxActivity.class;
        }
        else if(source == buttonMasterPyraminx){
            classToLoad = PuzzleMasterPyraminxActivity.class;
        }

        //Defining which class to load in the new intent
        intent = new Intent(this, classToLoad);
        try{
            startActivity(intent);
        }catch(NullPointerException NPE){
            Log.d("ActivityError", NPE.getMessage());
        }
    }
}