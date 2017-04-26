package com.jiuli.library.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jiuli.library.R;
import com.jiuli.library.utils.LibraryLogUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * Created by siberiawolf on 16/4/13.
 */
public class GlideImageView extends ImageView {

    private static final String TAG = "GlideImageView";

    Context mContext;

    private String mUrl;
    private Drawable placeholderDrawable;
    private Drawable errorDrawable;
    private int loadres;

    public GlideImageView(Context context) {
        this(context,null);
    }

    public GlideImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GlideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GlideImageView, defStyleAttr, 0);

        placeholderDrawable = a.getDrawable(R.styleable.GlideImageView_placeholder);
        errorDrawable = a.getDrawable(R.styleable.GlideImageView_error);
        mUrl = a.getString(R.styleable.GlideImageView_loadurl);
        loadres = a.getResourceId(R.styleable.GlideImageView_loadres,0);

        a.recycle();

        initGlideImageView(context);


    }

    private void initGlideImageView(Context context){
        try{
            //new RoundedCornersTransformation(mContext, 30, 0,RoundedCornersTransformation.CornerType.ALL
            if(!TextUtils.isEmpty(mUrl)){
                Glide.with(context)
                        .load(mUrl)
                        .placeholder(placeholderDrawable)
                        .error(errorDrawable)
                        .listener(requestListener)
                        .into(this);
            }else if(loadres != 0){
                Glide.with(context)
                        .load(loadres)
                        .into(this);
            }


        }catch (Throwable e){
            LibraryLogUtils.e(TAG,e);
        }
    }



    public void load(String url){
        try{
            Glide.with(mContext)
                    .load(url)
                    .placeholder(placeholderDrawable)
                    .error(errorDrawable)
                    .listener(requestListener)
                    .into(this);
        }catch (Throwable e){
            LibraryLogUtils.e(TAG,e);
        }
    }

    public void loadBlur(String url,int radius){
        try{
            Glide.with(mContext)
                    .load(url)
                    .placeholder(placeholderDrawable)
                    .error(errorDrawable)
                    .bitmapTransform(new BlurTransformation(mContext,radius))
                    .listener(requestListener)
                    .into(this);
        }catch (Throwable e){
            LibraryLogUtils.e(TAG,e);
        }
    }

    public void loadCropCircle(String url){
        try{
            Glide.with(mContext)
                    .load(url)
                    .placeholder(placeholderDrawable)
                    .error(errorDrawable)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .listener(requestListener)
                    .into(this);
        }catch (Throwable e){
            LibraryLogUtils.e(TAG,e);
        }
    }


    public void loadGrayscale(String url){
        try{
            Glide.with(mContext)
                    .load(url)
                    .placeholder(placeholderDrawable)
                    .error(errorDrawable)
                    .bitmapTransform(new GrayscaleTransformation(mContext))
                    .listener(requestListener)
                    .into(this);
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
