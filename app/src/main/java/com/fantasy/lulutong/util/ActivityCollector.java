package com.fantasy.lulutong.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理器
 * @author Fantasy
 * @version 1.0, 2017-02-17
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    /**
     * 添加一个活动到活动管理器中
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 把某个活动从活动管理器中移除
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 销毁掉活动管理器中所有活动
     */
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
