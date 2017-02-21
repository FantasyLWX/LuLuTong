package com.fantasy.lulutong.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Volley的单例类
 * @author Fantasy
 * @version 1.0, 2017-02-20
 */
public class VolleySingleton {

    private static VolleySingleton volleySingleton;
    private RequestQueue mRequestQueue;
    private Context mContext;

    private VolleySingleton(Context context) {
        this.mContext = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * 获取RequestQueue的实例
     * @return 返回RequestQueue的实例
     */
    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * 获取VolleySingleton的实例
     * @param context
     * @return 返回VolleySingleton的实例
     */
    public static synchronized VolleySingleton getVolleySingleton(Context context) {
        if (volleySingleton == null) {
            volleySingleton = new VolleySingleton(context);
        }
        return volleySingleton;
    }

    /**
     * 将Request对象添加到RequestQueue里面
     * @param req Request对象
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
