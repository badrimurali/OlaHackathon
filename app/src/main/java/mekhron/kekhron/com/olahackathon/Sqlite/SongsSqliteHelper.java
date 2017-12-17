package mekhron.kekhron.com.olahackathon.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mekhron.kekhron.com.olahackathon.Model.Song;

/**
 * Created by badri on 17/12/17.
 */

public class SongsSqliteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "OPS";
    private static final String TABLE_NAME = "songs";
    private static final String SONGS_KEY = "song";
    private static final String URL_KEY = "url";
    private static final String ARTISTS_KEY = "artists";
    private static final String COVER_IMAGE_KEY  = "cover_image";

    public SongsSqliteHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+" (song TEXT, artists TEXT, url TEXT, cover_image TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(Song song) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SONGS_KEY, song.getSong());
        contentValues.put(ARTISTS_KEY, song.getArtists());
        contentValues.put(URL_KEY, song.getUrl());
        contentValues.put(COVER_IMAGE_KEY, song.getCover_image());
        Long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if(result == -1L)
            return false;
        return true;
    }

    public Cursor search(String queryStr) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select DISTINCT song, artists, cover_image, url from "+TABLE_NAME+" where song like '%"+queryStr+"%' OR artists like '%"+queryStr+"%'", null);
    }
}
