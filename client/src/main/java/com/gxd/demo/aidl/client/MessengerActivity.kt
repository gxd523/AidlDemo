package com.gxd.demo.aidl.client

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Button

class MessengerActivity : Activity() {
    private var messenger: Messenger? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messenger = Messenger(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            messenger = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = Button(this).apply {
            text = "Messenger测试"
        }
        setContentView(button)
        button.setOnClickListener {
            val message = Message.obtain().apply {
                data = Bundle().apply { putString("messenger_data", "msg from client") }
            }
            messenger?.send(message)
        }

        val intent = Intent().apply {
            val packageName = "com.gxd.demo.aidl.server"
            setClassName(packageName, "$packageName.MessengerService")
            setPackage(packageName)
        }
        val bindResult = bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        "bindResult = $bindResult".log()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (messenger != null) unbindService(serviceConnection)
    }
}