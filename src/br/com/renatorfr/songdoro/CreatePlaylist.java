package br.com.renatorfr.songdoro;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class CreatePlaylist extends Fragment {
	private SeekBar sbPlaylistDuration;
	private TextView tvPlaylistDuration;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.create_playlist, container, false);
		return view;
	}

	public void setPlaylistDurationText(int minutes) {
		sbPlaylistDuration = (SeekBar) getView().findViewById(R.id.sbPlaylistDuration);
		tvPlaylistDuration = (TextView) getView().findViewById(R.id.tvPlaylistDuration);

		sbPlaylistDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				tvPlaylistDuration.setText(progress + "");
			}
		});
	}
}
