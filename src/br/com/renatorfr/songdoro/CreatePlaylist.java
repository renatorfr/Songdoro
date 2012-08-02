package br.com.renatorfr.songdoro;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class CreatePlaylist extends Fragment {

	private final static int NUMBERPICKER_MAX_VALUE = 180;
	private final static int NUMBERPICKER_MIN_VALUE = 1;
	private final static int NUMBERPICKER_VALUE = 30;

	View view;
	EditText edtPlaylistName;
	TextView tvPlaylistFinal;
	NumberPicker npDuration;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.create_playlist, container, false);

		edtPlaylistName = (EditText) view.findViewById(R.id.txtPlaylistName);
		final Button btnCreatePlaylistButton = (Button) view.findViewById(R.id.btnCreatePlaylist);
		tvPlaylistFinal = (TextView) view.findViewById(R.id.tvPlaylistFinal);

		npDuration = (NumberPicker) view.findViewById(R.id.npDuration);
		npDuration.setMinValue(NUMBERPICKER_MIN_VALUE);
		npDuration.setMaxValue(NUMBERPICKER_MAX_VALUE);
		npDuration.setValue(NUMBERPICKER_VALUE);

		btnCreatePlaylistButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				createPlaylist(edtPlaylistName.getText().toString(), (long) npDuration.getValue());
			}
		});

		return view;
	}

	protected void createPlaylist(String playlistName, Long playlistDuration) {

		// Converts the playlistDuration from minutes to seconds
		playlistDuration = TimeUnit.MINUTES.toSeconds(playlistDuration);

		long temp = 0;

		// The new playlist
		List<Music> newPlaylist = new ArrayList<Music>();

		// This list will hold the TITLE_KEY field and it will be used to not
		// repeat music
		List<String> newPlaylistMusics = new ArrayList<String>();

		// Runs while the duration is greater than 0
		while (playlistDuration > 0) {
			// Gets a list of musics based on it's duration
			List<Music> listFiltered = ContentProviderHelper.getMusics(view.getContext(), playlistDuration, newPlaylistMusics);

			if (listFiltered != null) {
				// Gets a music from the filteredList randomly
				Music music = listFiltered.get((int) (Math.random() * listFiltered.size()));

				// Adds the music to the new playlist and subtract the music
				// duration from the variable
				newPlaylist.add(music);
				temp += music.getDuration();
				playlistDuration -= music.getDuration();

				// Add the TITLE_KEY field on the newPlaylistMusics
				newPlaylistMusics.add(music.getTitleKey());

			} else if (playlistDuration <= 0) {
				CharSequence text = "A new and fresh playlist is waiting for you! :)";

				Toast toast = Toast.makeText(getView().getContext(), text, Toast.LENGTH_LONG);
				toast.show();
				break;
			} else {
				CharSequence text = "Sorry, I could not create a playlist with this duration! :(";

				Toast toast = Toast.makeText(getView().getContext(), text, Toast.LENGTH_LONG);
				toast.show();
				break;
			}
		}
		tvPlaylistFinal.setText("Nova Playlist: ");
		for (Music music : newPlaylist) {
			tvPlaylistFinal.setText(tvPlaylistFinal.getText().toString() + music.getName() + " // ");
		}

		tvPlaylistFinal.setText(tvPlaylistFinal.getText() + " - Total: " + (double) temp / 60);
		tvPlaylistFinal.setText(tvPlaylistFinal.getText() + " - Duration: " + (double) playlistDuration / 60);

		// Save the new Playlist using the Content Provider
		ContentProviderHelper.SavePlaylist(getView().getContext(), newPlaylist, edtPlaylistName.getText().toString().trim());
	}
}
