package hu.okrim.puzzlecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button buttonTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTimer = findViewById(R.id.buttonTimer);
    }

    public void loadSelectedPage(android.view.View source){
        Class classToLoad = null;
        Intent intent = null;

        if(source == buttonTimer){
            classToLoad = TimerActivity.class;
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