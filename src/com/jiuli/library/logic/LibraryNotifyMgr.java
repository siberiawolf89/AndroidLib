package com.jiuli.library.logic;

import android.app.NotificationManager;
import android.content.Context;
import android.widget.Toast;

import com.jiuli.library.comm.LibraryGlobal;

/**
 * Created by siberiawolf on 15/10/23.
 */
public class LibraryNotifyMgr {

    public static Toast notifyToast = null;
    public static NotificationManager noticeMgr = (NotificationManager) LibraryGlobal.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    public LibraryNotifyMgr(){

    }

    public static void showShortToast(String info) {

        if (LibraryGlobal.mContext != null && info != null) {
            if (notifyToast != null)
                notifyToast.cancel();

            notifyToast = Toast.makeText(LibraryGlobal.mContext, info, Toast.LENGTH_SHORT);
            notifyToast.show();

        }
    }

    public static void showToastLonger(String info) {
        if (LibraryGlobal.mContext != null && info != null) {
            if (notifyToast != null)
                notifyToast.cancel();
            notifyToast = Toast.makeText(LibraryGlobal.mContext, info, Toast.LENGTH_LONG);
            notifyToast.show();

        }
    }
}
