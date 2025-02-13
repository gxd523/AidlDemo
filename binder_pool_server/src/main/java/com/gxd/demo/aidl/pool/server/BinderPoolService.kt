package com.gxd.demo.aidl.pool.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import android.util.Log
import com.gxd.demo.aidl.IBinderPool
import com.gxd.demo.aidl.IMinusOperation
import com.gxd.demo.aidl.IPlusOperation

class BinderPoolService : Service() {
    private val plusBinder by lazy {
        object : IPlusOperation.Stub() {
            override fun plus(a: Int, b: Int): Int {
                "plus...".log()
                return a + b
            }
        }
    }
    private val minusBinder by lazy {
        object : IMinusOperation.Stub() {
            override fun minus(a: Int, b: Int): Int {
                "minus...".log()
                return a - b
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = object : IBinderPool.Stub() {
        override fun getBinder(binderCode: Int): IBinder? = when (binderCode) {
            100 -> plusBinder
            200 -> minusBinder
            else -> null
        }
    }

    private val logPrefix: String
        get() = "Server...pid = ${Process.myPid()}, thread = ${Thread.currentThread().name}-${Thread.currentThread().id}"

    private fun String.log() {
        Log.d("ggg", "$logPrefix, $this")
    }
}
