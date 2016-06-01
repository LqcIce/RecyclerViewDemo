package edu.hrbeu.ice.recyclerviewdemo;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.widget.AbsListView;
import android.widget.ImageView;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ice on 2016/5/27.
 */
public class MyImageLoader {

    private static final String TAG = "MyImageLoader";
    private LruCache<String, Bitmap> mLruCache;
    //   private ImageView mImageView;
    //   private String mUrl;
    private Context context;

    public MyImageLoader(Context context) {
        this.context = context;
        //获取最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //初始化lrucache选最大内存四分之一作为其缓存大小
        mLruCache = new LruCache<String, Bitmap>(maxMemory / 4) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //重写sizeof方法，原本是根据元素数目计算内存使用了多少，重写为根据图片大小统计使用了多少内存
                return value.getByteCount();
            }
        };
    }

    /**
     * 滚动停止时，批量载入当前显示的item对应图片
     *
     * @param view
     * @param start
     * @param end
     * @param arrayList
     */
    public void loadAllImage(RecyclerView view, int start, int end, ArrayList<ItemBean> arrayList) {

        for (int i = start; i < end; i++) {
              getImageByThread((ImageView) view.findViewWithTag(arrayList.get(i).drawable), arrayList.get(i).drawable);
        }
    }

    /**
     * 添加元素到LruCache
     *
     * @param url
     * @param bitmap
     */
    public void addToLruCache(String url, Bitmap bitmap) {

        if (getFromLruCache(url) == null) {
            mLruCache.put(url, bitmap);
        }
    }

    /**
     * 从LruCache根据url获取对应图片
     *
     * @param url
     * @return
     */
    public Bitmap getFromLruCache(String url) {

        return mLruCache.get(url);
    }



    public boolean getImageByCache(final ImageView imageView, final String url) {
        if (getFromLruCache(url) != null) {
            //if (imageView.getTag().equals(url))
            imageView.setImageBitmap(getFromLruCache(url));
            return true;
        }
        return false;
    }



    /**
     * 通过多线程异步请求图片，如果LruCache中已存在该图片，则直接使用，否则再开线程异步请求
     *
     * @param imageView
     * @param url
     */
    public void getImageByThread(final ImageView imageView, final String url) {
        //mImageView = imageView;
        // mUrl = url;

        if (getFromLruCache(url) == null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    final Bitmap bitmap = getImage(url);
                    if (bitmap != null) {
                        addToLruCache(url, bitmap);
                    }

                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //为了防止快速滑动时，view被复用时，在复用之前从网络获取图片的请求，现在到达，导致当前显示图片错乱
                            if (imageView.getTag().equals(url))
                                imageView.setImageBitmap(bitmap);
                        }
                    });

                }
            }.start();
        }
    }

    /**
     * 从网络中获取图片
     *
     * @param imageurl
     * @return
     */
    public Bitmap getImage(String imageurl) {
        Bitmap bitmap;
        InputStream is = null;
        URL url = null;
        try {
            url = new URL(imageurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return null;
    }


}