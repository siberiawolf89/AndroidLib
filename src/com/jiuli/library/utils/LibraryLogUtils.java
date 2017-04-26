/**
 *
 */
package com.jiuli.library.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LibraryLogUtils {



	public static int V = 5;
	public static int D = 4;
	public static int I = 3;
	public static int W = 2;
	public static int E = 1;

	private static  String DEF_FILEPATH = "/sdcard/androidlibrary.txt";

	private static String TAG = "androidlibrarylog";

	private static int LOGTYPE = 5;





	public static void v(String tag,Object msg){
		if(LOGTYPE >= V){
			writeFileToSDLog(tag,msg);
			Log.v(tag, String.valueOf(msg));
		}
	}

	public static void d(String tag,Object msg){
		if(LOGTYPE >= D){
			writeFileToSDLog(tag,msg);
			Log.d(tag, String.valueOf(msg));
		}
	}

	public static void i(String tag,Object msg){
		if(LOGTYPE >= I){
			writeFileToSDLog(tag,msg);
			Log.i(tag, String.valueOf(msg));
		}
	}

	public static void w(String tag,Object msg){
		if(LOGTYPE >= W){
			writeFileToSDLog(tag,msg);
			Log.w(tag, String.valueOf(msg));
		}
	}

	public static void e(String tag,Object msg){
		if(LOGTYPE >= E){
			writeFileToSDLog(tag,msg);
			Log.e(tag, String.valueOf(msg));

		}
	}

	public static void e(String msg){
		if(LOGTYPE >= E){
			writeFileToSDLog(TAG,msg);
			Log.e(TAG, String.valueOf(msg));

		}
	}


	public static void e(String tag, Throwable e) {
		String msg = getTrace(e);
		if (LOGTYPE >= E) {
			if (null != e) {
				Log.e(tag, msg);
				e.printStackTrace();
				writeFileToSDLog("aiderror"+tag, msg);
			}
		}
	}

	public static void info(String msg){
		v(TAG,msg);
	}



	public static void writeFileToSDLog(String key, Object value) {
		try{
			if (!isHasSDcard())
				return;

			if (0 == 0) {
				File file = new File(DEF_FILEPATH);
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				FileWriter fw = null;
				BufferedWriter bw = null;
				try {
					fw = new FileWriter(DEF_FILEPATH, true);
					bw = new BufferedWriter(fw);
					bw.newLine();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String time = format.format(new Date());
					bw.write(time + "##" + key + "####" + value);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null != bw) {
					try {
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (null != fw) {
					try {
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}catch(Throwable e){
			e(TAG,e);
		}

	}

	public static String getTrace(Throwable t) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		t.printStackTrace(printWriter);
		Throwable cause = t.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		return result;
	}

	public static boolean isHasSDcard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}


	public static void setDefFilepath(String defFilepath) {
		DEF_FILEPATH = defFilepath;
	}

	public static void setTAG(String TAG) {
		LibraryLogUtils.TAG = TAG;
	}

	public static void setLOGTYPE(int LOGTYPE) {
		LibraryLogUtils.LOGTYPE = LOGTYPE;
	}
}
