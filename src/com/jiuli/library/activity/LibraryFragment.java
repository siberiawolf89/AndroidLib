package com.jiuli.library.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.jiuli.library.R;
import com.jiuli.library.bean.ResponseBean;
import com.jiuli.library.listener.ActivtyDataRefreshListener;
import com.jiuli.library.logic.LibraryNotifyMgr;
import com.jiuli.library.utils.LibraryLogUtils;
import com.jiuli.library.utils.okhttp.callback.ResultCallback;
import com.jiuli.library.utils.okhttp.request.OkHttpPostRequest;
import com.jiuli.library.utils.okhttp.request.OkHttpRequest;

import java.io.File;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import okhttp3.Request;

/**
 * Created by siberiawolf on 15/12/16.
 */
public class LibraryFragment extends Fragment implements View.OnClickListener,ActivtyDataRefreshListener {


    private static final String TAG = LibraryActivity.class.getName();
    public static final int DISMISS_ALERT_DIALOG_TYPE = 100000;
    public static final int DISMISS_PROGRESS_DIALOG_TYPE = 10001;
    public static final int SHOW_PROGRESS_DIALOG_TYPE = 10002;
    public static final int NETWORK_EXCEPTION_TYPE = 10003;
    public static final int NETWORK_SUCCESSFUL_TYPE = 10004;
    public static final int NETWORK_FAIL_TYPE = 10005;
    public static final int JSON_DATA_ERROR_TYPE = 10006;


    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;

