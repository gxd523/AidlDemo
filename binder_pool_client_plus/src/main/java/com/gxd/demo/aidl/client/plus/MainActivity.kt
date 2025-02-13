package com.gxd.demo.aidl.client.plus

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.util.Log
import android.widget.Button
import com.gxd.demo.aidl.IBinderPool
import com.gxd.demo.aidl.IPlusOperation

class MainActivity : Activity() {
    private var plusOperation: IPlusOperation? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
            val binderPool = IBinderPool.Stub.asInterface(iBinder)
            val binder = binderPool.getBinder(100)
            plusOperation = IPlusOperation.Stub.asInterface(binder)
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            plusOperation = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent().apply {
            val packageName = "com.gxd.demo.aidl.pool.server"
            setClassName(packageName, "$packageName.BinderPoolService")
            setPackage(packageName)
        }
        val bindResult = bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        "bindResult = $bindResult".log()

        val button = Button(this)
        with(button) {
            button.text = "Plus"
            button.setOnClickListener {
                val result = plusOperation?.plus(1, 2)
                "1 + 2 = $result".log()
            }
        }
        setContentView(button)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (plusOperation != null) unbindService(serviceConnection)
    }

    private val logPrefix: String
        get() = "Client...pid = ${Process.myPid()}, thread = ${Thread.currentThread().name}-${Thread.currentThread().id}"

    private fun String.log() {
        Log.d("ggg", "$logPrefix, $this")
    }
}