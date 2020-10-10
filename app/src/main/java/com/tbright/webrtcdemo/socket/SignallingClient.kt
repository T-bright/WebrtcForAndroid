package com.tbright.webrtcdemo.socket

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class SignallingClient(serverUri : URI): WebSocketClient(serverUri) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        log("onOpen")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        log("onClose")
    }

    override fun onMessage(message: String?) {
        log("onMessage")
    }

    override fun onError(ex: Exception?) {
        log("onError")
    }

    private fun log(message: String) {
        Log.d("SignallingClient", message)
    }
}