    private OnFragmentInteractionListener mListener;



    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                switch (msg.what){
                    case SHOW_PROGRESS_DIALOG_TYPE:

                        if(mProgressDialog == null)
                            initProgressDialog();

                        if(msg.obj != null)
                            mProgressDialog.setMessage(String.valueOf(msg.obj));

                        mProgressDialog.show();



                        break;
                    case DISMISS_ALERT_DIALOG_TYPE:
                        if(mAlertDialog != null && mAlertDialog.isShowing())
                            mAlertDialog.dismiss();
                        break;
                    case DISMISS_PROGRESS_DIALOG_TYPE:
                        if(mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        break;
                    case NETWORK_EXCEPTION_TYPE:

                        dismissAlertDialog();
                        dismissProgressDialog();

                        if(msg.obj == null)
                            return;
                        LibraryNotifyMgr.showShortToast(String.valueOf(msg.obj));

                        break;
                    case NETWORK_SUCCESSFUL_TYPE:
                        aidHandleMessage(msg.what,msg.obj);
                        break;
                    case NETWORK_FAIL_TYPE:
                        aidHandleMessage(msg.what,msg.obj);
                        break;
                    default:
                        aidHandleMessage(msg.what,msg.obj);

                }
            }catch (Throwable e){
                Log.e(TAG, "handleMessage", e);
            }
        }
    };


    public void aidHandleMessage(int what,Object obj){};
    public void aidsendMessage(int what,Object obj){
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }

    public void aidsendMessage(int what,int arg1,Object obj){
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }

    public void aidsendMessage(int what,Object obj,long delayMillis){
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessageDelayed(msg, delayMillis);
    }



    public void showAlertDialog(String message){
        showAlertDialog("提示", message, "确定", "取消", DISMISS_ALERT_DIALOG_TYPE, DISMISS_ALERT_DIALOG_TYPE, null, null);
    }


    public void showAlertDialog(String title,String message,String sureString,String cancelString,final int sureType,final int cancelType,final Object sureObject,final Object cancelObject ){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
            if(!TextUtils.isEmpty(title))
                builder.setTitle(title);

            builder.setMessage(message)
                    .setNegativeButton(sureString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            aidsendMessage(sureType,sureObject);

                        }
                    })
                    .setPositiveButton(cancelString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            aidsendMessage(cancelType,cancelObject);

                        }
                    });

					/*.setNeutralButton("让我想想", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});*/
            mAlertDialog = builder.create();
            mAlertDialog.show();

			/*Button button = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
			button.setTextColor(getResources().getColor(R.color.black));*/


        }catch(Throwable e){
            Log.e(TAG,"showAlertDialog",e);
        }
    }

    private void initProgressDialog(){
        try{
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("正在加载，请稍后...");
            mProgressDialog.setCancelable(true);
        }catch (Throwable e){
            Log.e(TAG,"initProgressDialog",e);
        }
    }

    public void showProgressDialog(){
        try{
            showProgressDialog(null);
        }catch(Throwable e){
            Log.e(TAG,"showProgressDialog",e);
        }
    }

    public void showProgressDialog(String message){
        try{
            aidsendMessage(SHOW_PROGRESS_DIALOG_TYPE, message);
        }catch(Throwable e){
            Log.e(TAG,"showProgressDialog",e);
        }
    }

    public void dismissProgressDialog(){
        try {
            aidsendMessage(DISMISS_PROGRESS_DIALOG_TYPE, null);
        }catch (Throwable e){
            Log.e(TAG,"dismissProgressDialog",e);

        }
    }

    public void dismissAlertDialog(){
        try{
            aidsendMessage(DISMISS_ALERT_DIALOG_TYPE, null);
        }catch(Throwable e){
            Log.e(TAG,"dismissAlertDialog",e);
        }
    }


    public void sendHttpGet(String url,HashMap<String,String> map, final Type type,final int refreshType){
        try{
            LibraryLogUtils.info("GET请求url==>" + url);
            new OkHttpRequest.Builder().url(url).params(map).get(new ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    LibraryLogUtils.e(TAG, e.getMessage() + "===" + e.getLocalizedMessage());
                    String errorMsg = "网络错误";
                    if (e instanceof UnknownHostException) {
                        errorMsg = NETWORK_EXCEPTION_TYPE+":当前网络不可用，请检查网络再试";
                        //aidsendMessage(NETWORK_EXCEPTION_TYPE, "当前网络不可用，请检查网络再试");
                    } else if (e instanceof SocketTimeoutException) {
                        errorMsg = NETWORK_EXCEPTION_TYPE+":请求超时，请稍后再试";
                        //aidsendMessage(NETWORK_EXCEPTION_TYPE, "请求超时，请稍后再试");
                    } else {
                        errorMsg = NETWORK_EXCEPTION_TYPE+":网络异常，请稍后再试";
                        //aidsendMessage(NETWORK_EXCEPTION_TYPE, "网络异常，请稍后再试");
                    }

                    //将错误信息返回上一层  上层可以不处理该回调的结果
                    ResponseBean responseBean = new ResponseBean();
                    responseBean.refreshType = refreshType;
                    responseBean.obj = errorMsg;
                    aidsendMessage(NETWORK_FAIL_TYPE, responseBean);
                }

                @Override
                public void onResponse(String response) {
                    ResponseBean responseBean = new ResponseBean();
                    responseBean.refreshType = refreshType;

                    LibraryLogUtils.info("结果==>" + response);

                    responseBean.obj = response;
                    responseBean.mType = type;
                    aidsendMessage(NETWORK_SUCCESSFUL_TYPE, responseBean);
                }
            });

        }catch (Throwable e){
            Log.e(TAG,"sendHttp",e);
        }
    }


    public void sendHttpPost(String url,HashMap<String,String> map,final Type type,final int refreshType){
        try{
            LibraryLogUtils.info("POST请求url==>"+url);
            new OkHttpRequest.Builder().url(url).params(map).post(new ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    LibraryLogUtils.e(TAG, e.getMessage() + "===" + e.getLocalizedMessage());

                    if (e instanceof UnknownHostException) {
                        aidsendMessage(NETWORK_EXCEPTION_TYPE, "当前网络不可用，请检查网络再试");
                    } else if (e instanceof SocketTimeoutException) {
                        aidsendMessage(NETWORK_EXCEPTION_TYPE, "请求超时，请稍后再试");
                    } else {
                        aidsendMessage(NETWORK_EXCEPTION_TYPE, "网络异常，请稍后再试");
                    }

                    //将错误信息返回上一层  上层可以不处理该回调的结果
                    ResponseBean responseBean = new ResponseBean();
                    responseBean.refreshType = refreshType;
                    responseBean.obj = e;
                    aidsendMessage(NETWORK_FAIL_TYPE, responseBean);
                }

                @Override
                public void onResponse(String response) {
                    ResponseBean responseBean = new ResponseBean();
                    responseBean.refreshType = refreshType;

                    LibraryLogUtils.info("结果==>" + response);

                    responseBean.obj = response;
                    responseBean.mType = type;
                    aidsendMessage(NETWORK_SUCCESSFUL_TYPE, responseBean);
                }
            });

        }catch (Throwable e){
            Log.e(TAG,"sendHttp",e);
        }
    }

    public void sendHttpPost(String url,String json,final Type type,final int refreshType){
        try{
            new OkHttpPostRequest.Builder()
                    .url(url)
                    .json(json)
                    .post(new ResultCallback<String>() {
                        @Override
                        public void onError(Request request, Exception e) {
                            LibraryLogUtils.e(TAG, e.getMessage() + "===" + e.getLocalizedMessage());
                            String errorMsg = "网络错误";
                            if (e instanceof UnknownHostException) {
                                errorMsg = NETWORK_EXCEPTION_TYPE+":当前网络不可用，请检查网络再试";
                                //aidsendMessage(NETWORK_EXCEPTION_TYPE, "当前网络不可用，请检查网络再试");
                            } else if (e instanceof SocketTimeoutException) {
                                errorMsg = NETWORK_EXCEPTION_TYPE+":请求超时，请稍后再试";
                                //aidsendMessage(NETWORK_EXCEPTION_TYPE, "请求超时，请稍后再试");
                            } else {
                                errorMsg = NETWORK_EXCEPTION_TYPE+":网络异常，请稍后再试";
                                //aidsendMessage(NETWORK_EXCEPTION_TYPE, "网络异常，请稍后再试");
                            }

                            //将错误信息返回上一层  上层可以不处理该回调的结果
                            ResponseBean responseBean = new ResponseBean();
                            responseBean.refreshType = refreshType;
                            responseBean.obj = errorMsg;
                            aidsendMessage(NETWORK_FAIL_TYPE, responseBean);
                        }

                        @Override
                        public void onResponse(String response) {
                            ResponseBean responseBean = new ResponseBean();
                            responseBean.refreshType = refreshType;

                            LibraryLogUtils.info("结果==>" + response);

                            responseBean.obj = response;
                            responseBean.mType = type;
                            aidsendMessage(NETWORK_SUCCESSFUL_TYPE, responseBean);
                        }
                    });

        }catch (Throwable e){
            Log.e(TAG,"sendHttpPost",e);
        }
    }

    public void sendHttpUpload(String url, HashMap<String, String> map, final Type type, final int refreshType, Pair<String, File>... files){
        try{
            new OkHttpRequest.Builder()
                    .url(url)
                    .params(map)
                    .files(files)
                    .upload(new ResultCallback<String>() {
                        @Override
                        public void onError(Request request, Exception e) {
                            LibraryLogUtils.e(TAG, e.getMessage() + "===" + e.getLocalizedMessage());
                            e.printStackTrace();
                            String errorMsg = "网络错误";
                            if (e instanceof UnknownHostException) {
                                errorMsg = NETWORK_EXCEPTION_TYPE+":当前网络不可用，请检查网络再试";
                                //aidsendMessage(NETWORK_EXCEPTION_TYPE, "当前网络不可用，请检查网络再试");
                            } else if (e instanceof SocketTimeoutException) {
                                errorMsg = NETWORK_EXCEPTION_TYPE+":请求超时，请稍后再试";
                                //aidsendMessage(NETWORK_EXCEPTION_TYPE, "请求超时，请稍后再试");
                            } else {
                                errorMsg = NETWORK_EXCEPTION_TYPE+":网络异常，请稍后再试";
                                //aidsendMessage(NETWORK_EXCEPTION_TYPE, "网络异常，请稍后再试");
                            }

                            //将错误信息返回上一层  上层可以不处理该回调的结果
                            ResponseBean responseBean = new ResponseBean();
                            responseBean.refreshType = refreshType;
                            responseBean.obj = errorMsg;
                            aidsendMessage(NETWORK_FAIL_TYPE, responseBean);
                        }
                        @Override
                        public void onResponse(String response) {

                            try{
                                ResponseBean responseBean = new ResponseBean();
                                responseBean.refreshType = refreshType;

                                LibraryLogUtils.info("结果==>" + response);

                                responseBean.obj = response;
                                responseBean.mType = type;
                                aidsendMessage(NETWORK_SUCCESSFUL_TYPE, responseBean);
                            }catch (Throwable e){
                                LibraryLogUtils.e(TAG,e);
                            }



                        }
                        @Override
                        public void inProgress(float progress) {
                            super.inProgress(progress);
                            try{
                                LibraryLogUtils.info("progress==>"+progress);
                                //showProgressDialog("正在上传"+progress*100+"%");

                            }catch (Throwable e){
                                LibraryLogUtils.e(TAG,e);
                            }

                        }
                    });

        }catch (Throwable e){
            Log.e(TAG,"sendHttpUpload",e);
        }
    }



    @Override
    public void onRefresh(int refreshtype, Object obj) {
        aidsendMessage(refreshtype,obj);
    }

    @Override
    public void onClick(View v) {
        aidsendMessage(v.getId(),v);
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int type,Object obj) {
        if (mListener != null) {
            mListener.onFragmentInteraction(type,obj);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int type,Object obj);
    }
}
