package br.com.renatorfr.songdoro;

import android.app.Activity;
import android.os.Bundle;

public class SongdoroActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Google Analytics
		GATracker.getInstance(getApplicationContext()).trackPageView("/" + this.getClass().getSimpleName());

		setContentView(R.layout.main);
	}
}