package com.capstone.file.utilities;

import java.io.File;
import android.content.Context;
import android.os.Environment;

public class FileUtilities 
{

	public static File getPrivateFile(Context context, String name) 
	{
//		Log.d(LOG_TAG, "getOutputMediaFile() type:" + type);
		
		// The directory where we'll store the file
		File storageDir = null;
		
		// The name of the file we'll return
		File outputFile = null;
		
		// Security is private, store it in the app's private directory.
		storageDir = context.getFilesDir();
		
		// If a name was specified, use that filename.
		if (name != null && storageDir != null) 
		{
			outputFile = new File(storageDir.getPath() + File.separator + name);
		}
		
		return outputFile;
	}
	
	
	public static File getPublicPicturesFile(Context context, String name)
	{
		   // Get the directory for the user's public pictures directory.
	    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), name);
	    if (!file.mkdirs()) 
	    {
	    	return null;
	    }
	    return file;
	}

	public static File getPublicCacheFile(Context context, String name)
	{
		   // Get the directory for the user's public pictures directory.
	    File file = new File(context.getExternalCacheDir(), name);
	    return file;
	}
	
	public boolean isExternalStorageWritable() 
	{
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) 
	    {
	        return true;
	    }
	    return false;
	}
}
