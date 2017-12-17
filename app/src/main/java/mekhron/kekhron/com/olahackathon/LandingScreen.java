package mekhron.kekhron.com.olahackathon;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mekhron.kekhron.com.olahackathon.Model.Song;
import mekhron.kekhron.com.olahackathon.Rest.RestServices;
import mekhron.kekhron.com.olahackathon.Sqlite.SongsSqliteHelper;
import mekhron.kekhron.com.olahackathon.Utils.SharedPref;

public class LandingScreen extends AppCompatActivity implements SongsAdapter.OnClickListener {

    private ProgressDialog progressDialog;
    private RecyclerView rvSongs;
    private SongsSqliteHelper songsSqliteHelper;
    private List<Song> songsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        setUpToolbar();
        progressDialog = new ProgressDialog(LandingScreen.this);
        progressDialog.setMessage("loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        songsSqliteHelper = new SongsSqliteHelper(LandingScreen.this);
        RestServices.callSongs(new Consumer<List<Song>>() {
            @Override
            public void consume(List<Song> songs) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                songs = sort(songs);
                songsList = songs;
                SharedPref.saveSongs(LandingScreen.this, songs);
                for(Song s : songs) {
                    songsSqliteHelper.insert(s);
                }
                initViews(songs);
            }
        }, new Consumer<String>() {
            @Override
            public void consume(String s) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                CustomSnackBar.show(LandingScreen.this, "Songs fetch Failed!!", "OK");
            }
        });
    }

    private void initViews(List<Song> songs) {
        rvSongs = findViewById(R.id.rv_songs);
        rvSongs.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LandingScreen.this);
        rvSongs.setNestedScrollingEnabled(false);
        rvSongs.setLayoutManager(layoutManager);
        SongsAdapter adapter = new SongsAdapter(LandingScreen.this, songs, this);
        rvSongs.setAdapter(adapter);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_landing_page_toolbar, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor cursor = songsSqliteHelper.search(newText);
                List<Song> songs = new ArrayList<>();
                if(cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        Song song = new Song();
                        song.setSong(cursor.getString(0));
                        song.setArtists(cursor.getString(1));
                        song.setCover_image(cursor.getString(2));
                        song.setUrl(cursor.getString(3));
                        songs.add(song);
                    }
                    SongsAdapter songsAdapter = new SongsAdapter(LandingScreen.this, songs, LandingScreen.this);
                    rvSongs.setAdapter(songsAdapter);
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Song> sort(List<Song> songs) {
        List<Song> sortedSongs = new ArrayList<>();
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

    @Override
    public void onClick(int position) {
        Intent i = new Intent(LandingScreen.this, MusicPlayer.class);
        i.putExtra("position", position);
        startActivity(i);
    }
}
