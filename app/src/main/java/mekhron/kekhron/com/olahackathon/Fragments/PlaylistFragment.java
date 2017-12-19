package mekhron.kekhron.com.olahackathon.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mekhron.kekhron.com.olahackathon.LandingScreen;
import mekhron.kekhron.com.olahackathon.Model.Song;
import mekhron.kekhron.com.olahackathon.MusicPlayer;
import mekhron.kekhron.com.olahackathon.R;
import mekhron.kekhron.com.olahackathon.SongsAdapter;
import mekhron.kekhron.com.olahackathon.Utils.SharedPref;

/**
 * Created by badri on 19/12/17.
 */

public class PlaylistFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        return view;
    }
}
