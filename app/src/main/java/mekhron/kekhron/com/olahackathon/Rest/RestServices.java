package mekhron.kekhron.com.olahackathon.Rest;

import java.util.List;

import mekhron.kekhron.com.olahackathon.Consumer;
import mekhron.kekhron.com.olahackathon.Model.Song;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by badri on 17/12/17.
 */

public class RestServices {

    public static void callSongs(final Consumer<List<Song>> successConsumer, final Consumer<String> failureConsumer) {
        ApiInterface apiInterface = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<List<Song>> callSongs = apiInterface.getSongs();
        callSongs.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                successConsumer.consume(response.body());
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                failureConsumer.consume("");
            }
        });
    }

}
