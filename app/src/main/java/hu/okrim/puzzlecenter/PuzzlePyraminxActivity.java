package hu.okrim.puzzlecenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;

public class PuzzlePyraminxActivity extends AppCompatActivity {

    CardView cardViewStep0;
    CardView cardViewStep1;
    CardView cardViewStep2;
    CardView cardViewStep3;
    CardView cardViewStep4;
    CardView cardViewStep5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_pyraminx);

        cardViewStep0 = findViewById(R.id.cVStep0);
        cardViewStep1 = findViewById(R.id.cVStep1);
        cardViewStep2 = findViewById(R.id.cVStep2);
        cardViewStep3 = findViewById(R.id.cVStep3);
        cardViewStep4 = findViewById(R.id.cVStep4);
        cardViewStep5 = findViewById(R.id.cVStep5);

        if(getIntent().getExtras().getBoolean("onlyAlgs")){
            cardViewStep0.setVisibility(View.GONE);
            cardViewStep1.setVisibility(View.GONE);
            cardViewStep2.setVisibility(View.GONE);
            cardViewStep3.setVisibility(View.GONE);
            cardViewStep4.setVisibility(View.GONE);
            cardViewStep5.setVisibility(View.GONE);
        }else{
            cardViewStep1.setVisibility(View.VISIBLE);
            cardViewStep2.setVisibility(View.VISIBLE);
            cardViewStep2.setVisibility(View.VISIBLE);
            cardViewStep3.setVisibility(View.VISIBLE);
            cardViewStep4.setVisibility(View.VISIBLE);
            cardViewStep5.setVisibility(View.VISIBLE);
        }
    }
}