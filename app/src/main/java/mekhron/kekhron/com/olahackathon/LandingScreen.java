package mekhron.kekhron.com.olahackathon;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mekhron.kekhron.com.olahackathon.Model.Song;
import mekhron.kekhron.com.olahackathon.Rest.RestServices;
import mekhron.kekhron.com.olahackathon.Sqlite.SongsSqliteHelper;
import mekhron.kekhron.com.olahackathon.Utils.SharedPref;

public class LandingScreen extends AppCompatActivity
        implements TabLayout.OnTabSelectedListener{
    private RecyclerView rvSongs;
    private TabLayout tlTabs;
    private ViewPager vpScreens;
    List<TabLayout.Tab> tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        setUpToolbar();
        tlTabs = findViewById(R.id.tl_tabs);
        tabs = new ArrayList<>();
        tabs.add(tlTabs.newTab().setIcon(R.drawable.ic_library_music));
        tabs.add(tlTabs.newTab().setIcon(R.drawable.ic_queue_music));
        for (TabLayout.Tab t: tabs) {
            tlTabs.addTab(t);
        }
        tlTabs.addOnTabSelectedListener(this);
        vpScreens = findViewById(R.id.vp_tabs);
        TabsPagerAdapter tpa = new TabsPagerAdapter(getSupportFragmentManager());
        vpScreens.setAdapter(tpa);
        vpScreens.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabs.get(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabs.get(0).select();
        vpScreens.setCurrentItem(0);
    }
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vpScreens.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
