package com.tbright.webrtcdemo.utils

import android.util.Log
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.transport.NamespaceClient
import com.tbright.webrtcdemo.constant.PORT
import com.tbright.webrtcdemo.socket.SignallingClient
import com.tbright.webrtcdemo.socket.SignallingServer
import com.tbright.webrtcdemo.webrtc.Signalling
import io.socket.client.IO
import io.socket.client.Socket
import java.net.InetSocketAddress
import java.net.URI
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

/**
 * socket管理类。包含socket服务端和socket客户端。
 * 用到了两个第三方库：
 * @see io.socket:socket.io-client:1.0.0：socket客户端。
 *
 * @see com.corundumstudio.socketio:netty-socketio:1.7.12：socket服务端
 *
 */
object SocketManager {
    private var serverSocket: SocketIOServer? = null
    private var clientSocket: Socket? = null
    fun startServerSocket() {
        getServerSocket()
        return
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
    private fun getServerSocket(){
//        ssChannel.register(selector,SelectionKey.OP_CONNECT)
//        ssChannel.register(selector,SelectionKey.OP_READ)
//        ssChannel.register(selector,SelectionKey.OP_WRITE)
        var ssChannel = ServerSocketChannel.open()
        ssChannel.configureBlocking(false)
        var serverSocket = ssChannel.socket()
        serverSocket.bind(InetSocketAddress(PORT))
        val selector = Selector.open()
        ssChannel.register(selector,SelectionKey.OP_ACCEPT)
        while (true){
            var num = selector.select()
            if(num < 1) continue
            val selectedKeys = selector.selectedKeys()
            val keyIterator = selectedKeys.iterator()
            while (keyIterator.hasNext()){
                val key = keyIterator.next()
                if((key.readyOps() and SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
                    //新的链接进来了
                    var serverChannel = key.channel() as ServerSocketChannel
                    var socketChannel = serverChannel.accept()
                    socketChannel.configureBlocking(false)
                    socketChannel.register(selector,SelectionKey.OP_READ)
                    keyIterator.remove()
                }else{
                    //接收到之前的链接发来的消息
                    key.isAcceptable
                    if((key.readyOps() and SelectionKey.OP_READ)== SelectionKey.OP_READ){
                        var socketChannel = key.channel() as SocketChannel
                        var buffer = ByteBuffer.allocate(1024)
                        val data = StringBuilder()
                        while (true) {
                            buffer.clear()
                            val n: Int = socketChannel.read(buffer)
                            if (n == -1) {
                                break
                            }
                            buffer.flip()
                            val limit = buffer.limit()
                            val dst = CharArray(limit)
                            for (i in 0 until limit) {
                                dst[i] = buffer[i].toChar()
                            }
                            data.append(dst)
                            buffer.clear()
                        }
                        log("isReadable : ${data.toString()}")
                        socketChannel.close()
                        log("isReadable close: ")
                        keyIterator.remove()
                    }
                }



//
//                if(key.isConnectable){
//                    log("isConnectable")
//                }else if(key.isAcceptable){
//                    var serverSocketChannel = key.channel() as ServerSocketChannel
//                    var sChannel = serverSocketChannel.accept()
//                    sChannel.configureBlocking(false)
//                    sChannel.register(selector,SelectionKey.OP_READ)
//                    log("isAcceptable :${sChannel.remoteAddress}")
//                }else if(key.isReadable){
//                    val socketChannel: SocketChannel = key.channel() as SocketChannel
//                    var buffer = ByteBuffer.allocate(1024)
//                    val data = StringBuilder()
//                    while (true) {
//                        buffer.clear()
//                        val n: Int = socketChannel.read(buffer)
//                        if (n == -1) {
//                            break
//                        }
//                        buffer.flip()
//                        val limit = buffer.limit()
//                        val dst = CharArray(limit)
//                        for (i in 0 until limit) {
//                            dst[i] = buffer[i].toChar()
//                        }
//                        data.append(dst)
//                        buffer.clear()
//                    }
//                    log("isReadable : ${data.toString()}")
//                    socketChannel.close()
//                    log("isReadable close: ")
//                }
            }

        }
    }


    fun getWebSocketServer(){
        var inetSocketAddress = InetSocketAddress(PORT)
        log("服务已开启：host: ${NetworkUtils.getIpAddressByWifi()}--port: $PORT")
        SignallingServer(inetSocketAddress).start()
    }


    fun startWebSocket(ip: String) {
        var uri = URI.create("ws://${ip}:${PORT}")
        SignallingClient(uri).connectBlocking()
    }
    fun startClientSocket(ip: String) {
        try {
            val option = IO.Options()
            option.path = "/socket.io"
            clientSocket = IO.socket(ip, option)
            clientSocket?.on("connection") { args ->
                log(args.get(0) as String)
            }
            clientSocket?.on("disconnect") { args ->
                log(args.get(0) as String)
            }

            clientSocket?.connect()
            clientSocket?.emit("init")
            if (clientSocket?.connected() == true) {
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