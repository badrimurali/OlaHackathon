package mekhron.kekhron.com.olahackathon;

import android.app.ProgressDialog;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

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
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mekhron.kekhron.com.olahackathon.Model.Song;
import mekhron.kekhron.com.olahackathon.Utils.SharedPref;

/**
 * Created by badri on 17/12/17.
 */

public class MusicPlayer extends AppCompatActivity{
    private Song song;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_music_player);
        Bundle bundle = getIntent().getExtras();
        progressDialog = new ProgressDialog(MusicPlayer.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading");
        progressDialog.show();
        if(bundle == null) {
            finish();
            return;
        }
        song = new Gson().fromJson(bundle.getString("song"), Song.class);
        if(song != null) {
            initMediaPlayer();
        }
    }

    private void initMediaPlayer() {
        Handler mHandler = new Handler();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(mHandler, new BandwidthMeter.EventListener() {
            @Override
            public void onBandwidthSample(int elapsedMs, long bytes, long bitrate) {
                System.out.println(" asdf bandwidth sample  ");

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
        System.out.println("asdf duration minutes "+ minutes);
        System.out.println("asdf duration "+mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        System.out.println("asdf artist "+mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        System.out.println("asdf author "+mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));


        MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, Mp3Extractor.FACTORY,
                mHandler, new ExtractorMediaSource.EventListener() {
            @Override
            public void onLoadError(IOException error) {

            }
        });

        TrackSelector trackSelector = new DefaultTrackSelector(bandwidthMeter);
        DefaultLoadControl loadControl = new DefaultLoadControl();
        ExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playWhenReady) {
                    if(progressDialog != null && progressDialog.isShowing()) {
                        System.out.println(" asdf player state change");
                        progressDialog.dismiss();
                    }
                }

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

}
