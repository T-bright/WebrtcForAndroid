package com.tbright.webrtcdemo.utils

import android.util.Log
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.listener.DataListener
import com.tbright.webrtcdemo.constant.PORT
import io.socket.client.IO
import io.socket.client.Socket
import kotlin.concurrent.thread

/**
 * socket管理类。包含socket服务端和socket客户端。
 * 用到了两个第三方库：
 * @see io.socket:socket.io-client:1.0.0：socket客户端。
 *
 * @see com.corundumstudio.socketio:netty-socketio:1.7.12：socket服务端
 *
 */
class SocketManager {
    private var serverSocket: SocketIOServer? = null
    private var clientSocket: Socket? = null
    fun startServerSocket() {
        thread {
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
                }

                //有socket断开
                serverSocket?.addDisconnectListener { client ->
                    log("disconnectListener sessionId:  ${client.getSessionId()}")
                }
                serverSocket?.addEventListener("init",String::class.java){ client, data, ackSender ->
                    log("init")
                }

                serverSocket?.start()
                log("服务已开启：hostname-${config.hostname}")
            } catch (e: Exception) {
                e.printStackTrace()
                log("服务已开启失败")
            }
        }
    }

    fun startClientSocket(ip: String) {
        try {
            val option = IO.Options()
            option.path = "/socket.io"
            clientSocket = IO.socket(ip,option)
            clientSocket?.on("connection"){args->
                log(args.get(0) as String)
            }
            clientSocket?.on("disconnect"){args->
                log(args.get(0) as String)
            }

            clientSocket?.connect()
            clientSocket?.emit("init")
            if(clientSocket?.connected() == true){
                ToastUtils.showShort("init")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun log(message: String) {
        Log.d("SocketManager", message)
    }
}