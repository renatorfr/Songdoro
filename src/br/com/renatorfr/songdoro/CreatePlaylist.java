package br.com.renatorfr.songdoro;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class CreatePlaylist extends Fragment {

	private final static int	NUMBERPICKER_MAX_VALUE	= 180;
	private final static int	NUMBERPICKER_MIN_VALUE	= 1;
	private final static int	NUMBERPICKER_VALUE		= 30;
	private final static String	PD_TITLE_STRING			= "Wait...";
	private final static String	PD_MESSAGE_STRING		= "Creating the best playlist ever...";
	private final static int	NUMBER_OF_ATTEMPTS		= 3;
	private final static int	DURATION_RANGE			= 5000;

	View						view;
	EditText					edtPlaylistName;
	NumberPicker				npDuration;
	ProgressDialog				pdDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.create_playlist, container, false);

		edtPlaylistName = (EditText) view.findViewById(R.id.txtPlaylistName);
		final Button btnCreatePlaylistButton = (Button) view.findViewById(R.id.btnCreatePlaylist);

		npDuration = (NumberPicker) view.findViewById(R.id.npDuration);
		npDuration.setMinValue(NUMBERPICKER_MIN_VALUE);
		npDuration.setMaxValue(NUMBERPICKER_MAX_VALUE);
		npDuration.setValue(NUMBERPICKER_VALUE);

		btnCreatePlaylistButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String playlistName = edtPlaylistName.getText().toString().trim().equals("") ? String.valueOf(npDuration.getValue())
						: edtPlaylistName.getText().toString();
				createPlaylist(playlistName, (long) npDuration.getValue());
			}
		});

		final CheckBox chbPlaylistName = (CheckBox) view.findViewById(R.id.chbPlaylistName);

		chbPlaylistName.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				UseDurationAsPlaylistName(chbPlaylistName.isChecked());
			}
		});

		chbPlaylistName.setChecked(true);
		UseDurationAsPlaylistName(true);

		return view;
	}

	protected void UseDurationAsPlaylistName(boolean checked) {
		edtPlaylistName.setEnabled(!checked);
		edtPlaylistName.setText(null);
	}

	protected void createPlaylist(String playlistName, Long playlistDuration) {

		// Starts the progress dialog
		pdDialog = ProgressDialog.show(view.getContext(), PD_TITLE_STRING, PD_MESSAGE_STRING);

		// Converts the playlistDuration from minutes to seconds
		playlistDuration = TimeUnit.MINUTES.toSeconds(playlistDuration);

		// Save the original playlist duration for re-use
		Long playlistDurationTemp = playlistDuration;

		// The new playlist
		List<Music> newPlaylist = new ArrayList<Music>();

		// This list will hold the TITLE_KEY field and it will be used to not
		// repeat music
		List<String> newPlaylistMusics = new ArrayList<String>();

		// Number of attempts
		int attempts = 1;

		// Runs while the duration is greater than 0
		while (playlistDurationTemp > 0) {
			// Gets a list of musics based on it's duration
			List<Music> listFiltered = ContentProviderHelper.getMusics(view.getContext(), playlistDurationTemp, DURATION_RANGE, newPlaylistMusics);

			// If there is any music shorter than the duration remaining
			if (listFiltered != null) {
				// Gets a music from the filteredList randomly
				Music music = listFiltered.get((int) (Math.random() * listFiltered.size()));

				// Adds the music to the new playlist and subtract the music
				// duration from the variable
				newPlaylist.add(music);
				playlistDurationTemp -= music.getDuration();

				// Add the TITLE_KEY field on the newPlaylistMusics
				newPlaylistMusics.add(music.getTitleKey());

				// If the duration is within the range
			} else if ((playlistDurationTemp <= TimeUnit.MILLISECONDS.toSeconds(DURATION_RANGE))
					&& (playlistDurationTemp >= 0 - TimeUnit.MILLISECONDS.toSeconds(DURATION_RANGE))) {

				// Save the new Playlist using the Content Provider
				ContentProviderHelper.SavePlaylist(getView().getContext(), newPlaylist, playlistName.trim());

				CharSequence text = "A new and fresh playlist is waiting for you!";

				Toast toast = Toast.makeText(getView().getContext(), text, Toast.LENGTH_LONG);
				toast.show();
				break;

				// If the duration is not within the range and the number of
				// attempts is lower than the max number of attempts
			} else if (attempts <= NUMBER_OF_ATTEMPTS) {
				attempts++;
				newPlaylist.clear();
				newPlaylistMusics.clear();
				playlistDurationTemp = playlistDuration;

				// If the duration is not within the range and the number of
				// attempts is greater than the max number of attempts
			} else {
				CharSequence text = "Sorry, I could not create a playlist with this duration! :(";

				Toast toast = Toast.makeText(getView().getContext(), text, Toast.LENGTH_LONG);
				toast.show();
				break;
			}
		}

		// Stops the progress dialog
		pdDialog.dismiss();
	}
}
