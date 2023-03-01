package hu.okrim.puzzlecenter;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

public abstract class SharedPreferenceController {

    public static HashMap<Integer, String> n2Records = new HashMap<>();
    public static HashMap<Integer, String> n3Records = new HashMap<>();
    public static HashMap<Integer, String> n4Records = new HashMap<>();
    public static HashMap<Integer, String> n5Records = new HashMap<>();
    public static HashMap<Integer, String> n6Records = new HashMap<>();
    public static HashMap<Integer, String> n7Records = new HashMap<>();
    public static HashMap<Integer, String> pyraminxRecords = new HashMap<>();
    public static HashMap<Integer, String> megaminxRecords = new HashMap<>();
    public static HashMap<Integer, String> squareOneRecords = new HashMap<>();
    public static HashMap<Integer, String> skewbRecords = new HashMap<>();
    public static HashMap<Integer, String> clockRecords = new HashMap<>();
    public static HashMap<Integer, String> voidCubeRecords = new HashMap<>();
    public static HashMap<Integer, String> superIvyCubeRecords = new HashMap<>();
    public static HashMap<Integer, String> pentacleCubeRecords = new HashMap<>();
    public static HashMap<Integer, String> n2PyraminxRecords = new HashMap<>();
    public static HashMap<Integer, String> x2x4Records = new HashMap<>();
    public static HashMap<Integer, String> masterPyraminxRecords = new HashMap<>();

    public static void saveAllElementsToSharedPrefences(HashMap<Integer, String> map, String mapName, SharedPreferences.Editor editor){
        for(int i = 1; i < map.size() + 1; i++){
            try{
                //Concating mapName with key index, example: 3x3x3_1
                String keyName = mapName.concat("_").concat(String.valueOf(i));
                editor.putString(keyName, map.get(i));
                System.out.println("Saving: " + keyName + " = " + map.get(i));
                editor.apply();
            }catch(NullPointerException NPE){
//                Log.d("SharedPreferenceSaveError", "NullPointerException occurred whilst saving.");
                System.out.println("NullPointerException occurred whilst saving to sharedPreferences.");
            }
        }
    }

    public static void loadAllElementsFromSharedPrefences(HashMap<Integer, String> map, String key, SharedPreferences sharedPreferences){
        //We know each key has 3 entries maximum in the map since we've got a top 3 leaderboard
        for(int i = 1; i < 3 + 1; i++){
            try{
                //Concating mapName with key index, example: 3x3x3_1
                String keyName = key.concat("_").concat(String.valueOf(i));
                //If element is not found then we just default it to null
                String valueToStore = sharedPreferences.getString(keyName, null);
                map.put(i,valueToStore);
            }catch(NullPointerException NPE){
//                Log.d("SharedPreferenceLoadError", "NullPointerException occurred whilst loading.");
                System.out.println("NullPointerException occurred whilst loading from sharedPreferences.");
            }
        }
    }
}
