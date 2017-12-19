package mekhron.kekhron.com.olahackathon.Model;

/**
 * Created by badri on 20/12/17.
 */

public class SongHistory {
    private Song song;
    private long seekPosition;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public long getSeekPosition() {
        return seekPosition;
    }

    public void setSeekPosition(long seekPosition) {
        this.seekPosition = seekPosition;
    }
}
