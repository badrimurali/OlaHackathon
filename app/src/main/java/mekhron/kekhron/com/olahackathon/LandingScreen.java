package mekhron.kekhron.com.olahackathon;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import mekhron.kekhron.com.olahackathon.Model.Song;
import mekhron.kekhron.com.olahackathon.Rest.RestServices;
import mekhron.kekhron.com.olahackathon.Utils.SharedPref;

public class LandingScreen extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private RecyclerView rvSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        progressDialog = new ProgressDialog(LandingScreen.this);
        progressDialog.setMessage("loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RestServices.callSongs(new Consumer<List<Song>>() {
            @Override
            public void consume(List<Song> songs) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                System.out.println("asdf songs "+ new Gson().toJson(songs));
                SharedPref.saveSongs(LandingScreen.this, songs);
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
        SongsAdapter adapter = new SongsAdapter(LandingScreen.this, songs);
        rvSongs.setAdapter(adapter);
    }
}
