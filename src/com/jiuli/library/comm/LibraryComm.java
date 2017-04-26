package com.jiuli.library.comm;

import android.os.Environment;

public abstract class LibraryComm {


    public static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
     
    public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
    

}
