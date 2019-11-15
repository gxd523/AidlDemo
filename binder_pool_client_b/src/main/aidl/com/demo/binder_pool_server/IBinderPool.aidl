package com.demo.binder_pool_server;

interface IBinderPool {
    IBinder getBinder(int binderCode);
}
