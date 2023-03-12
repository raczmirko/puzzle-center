package hu.okrim.puzzlecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

@SuppressWarnings("rawtypes")
public class MainActivity extends AppCompatActivity {

    Button buttonTimer;
    Button buttonRecords;
    Button buttonNCubes;
    Button buttonWCAPuzzles;
    Button buttonNonWCAPuzzles;
    Button buttonStatistics;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchOnlyAlgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTimer = findViewById(R.id.buttonTimer);
        buttonRecords = findViewById(R.id.buttonRecords);
        buttonNCubes = findViewById(R.id.buttonNCubes);
        buttonWCAPuzzles = findViewById(R.id.buttonWCAPuzzles);
        buttonNonWCAPuzzles = findViewById(R.id.buttonNonWCAPuzzles);
        buttonStatistics = findViewById(R.id.buttonStatistics);
        switchOnlyAlgs = findViewById(R.id.switchOnlyAlgs);
    }

    public void loadSelectedPage(android.view.View source){
        Class classToLoad = null;
        Intent intent;

        if(source == buttonTimer){
            classToLoad = TimerActivity.class;
        }
        else if(source == buttonRecords){
            classToLoad = RecordsActivity.class;
        }
        else if(source == buttonNCubes){
            classToLoad = NCubesMenuActivity.class;
        }
        else if(source == buttonWCAPuzzles){
            classToLoad = WCAMenuActivity.class;
        }
        else if(source == buttonNonWCAPuzzles){
            classToLoad = NonWCAMenuActivity.class;
        }
        else if(source == buttonStatistics){
            classToLoad = Statistics.class;
        }

        //Defining which class to load in the new intent
        intent = new Intent(this, classToLoad);
        intent.putExtra("onlyAlgs", switchOnlyAlgs.isChecked());
        try{
            startActivity(intent);
        }catch(NullPointerException NPE){
            Log.d("ActivityError", NPE.getMessage());
        }
    }
}