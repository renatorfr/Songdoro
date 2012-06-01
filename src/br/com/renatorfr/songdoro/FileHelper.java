package br.com.renatorfr.songdoro;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.text.GetChars;

public class FileHelper {
	
	public static List<Music> SearchMusics(Context context){
		
		//Checking the external storage state
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can read and write the media
		    File file = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
		    
		    file.list();
		    
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		}
		
		return null;
	}

}
