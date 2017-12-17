package mekhron.kekhron.com.olahackathon;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import mekhron.kekhron.com.olahackathon.Model.Song;

/**
 * Created by badri on 17/12/17.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {
    private Context context;
    private List<Song> songs;
    private OnClickListener onClickListener;

    public SongsAdapter(Context context, List<Song> songs, OnClickListener onClickListener) {
        this.context = context;
        this.songs = songs;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolder(inflater.inflate(R.layout.item_songs, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setViews(position);
    }

    @Override
    public int getItemCount() {
        return this.songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSongName, tvArtistsName;
        private ImageView ivSong, ivPlay;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvArtistsName = itemView.findViewById(R.id.tv_artists_name);
            ivSong = itemView.findViewById(R.id.iv_song_image);
            ivPlay = itemView.findViewById(R.id.iv_play);
        }

        public void setViews(final int position) {
            Song song = songs.get(position);
            tvSongName.setText(song.getSong());
            tvArtistsName.setText(song.getArtists());
            Glide.with(context)
                    .load(Uri.parse(song.getCover_image()))
                    .apply(new RequestOptions().centerCrop())
                    .into(ivSong);

            ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(position);
                }
            });
        }
    }

    public interface OnClickListener {
        void onClick(int position);
    }
}
