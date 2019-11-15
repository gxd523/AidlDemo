package com.demo.binder_pool_client_b;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.demo.binder_pool_server.IBinderPool;
import com.demo.binder_pool_server.IMinusOperation;

public class MainActivity extends Activity {
    private IMinusOperation minusOperation;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBinderPool binderPool = IBinderPool.Stub.asInterface(iBinder);
            try {
                IBinder binder = binderPool.getBinder(200);
                minusOperation = IMinusOperation.Stub.asInterface(binder);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            minusOperation = null;
        }
    };

    public void onMinusBtnClick(View view) {
        try {
            if (minusOperation != null) {
                int result = minusOperation.minus(5, 2);
                Log.d("gxd", "5 - 2 = " + result);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClassName("com.demo.binder_pool_server", "com.demo.binder_pool_server.BinderPoolService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (minusOperation != null) {
            unbindService(serviceConnection);
        }
    }
}
