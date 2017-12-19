package mekhron.kekhron.com.olahackathon;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mekhron.kekhron.com.olahackathon.Fragments.PlaylistFragment;
import mekhron.kekhron.com.olahackathon.Fragments.SongHistoryFragment;
import mekhron.kekhron.com.olahackathon.Fragments.SongsListFragment;

/**
 * Created by badri on 19/12/17.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PlaylistFragment();
            case 1:
                return new SongsListFragment();
            case 2:
                return new SongHistoryFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
