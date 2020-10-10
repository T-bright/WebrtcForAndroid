package com.tbright.webrtcdemo.socket

import android.util.Log

interface Service {
    fun start()

    fun stop()

    fun sendMessage(message: String)

    fun log(message: String) {
        Log.d(this.javaClass.name, message)
    }
}