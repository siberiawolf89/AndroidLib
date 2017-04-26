package com.jiuli.library.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jiuli.library.comm.LibraryGlobal;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * Created by siberiawolf on 16/4/13.
 */
public abstract class LibraryGlideUtils {

    private static final String TAG = "LibraryGlideUtils";


    public abstract int getPlaceholderRes();
    public abstract int getErrorRes();


    public void load(ImageView imageView, String url){
        load(LibraryGlobal.mContext,imageView,url);
    }

    public void load(Context mContext, ImageView imageView, String url){
        try{
            Glide.with(mContext)
                    .load(url)
                    .placeholder(getPlaceholderRes())
                    .error(getErrorRes())
                    .listener(requestListener)
                    .into(imageView);
        }catch (Throwable e){
            LibraryLogUtils.e(TAG,e);
        }
    }

    public void loadBlur(Context mContext, ImageView imageView,String url,int radius){
        try{
            Glide.with(mContext)
                    .load(url)
                    .placeholder(getPlaceholderRes())
                    .error(getErrorRes())
                    .bitmapTransform(new BlurTransformation(mContext,radius))
                    .listener(requestListener)
                    .into(imageView);
        }catch (Throwable e){
            LibraryLogUtils.e(TAG,e);
        }
    }



    public void loadCropCircle(Context mContext, ImageView imageView,String url){
        try{
            Glide.with(mContext)
                    .load(url)
                    .placeholder(getPlaceholderRes())
                    .error(getErrorRes())
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .listener(requestListener)
                    .into(imageView);
        }catch (Throwable e){
            LibraryLogUtils.e(TAG,e);
        }
    }


    public void loadGrayscale(Context mContext, ImageView imageView,String url){
        try{
            Glide.with(mContext)
                    .load(url)
                    .placeholder(getPlaceholderRes())
                    .error(getErrorRes())
                    .bitmapTransform(new GrayscaleTransformation(mContext))
                    .listener(requestListener)
                    .into(imageView);
        }catch (Throwable e){
            LibraryLogUtils.e(TAG,e);
        }
    }



    // 监听图片的加载 回调
    private RequestListener<String,GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            LibraryLogUtils.e(TAG,e);
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            LibraryLogUtils.info(model+isFromMemoryCache+isFirstResource);

            return false;
        }
    };
}
