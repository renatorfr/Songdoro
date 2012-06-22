package br.com.renatorfr.songdoro;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class ContentProviderHelper {

	private final static int DURATION_RANGE = 3000;

	public static List<Music> getMusics(Context context, Long duration, List<String> newPlaylistMusics) {
		// Defining the minimum and maximum duration of the music
		Long maxDuration = TimeUnit.SECONDS.toMillis(duration) + DURATION_RANGE;

		// Which columns to return
		String columns[] = new String[] { MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.TITLE_KEY };

		// URI to the external storage
		Uri mAudio = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

		// Define the WHERE clause
		String whereClause = MediaStore.Audio.Media.IS_MUSIC + " <> 0";
		whereClause += duration == null ? "" : " AND " + MediaStore.Audio.Media.DURATION + " < " + maxDuration;

		// Prepare the TITLE_KEY list
		String titleKey = new String();
		for (String string : newPlaylistMusics) {
			titleKey.concat('"' + string + ',');
		}
		whereClause += newPlaylistMusics.size() <= 0 ? "" : " AND " + MediaStore.Audio.Media.TITLE_KEY + " NOT IN (" + titleKey + ")";

		// Gets the cursor with the musics found
		Cursor cursor = context.getContentResolver().query(mAudio, columns, whereClause, null, null);

		// Run through the cursor and fill a list with the music
		List<Music> musicList = null;
		if (cursor.moveToFirst()) {
			musicList = new ArrayList<Music>();
			do {
				// Create a new instance of Music class and fill it with the
				// information from Content Provider
				Music music = new Music();
				music.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
				music.setDuration(TimeUnit.MILLISECONDS.toSeconds(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));
				music.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
				music.setTitleKey(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE_KEY)));

				musicList.add(music);
			} while (cursor.moveToNext());
		}

		return musicList;
	}
}
