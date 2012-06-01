package br.com.renatorfr.songdoro;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class CreatePlaylist extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.create_playlist, container, false);

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
				getMusicList();	
			}
		});

		return view;
	}
	
	private void getMusicList() {
//		FileHelper.SearchMusics(this);
	}
}
