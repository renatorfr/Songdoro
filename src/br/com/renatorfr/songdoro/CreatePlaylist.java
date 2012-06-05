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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class CreatePlaylist extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.create_playlist, container, false);

		final EditText edtPlaylistName = (EditText) view.findViewById(R.id.txtPlaylistName);
		final SeekBar sbPlaylistDuration = (SeekBar) view.findViewById(R.id.sbPlaylistDuration);
		final TextView tvPlaylistDuration = (TextView) view.findViewById(R.id.tvPlaylistDuration);
		final Button btnCreatePlaylistButton = (Button) view.findViewById(R.id.btnCreatePlaylist);

		sbPlaylistDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvPlaylistDuration.setText(progress + "");
			}
		});

		btnCreatePlaylistButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				createPlaylist(edtPlaylistName.getText().toString(), Long.parseLong(tvPlaylistDuration.getText().toString()));
			}
		});

		return view;
	}

	protected void createPlaylist(String playlistName, Long playlistDuration) {
		// Converts the playlistDuration from minutes to seconds
		playlistDuration = TimeUnit.MINUTES.toSeconds(playlistDuration);

		// The new playlist
		List<Music> newPlaylist = new ArrayList<Music>();

		// Runs while the duration is greater than 0
		while (playlistDuration > 0) {
			// Gets a list of musics based on it's duration
			List<Music> listFiltered = ContentProviderHelper.getMusics(getView().getContext(), playlistDuration);

			if (listFiltered != null) {
				// Gets a music from the filteredList randomly
				Music music = listFiltered.get((int) (Math.random() * listFiltered.size()));

				// Adds the music to the new playlist and subtract the music
				// duration from the variable
				newPlaylist.add(music);
				playlistDuration -= music.getDuration();
			} else {
				CharSequence text = "Sorry, I could not create a playlist with this duration! :(";

				Toast toast = Toast.makeText(getView().getContext(), text, Toast.LENGTH_LONG);
				toast.show();
				break;
			}
		}
	}
}
