package com.gxd.demo.aidl.server

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Message
import android.os.Messenger

class MessengerService : Service() {
    private val handlerThread by lazy { HandlerThread("messenger-thread") }
    private val handler by lazy {
        Handler(handlerThread.looper, object : Handler.Callback {
            override fun handleMessage(msg: Message): Boolean {
                "handleMessage = ${msg.data?.getString("messenger_data")}".log()
                return true
            }
        })
    }
    private val messenger by lazy { Messenger(handler) }

    override fun onBind(intent: Intent?): IBinder? {
        handlerThread.start()
        return messenger.binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        handlerThread.quitSafely()
        return super.onUnbind(intent)
    }
}