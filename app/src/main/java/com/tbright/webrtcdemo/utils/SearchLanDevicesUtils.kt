package com.tbright.webrtcdemo.utils

import android.util.Log
import java.util.concurrent.Executors

//搜索局域网的设备工具类
class SearchLanDevicesUtils {
    private var executorService = Executors.newFixedThreadPool(255)
    fun search() {
        for (index in 0..255) {
            executorService.execute {
                Log.e("AA", "${Thread.currentThread().name}---${index}")
            }
        }
    }



}