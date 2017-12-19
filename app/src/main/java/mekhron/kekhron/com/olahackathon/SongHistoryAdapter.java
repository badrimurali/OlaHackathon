package mekhron.kekhron.com.olahackathon;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import mekhron.kekhron.com.olahackathon.Model.Song;
import mekhron.kekhron.com.olahackathon.Model.SongHistory;

/**
 * Created by badri on 20/12/17.
 */

public class SongHistoryAdapter extends RecyclerView.Adapter<SongHistoryAdapter.ViewHolder> {
    private ArrayList<SongHistory> songHistories;
    private Context context;
    private OnClickListener onClickListener;

    public SongHistoryAdapter(Context context, ArrayList<SongHistory> songHistories, OnClickListener onClickListener) {
        this.context = context;
        this.songHistories = songHistories;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ViewHolder(inflater.inflate(R.layout.item_song_history, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setViews(position);
    }

    @Override
    public int getItemCount() {
        return songHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvSongName, tvArtistsName;
        CardView cvSongItem;
        private ImageView ivSong;
        private SeekBar sbSeek;

        public ViewHolder(View itemView) {
            super(itemView);
            cvSongItem = itemView.findViewById(R.id.cv_song_item);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvArtistsName = itemView.findViewById(R.id.tv_artists_name);
            ivSong = itemView.findViewById(R.id.iv_song_image);
            sbSeek = itemView.findViewById(R.id.sb_seek_history);
            sbSeek.setOnSeekBarChangeListener(null);
        }
        public void setViews(final int position) {
            Song song = songHistories.get(position).getSong();
            tvSongName.setText(song.getSong());
            tvArtistsName.setText(song.getArtists());
            Glide.with(context)
                    .load(Uri.parse(song.getCover_image()))
                    .apply(new RequestOptions().centerCrop())
                    .into(ivSong);
            sbSeek.setProgress((int)songHistories.get(position).getSeekPosition());

            cvSongItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(position);
                }
            });
        }
    }
    public interface OnClickListener {
        public void onClick(int position);
    }
}
