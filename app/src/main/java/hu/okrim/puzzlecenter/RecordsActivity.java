package hu.okrim.puzzlecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

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
    DatabaseController databaseController = new DatabaseController(RecordsActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        initUI();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_records);
        initUI();
    }

    private void initUI() {
        value1 = findViewById(R.id.textViewRecords1stValue);
        value2 = findViewById(R.id.textViewRecords2ndValue);
        value3 = findViewById(R.id.textViewRecords3rdValue);
        date1 = findViewById(R.id.textViewRecords1stDate);
        date2 = findViewById(R.id.textViewRecords2ndDate);
        date3 = findViewById(R.id.textViewRecords3rdDate);
        spinnerRecord = findViewById(R.id.spinnerRecord);
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerPuzzleType, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        spinnerRecord.setAdapter(spinnerAdapter);
        spinnerRecord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Setting spinner text color and size
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(30);
                puzzleToLoad = spinnerRecord.getSelectedItem().toString().toLowerCase();
                currentPuzzleID = position;
                loadRecordsToViews();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void loadRecordsToViews(){
        List<DataEntryModel> dataEntryList = databaseController.getTop3Solves(puzzleToLoad);
        //Setting everything to default values
        value1.setText("-");
        value2.setText("-");
        value3.setText("-");
        date1.setText("Not achieved yet.");
        date2.setText("Not achieved yet.");
        date3.setText("Not achieved yet.");

        //If size is 0 then nothing happens
        //If size is 1 then only first place is updated
        if (dataEntryList.size() == 1) {
            //Getting the first element's solvetime saved in nanos, then formatting it
            String formattedTime = TimeFormatController.createTimeText(dataEntryList.get(0).getSolveTime());
            value1.setText(formattedTime);
            date1.setText(dataEntryList.get(0).getDateString());
        }
        //If size is 2 then 2 records exist, 3rd doesn't so it is left default
        else if (dataEntryList.size() == 2){
            String formattedTime1 = TimeFormatController.createTimeText(dataEntryList.get(0).getSolveTime());
            String formattedTime2 = TimeFormatController.createTimeText(dataEntryList.get(1).getSolveTime());
            value1.setText(formattedTime1);
            value2.setText(formattedTime2);
            date1.setText(dataEntryList.get(0).getDateString());
            date2.setText(dataEntryList.get(1).getDateString());
        }
        //All 3 records exist (or more)
        else if (dataEntryList.size() > 2){
            String formattedTime1 = TimeFormatController.createTimeText(dataEntryList.get(0).getSolveTime());
            String formattedTime2 = TimeFormatController.createTimeText(dataEntryList.get(1).getSolveTime());
            String formattedTime3 = TimeFormatController.createTimeText(dataEntryList.get(2).getSolveTime());
            value1.setText(formattedTime1);
            value2.setText(formattedTime2);
            value3.setText(formattedTime3);
            date1.setText(dataEntryList.get(0).getDateString());
            date2.setText(dataEntryList.get(1).getDateString());
            date3.setText(dataEntryList.get(2).getDateString());
        }
    }
}