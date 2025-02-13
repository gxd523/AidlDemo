package com.gxd.demo.aidl;

interface IBinderPool {
    IBinder getBinder(int binderCode);
}
