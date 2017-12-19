package mekhron.kekhron.com.olahackathon.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mekhron.kekhron.com.olahackathon.Model.Song;
import mekhron.kekhron.com.olahackathon.Model.SongHistory;

/**
 * Created by badri on 20/12/17.
 */

public class HistorySqliteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "HIS";
    private static final String TABLE_NAME = "history";
    private static final String SONGS_KEY = "song";
    private static final String URL_KEY = "url";
    private static final String ARTISTS_KEY = "artists";
    private static final String COVER_IMAGE_KEY  = "cover_image";
    private static final String SEEK_POSITION_KEY = "seek_position";

    public HistorySqliteHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+" (song TEXT PRIMARY KEY, artists TEXT, url TEXT, cover_image TEXT, seek_position INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(SongHistory songHistory) {
        System.out.println("asdf song history "+ new Gson().toJson(songHistory));
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues =  new ContentValues();
        contentValues.put(SONGS_KEY, songHistory.getSong().getSong());
        contentValues.put(ARTISTS_KEY, songHistory.getSong().getArtists());
        contentValues.put(URL_KEY, songHistory.getSong().getUrl());
        contentValues.put(COVER_IMAGE_KEY, songHistory.getSong().getCover_image());
        contentValues.put(SEEK_POSITION_KEY, songHistory.getSeekPosition());
        Long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        System.out.println("asdf result insert "+ result);
        return result != -1L;
    }

    public ArrayList<SongHistory> getAll() {
        ArrayList<SongHistory> historyList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor curs = sqLiteDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+TABLE_NAME+"'", null);
        boolean tableExists = false;
        if(curs!=null) {
            if(curs.getCount()>0) {
                curs.close();
                tableExists = true;
            }
            curs.close();
        }
        if(tableExists) {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    SongHistory songHistory = new SongHistory();
                    Song song = new Song();
                    song.setSong(cursor.getString(0));
                    song.setArtists(cursor.getString(1));
                    song.setCover_image(cursor.getString(2));
                    song.setUrl(cursor.getString(3));
                    songHistory.setSong(song);
                    songHistory.setSeekPosition(cursor.getLong(4));
                    historyList.add(songHistory);
                }
            }
        }
        return historyList;
    }
}
