package mekhron.kekhron.com.olahackathon;

import android.app.ProgressDialog;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import mekhron.kekhron.com.olahackathon.Model.Song;

/**
 * Created by badri on 17/12/17.
 */

public class MusicPlayer extends AppCompatActivity{
    private Song song;
    private ProgressDialog progressDialog;
    private ImageView ivCover, ivPlayPause, ivRewind, ivForward;
    private TextView tvSongName, tvArtistsName, tvDuration;
    private SeekBar seekBar;
    private ExoPlayer exoPlayer;
    private int playToggle = 0;
    private int seekCache = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_music_player);
        setUpToolbar();
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            finish();
            return;
        }
        song = new Gson().fromJson(bundle.getString("song"), Song.class);
        ivCover = findViewById(R.id.iv_cover_image);
        tvSongName = findViewById(R.id.tv_song_name);
        tvArtistsName = findViewById(R.id.tv_artists_name);
        tvDuration = findViewById(R.id.tv_duration);
        ivPlayPause = findViewById(R.id.iv_play_pause);
        ivRewind = findViewById(R.id.iv_rewind);
        ivForward = findViewById(R.id.iv_forward);
        seekBar = findViewById(R.id.seek_bar);
        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playToggle == 1) {
                    ivPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                    exoPlayer.setPlayWhenReady(true);
                } else {
                    ivPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    exoPlayer.setPlayWhenReady(false);
                }
                playToggle = playToggle == 1 ? 0 : 1;
            }
        });

        ivRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.seekToDefaultPosition();
            }
        });

        ivRewind.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                exoPlayer.seekTo(exoPlayer.getCurrentPosition() - 5);
                return true;
            }
        });

        ivForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println("asdf i "+ i);
                exoPlayer.seekTo(exoPlayer.getCurrentPosition() + (i > seekCache ? i : -seekCache) * 1000);
                seekCache = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ivForward.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                exoPlayer.seekTo(exoPlayer.getCurrentPosition() + (5 * 1000));
                seekBar.setProgress(seekBar.getProgress() + 5);
                seekBar.postDelayed(runnable, 1000);
                return true;
            }
        });
        if(song != null) {
            initMediaPlayer();
        }
        Glide.with(this)
                .load(Uri.parse(song.getCover_image()))
                .apply(RequestOptions.circleCropTransform())
                .into(ivCover);
        tvSongName.setText(song.getSong());
        tvArtistsName.setText(song.getArtists());
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateSeekbar();
        }
    };

    private void updateSeekbar() {
        seekBar.setProgress((int)exoPlayer.getCurrentPosition());
        seekBar.postDelayed(runnable, 1000);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initMediaPlayer() {
        Handler mHandler = new Handler();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(mHandler, new BandwidthMeter.EventListener() {
            @Override
            public void onBandwidthSample(int elapsedMs, long bytes, long bitrate) {
            }
        });

        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:40.0) Gecko/20100101 Firefox/40.0";
        Uri uri = Uri.parse(song.getUrl());
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent, null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true);

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(song.getUrl(), new HashMap<String, String>());
        Long duration = Long.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        Long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;

        tvDuration.setText(String.format("%s.%s", String.valueOf(minutes), String.valueOf(seconds)));

        MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, Mp3Extractor.FACTORY,
                mHandler, new ExtractorMediaSource.EventListener() {
            @Override
            public void onLoadError(IOException error) {

            }
        });

        TrackSelector trackSelector = new DefaultTrackSelector(bandwidthMeter);
        DefaultLoadControl loadControl = new DefaultLoadControl();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                System.out.println("asdf isLoading "+ isLoading);
                if(isLoading) {
                    System.out.println("asdf loading if "+ isLoading);
                    progressDialog = new ProgressDialog(MusicPlayer.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
                else {
                    System.out.println("asdf loading else "+ isLoading);
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    playToggle = 1;
                    ivPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                System.out.println("asdf play back state "+ playbackState);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            exoPlayer.stop();
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.stop();
        finish();
    }
}
