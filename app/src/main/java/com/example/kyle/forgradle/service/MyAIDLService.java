package com.example.kyle.forgradle.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kyle.forgradle.IMyAidlInterface;
import com.example.kyle.forgradle.entity.EntityParcelable;


public class MyAIDLService extends Service {
    private static final String TAG = "MyAIDLService";

    private int counter = 999;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IMyAidlInterface.Stub() {
            @Override
            public String helloAIDL() throws RemoteException {
                counter = 999;
                return "Hello AIDL, " + Thread.currentThread().toString();
            }

            @Override
            public void updateServerInfo(EntityParcelable entity) throws RemoteException {
                Log.e(TAG, "c->s: " + entity.toString());
                entity.setString("This should not show when client click");
            }

            @Override
            public void dealWithClientInfo(EntityParcelable entity) throws RemoteException {
                Log.e(TAG, "s->c: before s change, " + entity.toString());
                entity.setValues("From Server values", 66.6, counter--, true);
                Log.e(TAG, "s->c: after s change, " + entity.toString());
            }

            @Override
            public void useSameInfo(EntityParcelable entity) throws RemoteException {
                Log.e(TAG, "s<->c: before s change, " + entity.toString());
                entity.setString("change only string" + counter--);
                Log.e(TAG, "s<->c: after s change, " + entity.toString());
            }
        };
    }

}
