package mekhron.kekhron.com.olahackathon.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import mekhron.kekhron.com.olahackathon.Model.SongHistory;
import mekhron.kekhron.com.olahackathon.MusicPlayer;
import mekhron.kekhron.com.olahackathon.R;
import mekhron.kekhron.com.olahackathon.SongHistoryAdapter;
import mekhron.kekhron.com.olahackathon.Sqlite.HistorySqliteHelper;

/**
 * Created by badri on 19/12/17.
 */

public class SongHistoryFragment extends Fragment implements SongHistoryAdapter.OnClickListener {
    private View rootView;
    private HistorySqliteHelper historySqliteHelper;
    ArrayList<SongHistory> songHistories;
    private RecyclerView rvHistory;
    SongHistoryAdapter adapter;
    private CardView cvHistory;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_song_history, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        historySqliteHelper = new HistorySqliteHelper(getActivity());
        songHistories = historySqliteHelper.getAll();
        System.out.println("asdf song histories "+ new Gson().toJson(songHistories));
        rvHistory = rootView.findViewById(R.id.rv_history);
        cvHistory = rootView.findViewById(R.id.cv_no_history);
        if(songHistories.isEmpty()){
            cvHistory.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
        } else {
            cvHistory.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
            rvHistory.setHasFixedSize(false);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rvHistory.setNestedScrollingEnabled(true);
            rvHistory.setLayoutManager(layoutManager);
            adapter = new SongHistoryAdapter(getActivity(), songHistories, this);
            rvHistory.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(int position) {
        Intent i = new Intent(getActivity(), MusicPlayer.class);
        i.putExtra("position", position);
        i.putExtra("from", SongHistoryAdapter.class.getSimpleName());
        startActivity(i);
    }
}
