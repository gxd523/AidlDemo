package com.gxd.demo.aidl.client

import android.os.Process
import android.util.Log

private val logPrefix: String
    get() = "Client...pid = ${Process.myPid()}, thread = ${Thread.currentThread().name}-${Thread.currentThread().id}"

fun String.log() {
    Log.d("ggg", "$logPrefix, $this")
}