package masterapp.want.com.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.Random;

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
    class MyBinder extends MasterAidlInterface.Stub{

        @Override
        public String getName() throws RemoteException {
            return "我是masterapp";
        }

        @Override
        public User getUser() throws RemoteException {
            User user=new User();
            user.name="wisn";
            user.age=new Random().nextInt(100);
            return user;
        }
    }
}
