package br.com.renatorfr.songdoro;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;

public class SongdoroActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		EditText editText = (EditText) findViewById(R.id.editText1);
		String columns[] = new String[] { MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION };
		Uri mAudio = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

		Cursor cursor = managedQuery(mAudio, columns, null, null, null);

		if (cursor.moveToFirst()) {
			String artist;
			String title;
			long duration;
			do {
				// Get the field values
				artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
				duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
				editText.setText(editText.getText() + artist + " - " + title + " - " + getDuration(duration));
			} while (cursor.moveToNext());
		}
	}

	/**
	 * @param duration
	 * @return
	 */
	private String getDuration(long duration) {
		return String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(duration),
				TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
	}
}