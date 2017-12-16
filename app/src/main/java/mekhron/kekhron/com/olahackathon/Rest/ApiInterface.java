package mekhron.kekhron.com.olahackathon.Rest;

import java.util.List;

import mekhron.kekhron.com.olahackathon.Model.Song;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by badri on 17/12/17.
 */

public interface ApiInterface {
    @GET("studio")
    Call<List<Song>> getSongs();



}
