package hu.okrim.puzzlecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

public class RecordsActivity extends AppCompatActivity {

    ArrayAdapter<CharSequence> spinnerAdapter;
    int currentPuzzleID;
    HashMap<Integer, String> map = new HashMap<>();
    Spinner spinnerRecord;
    SharedPreferences sharedPreferences;
    String puzzleToLoad = null;
    TextView value1;
    TextView value2;
    TextView value3;
    TextView date1;
    TextView date2;
    TextView date3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        value1 = findViewById(R.id.textViewRecords1stValue);
        value2 = findViewById(R.id.textViewRecords2ndValue);
        value3 = findViewById(R.id.textViewRecords3rdValue);
        date1 = findViewById(R.id.textViewRecords1stDate);
        date2 = findViewById(R.id.textViewRecords2ndDate);
        date3 = findViewById(R.id.textViewRecords3rdDate);
        spinnerRecord = findViewById(R.id.spinnerRecord);
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerPuzzleType, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecord.setAdapter(spinnerAdapter);
        spinnerRecord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Setting spinner text color and size
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                puzzleToLoad = spinnerRecord.getSelectedItem().toString().toLowerCase();
                currentPuzzleID = position;
                loadRecordsToMap(puzzleToLoad);
                loadRecordsToViews(map);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void loadRecordsToMap(String puzzleToLoad){
        SharedPreferenceController.loadAllElementsFromSharedPrefences(map,puzzleToLoad,sharedPreferences);
    }

    void loadRecordsToViews(HashMap<Integer, String> map){
        try{
            if(map.get(1) != null){
                value1.setText(TimeFormatController.createTimeText(Integer.parseInt(map.get(1).split("-")[1])));
                date1.setText(map.get(1).split("-")[0]);
            }else{
                value1.setText("-");
                date1.setText("Not achieved yet.");
            }
            if(map.get(2) != null){
                value2.setText(TimeFormatController.createTimeText(Integer.parseInt(map.get(2).split("-")[1])));
                date2.setText(map.get(2).split("-")[0]);
            }else{
                value2.setText("-");
                date2.setText("Not achieved yet.");
            }
            if(map.get(3) != null){
                value3.setText(TimeFormatController.createTimeText(Integer.parseInt(map.get(3).split("-")[1])));
                date3.setText(map.get(3).split("-")[0]);
            }else{
                value3.setText("-");
                date3.setText("Not achieved yet.");
            }
        }catch(NullPointerException NPE){
            System.out.println(NPE.getMessage());
            Log.d("ErrorLoadingRecords", NPE.getMessage());
        }
    }
}