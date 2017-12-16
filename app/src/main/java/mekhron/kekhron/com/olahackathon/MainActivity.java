package mekhron.kekhron.com.olahackathon;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import java.util.List;

import mekhron.kekhron.com.olahackathon.Model.Song;
import mekhron.kekhron.com.olahackathon.Rest.RestServices;
import mekhron.kekhron.com.olahackathon.Utils.SharedPref;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        RestServices.callSongs(new Consumer<List<Song>>() {
            @Override
            public void consume(List<Song> songs) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                System.out.println("asdf songs "+ new Gson().toJson(songs));
                SharedPref.saveSongs(MainActivity.this, songs);
            }
        }, new Consumer<String>() {
            @Override
            public void consume(String s) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                CustomSnackBar.show(MainActivity.this, "Songs fetch Failed!!", "OK");
            }
        });
    }
}
