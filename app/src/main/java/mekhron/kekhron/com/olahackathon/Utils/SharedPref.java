package mekhron.kekhron.com.olahackathon.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mekhron.kekhron.com.olahackathon.Model.Song;
import mekhron.kekhron.com.olahackathon.MusicPlayer;

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

    public static ArrayList<Song> getSongs(Context context) {
        JSONArray array = null;
        ArrayList<Song> songs = new ArrayList<>();
        try {
            array = new JSONArray(getStringFromSP(context, SONGS_LIST_KEY));
            for(int i =0; i< array.length(); i++) {
                try {
                    JSONObject object = array.getJSONObject(i);
                    Song song = new Song();
                    song.setSong(object.getString("song"));
                    song.setUrl(object.getString("url"));
                    song.setCover_image(object.getString("cover_image"));
                    song.setArtists(object.getString("artists"));
                    songs.add(song);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return songs;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public static ArrayList<Song> sort(List<Song> songs) {
        ArrayList<Song> sortedSongs = new ArrayList<>();
        ArrayList<String> songNames = new ArrayList<>();
        for(Song s : songs) {
            songNames.add(s.getSong());
        }
        Collections.sort(songNames);
        for(int i = 0; i < songNames.size(); i++) {
            for(Song s: songs) {
                if(songNames.get(i).equals(s.getSong())) {
                    sortedSongs.add(s);
                }
            }
        }
        return sortedSongs;
    }
}
