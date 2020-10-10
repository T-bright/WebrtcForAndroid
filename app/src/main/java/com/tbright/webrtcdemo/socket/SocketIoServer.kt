package com.tbright.webrtcdemo.socket

import com.blankj.utilcode.util.NetworkUtils
import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import com.tbright.webrtcdemo.constant.PORT
import com.tbright.webrtcdemo.webrtc.Signalling
import io.socket.client.Socket

class SocketIoServer : Service {
    private var serverSocket: SocketIOServer? = null
    private var clientSocket: Socket? = null

    override fun start() {
        try {
            val config = Configuration()
            //设置主机名
            config.hostname = NetworkUtils.getIpAddressByWifi()
            //设置监听端口
            config.port = PORT
            serverSocket = SocketIOServer(config)

            //有socket连上
            serverSocket?.addConnectListener { client ->
                log("connectListener sessionId:  ${client.getSessionId()}")
                log("connectListener remoteIp:  ${client.remoteAddress}")
                client.sendEvent(Signalling.offer)
            }
            //有socket断开
            serverSocket?.addDisconnectListener { client ->
                log("disconnectListener sessionId:  ${client.getSessionId()}")
            }
            serverSocket?.addEventListener("init", String::class.java) { client, data, ackSender ->
                log("init")
            }

            serverSocket?.start()
            log("服务已开启：host: ${config.hostname}--port: $PORT")
        } catch (e: Exception) {
            e.printStackTrace()
            log("服务已开启失败")
        }
    }

    override fun stop() {

    }

    override fun sendMessage(message: String) {

    }
}