package com.demo.binder_pool_server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BinderPoolService extends Service {
    public BinderPoolService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new IBinderPool.Stub() {
            @Override
            public IBinder getBinder(int binderCode) {
                switch (binderCode) {
                    case 100:
                        return new IPlusOperation.Stub() {
                            @Override
                            public int plus(int a, int b) {
                                return a + b;
                            }
                        };
                    case 200:
                        return new IMinusOperation.Stub() {
                            @Override
                            public int minus(int a, int b) {
                                return a - b;
                            }
                        };
                }
                return null;
            }
        };
    }
}
