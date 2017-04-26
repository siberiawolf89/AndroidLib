package com.jiuli.library.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiuli.library.R;
import com.jiuli.library.utils.LibraryLogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

@Deprecated
public class WebImageView extends ImageView {

	/*
	*
	* "http://site.com/image.png" // from Web
"file:///mnt/sdcard/image.png" // from SD card
"file:///mnt/sdcard/video.mp4" // from SD card (video thumbnail)
"content://media/external/images/media/13" // from content provider
"content://media/external/video/media/13" // from content provider (video thumbnail)
"assets://image.png" // from assets
"drawable://" + R.drawable.img // from drawables (non-9patch images)
	*
	* */

	private static final String TAG = WebImageView.class.getSimpleName();

	private static final int IMAGE_SOURCE_UNKNOWN = -1;
	private static final int IMAGE_SOURCE_RESOURCE = 0;
	private static final int IMAGE_SOURCE_DRAWABLE = 1;
	private static final int IMAGE_SOURCE_BITMAP = 2;

	private int mImageSource;
	private Bitmap mDefaultBitmap;
	private Drawable mDefaultDrawable;
	private int mDefaultResId;

	private int rounded;

	DisplayImageOptions options = null;

	private String mUrl;


	private Bitmap mBitmap;


	private String originalUrl;

	private String thumb;

	Context mContext;


	public WebImageView(Context context) {
		this(context, null);
	}

	public WebImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WebImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mContext = context;

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WebImageView, defStyle, 0);

		Drawable d = a.getDrawable(R.styleable.WebImageView_defaultSrc);
		if (d != null) {
			setDefaultImageDrawable(d);
		}

		final int inDensity = a.getInt(R.styleable.WebImageView_inDensity, -1);
		if (inDensity != -1) {
		}

		rounded = a.getInt(R.styleable.WebImageView_rounded,0);


		initializeDefaultValues();

		a.recycle();
	}

	private void initializeDefaultValues() {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		//builder.showImageOnLoading(R.drawable.img_def); // //设置图片在下载期间显示的图片
		//builder.showImageForEmptyUri(R.drawable.img_def); // 设置图片Uri为空或是错误的时候显示的图片
		//builder.showImageOnFail(R.drawable.img_def); // //设置图片加载/解码过程中错误时候显示的图片
		builder.resetViewBeforeLoading(false);   //设置图片在下载前是否重置，复位
		//builder.delayBeforeLoading(1000); //设置图片下载前的延迟
		builder.cacheInMemory(true); // //设置下载的图片是否缓存在内存中
		builder.cacheOnDisk(true); // //设置下载的图片是否缓存在SD卡中
		//builderpreProcessor(BitmapProcessor preProcessor); //设置图片加入缓存前，对bitmap进行设置
		//builderpostProcessor(BitmapProcessor postProcessor);//设置显示前的图片，显示后这个图片一直保留在缓存中
		//builderdecodingOptions(android.graphics.BitmapFactory.Options decodingOptions);//设置图片的解码配置
		builder.considerExifParams(false); // default
		builder.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2); //设置图片以如何的编码方式显示
		builder.bitmapConfig(Bitmap.Config.RGB_565); // //设置图片的解码类型
		builder.displayer(new SimpleBitmapDisplayer()); // default
		builder.handler(new Handler()); // default

		if(rounded>0)
		{
			builder.displayer(new RoundedBitmapDisplayer(rounded));
		}
		options = builder.build();
	}

	public void setUrl(String url) {
		ImageLoader.getInstance().displayImage(url, new ImageViewAware(this), options);

	}



	public void setGlideUrl(String url,int resId){
		try{

			LibraryLogUtils.info(url);
			Glide.with(mContext)
					.load(url)
					.placeholder(resId) // 设置加载前的默认图片
					.crossFade() // 图片加
					//.dontAnimate() // 关闭图片加载的动画
					//.error(resId)  // 错误时显示的
					.override(200,200) // 固定图片的大小
					.centerCrop() // 缩放图片 填充整个控件 但图像可能不会完整显示。
					.fitCenter() // 按图片以原始比例缩放
					.skipMemoryCache(false)  //跳过内存的缓存

					/*  DiskCacheStrategy.NONE 什么都不缓存，就像刚讨论的那样
						DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像。在我们上面的例子中，将会只有一个 1000x1000 像素的图片
						DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即，降低分辨率后的（或者是转换后的）
						DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
					 *  */
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					/*
					*   Priority.LOW
						Priority.NORMAL
						Priority.HIGH
						Priority.IMMEDIATE
					* */
					.priority(Priority.NORMAL) //加载的优先级
					// 缩放比例
					.thumbnail(0.00001f)
					.into(this);
		}catch(Throwable e){
			LibraryLogUtils.e(TAG,e);
		}
	}

	/**
	 * 设置download url，开始下载
	 *
	 * @param url
	 */
	public void load(String url,Integer rid) {

		if(rid!=null)
		{

			this.setImageResource(rid);
		}
		if( url==null || url.endsWith("null"))
		{
			this.setImageResource(rid);
			return;
		}
		this.originalUrl = url;
		ImageLoader.getInstance().displayImage(url, this, options);
	}


	public void display(String thumb,String originalUrl) {
		this.thumb = thumb;
		this.originalUrl = originalUrl;
		ImageLoader.getInstance().displayImage(thumb, this, options);
	}



	/**
	 * Set the default bitmap as the content of this AsyncImageView
	 *
	 * @param bitmap The bitmap to set
	 */
	public void setDefaultImageBitmap(Bitmap bitmap) {
		mImageSource = IMAGE_SOURCE_BITMAP;
		mDefaultBitmap = bitmap;
		setDefaultImage();
	}

	/**
	 * Set the default drawable as the content of this AsyncImageView
	 *
	 * @param drawable The drawable to set
	 */
	public void setDefaultImageDrawable(Drawable drawable) {
		mImageSource = IMAGE_SOURCE_DRAWABLE;
		mDefaultDrawable = drawable;
		setDefaultImage();
	}

	/**
	 * Set the default resource as the content of this AsyncImageView
	 *
	 * @param resId The resource identifier to set
	 */
	public void setDefaultImageResource(int resId) {
		mImageSource = IMAGE_SOURCE_RESOURCE;
		mDefaultResId = resId;
		setDefaultImage();
	}



	private void setDefaultImage() {
		if (mBitmap == null) {
			switch (mImageSource) {
				case IMAGE_SOURCE_BITMAP:
					setImageBitmap(mDefaultBitmap);
					break;
				case IMAGE_SOURCE_DRAWABLE:
					setImageDrawable(mDefaultDrawable);
					break;
				case IMAGE_SOURCE_RESOURCE:
					setImageResource(mDefaultResId);
					break;
				default:
					setImageDrawable(null);
					break;
			}
		}
	}

}
