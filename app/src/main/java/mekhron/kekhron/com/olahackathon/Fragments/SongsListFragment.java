package mekhron.kekhron.com.olahackathon.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mekhron.kekhron.com.olahackathon.Model.Song;
import mekhron.kekhron.com.olahackathon.MusicPlayer;
import mekhron.kekhron.com.olahackathon.R;
import mekhron.kekhron.com.olahackathon.SongsAdapter;
import mekhron.kekhron.com.olahackathon.Sqlite.SongsSqliteHelper;
import mekhron.kekhron.com.olahackathon.Utils.SharedPref;

/**
 * Created by badri on 19/12/17.
 */

public class SongsListFragment extends Fragment implements SongsAdapter.OnClickListener {
    private RecyclerView rvSongs;
    private View rootView;
    private SearchView searchView;
    private SongsAdapter adapter;
    private ImageView ivNext, ivPrevious;
    private TextView tvPage;
    private static int currentPage = 1;
    private static int totalPage = 1;
    private static int startPos = 0;
    private static int endPos = 5;
    List<Song> songs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_songs_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        songs = SharedPref.getSongs(getActivity());
        initViews(rootView, songs);
    }

    private void initViews(View view, final List<Song> songs) {
        rvSongs = view.findViewById(R.id.rv_songs);
        rvSongs.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvSongs.setNestedScrollingEnabled(true);
        rvSongs.setLayoutManager(layoutManager);
        ivPrevious = rootView.findViewById(R.id.iv_previous_page);
        tvPage = rootView.findViewById(R.id.tv_page);
        ivNext = rootView.findViewById(R.id.iv_next_page);
        totalPage = songs.size() / 5;
        System.out.println("asdf size by 5 "+ songs.size() / 5);
        totalPage = totalPage + ((songs.size() % 5 == 0) ? 0 : 1);
        System.out.println("asdf total size "+ songs.size());
        System.out.println("asdf total page "+ totalPage);
        System.out.println("asdf current page "+ currentPage);
        setPageNumber();
        ivPrevious.setEnabled(false);
        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage < totalPage) {
                    ivNext.setEnabled(true);
                }
                if(currentPage > 1) {
                    --currentPage;
                    setPageNumber();
                    endPos = startPos;
                    startPos = endPos - 5;
                    adaptPageNumber();
                }
                ivPrevious.setEnabled(currentPage == 1);
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage > 1) {
                    ivPrevious.setEnabled(true);
                }
                if(currentPage < totalPage) {
                    ++currentPage;
                    setPageNumber();
                    startPos = endPos;
                    if(currentPage * 5 > songs.size()) {
                        endPos = songs.size();
                    } else {
                        endPos = currentPage * 5;
                    }
                    adaptPageNumber();
                }
                ivNext.setEnabled(currentPage == totalPage);
            }
        });
        adaptPageNumber();
        searchView = rootView.findViewById(R.id.sv_search_song);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SongsSqliteHelper songsSqliteHelper = new SongsSqliteHelper(getActivity());
                Cursor cursor = songsSqliteHelper.search(newText);
                List<Song> songs = new ArrayList<>();
                if(cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        Song song = new Song();
                        song.setSong(cursor.getString(0));
                        song.setArtists(cursor.getString(1));
                        song.setCover_image(cursor.getString(2));
                        song.setUrl(cursor.getString(3));
                        songs.add(song);
                    }
                    adapter = new SongsAdapter(getActivity(), songs, SongsListFragment.this);
                    rvSongs.setAdapter(adapter);
                }
                return true;
            }
        });
    }

    private void adaptPageNumber() {
        System.out.println("asdf start pos "+ startPos);
        System.out.println("asdf end pos "+ endPos);
        List<Song> subSongs = songs.subList(startPos, endPos);
        System.out.println("asdf sub list size "+ subSongs.size());
        adapter = new SongsAdapter(getActivity(), subSongs, SongsListFragment.this);
        rvSongs.setAdapter(adapter);
    }

    private void setPageNumber() {
        tvPage.setText(String.format("%s/%s", currentPage, totalPage));
    }
    @Override
    public void onClick(int position) {
        Intent i = new Intent(getActivity(), MusicPlayer.class);
        i.putExtra("position", position);
        startActivity(i);
    }
}
