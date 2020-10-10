package com.tbright.webrtcdemo.socket

import android.util.Log
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress

class SignallingServer(inetSocketAddress: InetSocketAddress) : WebSocketServer(inetSocketAddress) {

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        log("onOpen")
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        log("onClose")
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        log("onMessage")
    }

    override fun onStart() {
        log("onStart")
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        log("onError")
    }

    private fun log(message: String) {
        Log.d("SignallingServer", message)
    }
}