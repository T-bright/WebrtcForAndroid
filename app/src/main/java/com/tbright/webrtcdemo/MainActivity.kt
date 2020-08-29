package com.tbright.webrtcdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tbright.webrtcdemo.utils.SearchLanDevicesUtils
import com.tbright.webrtcdemo.utils.SocketManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createChatRoom.setOnClickListener {
//            RoomActivity.start(this@MainActivity,"")
            SocketManager.startServerSocket()
        }
        searchChatRoom.setOnClickListener {
//            SearchLanDevicesUtils().search()
            SocketManager.startClientSocket("http://192.168.16.237:5555/")
        }
    }
}
