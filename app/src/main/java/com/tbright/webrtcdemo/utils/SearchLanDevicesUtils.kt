package com.tbright.webrtcdemo.utils

import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.NetworkUtils
import com.tbright.webrtcdemo.constant.PORT
import com.tbright.webrtcdemo.constant.TIME_OUT
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Executors

//搜索局域网的设备工具类
class SearchLanDevicesUtils {
    private var executorService = Executors.newFixedThreadPool(255)
    private var ipAddress: String? = null


    /**
     * 搜索局域网内的设备
     */
    fun search() {
        val locAddress: String? = getLocAddress() ?: return
        for (index in 0..255) {
            val hostName = locAddress + index
            if (ipAddress == hostName) continue
            if(index != 238){
                continue
            }
            executorService.execute {
                val socket = Socket()
                try {
                    val socketAddress = InetSocketAddress(hostName, PORT)
                    socket.connect(socketAddress, TIME_OUT)
                    socket.close()
                } catch (e: IOException) {
                } finally {
                    try {
                        socket.close()
                    } catch (e: IOException) {
                    }
                }
            }
        }
    }

    private fun getLocAddress(): String? {
        ipAddress = NetworkUtils.getIpAddressByWifi()
        var locAddress: String? = null
        if (!TextUtils.isEmpty(ipAddress)) {
            locAddress = ipAddress?.substring(0, (ipAddress?.lastIndexOf(".") ?: 0) + 1)
        }
        if (TextUtils.isEmpty(locAddress)) {
            return null
        }
        return locAddress
    }

}