package com.want.bll;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import masterapp.want.com.demo.MasterAidlInterface;

/**
 * Created by wisn on 2017/11/30.
 */

public class MyApplication extends Application {
    public static String TAG = "MyApplication";
    public MasterAidlInterface masterAidlInterface;
    private static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        aidl();
        app = this;
    }

    public MasterAidlInterface getBllAidlInterface() {
        if (masterAidlInterface == null) {
            aidl();
        }
        return masterAidlInterface;
    }

    public static MyApplication getApp() {
        return app;
    }

    private void aidl() {
        try {
            //连接远程服务
            ServiceConnection mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    masterAidlInterface = MasterAidlInterface.Stub.asInterface(service);
                    Log.d(TAG, "获取aidl远程服务成功对象值 mContentService" + masterAidlInterface);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    masterAidlInterface = null;
                    Log.d(TAG, "远程服务aidl断开 ComponentName " + name);
                }
            };
            //使用意图对象绑定开启服务
            Intent intent = new Intent();
            intent.setAction("masterapp.want.com.demo.ACTION_CONTENT");
            //            在5.0及以上版本必须要加上这个
            intent.setPackage("masterapp.want.com.demo");
            Intent tempIntent = getExplicitIntent(this, intent);
            if (tempIntent == null) {
                intent.setAction("masterapp.want.com.demo.ACTION_CONTENT");
                //            在5.0及以上版本必须要加上这个
                intent.setPackage("masterapp.want.com.demo");
            } else {
                intent = tempIntent;
            }
            boolean isBindService = bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Aidl获取svm状态异常" + e.toString());
        }
    }


    public static Intent getExplicitIntent(Context context, Intent intent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(intent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(intent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}
