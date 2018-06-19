package com.ten.half.rxmvp.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.commonslibrary.commons.config.SystemConfig;
import com.commonslibrary.commons.utils.NetWorkUtils;

/**
 * Created by wugang on 2017/12/26.
 */

public class BaseApplication extends Application {
    //public static final String HOST = "http://tenthirty.jiugoule.net/";
    public static String HOST = "http://www.tpgao.cn/";
    public static BaseApplication INSTANCE;
    private String apiagent = "";
    public BaseApplication() {
        INSTANCE = this;
    }
    /**
     * 获得当前app运行的AppContext
     */
    public static BaseApplication getInstance() {
        return INSTANCE;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static Context getMContext() {
        if (INSTANCE ==null){
            INSTANCE  = new BaseApplication();
        }
        return INSTANCE.getApplicationContext();
    }

    public static String getCacheFileDir() {
        if (getMContext().getExternalCacheDir() != null && SystemConfig.isSDCardEnable()) {
            return getMContext().getExternalCacheDir().toString();
        } else {
            return getMContext().getCacheDir().toString();
        }
    }
    public String getApiagent() {
        if (apiagent.equals("")) {
            return apiagent =getVersionCode(getMContext()) + ",," + "android" + ",," + Build.VERSION.RELEASE + ",,"
                    + android.os.Build.MANUFACTURER + ":" + android.os.Build.MODEL + ",," + NetWorkUtils.getNetWorkType(getMContext());

        } else {
            return apiagent;
        }
    }
    public static int getVersionCode(Context context) {
        int versionCode;
        try {
            versionCode =context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            ex.printStackTrace();
            versionCode = 0;
        }
        return versionCode;
    }
}
