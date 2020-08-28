package com.tbright.webrtcdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tbright.webrtcdemo.utils.SearchLanDevicesUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SearchLanDevicesUtils().search()
    }
}
