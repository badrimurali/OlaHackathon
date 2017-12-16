package mekhron.kekhron.com.olahackathon.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import mekhron.kekhron.com.olahackathon.Model.Song;

/**
 * Created by badri on 17/12/17.
 */

public class SharedPref {
    private static final String SHARED_PREF_NAME = "OLA_HACKATHON_SHARED_PREF";
    private static final String SONGS_LIST_KEY = "SONGS_LIST_KEY";
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    private static void saveStringInSp(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        editor.apply();
    }

    private static String getStringFromSP(Context context, String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

    public static void saveSongs(Context context, List<Song> songs) {
        saveStringInSp(context, SONGS_LIST_KEY, new Gson().toJson(songs));
    }

    public List getSongs(Context context) {
        return new Gson().fromJson(getStringFromSP(context, SONGS_LIST_KEY), List.class);
    }

}
