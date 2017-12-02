package com.want.bll;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import masterapp.want.com.demo.bean.User;

/**
 * Created by wisn on 2017/11/30.
 */

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    class MyBinder extends BllAidlInterface.Stub {

        @Override
        public String getName() throws RemoteException {
            if (MyApplication.getApp().getBllAidlInterface() != null) {
                User user = MyApplication.getApp().getBllAidlInterface().getUser();
                return "bllapp 获取: " + MyApplication.getApp().getBllAidlInterface().getName() +
                       user.toString();
            }
            return "我是Bllapp";
        }
    }
}
