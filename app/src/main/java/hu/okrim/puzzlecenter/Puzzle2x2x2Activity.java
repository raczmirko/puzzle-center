package hu.okrim.puzzlecenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;

public class Puzzle2x2x2Activity extends AppCompatActivity {

    CardView cardViewStep1;
    CardView cardViewStep2;
    CardView cardViewStep3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2x2x2);

        cardViewStep1 = findViewById(R.id.cVStep1);
        cardViewStep2 = findViewById(R.id.cVStep2);
        cardViewStep3 = findViewById(R.id.cVStep3);

        if(getIntent().getExtras().getBoolean("onlyAlgs")){
            cardViewStep1.setVisibility(View.GONE);
            cardViewStep2.setVisibility(View.GONE);
            cardViewStep3.setVisibility(View.GONE);
        }else{
            cardViewStep1.setVisibility(View.VISIBLE);
            cardViewStep2.setVisibility(View.VISIBLE);
            cardViewStep3.setVisibility(View.VISIBLE);
        }
    }
}